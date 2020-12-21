import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.*;

public class Server {

    public static String[] commands = {"CRT", "MSG", "DLT", "EDT", "LST", "RDT", "UPD", "DWN", "RMV", "XIT", "SHT"};
    public static String adminPass = "";
    public static boolean systemShutdown = false;
    public static boolean exitFlagOut = false;


    public static void main(String[] args) throws IOException {
        if (args.length != 2) {
            System.out.println("Usage: java server_port admin_passwd");
            System.exit(1);
        }
        adminPass = args[1];
        // port number
        int server_port = Integer.parseInt(args[0]);
        System.out.println("Waiting for clients");
        // create socket
        ServerSocket serverSocket = new ServerSocket(server_port);

        Socket socket = null;
        while (!systemShutdown) {
            try {
                socket = serverSocket.accept();
                // socket.setOOBInline(true);
                // serverSocket.setSoTimeout(900 * 1000);
            } catch (SocketException e) {
                break;
            }
            System.out.println("Client connected");
            SocketThread socketThread = new SocketThread(socket, serverSocket);
            socketThread.start();
        }
        System.out.println("Server shutting down");
        serverSocket.close();
        System.exit(1);
    }


}

class User {
    public static String username;
    public static List<String> UserList = new ArrayList<String>();
}


class SocketThread extends Thread {
    private Socket socket;
    private ServerSocket serverSocket;
    private boolean check_username = false;
    private boolean login_flag = false;
    private boolean check_password = false;
    private boolean exitFlag = false;

    public SocketThread(Socket socket, ServerSocket serverSocket) {
        this.socket = socket;
        this.serverSocket = serverSocket;
    }

    @Override
    public void run() {
        while (!Server.exitFlagOut) {
            String username = null;
            BufferedReader br_client = null;
            BufferedWriter bw = null;
            try {
                br_client = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            } catch (IOException e) {
                // e.printStackTrace();
                break;
            }
            while (!login_flag) {
                if (!check_password) {
                    if (!check_username) {
                        try {
                            // System.out.println(User.UserList.contains(username));
                            // if (User.UserList.contains(username)) {
                            //     System.out.println(username + " has already logged in");
                            //     bw.write("already logged in");
                            //     bw.newLine();
                            //     bw.flush();
                            //     continue;
                            // }
                            username = UserAuthentication(br_client, bw);
                        } catch (IOException e) {
                            // e.printStackTrace();
                            break;
                        }
                        continue;
                    }
                    try {
                        PasswordAuthentication(br_client, bw, username);
                        User.UserList.add(username);
                    } catch (IOException e) {
                        // e.printStackTrace();
                        break;
                    }
                }
            }

            while (!exitFlag) {
                // String command = null;
                try {
                    // command = br_client.readLine();
                    MainFeature(username, br_client, bw);
                } catch (SocketException e) {
                    break;
                } catch (IOException e) {
                    // e.printStackTrace();
                    break;
                } catch (NullPointerException e) {
                    break;
                }
            }
            // Server.systemShutdown = true;
            try {
                if (br_client != null) {
                    br_client.close();
                }
                if (bw != null) {
                    bw.close();
                }
                if (socket != null) {
                    socket.close();
                }
                if (serverSocket != null && Server.systemShutdown) {
                    serverSocket.close();
                }
            } catch (IOException e) {
                // e.printStackTrace();
            }

            // try {
            //     serverSocket.close();
            // } catch (IOException e) {
            //     e.printStackTrace();
            // }
        }

    }

