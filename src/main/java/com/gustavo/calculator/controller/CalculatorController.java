package com.gustavo.calculator.controller;

import com.gustavo.calculator.model.CalculatorState;
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

    private final CalculatorState state;


    public CalculatorController(CalculatorState state) {
        this.state = state;
    }

    @FXML
    public void initialize(){

        expressionLabel.setText(state.getExpression());
        resultLabel.setText(state.getResult());
    }

    @FXML
    private void handleNumber(ActionEvent event){

       Button button = (Button) event.getSource();

       String numero = button.getText();

       String atual = resultLabel.getText();

       if (state.isNovoOperando()) {

           resultLabel.setText(numero);

           state.setNovoOperando(false);

       }else if (atual.equals("0")) {

           resultLabel.setText(numero);

       } else {

           resultLabel.setText(atual + numero);
       }

       state.setResult(resultLabel.getText());
    }

    @FXML
    private void handleOperator(ActionEvent event){

        Button button = (Button)  event.getSource();
        String novoOperador = button.getText();

        double segundoOperando = Double.parseDouble(resultLabel.getText());

        if (state.getOperador() != null){

            state.setPrimeiroOperando(calcular(state.getPrimeiroOperando(), segundoOperando, state.getOperador()));

            resultLabel.setText(formatarResultado(state.getPrimeiroOperando()));
        }else {

            state.setPrimeiroOperando(segundoOperando);
        }

        state.setOperador(novoOperador);

        expressionLabel.setText(formatarResultado(state.getPrimeiroOperando()) + " " + state.getOperador());

        state.setNovoOperando(true);

        state.setExpression(expressionLabel.getText());
        state.setResult(resultLabel.getText());
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

        if (state.getOperador() == null){
            return;
        }

        double segundoOperando = Double.parseDouble(resultLabel.getText());

        double resultado = calcular(state.getPrimeiroOperando(), segundoOperando, state.getOperador());

        expressionLabel.setText(formatarResultado(state.getPrimeiroOperando()) + " " + state.getOperador() + " " +
                 formatarResultado(segundoOperando));

        resultLabel.setText(formatarResultado(resultado));

        state.setPrimeiroOperando(resultado);
        state.setOperador(null);
        state.setNovoOperando(true);

        state.setExpression(expressionLabel.getText());
        state.setResult(resultLabel.getText());
    }

    @FXML
    private void handleDecimal(ActionEvent event){

        String atual = resultLabel.getText();

        if (state.isNovoOperando()) {

            resultLabel.setText("0.");
            state.setNovoOperando(false);
            return;
        }

        if (!atual.contains(".")) {
            resultLabel.setText(atual + ".");
        }

        state.setResult(resultLabel.getText());
    }


    @FXML
    private void switchToScientific(ActionEvent event) throws Exception{

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/ScientificCalculator.fxml"));

        loader.setControllerFactory(type ->{

            if (type == CalculatorController.class){

                return new CalculatorController(state);
            }

            try{

                return type.getDeclaredConstructor().newInstance();

            }catch (Exception e){

                throw new RuntimeException(e);
            }

        });

        Parent root = loader.load();

        Scene scene = ((Node) event.getSource()).getScene();
        scene.setRoot(root);

        Stage stage = (Stage) scene.getWindow();
        stage.sizeToScene();
    }

    @FXML
    private void switchToSimple(ActionEvent event) throws Exception{


        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/SimpleCalculator.fxml"));

        loader.setControllerFactory(type -> {

            if (type == CalculatorController.class) {
                return new CalculatorController(state);
            }

            try {

                return type.getDeclaredConstructor().newInstance();

            } catch (Exception e) {

                throw new RuntimeException(e);
            }
        });

        Parent root = loader.load();

        Scene scene = ((Node) event.getSource()).getScene();
        scene.setRoot(root);

        Stage stage = (Stage) scene.getWindow();
        stage.sizeToScene();
    }
}
