package ru.vsu.perceptron;

public class ActivationFunction {

    public int apply(double input) {
        return input >= 0 ? 1 : 0;
    }
}
