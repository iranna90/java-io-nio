package socketproraming.tcp.io;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.time.LocalTime;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by iranna.patil on 20-11-2016.
 */
public class Client {

    private static final int PORT = 1234;

    public static void main(String[] args) throws IOException, InterruptedException {
        Client client = new Client();
        ExecutorService executorService = Executors.newFixedThreadPool(5);
        for (int i = 0; i < 10; i++) {
            executorService.submit(() -> {
                try {
                    client.start();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        }
        executorService.shutdown();
    }

    private void start() throws IOException, InterruptedException {

        // define server address where it should connect to
        InetAddress serverAddress = InetAddress.getLocalHost();
        // socket is created
        Socket socket = new Socket(serverAddress, PORT);
        // now receive data from socket
        InputStream inputStream = socket.getInputStream();
        // buffer at which we have data
        byte[] buffer = new byte[10];
        int read = inputStream.read(buffer);
        while (read != -1) {
            System.out.println(Thread.currentThread().getName() + " time " + LocalTime.now());
            System.out.println(new String(buffer, "UTF-8"));
            Thread.sleep(1000 * 2);
            read = inputStream.read(buffer);
        }
    }
}
