package com.universe.nio.reactor.pingpong;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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
            Thread.sleep(new Random().nextInt(10) + 1);
            //建立连接
            this.socketChannel.connect(new InetSocketAddress(8089));
            System.out.println(this.socketChannel + "发起链接");


            ByteBuffer byteBuffer = ByteBuffer.allocate(4);
            readAndWrite(byteBuffer);

            System.out.println(this.socketChannel + "关闭链接");
            this.socketChannel.close();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }


    }

    private void readAndWrite(ByteBuffer byteBuffer) throws IOException {
        //PING
        byteBuffer.clear();
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
        List<Thread> threads = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            threads.add(new Thread(new Client()));
        }
        threads.forEach(Thread::start);

    }
}

