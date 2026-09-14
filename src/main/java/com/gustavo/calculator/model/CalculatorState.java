package com.gustavo.calculator.model;


/**
 * Armazena o estado atual da calculadora.
 *
 * <p>Mantém as informações necessárias para preservar os valores e
 * operações realizadas durante a utilização da calculadora, permitindo
 * o compartilhamento do estado entre os diferentes modos da aplicação.</p>
 */
public class CalculatorState {

    /** Expressão matemática atualmente exibida no visor. */
    private String expression = "";

    /** Resultado atual exibido no visor. */
    private String result = "0";

    /** Primeiro operando utilizado na operação. */
    private double primeiroOperando;

    /** Operador matemático atualmente selecionado. */
    private String operador;

    /** Indica se a calculadora está aguardando a entrada de um novo operando. */
    private boolean novoOperando = true;

    /**
     * Retorna o primeiro operando armazenado.
     *
     * @return primeiro operando da operação
     */
    public double getPrimeiroOperando() {
        return primeiroOperando;
    }

    /**
     * Define o primeiro operando da operação.
     *
     * @param primeiroOperando valor do primeiro operando
     */
    public void setPrimeiroOperando(double primeiroOperando) {
        this.primeiroOperando = primeiroOperando;
    }

    /**
     * Retorna o operador atualmente selecionado.
     *
     * @return operador da operação
     */
    public String getOperador() {
        return operador;
    }

    /**
     * Define o operador da operação.
     *
     * @param operador operador matemático selecionado
     */
    public void setOperador(String operador) {
        this.operador = operador;
    }

    /**
     * Verifica se a calculadora está aguardando um novo operando.
     *
     * @return {@code true} se estiver aguardando um novo operando;
     *         {@code false} caso contrário
     */
    public boolean isNovoOperando() {
        return novoOperando;
    }

    /**
     * Define se a calculadora está aguardando um novo operando.
     *
     * @param novoOperando indica se um novo operando deve ser iniciado
     */
    public void setNovoOperando(boolean novoOperando) {
        this.novoOperando = novoOperando;
    }

    /**
     * Retorna a expressão atualmente armazenada.
     *
     * @return expressão matemática
     */
    public String getExpression() {
        return expression;
    }

    /**
     * Define a expressão atualmente armazenada.
     *
     * @param expression expressão matemática
     */
    public void setExpression(String expression) {
        this.expression = expression;
    }

    /**
     * Retorna o resultado atualmente armazenado.
     *
     * @return resultado da operação
     */
    public String getResult() {
        return result;
    }

    /**
     * Define o resultado atualmente armazenado.
     *
     * @param result resultado da operação
     */
    public void setResult(String result) {
        this.result = result;
    }
}
