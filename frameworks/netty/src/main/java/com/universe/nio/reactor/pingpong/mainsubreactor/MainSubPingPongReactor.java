package com.universe.nio.reactor.pingpong.mainsubreactor;


import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author agui93
 * @since 2020/8/2
 */
public class MainSubPingPongReactor {
    //Responds to i/o events by dispatching the appropriate handler
    static class Reactor implements Runnable {
        private Selector selector;   //demultiplexer
        private ServerSocketChannel serverSocketChannel;

        private SubSelector[] subSelectors;
        private int next;
        private ExecutorService subSelectorsExecutorService;

        public Reactor() {
            try {
                this.selector = Selector.open();
                this.serverSocketChannel = ServerSocketChannel.open();
                this.serverSocketChannel.configureBlocking(false);
                this.serverSocketChannel.bind(new InetSocketAddress(8089));
                SelectionKey sk = this.serverSocketChannel.register(this.selector, SelectionKey.OP_ACCEPT);
                sk.attach(new Reactor.Acceptor());

                int n = 2 * Runtime.getRuntime().availableProcessors();
                subSelectorsExecutorService = Executors.newFixedThreadPool(n);
                subSelectors = new SubSelector[n];
                for (int i = 0; i < n; i++) {
                    subSelectors[i] = new SubSelector();
                    subSelectorsExecutorService.execute(subSelectors[i]);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            try {
                while (!Thread.interrupted()) {
//                    int selectNum = this.selector.select(100);
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


                for (SubSelector subSelector : this.subSelectors) {
                    subSelector.close();
                }

                this.subSelectorsExecutorService.shutdown();

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
            public synchronized void run() {
                try {
                    SocketChannel socketChannel = serverSocketChannel.accept();//non-blocking
                    if (socketChannel != null) {
                        System.out.println("Accept Connect:" + socketChannel);
                        new Handler(subSelectors[next], socketChannel);
                        if (++next == subSelectors.length) {
                            next = 0;
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    static class SubSelector implements Runnable {

        private Selector rawSubSelector;
        private volatile boolean running;

        public SubSelector() throws IOException {
            this.rawSubSelector = Selector.open();
            this.running = true;
        }

        @Override
        public void run() {
            while (running) {
                try {
                    int selectNum = this.rawSubSelector.select(10);
                    if (selectNum > 0) {
                        Set<SelectionKey> selectionKeys = this.rawSubSelector.selectedKeys();
                        Iterator<SelectionKey> iterator = selectionKeys.iterator();
                        while (iterator.hasNext()) {
                            Runnable r = (Runnable) iterator.next().attachment();
                            if (r != null) {
                                r.run();
                            }
                            iterator.remove();
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        public void stop() {
            this.running = false;
        }

        public void close() throws IOException {
            stop();
            this.rawSubSelector.close();
        }

        public Selector rawSubSelector() {
            return this.rawSubSelector;
        }

    }

    static class Handler implements Runnable {
        final SocketChannel socketChannel;
        final SelectionKey selectionKey;
        ByteBuffer input = ByteBuffer.allocate(4);//PING
        ByteBuffer output = ByteBuffer.allocate(4);//PONG
        static final int READING = 0, SENDING = 1;
        static final int PROCESSING = 3;
        int state = READING;

        static Executor executor = Executors.newFixedThreadPool(5);


        public Handler(SubSelector subSelector, SocketChannel socketChannel) throws IOException {
            this.socketChannel = socketChannel;
            this.socketChannel.configureBlocking(false);
            // This method will then synchronize on the selector's key set and therefore may
            // block if invoked concurrently with another registration or selection
            // operation involving the same selector.
            this.selectionKey = socketChannel.register(subSelector.rawSubSelector, 0);
            this.selectionKey.attach(this);
            this.selectionKey.interestOps(SelectionKey.OP_READ);
            subSelector.rawSubSelector.wakeup();
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

        private synchronized void read() throws IOException {
            int bytes = socketChannel.read(input);
            if (bytes > 0) {
                if (inputIsComplete()) {
                    state = PROCESSING;
                    executor.execute(new Handler.Processor());
                }
            } else if (bytes == -1) {
                System.out.println(this.socketChannel + "关闭链接");
                socketChannel.close();
                selectionKey.cancel();
            }
        }

        private synchronized void send() throws IOException {
            socketChannel.write(output);
            if (outputIsComplete()) {
                System.out.println(this.socketChannel + "发送数据:PONG");
                state = READING;
                selectionKey.interestOps(SelectionKey.OP_READ);
            }
        }

        private void process() {
            input.flip();
            System.out.print("接受到(" + this.socketChannel + ")的数据:");
            while (input.hasRemaining()) {
                System.out.print((char) input.get());
            }
            input.clear();
            System.out.println();
        }

        synchronized void processAndHandOff() {
            process();
            state = SENDING;
            output.clear();
            output.put("PONG".getBytes());
            output.flip();
            selectionKey.interestOps(SelectionKey.OP_WRITE);
            selectionKey.selector().wakeup();
        }

        private boolean inputIsComplete() {
            return !input.hasRemaining();
        }

        private boolean outputIsComplete() {
            return !output.hasRemaining();
        }

        class Processor implements Runnable {
            public void run() {
                processAndHandOff();
            }
        }
    }


    public static void main(String[] args) throws IOException {
        new Reactor().run();

    }
}
