package com.gustavo.calculator.model;

public class CalculatorState {

    private String expression = "";
    private String result ="0";
    private double primeiroOperando;
    private String operador;
    private boolean novoOperando = true;

    public double getPrimeiroOperando() {
        return primeiroOperando;
    }

    public void setPrimeiroOperando(double primeiroOperando) {
        this.primeiroOperando = primeiroOperando;
    }

    public String getOperador() {
        return operador;
    }

    public void setOperador(String operador) {
        this.operador = operador;
    }

    public boolean isNovoOperando() {
        return novoOperando;
    }

    public void setNovoOperando(boolean novoOperando) {
        this.novoOperando = novoOperando;
    }

    public String getExpression() {
        return expression;
    }

    public void setExpression(String expression) {
        this.expression = expression;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}