    public synchronized String UserAuthentication(BufferedReader br, BufferedWriter bw) throws IOException {
        String filePath = "credentials.txt";
        File CredFile = new File(filePath);
        if (!CredFile.exists()) {
            CredFile.createNewFile();
        }
        BufferedReader bufferedReader = null;
        try {
            bufferedReader = new BufferedReader(new FileReader(new File(filePath)));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        HashMap<String, String> hashMap = new HashMap<>();
        String s = null;
        while ((s = bufferedReader.readLine()) != null) {
            int index = s.indexOf(" ");
            String name = s.substring(0, index);
            String pwd = s.substring(index + 1);
            hashMap.put(name, pwd);
        }
        bufferedReader.close();
        String username = br.readLine();
        if (User.UserList.contains(username)) {
            System.out.println(username + " has already logged in");
            bw.write("already logged in");
            bw.newLine();
            bw.flush();
            return " ";
        } else if (hashMap.containsKey(username)) {
            bw.write("valid username");
            bw.newLine();
            bw.flush();
            check_username = true;
        } else {
            bw.write("new user");
            bw.newLine();
            bw.flush();

            bufferedReader = new BufferedReader(new FileReader(new File(filePath)));
            int countCred = 0;
            while ((bufferedReader.readLine()) != null) {
                countCred++;
            }


            String newUserPass = br.readLine();
            BufferedWriter writer = null;
            writer = new BufferedWriter(new FileWriter(filePath, true));
            if (countCred == 0) {
                String content = username + " " + newUserPass;
                writer.write(content);
                writer.flush();
            } else {
                writer.write("\n" + username + " " + newUserPass);
                writer.flush();
            }
            writer.close();

            System.out.println("New user");
            User.UserList.add(username);
            check_username = true;
            check_password = true;
            login_flag = true;
        }

        return username;
    }

    public synchronized void PasswordAuthentication(BufferedReader br, BufferedWriter bw, String username) throws IOException {
        String filePath = "credentials.txt";
        BufferedReader bufferedReader = new BufferedReader(new FileReader(new File(filePath)));
        HashMap<String, String> hashMap = new HashMap<>();
        String s = null;
        while ((s = bufferedReader.readLine()) != null) {
            int index = s.indexOf(" ");
            String name = s.substring(0, index);
            String pwd = s.substring(index + 1);
            hashMap.put(name, pwd);
        }
        bufferedReader.close();
        String password = br.readLine();
        if (hashMap.get(username).equals(password)) {
            System.out.println(username + " successful login");
            bw.write("Welcome to the forum");
            check_password = true;
            login_flag = true;
        } else {
            System.out.println("Incorrect password");
            bw.write("Invalid password");
            check_password = false;
        }
        bw.newLine();
        bw.flush();
    }

    public synchronized void MainFeature(String username, BufferedReader br_client, BufferedWriter bw) throws IOException {

        String filePath = null;
        File file = null;
        String command = br_client.readLine();
        String prefix = command.substring(0, 3);
        List<String> commandList = Arrays.asList(Server.commands);
        if (!commandList.contains(prefix)) {
            bw.write("Invalid command");
            bw.newLine();
            bw.flush();
            return;
        }

        switch (prefix) {
            case "CRT": // CRT 3331
                String suffix = command.substring(command.indexOf(" ") + 1); // 3331
                System.out.println(username + " issued CRT command");
                filePath = suffix;
                file = new File(filePath);
                BufferedWriter writer = null;
                if (!file.exists()) {
                    file.createNewFile();
                    writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filePath)));
                    writer.write(username + "\n");
                    writer.flush();
                    writer.close();

                    bw.write("Thread " + suffix + " created");
                    System.out.println("Thread " + suffix + " created");
                } else {
                    bw.write("Thread " + suffix + " exists");
                    System.out.println("Thread " + suffix + " exists");
                }
                bw.newLine();
                bw.flush();
                break;

