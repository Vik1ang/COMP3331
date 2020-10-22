import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Random;

public class PingClient {
    private static final int TIME_OUT = 600; // time out
    private static final int AVERAGE_DELAY = 100;  // milliseconds

    public static void main(String[] args) throws Exception {
        // Get command line argument
        if (args.length != 2) {
            System.out.println("Required arguments: host port");
            return;
        }
        InetAddress hostAddress = InetAddress.getByName(args[0]);
        int port = Integer.parseInt(args[1]);

        // Create random number generator for use in simulating
        // packet loss and network delay.
        Random random = new Random();

        // create socket which connects to server
        DatagramSocket clientSocket = new DatagramSocket();

        long maxRtt = Long.MIN_VALUE;
        long minRtt = Long.MAX_VALUE;
        long delay = 0;
        int count = 0;
        long totalRtt = 0;
        int seq = 3331;
        while (seq <= 3345) {
            long sendTime = System.currentTimeMillis();
            String send = "PING " + "seq = " + seq + " " + sendTime + "\r\n";

            byte[] buf = send.getBytes();

            DatagramPacket clientPacket = new DatagramPacket(buf, buf.length, hostAddress, port);

            // Simulate network delay
            Thread.sleep((int) (random.nextDouble() * 2 * AVERAGE_DELAY));

            try {
                // send
                clientSocket.send(clientPacket);
                // set time out to 1 sec
                clientSocket.setSoTimeout(TIME_OUT);
                // receive reply
                DatagramPacket responsePacket = new DatagramPacket(new byte[1024], 1024);
                clientSocket.receive(responsePacket);
                long receiveTime = System.currentTimeMillis();
                delay = receiveTime - sendTime;
                totalRtt += delay;
                printData(responsePacket, delay, seq);
                count++;
            } catch (Exception e) {
                System.out.println("ping to " + args[0] + ", seq = " + seq + ", time out");
            } finally {
                if (delay > maxRtt) {
                    maxRtt = delay;
                }
                if (delay < minRtt) {
                    minRtt = delay;
                }
            }
            seq++;
        }
        System.out.println("Minimum rtt :" + minRtt + " ms");
        System.out.println("Maximum rtt :" + maxRtt + " ms");
        System.out.println("Average rtt :" + totalRtt / count + " ms");

    }

    /*
     * Print ping data to the standard output stream.
     * this one imitates PingServer.java
     */
    private static void printData(DatagramPacket response, long delay, int sequence_Number) throws Exception {
        // Obtain references to the packet's array of bytes.
        // byte[] buf = response.getData();

        // Wrap the bytes in a byte array input stream,
        // so that you can read the data as a stream of bytes.
        // ByteArrayInputStream bais = new ByteArrayInputStream(buf);

        // Wrap the byte array output stream in an input stream reader,
        // so you can read the data as a stream of characters.
        // InputStreamReader isr = new InputStreamReader(bais);

        // Wrap the input stream reader in a bufferred reader,
        // so you can read the character data a line at a time.
        // (A line is a sequence of chars terminated by any combination of \r and \n.)
        // BufferedReader br = new BufferedReader(isr);

        // The message data is contained in a single line, so read this line.
        // String line = br.readLine();

        // Print host address and data received from it.
        System.out.println(
                "ping to " +
                        response.getAddress().getHostAddress() +
                        ", " + "seq = " + sequence_Number +
                        ", rtt = " + delay + " ms");
    }
}
