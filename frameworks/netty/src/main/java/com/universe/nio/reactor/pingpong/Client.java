package com.universe.nio.reactor.pingpong;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * @author agui93
 * @since 2020/8/1
 */
public class Client implements Runnable {
    SocketChannel socketChannel;

    public Client() throws IOException {
        this.socketChannel = SocketChannel.open();
    }

    @Override
    public void run() {
        try {
            //建立连接
            this.socketChannel.connect(new InetSocketAddress(8089));
            System.out.println(this.socketChannel + "发起链接");

            //PING
            ByteBuffer byteBuffer = ByteBuffer.allocate(4);
            readAndWrite(byteBuffer);

            this.socketChannel.close();
            System.out.println(this.socketChannel + "关闭链接");
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    private void readAndWrite(ByteBuffer byteBuffer) throws IOException {
        byteBuffer.put("PING".getBytes());
        byteBuffer.flip();
        while (byteBuffer.hasRemaining()) {
            this.socketChannel.write(byteBuffer);
        }
        System.out.println(this.socketChannel + "链接发送数据PING");

        //PONG
        byteBuffer.clear();
        while (byteBuffer.hasRemaining()) {
            this.socketChannel.read(byteBuffer);
        }
        byteBuffer.flip();
        System.out.print(this.socketChannel + "链接接受到数据:");
        while (byteBuffer.hasRemaining()) {
            System.out.print((char) byteBuffer.get());
        }
        System.out.println();
    }

    public static void main(String[] args) throws IOException {
        new Client().run();
    }
}