            case "MSG":
                System.out.println(username + " issued MSG command");
                String remain_msg = command.substring(command.indexOf(" ") + 1);
                String threadName_msg = remain_msg.substring(0, remain_msg.indexOf(" "));
                String message_msg = remain_msg.substring(remain_msg.indexOf(" ") + 1);
                filePath = threadName_msg;
                file = new File(filePath);
                if (!file.exists()) {
                    bw.write("Thread name not exists");
                    bw.newLine();
                    bw.flush();
                } else {
                    int countLineMsg = 0;
                    BufferedReader bufferedReader_msg = new BufferedReader(new InputStreamReader(new FileInputStream(filePath)));
                    String tempMsg = null;
                    while((tempMsg = bufferedReader_msg.readLine()) != null) {
                        try {
                            countLineMsg = Integer.parseInt(tempMsg.substring(0, tempMsg.indexOf(" ")));
                        } catch (Exception e) {
                        }
                    }
                    
                    writer = new BufferedWriter(new FileWriter(file, true));
                    String content = ++countLineMsg + " " + username + ": " + message_msg;
                    writer.write(content);
                    writer.newLine();
                    writer.flush();
                    writer.close();
                    System.out.println("Message posted to " + threadName_msg + " thread");
                    bw.write(username + " posted to " + threadName_msg + " thread");
                    bw.newLine();
                    bw.flush();
                }
                break;
            case "DLT": // DLT threadtitle messagenumber
                System.out.println(username + " issued DLT command");
                String remain_dlt = command.substring(command.indexOf(" ") + 1); // threadtitle messagenumber
                String threadName_dlt = remain_dlt.substring(0, remain_dlt.indexOf(" ")); // threadtitle
                int messageNumber_dlt = 0;
                try {
                    messageNumber_dlt = Integer.parseInt(remain_dlt.substring(remain_dlt.indexOf(" ") + 1)); // message number
                } catch (NumberFormatException e) {
                    bw.write("Invalid message number");
                    bw.newLine();
                    bw.flush();
                    break;
                }

                filePath = threadName_dlt;
                File fileDtl = new File(filePath);
                if (!fileDtl.exists()) {
                    bw.write("Thread name not exists");
                    bw.newLine();
                    bw.flush();
                    break;
                }
                BufferedReader bufferedReader_dlt = new BufferedReader(new InputStreamReader(new FileInputStream(filePath)));
                StringBuffer stringBuffer_dlt = new StringBuffer();

                int totalLine = 0;
                String line_dlt = null;
                while ((line_dlt = bufferedReader_dlt.readLine()) != null) {
                    totalLine++;
                    try {
                        Integer.parseInt(line_dlt.substring(0, line_dlt.indexOf(" ")));
                    } catch (Exception e) {
                        totalLine--;
                    }
                }
                bufferedReader_dlt.close();
                if (messageNumber_dlt > totalLine) {
                    bw.write("Invalid message number");
                    bw.newLine();
                    bw.flush();
                    break;
                }

                bufferedReader_dlt = new BufferedReader(new InputStreamReader(new FileInputStream(filePath)));
                line_dlt = null;
                int start_dlt = 0;
                int lineNum_dlt = 0;
                boolean dlt_flag = true;
                while ((line_dlt = bufferedReader_dlt.readLine()) != null) {
                    int temp_line = start_dlt;
                    try {
                        start_dlt = Integer.parseInt(line_dlt.substring(0, line_dlt.indexOf(" ")));
                    } catch (Exception e) {
                        start_dlt = temp_line;
                        stringBuffer_dlt.append(line_dlt);
                        stringBuffer_dlt.append("\n");
                        continue;
                    }
                    if (start_dlt == messageNumber_dlt) { // No. Username message
                        String first_cut = line_dlt.substring(line_dlt.indexOf(" ") + 1); // Username message
                        String user_check = first_cut.substring(0, first_cut.indexOf(" "));
                        if (!(username + ":").equals(user_check)) {
                            bw.write("The message belongs to another user and cannot be deleted");
                            bw.newLine();
                            bw.flush();
                            bufferedReader_dlt.close();
                            return;
                        }
                        dlt_flag = false;
                        lineNum_dlt++;
                        continue;
                    }

                    if (!dlt_flag) {
                        String temp = line_dlt.substring(line_dlt.indexOf(" ") + 1);
                        String update = lineNum_dlt + " " + temp;
                        stringBuffer_dlt.append(update);
                    } else {
                        stringBuffer_dlt.append(line_dlt);
                    }
                    lineNum_dlt++;
                    stringBuffer_dlt.append("\n");
                }
                bufferedReader_dlt.close();

                FileOutputStream fos_dlt = new FileOutputStream(new File(filePath));
                PrintWriter printWriterDlt = new PrintWriter(fos_dlt);
                printWriterDlt.write(stringBuffer_dlt.toString().toCharArray());
                printWriterDlt.flush();
                printWriterDlt.close();
                System.out.println(username + " delete a message");
                bw.write("The message has been deleted");
                bw.newLine();
                bw.flush();

