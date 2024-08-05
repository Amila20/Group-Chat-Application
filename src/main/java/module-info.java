module lk.ijse.gdse{
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;


    opens lk.ijse.gdse to javafx.fxml;

    exports lk.ijse.gdse;
    exports lk.ijse.gdse.controller;

 }