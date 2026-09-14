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

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

/**
 * Controlador responsável por gerenciar as interações da interface
 * da calculadora.
 *
 * <p>Processa operações básicas e científicas, atualiza os valores
 * exibidos na interface e mantém o estado compartilhado entre os
 * modos simples e científico.</p>
 */
public class CalculatorController {

    /**
     * Referência ao rótulo responsável por exibir a expressão matemática.
     */
    @FXML
    private Label expressionLabel;

    /**
     * Referência ao rótulo responsável por exibir o resultado atual.
     */
    @FXML
    private Label resultLabel;

    /**
     * Estado compartilhado da calculadora.
     */
    private final CalculatorState state;

    /**
     * Cria um controlador associado ao estado da calculadora.
     *
     * @param state estado compartilhado da calculadora
     */
    public CalculatorController(CalculatorState state) {
        this.state = state;
    }

    /**
     * Inicializa os componentes do controlador com os valores
     * atualmente armazenados no estado da calculadora.
     */
    @FXML
    public void initialize(){

        expressionLabel.setText(state.getExpression());
        resultLabel.setText(state.getResult());
    }

    /**
     * Alterna a interface da calculadora para o modo científico.
     *
     * <p>Carrega a interface científica, cria um novo controlador
     * compartilhando o mesmo estado da calculadora e substitui a raiz
     * atual da cena. Ao final, ajusta o tamanho da janela de acordo
     * com a nova interface.</p>
     *
     * @param event evento gerado pelo botão de alternância de modo
     * @throws Exception se ocorrer um erro durante o carregamento do FXML
     */
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

    /**
     * Alterna a interface da calculadora para o modo simples.
     *
     * <p>Carrega a interface simples, cria um novo controlador
     * compartilhando o mesmo estado da calculadora e substitui a raiz
     * atual da cena. Ao final, ajusta o tamanho da janela de acordo
     * com a nova interface.</p>
     *
     * @param event evento gerado pelo botão de alternância de modo
     * @throws Exception se ocorrer um erro durante o carregamento do FXML
     */
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


    /**
     * Processa a entrada de um número pressionado pelo usuário.
     *
     * <p>Adiciona o número ao resultado atual ou inicia um novo operando,
     * atualizando também o estado compartilhado da calculadora.</p>
     *
     * @param event evento gerado pelo botão numérico pressionado
     */
    @FXML
    private void handleNumber(ActionEvent event){

        Button button = (Button) event.getSource();

        String numero = button.getText();

        String atual = resultLabel.getText();

        String novoValor;

        if (state.isNovoOperando()) {

            novoValor = numero;

            state.setNovoOperando(false);

        } else if (atual.equals("0")) {

            novoValor = numero;

        } else {

            novoValor = atual + numero;
        }

        double valor = Double.parseDouble(novoValor.replace(".", "")
                .replace(",", "."));

        resultLabel.setText(formatarResultado(valor));
        state.setResult(resultLabel.getText());
    }

