package lk.ijse.gdse.controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.FileChooser;
import javafx.stage.Popup;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;

public class ChatFormController implements Runnable{
    public TextField txtSendMesg;
    public VBox MainContainer;
    public Button btnImoji;
    public AnchorPane Main;

    public Label lblUserName;
    BufferedReader reader;
    PrintWriter writer;
    DataInputStream inputStream;
    Socket socket;
    private Popup emojiPopup;

    String setName;





    public void initialize() throws IOException {

        socket = new Socket("localhost", 5010);
        reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        writer = new PrintWriter(socket.getOutputStream(), true);



        setupEmojiPicker();

        inputStream = new DataInputStream(socket.getInputStream());
        Thread thread=new Thread(this);
        thread.start();


    }
    public void setUserName(String userName) {
        lblUserName.setText(userName);
        setName=userName;


    }

    public void btnSendOnAction(ActionEvent actionEvent) {
        String sendMsg = txtSendMesg.getText();
        writer.println(setName+" : "+sendMsg);
        txtSendMesg.clear();

        Platform.runLater(() -> {
            Label messageLabel = new Label(" You : "+ sendMsg);
            messageLabel.setFont(new Font("Arial Rounded MT Bold",14));
            messageLabel.setTextFill(Color.BLUE);
            HBox messageBox = new HBox(12);
            messageBox.getChildren().add(messageLabel);
            messageBox.setAlignment(Pos.CENTER_RIGHT); // Align sent messages to the right
           // messageBox.setStyle("-fx-background-color:red;");
            MainContainer.getChildren().add(messageBox);
        });


    }
    @Override
    public void run() {
        while (true) {
            try {
                String message = reader.readLine();
                if (message.startsWith("IMAGE")) {
                    int length = Integer.parseInt(message.split(" ")[1]);
                    byte[] imageBytes = new byte[length];
                    inputStream.readFully(imageBytes);

                    // Display the image on the receiver's side
                    Platform.runLater(() -> displayImage(imageBytes, Pos.CENTER_LEFT));
                } else {
                    // Handle text messages
                    Platform.runLater(() -> {


                        Label messageLabel = new Label(message);
                        messageLabel.setFont(new Font("Arial Rounded MT Bold",14));
                        HBox messageBox = new HBox();
                        messageBox.getChildren().add(messageLabel);
                        messageBox.setAlignment(Pos.CENTER_LEFT);
                        MainContainer.getChildren().add(messageBox);
                    });
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }



    public void btnImagesSendOnAction(ActionEvent actionEvent) {

        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif"));
        File file = fileChooser.showOpenDialog(null);

        if (file != null) {
            try {
                byte[] imageBytes = Files.readAllBytes(file.toPath());
                sendImage(imageBytes);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public void sendImage(byte[] imageBytes) {
        try {
            // Send the length of the image data first
            writer.println("IMAGE " + imageBytes.length);
            writer.flush();

            // Send the image data
            OutputStream outputStream = socket.getOutputStream();
            outputStream.write(imageBytes);
            outputStream.flush();

            // Optionally, display the image on the sender's side
            displayImage(imageBytes, Pos.CENTER_RIGHT);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void displayImage(byte[] imageBytes, Pos alignment) {
        Image image = new Image(new ByteArrayInputStream(imageBytes));
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(200); // Adjust the size as needed
        imageView.setPreserveRatio(true);

        HBox imageBox = new HBox();
        imageBox.getChildren().add(imageView);
        imageBox.setAlignment(alignment);

        MainContainer.getChildren().add(imageBox);
    }

    private void setupEmojiPicker() {
          btnImoji.setOnAction(event -> {
            if (emojiPopup.isShowing()) {
                emojiPopup.hide();
            } else {
             emojiPopup.show(btnImoji, btnImoji.getLayoutY(), btnImoji.getLayoutY());
            }
        });

        // Create a simple emoji picker
        emojiPopup = new Popup();
        VBox emojiBox = new VBox();

        // Add some sample emojis
        String[] emojis = {"😀", "😂", "😍", "😎", "😭", "😡"};
        for (String emoji : emojis) {
            Button emojiItem = new Button(emoji);
            emojiItem.setOnAction(e -> {
                txtSendMesg.appendText(emoji);
                emojiPopup.hide();
            });
            emojiBox.getChildren().add(emojiItem);
        }

        emojiPopup.getContent().add(emojiBox);
    }


    public void btnImojiPopUpOnAction(ActionEvent actionEvent) {
          btnImoji.setOnAction(event -> {
            if (emojiPopup.isShowing()) {
                emojiPopup.hide();
            } else {
                emojiPopup.show(btnImoji, btnImoji.getLayoutY(), btnImoji.getLayoutY());
            }
        });

        emojiPopup = new Popup();
        VBox emojiBox = new VBox();

        // Add some sample emojis
        String[] emojis = {"😀", "😂", "😍", "😎", "😭", "😡"};
        for (String emoji : emojis) {
            Button emojiItem = new Button(emoji);
            emojiItem.setOnAction(e -> {
                txtSendMesg.appendText(emoji);
                emojiPopup.hide();
            });
            emojiBox.getChildren().add(emojiItem);
        }

        emojiPopup.getContent().add(emojiBox);




    }
}

