package com.gustavo.calculator.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.util.Objects;

public class CalculatorController {

    @FXML
    private Label expressionLabel;

    @FXML
    private Label resultLabel;

    @FXML
    public void initialize(){

        resultLabel.setText("0");
    }

    @FXML
    private void handleNumber(ActionEvent event){

        Button button = (Button) event.getSource();

        String value = button.getText();

        String currentText = resultLabel.getText();

        if (currentText.equals("0")){

            resultLabel.setText(value);
        } else {
            resultLabel.setText(currentText + value);
        }
    }


    @FXML
    private void switchToScientific(ActionEvent event) throws Exception{

        Parent root = FXMLLoader.load(Objects.requireNonNull(
                getClass().getResource("/fxml/ScientificCalculator.fxml")));

        Scene scene = ((Node) event.getSource()).getScene();
        scene.setRoot(root);

        Stage stage = (Stage) scene.getWindow();
        stage.sizeToScene();

    }

    @FXML
    private void switchToSimple(ActionEvent event) throws Exception{

        Parent root = FXMLLoader.load(Objects.requireNonNull(
                getClass().getResource("/fxml/SimpleCalculator.fxml")));

        Scene scene = ((Node) event.getSource()).getScene();
        scene.setRoot(root);

        Stage stage = (Stage) scene.getWindow();
        stage.sizeToScene();
    }
}
