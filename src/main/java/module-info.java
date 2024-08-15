module lk.ijse.gdse{
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;


    opens lk.ijse.gdse to javafx.fxml;
    opens lk.ijse.gdse.controller to javafx.fxml;

    exports lk.ijse.gdse;
    exports lk.ijse.gdse.controller;

 }