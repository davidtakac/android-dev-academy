package ada.osc.bmicalculator;

public class BmiCalculator {

    public static double getBmi(double weightInKg, double heightInM) {
        return weightInKg / (heightInM * heightInM);
    }
}
