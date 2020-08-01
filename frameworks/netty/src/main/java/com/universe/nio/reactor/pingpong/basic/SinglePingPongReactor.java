package com.universe.nio.reactor.pingpong.basic;

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
 * @since 2020/8/1
 */
public class SinglePingPongReactor {

    //Responds to i/o events by dispatching the appropriate handler
    static class Reactor implements Runnable {
        private Selector selector;   //demultiplexer
        private ServerSocketChannel serverSocketChannel;

        public Reactor() {
            try {
                this.selector = Selector.open();
                this.serverSocketChannel = ServerSocketChannel.open();
                this.serverSocketChannel.configureBlocking(false);
                this.serverSocketChannel.bind(new InetSocketAddress(8089));
                SelectionKey sk = this.serverSocketChannel.register(this.selector, SelectionKey.OP_ACCEPT);
                sk.attach(new Acceptor());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            try {
                while (!Thread.interrupted()) {
                    int selectNum = this.selector.select();
                    if (selectNum > 0) {
                        Set<SelectionKey> selectionKeys = this.selector.selectedKeys();
                        Iterator<SelectionKey> iterator = selectionKeys.iterator();
                        while (iterator.hasNext()) {
                            dispatch(iterator.next());
                            iterator.remove();
                        }
                    }
                }
                this.serverSocketChannel.close();
                this.selector.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        private void dispatch(SelectionKey selectionKey) {
            Runnable r = (Runnable) selectionKey.attachment();
            if (r != null) {
                r.run();
            }
        }

        class Acceptor implements Runnable {
            @Override
            public void run() {
                try {
                    SocketChannel socketChannel = serverSocketChannel.accept();//non-blocking
                    if (socketChannel != null) {
                        System.out.println("Accept Connect:" + socketChannel);
                        new Handler(selector, socketChannel);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    static class Handler implements Runnable {
        final SocketChannel socketChannel;
        final SelectionKey selectionKey;
        ByteBuffer input = ByteBuffer.allocate(4);//PING
        ByteBuffer output = ByteBuffer.allocate(4);//PONG
        static final int READING = 0, SENDING = 1;
        int state = READING;


        public Handler(Selector selector, SocketChannel socketChannel) throws IOException {

            this.socketChannel = socketChannel;
            socketChannel.configureBlocking(false);
            this.selectionKey = socketChannel.register(selector, 0);
            this.selectionKey.attach(this);
            this.selectionKey.interestOps(SelectionKey.OP_READ);
            selector.wakeup();
        }

        @Override
        public void run() {
            try {
                if (state == READING) {
                    read();
                } else if (state == SENDING) {
                    send();
                }
            } catch (IOException ex) { /* ... */ }
        }

        private void read() throws IOException {
            int bytes = socketChannel.read(input);
            if (bytes > 0) {
                if (inputIsComplete()) {
                    process();

                    state = SENDING;
                    output.clear();
                    output.put("PONG".getBytes());
                    output.flip();
                    selectionKey.interestOps(SelectionKey.OP_WRITE);
                }
            } else if (bytes == -1) {
                System.out.println(this.socketChannel + "关闭链接");
                socketChannel.close();
                selectionKey.cancel();
            }
        }

        private void send() throws IOException {
            socketChannel.write(output);
            if (outputIsComplete()) {
                System.out.println(this.socketChannel + "发送数据:PONG");
                state = READING;
                selectionKey.interestOps(SelectionKey.OP_READ);
                input.clear();
            }
        }

        private void process() {
            input.flip();
            System.out.print("接受到(" + this.socketChannel + ")的数据:");
            while (input.hasRemaining()) {
                System.out.print((char) input.get());
            }
            System.out.println();
        }

        private boolean inputIsComplete() {
            return !input.hasRemaining();
        }

        private boolean outputIsComplete() {
            return !output.hasRemaining();
        }
    }


    public static void main(String[] args) throws IOException {
        new Reactor().run();

    }

}
