package com.gustavo.calculator;

import com.gustavo.calculator.controller.CalculatorController;
import com.gustavo.calculator.model.CalculatorState;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.text.Font;
import javafx.stage.Stage;

/**
 * Classe principal responsável por iniciar a aplicação JavaFX.
 *
 * <p>Inicializa a interface da calculadora, configura o controlador,
 * carrega a fonte personalizada, aplica o arquivo CSS e exibe a
 * janela principal da aplicação.</p>
 */
public class MainApp extends Application {

    /**
     * Estado compartilhado da calculadora entre os diferentes modos
     * da interface.
     */
    private final CalculatorState calculatorState = new CalculatorState();

    /**
     * Inicializa a aplicação JavaFX e configura a janela principal.
     *
     * @param primaryStage janela principal da aplicação
     * @throws Exception se ocorrer um erro durante o carregamento dos recursos
     */
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

    /**
     * Ponto de entrada da aplicação.
     *
     * @param args argumentos fornecidos pela linha de comando
     */
    public static void main(String[] args) {
        launch(args);
    }
}
