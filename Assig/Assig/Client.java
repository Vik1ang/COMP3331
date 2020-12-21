import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;


public class Client {
    public static boolean flag = true;
    public static String command[] = {"CRT", "MSG", "DLT", "EDT", "LST", "RDT", "UPD", "DWN", "RMV", "XIT", "SHT"};

    // public static ArrayList

    public static void main(String[] args) throws IOException {

        if (args.length != 2) {
            System.out.println("Usage: java Client server_IP server_port");
            System.exit(1);
        }

        // ip address and port number
        InetAddress IpAddr = InetAddress.getByName(args[0]);
        int server_port = Integer.parseInt(args[1]);

        // 1. create socket
        Socket socket = new Socket(IpAddr, server_port);
        socket.setKeepAlive(true);
        // socket.setSoTimeout(3000);
        ThreadSend threadSend = new ThreadSend(socket);
        threadSend.start();

        // get input from keyboard
        BufferedReader br_user = new BufferedReader(new InputStreamReader(System.in));

        // get input from socket(server)
        BufferedReader br_server = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        // send
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        // get input from user and send to server
        String msg = null;
        String read_from_server;

        // username check
        String username = null;
        while (flag) {
            System.out.print("Enter username: ");
            msg = br_user.readLine(); // get username
            username = msg;
            bw.write(msg);
            bw.newLine();
            bw.flush();
            read_from_server = br_server.readLine();
            if ("invalid username".equals(read_from_server)) {
                System.out.println("Invalid username");
                flag = true;
            } else {
                flag = false;
            }
        }
        flag = true;

        // check password
        while (flag) {
            System.out.print("Enter password: ");
            msg = br_user.readLine();
            bw.write(msg);
            bw.newLine();
            bw.flush();
            read_from_server = br_server.readLine();
            if ("Invalid password".equals(read_from_server)) {
                System.out.println("Invalid password");
                flag = true;
            } else {
                System.out.println(read_from_server);
                flag = false;
            }
        }

        flag = true;
        String feedback = null;
        boolean exitClient = false;
        try {
            while (true) {
                System.out.print("Enter one of the following commands: CRT MSG, DLT, EDT, LST, RDT, UPD, DWN, RMV, XIT, SHT: ");
                String input = br_user.readLine();
                String[] inputArr = input.split(" ");
                if (input.startsWith("CRT")) {
                    if (inputArr.length != 2) {
                        System.out.println("Wrong commands, Usage: CRT threadtitle");
                        continue;
                    }
                    bw.write(input);
                    bw.newLine();
                    bw.flush();

                    feedback = br_server.readLine();
                    System.out.println(feedback);
                    continue;
                }
                else if (input.startsWith("MSG")) {
                    if (inputArr.length < 3) {
                        System.out.println("Wrong commands, Usage: MSG threadtitle message");
                        continue;
                    }
                    bw.write(input);
                    bw.newLine();
                    bw.flush();

                    feedback = br_server.readLine();
                    System.out.println(feedback);
                    continue;
                }

                else if (input.startsWith("DLT")) {
                    if (inputArr.length != 3) {
                        System.out.println("Wrong commands, Usage: DLT threadtitle messagenumber");
                        continue;
                    }
                    bw.write(input);
                    bw.newLine();
                    bw.flush();

                    feedback = br_server.readLine();
                    System.out.println(feedback);
                    continue;
                }
                else if (input.startsWith("EDT")) { // EDT threadtitle messagenumber message
                    if (inputArr.length < 4) {
                        System.out.println("Wrong commands, Usage: EDT threadtitle messagenumber message");
                        continue;
                    }
                    bw.write(input);
                    bw.newLine();
                    bw.flush();

                    feedback = br_server.readLine();
                    System.out.println(feedback);
                    continue;
                }
                else if (input.startsWith("LST")) {
                    if (inputArr.length != 1) {
                        System.out.println("Wrong commands, Usage: LST");
                    }
                    bw.write(input);
                    bw.newLine();
                    bw.flush();

                    feedback = br_server.readLine();
                    int count = Integer.parseInt(feedback);

                    if (count == 0) {
                        System.out.println("No threads to list");
                        continue;
                    }

                    while(count > 0) {
                        feedback = br_server.readLine();
                        System.out.println(feedback);
                        count--;
                    }
                    
                    continue;
                }

                else if (input.startsWith("RDT")) {
                    if (inputArr.length != 2) {
                        System.out.println("Wrong commands, Usage: RDT threadtitle");
                        continue;
                    }
                    bw.write(input);
                    bw.newLine();
                    bw.flush();

                    String threadTitle = input.substring(input.indexOf(" ") + 1);
                    feedback = br_server.readLine();
                    if (("Thread " + threadTitle + " does not exist").equals(feedback)) {
                        System.out.println(feedback);
                        continue;
                    } else if (("Thread " + threadTitle + " is empty").equals(feedback)) {
                        System.out.println(feedback);
                        continue;
                    }

                    int countRdt = Integer.parseInt(feedback);
                    while (countRdt > 0) {
                        feedback = br_server.readLine();
                        System.out.println(feedback);
                        countRdt--;
                    }
                    continue;
                }

                else if (input.startsWith("UPD")) { // UPD threadtitle filename
                    if (inputArr.length != 3) {
                        System.out.println("Wrong commands, Usage: UPD threadtitle filename")
                    }
                    bw.write(input);
                    bw.newLine();
                    bw.flush();
                    String remain = input.substring(input.indexOf(" ") + 1); // threadtitle filename
                    String thread = input.substring(0, remain.indexOf(" ")); // threadtitle
                    String filePath = remain.substring(remain.indexOf(" ") + 1); // filename
                    feedback = br_server.readLine();
                    if (("Thread " + thread + " does not exists").equals(feedback)) {
                        System.out.println(feedback);
                        continue;
                    } else {
                        FileReader fileReader = new FileReader(filePath);
                        BufferedReader bufferedReader = new BufferedReader(fileReader);
                        String str = null;
                        while ((str = bufferedReader.readLine()) != null) {
                            bw.write(str);
                            bw.newLine();
                            bw.flush();
                        }
                        bw.write("DoneDoneDoneDoneDone666Done");
                        bw.newLine();
                        bw.flush();
                        bufferedReader.close();
                        fileReader.close();
                    }
                    continue;
                }

                else if (input.startsWith("DWN")) { // DWN threadtitle filename
                    bw.write(input);
                    bw.newLine();
                    bw.flush();
                    String remain = input.substring(input.indexOf(" ") + 1); // threadtitle filename
                    String filePath = remain.substring(remain.indexOf(" ") + 1);
                    feedback = br_server.readLine();
                    if (!"Ok".equals(feedback)) {
                        System.out.println(feedback);
                        continue;
                    } else {
                        String line = null;
                        FileWriter fileWriter = new FileWriter(filePath);
                        BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
                        while ((line = br_server.readLine()) != null) {
                            if ("DoneDoneDoneDoneDone666Done".equals(line)) {
                                break;
                            }
                            bufferedWriter.write(line);
                            bufferedWriter.newLine();
                            bufferedWriter.flush();
                        }
                        fileWriter.close();
                        bufferedWriter.close();
                    }
                    continue;

                }

                else if (input.startsWith("XIT")) {
                    bw.write(input);
                    bw.newLine();
                    bw.flush();
                    System.out.println("Goodbye");
                    break;
                }

                else if (input.startsWith("SHT")) { // SHT admin_pass
                    bw.write(input);
                    bw.newLine();
                    bw.flush();
                    System.out.println("1234566");
                    br_server.readLine();
                    String line = br_server.readLine();
                    System.out.println(line);
                    if (!"Goodbye. Server shutting down".equals(line)) {
                        System.out.println("Wrong admin password");
                        continue;
                    } else {
                        System.out.println("Goodbye. Server shutting down");
                        break;
                    }

                }


                // bw.write(input);
                // bw.newLine();
                // bw.flush();

                // feedback = br_server.readLine();
                // System.out.println(feedback);


            }
        } catch (SocketTimeoutException e) {
            System.out.println("Goodbye. Server shutting down");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NumberFormatException e) {
        }

        // close all
        socket.close();

    }


}

class ThreadSend extends Thread {
    private Socket socket;

    ThreadSend(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            while (true) {
                socket.sendUrgentData(0xff);
                Thread.sleep(3000);
            }
        } catch (SocketException e) {
            try {
                socket.close();
                System.exit(1);
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}


