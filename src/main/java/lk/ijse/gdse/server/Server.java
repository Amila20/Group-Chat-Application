package lk.ijse.gdse.server;

import java.io.*;
import java.net.*;
import java.util.ArrayList;

public class Server implements Runnable {
    private static ArrayList<ClientHandler>client=new ArrayList<>();

    public static void main(String[] args) {
        Thread thread = new Thread(new Server());
        thread.start();
    }



    @Override
    public void run() {
        try {
            ServerSocket serverSocket = new ServerSocket(8001);
            System.out.println("Waiting for Client");
            Socket socket = serverSocket.accept();
            System.out.println("Connected");
            ClientHandler clientHandler=new ClientHandler(socket,client);
            client.add(clientHandler);
            clientHandler.start();




        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
