import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;

public class WebServer {
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Required arguments: port");
            return;
        }
        int port = Integer.parseInt(args[0]);

        System.out.println("Server Start listening on : " + port);
        System.out.println("Connection from 127.0.0.1:" + port);

        // (i) create a connection socket when contacted by a client (browser).
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            e.printStackTrace();
        }
        while (true) {
            Socket socket = null;
            StringBuilder request = null;
            try {
                assert serverSocket != null;
                socket = serverSocket.accept();

                // (ii) receive HTTP request from this connection.
                InputStream is = socket.getInputStream();
                BufferedReader br = new BufferedReader(new InputStreamReader(is));
                request = new StringBuilder();
                String line;
                while (!(line = br.readLine()).isBlank()) {
                    request.append(line).append("\r\n");
                }

                System.out.println(request.toString());
            } catch (IOException e) {
                e.printStackTrace();
            }


            // (iii) parse the request to determine the specific file being requested.
            assert request != null;
            String path = request.toString().split("\r\n")[0].split(" ")[1];
            // System.out.println(path.substring(1));

            String fileName = path.substring(1);

            int index = path.indexOf(".");
            if (index == -1) index = 0;
            String content = path.substring(index);
            // System.out.println(content);
            String contentType;
            if (".png".equals(content)) {
                contentType = "image/png";
            } else {
                contentType = "text/html";
            }

            try {
                // File file = new File("./src/lab3/exercise4/".concat(fileName)); // for Intellij idea
                File file = new File(fileName); // for vlab
                byte[] buf;
                OutputStream os = socket.getOutputStream();
                Date date = new Date();
                if (file.exists()) {

                    // (iv) get the requested file from the server's file system.
                    DataInputStream in = new DataInputStream(new FileInputStream(file));
                    int length = (int) file.length();
                    buf = new byte[length];
                    in.readFully(buf);
                } else {
                    os.write("HTTP/1.1 404 NOT Found \r\n".getBytes());
                    os.write(("ContentType: " + contentType + "\r\n").getBytes());
                    os.write(("Date: " + date.toString() + "\r\n").getBytes());
                    os.write("\r\n".getBytes()); // if not add this line the html do not show
                    os.write("<h1 color='red'>404 Not Found</h1>".getBytes());
                    os.write("\r\n".getBytes());
                    os.flush();
                    os.close();
                    System.out.println("File Request Fail");
                    continue;
                }


                // (v) create an HTTP response message consisting of the requested file preceded by header lines.
                // (vi) send the response over the TCP connection to the requesting browser.
                os.write("HTTP/1.1 200 OK \r\n".getBytes());
                // TODO: ContentType need be fixed
                os.write(("Content-Length: " + file.length() + "\r\n").getBytes());
                os.write(("ContentType: " + contentType + "\r\n").getBytes());
                os.write(("Date: " + date.toString() + "\r\n").getBytes());
                os.write("\r\n".getBytes()); // if not add this line the html do not show
                os.write(buf);
                os.write("\r\n".getBytes());
                os.flush();
                os.close();
                System.out.println("File Request Successfully");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }
}