    /**
     * Processa a seleção de um operador matemático.
     *
     * <p>Armazena o operando atual e o operador selecionado. Caso já
     * exista uma operação pendente, calcula o resultado intermediário
     * antes de armazenar o novo operador.</p>
     *
     * @param event evento gerado pelo botão de operador pressionado
     */
    @FXML
    private void handleOperator(ActionEvent event){

        Button button = (Button)  event.getSource();
        String novoOperador = button.getText();

        double segundoOperando = Double.parseDouble(resultLabel.getText().replace(".", "")
                .replace(",", "."));

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


    /**
     * Processa a entrada do ponto decimal.
     *
     * <p>Inicia um novo número com "0." quando necessário e impede
     * a inclusão de mais de um ponto decimal no mesmo número.</p>
     *
     * @param event evento gerado pelo botão decimal pressionado
     */
    @FXML
    private void handleDecimal(ActionEvent event){

        String atual = resultLabel.getText();

        if (state.isNovoOperando()) {

            resultLabel.setText("0.");
            state.setNovoOperando(false);
            state.setResult(resultLabel.getText());
            return;
        }

        if (!atual.contains(".")) {
            resultLabel.setText(atual + ".");
        }

        state.setResult(resultLabel.getText());
    }


    /**
     * Realiza uma operação matemática entre dois operandos.
     *
     * <p>Suporta as operações de adição, subtração, multiplicação,
     * divisão e potenciação.</p>
     *
     * @param primeiroOperando primeiro valor da operação
     * @param segundoOperando segundo valor da operação
     * @param operador operador matemático a ser utilizado
     * @return resultado da operação
     * @throws ArithmeticException se ocorrer uma divisão por zero
     */
    private double calcular(double primeiroOperando, double segundoOperando, String operador){

        return switch (operador) {
            case "+" -> primeiroOperando + segundoOperando;
            case "-" -> primeiroOperando - segundoOperando;
            case "×" -> primeiroOperando * segundoOperando;
            case "÷" -> {
                if (segundoOperando == 0) {

                    throw new ArithmeticException("Divisão por zero");
                }

                yield primeiroOperando / segundoOperando;
            }
            default -> segundoOperando;
        };
    }


    /**
     * Executa uma operação científica selecionada pelo usuário.
     *
     * <p>Processa operações como potenciação, raiz quadrada, inversão,
     * funções trigonométricas, logaritmos e porcentagem. A operação
     * {@code xʸ} é tratada como uma operação pendente, armazenando o
     * primeiro operando para ser utilizado posteriormente com o segundo.</p>
     *
     * <p>Operações inválidas, como raiz quadrada de número negativo,
     * divisão por zero e logaritmo de valor não positivo, resultam na
     * exibição de "Erro" e na restauração do estado da calculadora.</p>
     *
     * @param event evento gerado pelo botão da operação científica pressionado
     */
    @FXML
    private void handleScientificOperation(ActionEvent event) {

        Button button = (Button) event.getSource();
        String operacao = button.getText();

        double valor = Double.parseDouble(resultLabel.getText().replace(".", "")
                        .replace(",", "."));

        double valorOriginal = valor;

        try {

            double result = switch (operacao) {

                case "x²" -> Math.pow(valor, 2);

                case "√" -> {
                    if (valor < 0) {

                        throw new ArithmeticException("Raiz de número negativo");
                    }

                    yield Math.sqrt(valor);
                }

                case "1/x" -> {

                    if (valor == 0) {

                        throw new ArithmeticException("Divisão por zero");
                    }

                    yield 1 / valor;
                }

                case "sin" -> Math.sin(Math.toRadians(valor));
                case "cos" -> Math.cos(Math.toRadians(valor));
                case "tan" -> Math.tan(Math.toRadians(valor));
                case "log" -> {

                    if (valor <= 0) {

                        throw new ArithmeticException("Logaritmo inválido");
                    }

                    yield Math.log10(valor);
                }

                case "ln" -> {

                    if (valor <= 0) {

                        throw new ArithmeticException("Logaritmo inválido");
                    }

                    yield Math.log(valor);
                }

                case "%" -> valor / 100;
                case "xʸ" -> {

                    state.setPrimeiroOperando(valor);
                    state.setOperador(operacao);
                    state.setNovoOperando(true);

                    expressionLabel.setText(formatarResultado(valor) + " " + operacao);

                    state.setExpression(expressionLabel.getText());

                    yield valor;
                }

                default -> valor;
            };

            resultLabel.setText(formatarResultado(result));
            state.setResult(resultLabel.getText());

            if (!operacao.equals("xʸ")) {

                expressionLabel.setText(formatarResultado(valorOriginal) + " " + operacao);

                state.setExpression(expressionLabel.getText());
            }

        } catch (ArithmeticException e) {

            expressionLabel.setText("Erro");
            resultLabel.setText("Erro");

            state.setExpression("Erro");
            state.setResult("Erro");
            state.setPrimeiroOperando(0);
            state.setOperador(null);
            state.setNovoOperando(true);
        }
    }


    /**
     * Limpa a calculadora e restaura o estado inicial.
     *
     * <p>Remove a expressão exibida, redefine o resultado para zero
     * e limpa os valores armazenados para a próxima operação.</p>
     *
     * @param event evento gerado pelo botão de limpar pressionado
     */
    @FXML
    private void handleClear(ActionEvent event){

        resultLabel.setText("0");
        expressionLabel.setText("");

        state.setResult("0");
        state.setExpression("");
        state.setPrimeiroOperando(0);
        state.setOperador(null);
        state.setNovoOperando(true);
    }

    /**
     * Remove o último caractere do resultado exibido.
     *
     * <p>Caso exista apenas um caractere no resultado, o visor é
     * redefinido para zero.</p>
     *
     * @param event evento gerado pelo botão de apagar pressionado
     */
    @FXML
    private void handleBackspace(ActionEvent event) {

        String atual = resultLabel.getText();

        if (atual.length() <= 1) {
            resultLabel.setText("0");
        } else {
            resultLabel.setText(atual.substring(0, atual.length() - 1));
        }

        state.setResult(resultLabel.getText());
    }


    /**
     * Executa a operação matemática selecionada pelo usuário.
     *
     * <p>Obtém o segundo operando, realiza o cálculo utilizando o
     * operador armazenado e atualiza a expressão, o resultado e o
     * estado da calculadora. Em caso de erro aritmético, exibe
     * "Erro" no visor e restaura o estado da calculadora.</p>
     *
     * @param event evento gerado pelo botão de igualdade pressionado
     */
    @FXML
    private void handleEquals(ActionEvent event) {

        if (state.getOperador() == null) {
            return;
        }

        double segundoOperando = Double.parseDouble(resultLabel.getText().replace(".", "")
                        .replace(",", "."));

        try {

            double resultado = calcular(state.getPrimeiroOperando(), segundoOperando, state.getOperador());

            expressionLabel.setText(
                    formatarResultado(state.getPrimeiroOperando())
                            + " " + state.getOperador() + " " + formatarResultado(segundoOperando) + " =");

            resultLabel.setText(formatarResultado(resultado));

            state.setExpression(expressionLabel.getText());
            state.setResult(resultLabel.getText());

            state.setPrimeiroOperando(resultado);
            state.setOperador(null);
            state.setNovoOperando(true);

        } catch (ArithmeticException e) {

            expressionLabel.setText("Erro");
            resultLabel.setText("Erro");

            state.setExpression("Erro");
            state.setResult("Erro");
            state.setPrimeiroOperando(0);
            state.setOperador(null);
            state.setNovoOperando(true);
        }
    }

    /**
     * Formata um resultado numérico para exibição no visor da calculadora.
     *
     * <p>Quando o resultado não possui casas decimais significativas,
     * ele é exibido como um número inteiro.</p>
     *
     * @param resultado valor numérico a ser formatado
     * @return resultado formatado como texto
     */
    private String formatarResultado(double resultado){

        DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.forLanguageTag("pt-BR"));
        symbols.setGroupingSeparator('.');
        symbols.setDecimalSeparator(',');

        DecimalFormat format = new DecimalFormat("#,##0.##########", symbols);

        return format.format(resultado);
    }








}