                break;
            case "EDT":
                // EDT threadtitle messagenumber message
                System.out.println(username + " issued EDT command");
                String remain_edt = command.substring(command.indexOf(" ") + 1); // threadtitle messagenumber message
                String threadName_edit = remain_edt.substring(0, remain_edt.indexOf(" ")); // threadtitle
                String temp_edt = remain_edt.substring(remain_edt.indexOf(" ") + 1); // messagenumber message
                int messageNumber = Integer.parseInt(temp_edt.substring(0, temp_edt.indexOf(" "))); // messagenumber
                String message_edt = temp_edt.substring(temp_edt.indexOf(" ") + 1); // message

                //
                filePath = threadName_edit;

                File file_edt = new File(filePath);
                if (!file_edt.exists()) {
                    bw.write("Thread not exists");
                    bw.newLine();
                    bw.flush();
                    break;
                }

                BufferedReader bufferedReader_edt = new BufferedReader(new InputStreamReader(new FileInputStream(filePath)));
                int countMessageNum = 0;
                String line_edt = null;
                while ((line_edt = bufferedReader_edt.readLine()) != null) {
                    countMessageNum++;
                    try {
                        Integer.parseInt(line_edt.substring(0, line_edt.indexOf(" ")));
                    } catch (Exception e) {
                        countMessageNum--;
                    }
                }

                bufferedReader_edt.close();

                if (messageNumber > countMessageNum) {
                    bw.write("Invalid message number");
                    bw.newLine();
                    bw.flush();
                    break;
                }

                bufferedReader_edt = new BufferedReader(new InputStreamReader(new FileInputStream(filePath)));

                StringBuffer stringBuffer = new StringBuffer();
                int start;
                while ((line_edt = bufferedReader_edt.readLine()) != null) {
                    try {
                        start = Integer.parseInt(line_edt.substring(0, line_edt.indexOf(" ")));
                    } catch (Exception e) {
                        stringBuffer.append(line_edt);
                        stringBuffer.append("\n");
                        continue;
                    }
                    if (start == messageNumber) {
                        // check permissions
                        String first_cut = line_edt.substring(line_edt.indexOf(" ") + 1); // luke: Line2 
                        String user_check = first_cut.substring(0, first_cut.indexOf(" "));
                        if (!(username + ":").equals(user_check)) {
                            bw.write("The message belongs to another user and cannot be edited");
                            bw.newLine();
                            bw.flush();
                            System.out.println("Message cannot be edited");
                            return;
                        }
                        stringBuffer.append(messageNumber + " " + username + ": " + message_edt);
                        stringBuffer.append("\n");
                        continue;
                    }
                    stringBuffer.append(line_edt);
                    stringBuffer.append("\n");
                }

                bufferedReader_edt.close();
                // filePath = threadName_edit + ".txt";
                FileOutputStream fos = new FileOutputStream(new File(filePath));
                PrintWriter printWriter = new PrintWriter(fos);
                printWriter.write(stringBuffer.toString().toCharArray());
                printWriter.flush();
                printWriter.close();

                System.out.println("Message has been edited");
                bw.write("The message has been edited");
                bw.newLine();
                bw.flush();

                break;

            case "LST":
                System.out.println(username + " issued LST command");
                File file_lst = new File("./");
                File[] files_lst = file_lst.listFiles();
                int count = 0;
                for (int i = 0; i < files_lst.length; i++) {
                    if (files_lst[i].isFile()) {
                        if (!files_lst[i].getName().contains(".") && !files_lst[i].getName().contains("-")) {
                            count++;
                        }
                    }
                }

                bw.write(String.valueOf(count));
                bw.newLine();
                bw.flush();

                if (count == 0) {
                    break;
                }

                for (int i = 0; i < files_lst.length; i++) {
                    if (files_lst[i].isFile()) {
                        if (!files_lst[i].getName().contains(".") && !files_lst[i].getName().contains("-")) {
                            bw.write(files_lst[i].getName());
                            bw.newLine();
                            bw.flush();
                        }
                    }
                }


