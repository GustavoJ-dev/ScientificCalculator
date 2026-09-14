package com.gustavo.calculator;

import com.gustavo.calculator.controller.CalculatorController;
import com.gustavo.calculator.model.CalculatorState;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class MainApp extends Application {

    private final CalculatorState calculatorState = new CalculatorState();
    @Override
    public void start(Stage primaryStage) throws Exception {

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/SimpleCalculator.fxml"));

        fxmlLoader.setControllerFactory(type ->{

            if(type == CalculatorController.class){

                return new CalculatorController(calculatorState);
            }

            try{

                return type.getDeclaredConstructor().newInstance();

            }catch (Exception e){

                throw new RuntimeException(e);
            }
        });

        Font.loadFont(getClass().getResourceAsStream("/fonts/PressStart2P.ttf"), 16);

        Scene scene = new Scene(fxmlLoader.load());
        scene.getStylesheets().add(getClass().getResource("/css/calculator.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
