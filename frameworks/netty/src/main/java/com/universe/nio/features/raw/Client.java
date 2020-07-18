package com.universe.nio.features.raw;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * @author agui93
 * @since 2020/7/18
 */
public class Client {

    public static void main(String[] args) throws IOException, InterruptedException {
        int remotePort = 8089;
        Selector selector = Selector.open();

        SocketChannel socketChannel = SocketChannel.open();
        socketChannel.configureBlocking(false);
        socketChannel.register(selector, SelectionKey.OP_CONNECT).attach(socketChannel);
        socketChannel.connect(new InetSocketAddress(remotePort));

        ByteBuffer input = ByteBuffer.allocate(4);
        ByteBuffer output = ByteBuffer.allocate(4);
        boolean breakFlag = false;
        while (true) {
            selector.select();
            Set<SelectionKey> selected = selector.selectedKeys();
            Iterator<SelectionKey> it = selected.iterator();
            while (it.hasNext()) {
                SelectionKey selectionKey = it.next();
                if (selectionKey.isConnectable()) {
                    SocketChannel sc = (SocketChannel) selectionKey.attachment();
                    System.out.println("Connected");
                    System.out.println();
                    //sc.register(selector, SelectionKey.OP_WRITE).attach(sc);

                    while (!sc.finishConnect()) {
                    }
                    output.clear();
                    output.put((byte) 1);
                    output.put((byte) 2);
                    output.put((byte) 3);
                    output.put((byte) 4);
                    output.flip();
                    System.out.println("Write Start");
                    sc.write(output);
                    System.out.println("Write End");
                    System.out.println();

                    sc.register(selector, SelectionKey.OP_READ).attach(sc);
                    break;
                } else if (selectionKey.isReadable()) {
                    SocketChannel sc = (SocketChannel) selectionKey.attachment();
                    sc.read(input);
                    System.out.print("Received input from Server " + sc.getRemoteAddress() + "  ;; Data:");
                    input.flip();
                    while (input.hasRemaining()) {
                        System.out.print(input.get() + " ");
                    }
                    input.clear();
                    System.out.println(";Received end");
                    System.out.println();

                    breakFlag = true;
                } else if (selectionKey.isWritable()) {
                }

                it.remove();
            }
            if (breakFlag) {
                break;
            }
        }

        input.clear();
        output.clear();
        socketChannel.close();
        selector.close();
    }
}
