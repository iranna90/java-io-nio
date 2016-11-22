package socketproraming.udp.io;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

/**
 * Created by iranna.patil on 20-11-2016.
 */
public class Client {

    private static final int PORT = 5678;
    private static final String MULTICAST_ADDRESS = "224.0.0.3";

    public static void main(String[] args) throws IOException, InterruptedException {

        Client client = new Client();
        client.receiveDataGrams();

    }

    private void receiveDataGrams() throws IOException, InterruptedException {
        InetAddress inetAddress = InetAddress.getByName(MULTICAST_ADDRESS);
        System.out.println(inetAddress);
        byte[] data = new byte[1028];
        MulticastSocket multicastSocket = new MulticastSocket(PORT);
        multicastSocket.joinGroup(inetAddress);
        while (true) {
            DatagramPacket packet = new DatagramPacket(data, data.length);
            multicastSocket.receive(packet);
            byte[] packetData = packet.getData();
            boolean socketAddress = packet.getAddress().isMulticastAddress();
            System.out.println( new String(packetData, "UTF-8"));
            data = new byte[1028];
            Thread.sleep(1000 * 2);
        }
    }
}
