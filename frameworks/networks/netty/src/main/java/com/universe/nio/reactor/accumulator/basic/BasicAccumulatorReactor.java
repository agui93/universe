package com.universe.nio.reactor.accumulator.basic;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

/**
 * @author agui93
 * @since 2020/8/3
 */
public class BasicAccumulatorReactor {

    class Reactor implements Runnable {
        private Selector selector;
        private ServerSocketChannel serverSocketChannel;

        public Reactor() throws IOException {
            this.selector = Selector.open();
            this.serverSocketChannel = ServerSocketChannel.open();
            this.serverSocketChannel.configureBlocking(false);
            this.serverSocketChannel.bind(new InetSocketAddress(8089));
            SelectionKey selectionKey = this.serverSocketChannel.register(this.selector, SelectionKey.OP_ACCEPT);
            selectionKey.attach(new Acceptor());
        }

        @Override
        public void run() {
            while (!Thread.interrupted()) {
                try {
                    int selectN = this.selector.select();
                    if (selectN > 0) {
                        Iterator<SelectionKey> it = this.selector.selectedKeys().iterator();
                        while (it.hasNext()) {
                            dispatch(it.next());
                            it.remove();
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
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
                    SocketChannel socketChannel = serverSocketChannel.accept();
                    if (socketChannel != null) {//因为accept被设置为非阻塞了,所以需要判空
                        new Handler(socketChannel, selector);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }


    }

    class Handler implements Runnable {
        private SelectionKey selectionKey;
        private SocketChannel socketChannel;
        private int accumulatorNum = 0;

        private ByteBuffer input = ByteBuffer.allocate(100);
        private ByteBuffer output = ByteBuffer.allocate(10);

        public Handler(SocketChannel socketChannel, Selector selector) throws IOException {
            this.socketChannel = socketChannel;
            this.socketChannel.configureBlocking(false);
            this.selectionKey = this.socketChannel.register(selector, SelectionKey.OP_READ);
            selectionKey.attach(this);

            selector.wakeup();
        }


        @Override
        public void run() {
            if (this.selectionKey.isReadable()) {

            }
            if (this.selectionKey.isWritable()) {

            }
        }

        private void read() throws IOException {
            while (!inputComplete()) {
                this.socketChannel.read(input);
            }

            process();
        }

        private boolean inputComplete() {
            if (input.position() > 0) {
                char c = (char) input.get(0);
                return c == 'G' || (c == 'A' && input.position() >= 5) || !input.hasRemaining();
            }

            return false;
        }


        private void process() {
            //input的处理,避免数据分隔出错
            input.flip();

            while (input.hasRemaining()) {
                char flag = input.getChar();
                if (flag == 'A') {
                    int num = input.getInt();
                    input.getChar();
                    accumulatorNum = accumulatorNum + num;
                } else if (flag == 'G') {
                    break;
                }
            }
        }


        private void write() {
        }
    }


}
