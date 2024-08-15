package lk.ijse.gdse.server;

import java.io.*;
import java.net.*;
import java.util.ArrayList;

public class Server {
    private static ArrayList<ClientHandler> client = new ArrayList<>();


    public static void main(String[] args) throws IOException {

        ServerSocket serverSocket = new ServerSocket(5010);
        Socket socket;


        while (true) {


            System.out.println("Waiting for Client");
            socket = serverSocket.accept();
            System.out.println("Connected");
            ClientHandler clientHandler = new ClientHandler(socket, client);

            client.add(clientHandler);
            clientHandler.start();


        }

    }




}