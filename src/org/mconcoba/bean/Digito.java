
package org.mconcoba.bean;



import javafx.scene.Parent;
import javafx.scene.control.Label;



public final class Digito extends Parent {

    private final Label DIGIT;

    private static final String ZERO = new String("0");
    private static final String ONE = new String("1");
    private static final String TWO = new String("2");
    private static final String THREE = new String("3");
    private static final String FOUR = new String("4");
    private static final String FIVE = new String("5");
    private static final String SIX = new String("6");
    private static final String SEVEN = new String("7");
    private static final String EIGHT = new String("8");
    private static final String NINE = new String("9");

    public Digito(int numero) {
        DIGIT = new Label();
        actualizaDigito(numero);
        getChildren().add(DIGIT);
    }

    public void actualizaDigito(int numero) {
        switch (numero) {
            case 0:
                DIGIT.setText(ZERO);
                break;
            case 1:
                DIGIT.setText(ONE);
                break;
            case 2:
                DIGIT.setText(TWO);
                break;
            case 3:
                DIGIT.setText(THREE);
                break;
            case 4:
                DIGIT.setText(FOUR);
                break;
            case 5:
                DIGIT.setText(FIVE);
                break;
            case 6:
                DIGIT.setText(SIX);
                break;
            case 7:
                DIGIT.setText(SEVEN);
                break;
            case 8:
                DIGIT.setText(EIGHT);
                break;
            case 9:
                DIGIT.setText(NINE);
                break;
        }
    }

}