                break;

            case "RDT": // RDT threadtitle
                System.out.println(username + " issued RDT command");
                String thread_rdt = command.substring(command.indexOf(" ") + 1); // threadtitle
                File file_rdt = new File(thread_rdt);
                if (!file_rdt.exists()) {
                    System.out.println("Incorrect thread specified");
                    bw.write("Thread " + thread_rdt + " does not exist");
                    bw.newLine();
                    bw.flush();
                    break;
                }
                BufferedReader bufferedReader_rdt = new BufferedReader(new InputStreamReader(new FileInputStream(file_rdt)));
                String line_rdt = null;
                // StringBuffer stringBufferRdt = new StringBuffer();
                int countRdt = 0;
                while ((line_rdt = bufferedReader_rdt.readLine()) != null) {
                    countRdt++;
                }
                bufferedReader_rdt.close();
                if (countRdt == 1) {
                    System.out.println("Thread " + thread_rdt + " read");
                    bw.write("Thread " + thread_rdt + " is empty");
                    bw.newLine();
                    bw.flush();
                    break;
                }

                bw.write(String.valueOf(countRdt - 1));
                bw.newLine();
                bw.flush();

                System.out.println("Thread " + thread_rdt + " read");
                int lineNum = 0;
                bufferedReader_rdt = new BufferedReader(new InputStreamReader(new FileInputStream(file_rdt)));
                while ((line_rdt = bufferedReader_rdt.readLine()) != null) {
                    if (lineNum == 0) {
                        lineNum++;
                        continue;
                    }
                    bw.write(line_rdt);
                    bw.newLine();
                    bw.flush();
                    lineNum++;
                }

                break;

            case "UPD": // UPD threadtitle filename
                System.out.println(username + " issued UPD command");
                String remain_upd = command.substring(command.indexOf(" ") + 1); // threadtitle filename
                String thread_upd = remain_upd.substring(0, remain_upd.indexOf(" ")); // threadtitle
                String fileName_upd = remain_upd.substring(remain_upd.indexOf(" ") + 1); // filename
                filePath = thread_upd;
                File file_upd = new File(filePath);
                String newPathUpd = thread_upd + "-" + fileName_upd;
                File uploadFile = new File(newPathUpd);
                if (!file_upd.exists()) {
                    bw.write("Thread " + thread_upd + " does not exists");
                    bw.newLine();
                    bw.flush();
                    break;
                } else if (uploadFile.exists()) {
                    bw.write(fileName_upd + " exists");
                    bw.newLine();
                    bw.flush();
                    break;
                } else {
                    bw.write("OK");
                    bw.newLine();
                    bw.flush();
                }

                String return_msg = br_client.readLine();
                if ("Start send file".equals(return_msg)) {
                    String str_upd = null;
                    FileWriter fileWriterUpd = new FileWriter(uploadFile);
                    BufferedWriter bufferedWriterUpd = new BufferedWriter(fileWriterUpd);
                    while ((str_upd = br_client.readLine()) != null) {
                        if ("File upload Done".equals(str_upd)) {
                            break;
                        }
                        bufferedWriterUpd.write(str_upd);
                        bufferedWriterUpd.newLine();
                        bufferedWriterUpd.flush();
                    }
                    bufferedWriterUpd.close();
                    fileWriterUpd.close();
                    String messageUpd = username + " uploaded " + fileName_upd;
                    BufferedWriter bufferedWriterAddUpd = new BufferedWriter(new FileWriter(file_upd, true));
                    bufferedWriterAddUpd.write(messageUpd);
                    bufferedWriterAddUpd.newLine();
                    bufferedWriterAddUpd.flush();

                    bufferedWriterAddUpd.close();

                    System.out.println(username + " uploaded file " + fileName_upd + " to " + thread_upd + " thread");

                }
                break;


            case "DWN":
                System.out.println(username + " issued DWN command"); // DWN threadtitle filename
                String remain_dwn = command.substring(command.indexOf(" ") + 1); // threadtitle filename
                String thread_dwn = remain_dwn.substring(0, remain_dwn.indexOf(" ")); // threadtitle
                String fileName_dwn = remain_dwn.substring(remain_dwn.indexOf(" ") + 1); // filename
                String downloadPath = thread_dwn + "-" + fileName_dwn;
                File threadDwnFile = new File(thread_dwn);
                File downloadFile = new File(downloadPath);

