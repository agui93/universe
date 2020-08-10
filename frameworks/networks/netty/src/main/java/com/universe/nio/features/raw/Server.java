package com.universe.nio.features.raw;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * @author agui93
 * @since 2020/7/18
 */
public class Server {

    public static void main(String[] args) throws IOException {
        int port = 8089;
        Selector selector = Selector.open();

        ServerSocketChannel serverSocket = ServerSocketChannel.open();
        serverSocket.socket().bind(new InetSocketAddress(port));
        serverSocket.configureBlocking(false);

        serverSocket.register(selector, SelectionKey.OP_ACCEPT).attach(serverSocket);

        ByteBuffer input = ByteBuffer.allocate(4);
        ByteBuffer output = ByteBuffer.allocate(4);
        while (true) {
            selector.select();
            Set<SelectionKey> selected = selector.selectedKeys();
            selected.iterator();
            Iterator<SelectionKey> it = selected.iterator();
            while (it.hasNext()) {
                SelectionKey selectionKey = it.next();
                if (selectionKey.isAcceptable()) {
                    ServerSocketChannel ssc = (ServerSocketChannel) selectionKey.attachment();
                    SocketChannel socketChannel = ssc.accept();
                    socketChannel.configureBlocking(false);

                    System.out.println("Connected " + socketChannel.getRemoteAddress());

                    socketChannel.register(selector, SelectionKey.OP_READ).attach(socketChannel);
                } else if (selectionKey.isReadable()) {
                    SocketChannel socketChannel = (SocketChannel) selectionKey.attachment();
                    socketChannel.read(input);
                    input.flip();
                    System.out.print("Received input from Client" + socketChannel.getRemoteAddress() + "  ;; Data:");
                    while (input.hasRemaining()) {
                        System.out.print(input.get() + " ");
                    }
                    input.clear();
                    System.out.println(";Received end");
                    System.out.println();
                    socketChannel.register(selector, SelectionKey.OP_WRITE).attach(socketChannel);
                } else if (selectionKey.isWritable()) {
                    output.clear();
                    output.put((byte) 11);
                    output.put((byte) 22);
                    output.put((byte) 33);
                    output.put((byte) 44);
                    output.flip();

                    SocketChannel socketChannel = (SocketChannel) selectionKey.attachment();
                    socketChannel.write(output);
                    System.out.println("Write End For " + socketChannel.getRemoteAddress());
                    System.out.println();
                    selectionKey.cancel();
                }
                it.remove();
            }
        }

    }

}
