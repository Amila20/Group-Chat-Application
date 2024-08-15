package lk.ijse.gdse.server;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class ClientHandler extends Thread {
    private Socket socket;
    private BufferedReader reader;
    private PrintWriter writer;
    private DataOutputStream outputStream;
    private DataInputStream inputStream;
    private static ArrayList<ClientHandler> clients;


    public ClientHandler(Socket socket, ArrayList<ClientHandler> clients) {
        try {
            this.socket = socket;
            this.clients = clients;
            this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.writer = new PrintWriter(socket.getOutputStream(), true);
            this.inputStream = new DataInputStream(socket.getInputStream());
            this.outputStream = new DataOutputStream(socket.getOutputStream());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        try {
            String msg;
            while ((msg = reader.readLine()) != null) {
                System.out.println("Received: " + msg);

                if (msg.startsWith("IMAGE")) {
                    int length = Integer.parseInt(msg.split(" ")[1]);
                    byte[] imageBytes = new byte[length];
                    inputStream.readFully(imageBytes);

                    // Forward the image to all clients
                    forwardImage(msg, imageBytes);
                }else {
                    // Forward the text message to all clients
                    forwardMessage(msg);

                }

                if (msg.equalsIgnoreCase("exit")) {
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                reader.close();
                writer.close();
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void forwardMessage(String msg) {
        // Iterate through the list of clients and send the message to each one
        for (ClientHandler client : clients) {

            if (client != this) { // Do not send the message back to the sender
                client.writer.println(msg);

            }
        }
    }

    private void forwardImage(String header, byte[] imageBytes) {
        for (ClientHandler client : clients) {
            if (client != this) {
                try {
                    // Send the header first
                    client.writer.println(header);
                    client.writer.flush();

                    // Send the image data
                    client.outputStream.write(imageBytes);
                    client.outputStream.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
