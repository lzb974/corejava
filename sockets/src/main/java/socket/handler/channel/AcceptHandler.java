package socket.handler.channel;

import socket.handler.ClientHandler;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayDeque;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class AcceptHandler implements ClientHandler<SelectionKey> {
    private final Map<SocketChannel, Queue<ByteBuffer>> pendingData;

    public AcceptHandler(Map<SocketChannel, Queue<ByteBuffer>> pendingData) {
        this.pendingData = pendingData;
    }

    @Override
    public void handle(SelectionKey selectionKey) throws IOException {
        var serverChannel = (ServerSocketChannel) selectionKey.channel();
        var sc = serverChannel.accept();
        if (sc == null) return;
        System.out.println("Connected to " + sc);

        sc.configureBlocking(false);

        pendingData.put(sc, new ConcurrentLinkedQueue<>());

        sc.register(selectionKey.selector(), SelectionKey.OP_READ);
    }
}
