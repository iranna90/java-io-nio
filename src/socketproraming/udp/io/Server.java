package socketproraming.udp.io;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Created by iranna.patil on 20-11-2016.
 */
public class Server {

    private static final int PORT = 5678;
    private static final String MULTICAST_ADDRESS = "224.0.0.3";

    public static void main(String[] args) throws IOException {
        Server server = new Server();
        server.sendDataGrams();
    }

    private void sendDataGrams() throws IOException {
        Path path = Paths.get("C:\\Office\\persistence\\src\\test\\resources\\datafeed");
        MulticastSocket multicastSocket = new MulticastSocket();
        Files.lines(path).map(String::getBytes).forEach(data -> {
            try {
                System.out.println("sending    "+new String(data,"UTF-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            // create data gram and send after each second
            try {
                InetAddress inetAddress = InetAddress.getByName(MULTICAST_ADDRESS);
                DatagramPacket dataGram = new DatagramPacket(data, data.length, inetAddress, PORT);
                multicastSocket.send(dataGram);
                Thread.sleep(1000 * 2);
            } catch (SocketException e) {
                e.printStackTrace();
            } catch (UnknownHostException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
    }
}
