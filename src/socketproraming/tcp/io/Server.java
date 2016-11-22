package socketproraming.tcp.io;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalTime;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by iranna.patil on 20-11-2016.
 */
public class Server {

    private static final int PORT = 1234;

    public static void main(String[] args) throws IOException {

        // inet address
        //InetAddress inetAddress = InetAddress.getByAddress("1234",);
        Server server = new Server();
        server.start();
    }

    private void start() throws IOException {
        ServerSocket serverSocket = new ServerSocket(PORT);

        ExecutorService executorService = Executors.newFixedThreadPool(3);
        while (true) {
            System.out.println("waiting for client request");
            Socket accept = serverSocket.accept();
            executorService.submit(() -> {
                try {
                    processRequest(accept);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        }
    }

    private void processRequest(Socket socket) throws IOException, InterruptedException {
        System.out.println("started serving at " + LocalTime.now() + " for thread " + Thread.currentThread().getName());
        OutputStream outputStream = socket.getOutputStream();
        byte[] buffer = new byte[10];
        InputStream stream = new BufferedInputStream(new FileInputStream("C:\\Office\\persistence\\src\\test\\resources\\datafeed"));
        int read = stream.read(buffer);
        while (read != -1) {
            outputStream.write(buffer);
            Thread.sleep(1000 * 2);
            read = stream.read(buffer);
        }
        outputStream.flush();
        outputStream.close();
    }
}
