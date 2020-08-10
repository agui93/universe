package com.universe.nio.reactor.accumulator;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Random;

/**
 * @author agui93
 * @since 2020/8/3
 */
public class ReactorAccumulatorClient implements Runnable {

    SocketChannel socketChannel;


    public ReactorAccumulatorClient() throws IOException {
        this.socketChannel = SocketChannel.open();
    }

    @Override
    public void run() {
        try {
            Thread.sleep(new Random().nextInt(10) + 1);
            //建立连接
            this.socketChannel.connect(new InetSocketAddress(8089));
            System.out.println("发起链接->" + this.socketChannel);


            ByteBuffer byteBuffer = ByteBuffer.allocate(10);
            Random random = new Random();

            int expectResult = 0;
            int continueOpBeAppend = 0;
            for (int i = 0; i < 10; i++) {
                int randomNum = random.nextInt(1000);
                if (randomNum % 2 == 0) {
                    if (continueOpBeAppend == 3) {
                        Thread.sleep(10);//避免客户端数据发送过快
                        continueOpBeAppend = 0;
                    }
                    append2Server(byteBuffer, randomNum);
                    expectResult = expectResult + randomNum;
                    continueOpBeAppend++;
                } else {
                    continueOpBeAppend = 0;
                    if (expectResult != getFromServer(byteBuffer)) {
                        System.out.println("Wrong for:" + this.socketChannel);
                    }

                }
            }

            if (expectResult != getFromServer(byteBuffer)) {
                System.out.println("Wrong for:" + this.socketChannel);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void append2Server(ByteBuffer byteBuffer, int appendNum) throws IOException {
        byteBuffer.clear();
        byteBuffer.putChar('A');//标识
        byteBuffer.putInt(appendNum);
        socketChannel.write(byteBuffer);
    }

    private int getFromServer(ByteBuffer byteBuffer) throws IOException {
        byteBuffer.clear();
        byteBuffer.putChar('G');//标识
        socketChannel.write(byteBuffer);

        byteBuffer.clear();
        while (socketChannel.read(byteBuffer) > 0) ;
        return byteBuffer.getInt();
    }

    public static void main(String[] args) throws IOException {
        new ReactorAccumulatorClient().run();
    }
}
