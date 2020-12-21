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

        boolean userInputFlag = false;
        boolean passInputFlag = false;
        while (!userInputFlag) {
            System.out.print("Enter username: ");
            msg = br_user.readLine(); // get username
            username = msg;
            bw.write(msg);
            bw.newLine();
            bw.flush();
            read_from_server = br_server.readLine();
            if ("already logged in".equals(read_from_server)) {
                System.out.println(username + " has already logged in");
                continue;
            } else if ("new user".equals(read_from_server)) {
                System.out.print("Enter new password for " + username + ": ");
                String newPassword = br_user.readLine();

                bw.write(newPassword);
                bw.newLine();
                bw.flush();
                userInputFlag = true;
                passInputFlag = true;
            } else {
                userInputFlag = true;
            }
        }

        // check password
        while (!passInputFlag) {
            System.out.print("Enter password: ");
            msg = br_user.readLine();
            bw.write(msg);
            bw.newLine();
            bw.flush();
            read_from_server = br_server.readLine();
            if ("Invalid password".equals(read_from_server)) {
                System.out.println("Invalid password");
                passInputFlag = false;
            } else {
                System.out.println(read_from_server);
                passInputFlag = true;
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
                        System.out.println("Incorrect syntax for CRT, Usage: CRT threadtitle");
                        continue;
                    }
                    bw.write(input);
                    bw.newLine();
                    bw.flush();

                    feedback = br_server.readLine();
                    System.out.println(feedback);
                    continue;
                } else if (input.startsWith("MSG")) {
                    if (inputArr.length < 3) {
                        System.out.println("Incorrect syntax for MSG, Usage: MSG threadtitle message");
                        continue;
                    }
                    bw.write(input);
                    bw.newLine();
                    bw.flush();

                    feedback = br_server.readLine();
                    System.out.println(feedback);
                    continue;
                } else if (input.startsWith("DLT")) {
                    if (inputArr.length != 3) {
                        System.out.println("Incorrect syntax for DLT, Usage: DLT threadtitle messagenumber");
                        continue;
                    }
                    bw.write(input);
                    bw.newLine();
                    bw.flush();

                    feedback = br_server.readLine();
                    System.out.println(feedback);
                    continue;
                } else if (input.startsWith("EDT")) { // EDT threadtitle messagenumber message
                    if (inputArr.length < 4) {
                        System.out.println("Incorrect syntax for EDT, Usage: EDT threadtitle messagenumber message");
                        continue;
                    }
                    bw.write(input);
                    bw.newLine();
                    bw.flush();

                    feedback = br_server.readLine();
                    System.out.println(feedback);
                    continue;
                } else if (input.startsWith("LST")) {
                    if (inputArr.length != 1) {
                        System.out.println("Incorrect syntax for LST, Usage: LST");
                        continue;
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

                    System.out.println("The list of active threads:");
                    while (count > 0) {
                        feedback = br_server.readLine();
                        System.out.println(feedback);
                        count--;
                    }

                    continue;
                } else if (input.startsWith("RDT")) {
                    if (inputArr.length != 2) {
                        System.out.println("Incorrect syntax for RDT, Usage: RDT threadtitle");
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
                } else if (input.startsWith("UPD")) { // UPD threadtitle filename
                    if (inputArr.length != 3) {
                        System.out.println("Incorrect syntax for UPD, Usage: UPD threadtitle filename");
                        continue;
                    }
                    bw.write(input);
                    bw.newLine();
                    bw.flush();
                    String remain = input.substring(input.indexOf(" ") + 1); // threadtitle filename
                    String thread = remain.substring(0, remain.indexOf(" ")); // threadtitle
                    String filePath = remain.substring(remain.indexOf(" ") + 1); // filename
                    feedback = br_server.readLine();
                    if (("Thread " + thread + " does not exists").equals(feedback)) {
                        System.out.println(feedback);
                        continue;
                    } else if ((filePath + " exists").equals(feedback)) {
                        System.out.println(feedback);
                        continue;
                    }

                    if ("OK".equals(feedback)) {
                        if ((new File(filePath)).exists()) {
                            bw.write("Start send file");
                            bw.newLine();
                            bw.flush();

                            FileReader fileReader = new FileReader(filePath);
                            BufferedReader bufferedReader = new BufferedReader(fileReader);
                            String str = null;
                            while ((str = bufferedReader.readLine()) != null) {
                                bw.write(str);
                                bw.newLine();
                                bw.flush();
                            }
                            bw.write("File upload Done");
                            bw.newLine();
                            bw.flush();
                            bufferedReader.close();
                            fileReader.close();

                            System.out.println(filePath + " uploaded to " + thread + " thread");
                        } else {
                            bw.newLine();
                            bw.flush();
                            System.out.println(filePath + " not exists");
                        }

                    }
                    continue;
                } else if (input.startsWith("DWN")) { // DWN threadtitle filename
                    if (inputArr.length != 3) {
                        System.out.println("Incorrect syntax for DWN, Usage: DWN threadtitle filename");
                        continue;
                    }
                    bw.write(input);
                    bw.newLine();
                    bw.flush();
                    String remain = input.substring(input.indexOf(" ") + 1); // threadtitle filename
                    String thread = remain.substring(0, remain.indexOf(" ")); // threadtitle 
                    String filePath = remain.substring(remain.indexOf(" ") + 1); // filename

                    feedback = br_server.readLine();
                    if ("Thread not exists".equals(feedback)) {
                        System.out.println("Thread " + thread + " does not exists");
                        continue;
                    } else if ("file does not exists".equals(feedback)) {
                        System.out.println("File does not exist in Thread " + thread);
                        continue;
                    } else {
                        String line = null;
                        FileWriter fileWriter = new FileWriter(filePath);
                        BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
                        while ((line = br_server.readLine()) != null) {
                            if ("File transfer done".equals(line)) {
                                break;
                            }
                            bufferedWriter.write(line);
                            bufferedWriter.newLine();
                            bufferedWriter.flush();
                        }
                        fileWriter.close();
                        bufferedWriter.close();
                        System.out.println(filePath + " successfully download");
                    }

                    continue;

                } else if (input.startsWith("RMV")) { // RMV threadtitle
                    if (inputArr.length != 2) {
                        System.out.println("Incorrect syntax for RMV, Usage: RMV threadtitle");
                        continue;
                    }
                    String thread = input.substring(input.indexOf(" ") + 1);
                    bw.write(input);
                    bw.newLine();
                    bw.flush();

                    feedback = br_server.readLine();
                    if ("Thread not exists".equals(feedback)) {
                        System.out.println(thread + "not exists");
                        continue;
                    } else if ("Create by other user".equals(feedback)) {
                        System.out.println("Thread " + thread + " was created by another user and cannot be removed");
                    } else {
                        System.out.println("Thread " + thread + " removed");
                    }
                    continue;
                } else if (input.startsWith("XIT")) {
                    if (inputArr.length != 1) {
                        System.out.println("Invalid commands, Usage: XIT");
                    }
                    bw.write(input);
                    bw.newLine();
                    bw.flush();
                    System.out.println("Goodbye");
                    break;
                } else if (input.startsWith("SHT")) { // SHT admin_pass
                    if (inputArr.length != 2) {
                        System.out.println("Incorrect syntax for SHT, Usage: SHT admin_pass");
                        continue;
                    }
                    bw.write(input);
                    bw.newLine();
                    bw.flush();

                    String line = br_server.readLine();
                    if ("Goodbye. Server shutting down".equals(line)) {
                        System.out.println("Goodbye. Server shutting down");
                        break;
                    } else {
                        System.out.println("Wrong admin password");
                        continue;
                    }

                } else {
                    System.out.println("Invalid commands");
                    continue;
                }


            }
        } catch (SocketTimeoutException e) {
            // System.out.println("Goodbye. Server shutting down");
        } catch (IOException e) {
            System.out.println("123456");
            e.printStackTrace();
        } catch (NumberFormatException e) {
        }

        // close all
        socket.close();
        System.exit(0);

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
        } catch (Exception e) {
            try {
                socket.close();
            } catch (IOException ioException) {
                System.out.println("\nGoodbye. Server shutting down");
                System.exit(1);
            }
            System.out.println("\nGoodbye. Server shutting down");
            System.exit(1);
        }

    }
}


