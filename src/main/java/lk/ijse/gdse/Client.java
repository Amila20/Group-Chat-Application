package lk.ijse.gdse;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.*;
import java.net.*;

public class Client extends Application implements Runnable{
    String msg = "";
    String sendMsg = "Hello, How are you";

    public static void main(String[] args) {
        launch(args);
        Thread thread = new Thread(new Client());
        thread.start();
    }

    @Override
    public void run() {
        try {
            Socket socket = new Socket("localhost", 8001);
            DataInputStream input = new DataInputStream(socket.getInputStream());
            DataOutputStream output = new DataOutputStream(socket.getOutputStream());

            // Client reads the message sent by the server
            msg = input.readUTF();
            System.out.println(msg);

            // Client sends a message to the server
            output.writeUTF(sendMsg.trim());
            output.flush();


            socket.close();
         } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/lk/ijse/gdse/LoginForm.fxml"));
        Scene scene = new Scene(loader.load());
        stage.setScene(scene);
        stage.show();
    }
}