                if (!threadDwnFile.exists()) {
                    bw.write("Thread not exists");
                    bw.newLine();
                    bw.flush();
                    break;
                } else if (!downloadFile.exists()) {
                    System.out.println(fileName_dwn + " does not exist in Thread");
                    bw.write("file does not exists");
                    bw.newLine();
                    bw.flush();
                    break;
                } else {
                    bw.write("Start download file");
                    bw.newLine();
                    bw.flush();

                    FileReader fileReaderDwn = new FileReader(downloadFile);
                    BufferedReader bufferedReaderDwn = new BufferedReader(fileReaderDwn);
                    String dwn_line = null;
                    while ((dwn_line = bufferedReaderDwn.readLine()) != null) {
                        bw.write(dwn_line);
                        bw.newLine();
                        bw.flush();
                    }
                    bufferedReaderDwn.close();
                    fileReaderDwn.close();

                    bw.write("File transfer done");
                    bw.newLine();
                    bw.flush();

                    System.out.println(fileName_dwn + " downloaded from Thread " + thread_dwn);

                    // String message_dwn = username + " download " + fileName_dwn;
                    // BufferedWriter bufferedWriterDwn = new BufferedWriter(new FileWriter(threadDwnFile, true));
                    // bufferedWriterDwn.write(message_dwn);
                    // bufferedWriterDwn.newLine();
                    // bufferedWriterDwn.flush();

                    // bufferedReaderDwn.close();
                    break;
                }

            case "RMV": // RMV threadtitle
                System.out.println(username + " issued RMV command");
                String thread_rmv = command.substring(command.indexOf(" ") + 1); // threadtitle

                File filePathRmv = new File(thread_rmv);
                if (!filePathRmv.exists()) {
                    System.out.println("Thread " + thread_rmv + " cannot be removed");
                    bw.write("Thread not exists");
                    bw.newLine();
                    bw.flush();
                    break;
                }

                BufferedReader bufferedReader_rmv = new BufferedReader(new InputStreamReader(new FileInputStream(filePathRmv)));
                String line_rmv = null;
                String creator = bufferedReader_rmv.readLine();
                bufferedReader_rmv.close();
                if (!username.equals(creator)) {
                    System.out.println("Thread " + thread_rmv + " cannot be removed");
                    bw.write("Create by other user");
                    bw.newLine();
                    bw.flush();
                    break;
                }
                bw.write("Remove thread");
                bw.newLine();
                bw.flush();
                filePathRmv.delete();
                System.out.println("Thread " + thread_rmv + " removed");
                break;

            case "XIT":
                System.out.println(username + " issued XIT command");
                for (String userName : User.UserList) {
                    if (userName.equals(username)) {
                        User.UserList.remove(userName);
                        break;
                    }
                }
                System.out.println(username + " exited");
                System.out.println("Waiting for clients");
                break;

            case "SHT":
                System.out.println(username + " issued SHT command");
                String admin_pass = command.substring(command.indexOf(" ") + 1); // admin
                if (!Server.adminPass.equals(admin_pass)) {
                    System.out.println("Incorrect password");
                    bw.write("Wrong admin password");
                    bw.newLine();
                    bw.flush();
                } else {
                    bw.write("Goodbye. Server shutting down");
                    bw.newLine();
                    bw.flush();

                    File folder_SHT = new File("./");
                    File[] filesSht = folder_SHT.listFiles();
                    for (int i = 0; i < filesSht.length; i++) {
                        if (filesSht[i].isFile()) {
                            if (!filesSht[i].getName().contains(".java") && !filesSht[i].getName().contains(".class")) {
                                filesSht[i].delete();
                            }
                        }
                    }

                    Server.systemShutdown = true;
                    exitFlag = true;
                }

                // bw.newLine();
                // bw.flush();
                break;
            default:
                break;
        }

    }
}
