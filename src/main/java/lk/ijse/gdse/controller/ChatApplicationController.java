package lk.ijse.gdse.controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.*;
import java.net.Socket;

public class ChatApplicationController implements Runnable {


    public TextField txtSendMsg;
    public TextArea textAria;
    BufferedReader reader;
    PrintWriter writer;
    DataInputStream inputStream;

    public void initialize() throws IOException {

        Socket socket = new Socket("localhost", 5010);
        reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        writer = new PrintWriter(socket.getOutputStream(), true);

        inputStream = new DataInputStream(socket.getInputStream());
        Thread thread=new Thread(this);
        thread.start();


    }

    public void btnSendOnAction(ActionEvent actionEvent) {
        String sendMsg = txtSendMsg.getText();
        writer.println(sendMsg);


    }

    @Override
    public void run() {
        while (true) {
            try {
                String getMsg;
                while ((getMsg = reader.readLine()) != null) {
                   // String finalGetMsg = getMsg;
                  // Use Platform.runLater to ensure that the UI is updated on the JavaFX Application Thread
                  textAria.appendText("Incoming: " + getMsg+ "\n");
                  System.out.println(getMsg);
                }
               }catch(Exception e){
                throw new RuntimeException(e);
            }

        }
    }
}
