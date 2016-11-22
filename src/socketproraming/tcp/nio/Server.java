package socketproraming.tcp.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

public class Server {

    @SuppressWarnings("unused")
    public static void main(String[] args) throws IOException {

        // Selector: multiplexor of SelectableChannel objects
        Selector selector = Selector.open(); // selector is open here

        // ServerSocketChannel: selectable channel for stream-oriented listening sockets
        ServerSocketChannel channel = ServerSocketChannel.open();
        InetSocketAddress socketAddress = new InetSocketAddress("localhost", 1111);

        // second channel

        ServerSocketChannel secondChannel = ServerSocketChannel.open();
        InetSocketAddress socketAddress2 = new InetSocketAddress("localhost", 1112);

        // Binds the channel's socket to a local address and configures the socket to listen for connections
        channel.bind(socketAddress);
        secondChannel.bind(socketAddress2);

        // Adjusts this channel's blocking mode.
        channel.configureBlocking(false);
        secondChannel.configureBlocking(false);

        int ops = channel.validOps();
        SelectionKey selectKy = channel.register(selector, ops);

        // my channel
        SelectionKey secondSelectionKey = secondChannel.register(selector, ops);

        // Infinite loop..
        // Keep server running
        while (true) {

            log("i'm a server and i'm waiting for new connection and buffer select...");
            // Selects a set of keys whose corresponding channels are ready for I/O operations
            selector.select();

            // token representing the registration of a SelectableChannel with a Selector
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            Iterator<SelectionKey> iterator = selectionKeys.iterator();
            System.out.println("number of selector" + selectionKeys.size());
            while (iterator.hasNext()) {
                SelectionKey myKey = iterator.next();
                System.out.println("" + myKey.toString());
                // Tests whether this key's channel is ready to accept a new socket connection
                if (myKey.isAcceptable()) {
                    System.out.println("waiting for connection");
                    SocketChannel clientChannel = channel.accept();

                    // Adjusts this channel's blocking mode to false
                    clientChannel.configureBlocking(false);

                    // Operation-set bit for read operations
                    clientChannel.register(selector, SelectionKey.OP_READ);
                    System.out.println("Connection Accepted: " + clientChannel.getLocalAddress() + "\n");

                    // Tests whether this key's channel is ready for reading
                } else if (myKey.isReadable()) {
                    System.out.println("reading data now");
                    SocketChannel clientChannel = (SocketChannel) myKey.channel();
                    ByteBuffer dataBuffer = ByteBuffer.allocate(256);
                    clientChannel.read(dataBuffer);
                    System.out.println("Buffer capacity " + dataBuffer.capacity() + " but size is " + dataBuffer.limit());
                    String result = new String(dataBuffer.array()).trim();

                    System.out.println("Message received: " + result);

                    if (result.equals("Crunchify")) {
                        clientChannel.close();
                        log("\nIt's time to close connection as we got last company name 'Crunchify'");
                        log("\nServer will keep running. Try running client again to establish new connection");
                    }
                }
                System.out.println("removing from iterator");
                iterator.remove();
            }
        }
    }

    private static void log(String str) {
        System.out.println(str);
    }
}