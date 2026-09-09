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

    private double primeiroOperando;
    private String operador;
    private boolean novoOperando = true;

    @FXML
    public void initialize(){

        resultLabel.setText("0");
    }

    @FXML
    private void handleNumber(ActionEvent event){

       Button button = (Button) event.getSource();

       String numero = button.getText();

       if (novoOperando || resultLabel.getText().equals("0")) {
           resultLabel.setText(numero);
           novoOperando = false;
       } else {
           resultLabel.setText(resultLabel.getText() + numero);
       }
    }

    @FXML
    private void handleOperator(ActionEvent event){

        Button button = (Button)  event.getSource();
        String novoOperador = button.getText();

        double segundoOperando = Double.parseDouble(resultLabel.getText());

        if (operador != null){

            primeiroOperando = calcular(primeiroOperando, segundoOperando, operador);

            resultLabel.setText(formatarResultado(primeiroOperando));
        }else {

            primeiroOperando = segundoOperando;
        }

        operador = novoOperador;

        expressionLabel.setText(formatarResultado(primeiroOperando) + " " + operador);

        novoOperando = true;
    }

    private double calcular(double primeiroOperando, double segundoOperando, String operador){

        return switch (operador) {
            case "+" -> primeiroOperando + segundoOperando;
            case "-" -> primeiroOperando - segundoOperando;
            case "*" -> primeiroOperando * segundoOperando;
            case "/" -> primeiroOperando / segundoOperando;
            default -> segundoOperando;
        };
    }

    private String formatarResultado(double resultado){

        if (resultado == (long) resultado){

            return String.valueOf((long) resultado);

        } else {

            return String.valueOf(resultado);
        }
    }

    @FXML
    private void handleEquals(ActionEvent event){

        if (operador == null){
            return;
        }

        double segundoOperando = Double.parseDouble(resultLabel.getText());

        double resultado = calcular(primeiroOperando, segundoOperando, operador);

        expressionLabel.setText(formatarResultado(primeiroOperando) + " " + operador + " " +
                 formatarResultado(segundoOperando));

        resultLabel.setText(formatarResultado(resultado));

        primeiroOperando = resultado;
        operador = null;
        novoOperando = true;


    }

    @FXML
    private void handleDecimal(ActionEvent event){

        String atual = resultLabel.getText();

        if (novoOperando) {

            resultLabel.setText("0.");
            novoOperando = false;
            return;
        }

        if (!atual.contains(".")) {
            resultLabel.setText(atual + ".");
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
