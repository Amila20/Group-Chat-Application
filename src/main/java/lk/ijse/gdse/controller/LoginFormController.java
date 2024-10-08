package lk.ijse.gdse.controller;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LoginFormController {

    public TextField txtUserName;




    public void btnConnectOnAction(ActionEvent actionEvent) throws Exception {
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/lk/ijse/gdse/ChatForm.fxml"));
        Scene scene = new Scene(loader.load());
        stage.setScene(scene);
        stage.setTitle(txtUserName.getText());

        ChatFormController chatFormController = loader.getController();

        // Set the username in ChatFormController
        chatFormController.setUserName(txtUserName.getText());

        stage.show();
        txtUserName.clear();


    }




}

