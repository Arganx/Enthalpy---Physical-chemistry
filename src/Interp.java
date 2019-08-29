import java.util.ArrayList;

public class Interp {

    private ArrayList<Double> data;


    public double interpolatin_linear(double x, double xstart,double xfinish, double fxstart, double fxfinish) {
        double x1 = xstart; //początek przedziału
        double x2 = xfinish; //koniec przedziału
        double fx1 = fxstart; //wartość funkcji f(x1)
        double fx2 = fxfinish; //wartość funkcji f(x2)
        return fx1 * ((x - x2) / (x1 - x2)) + fx2 * ((x - x1) / (x2 - x1));
    }

    public double interpolacja_square(double x,double xstart,double xmid,double xfinish, double fxstart, double fxmid ,double fxfinish) {
        double x1 = xstart;
        double x2 = xmid;
        double x3 = xfinish;
        double fx1 = fxstart;
        double fx2 = fxmid;
        double fx3 = fxfinish;
        return fx1 * ((x - x2) / (x1 - x2))*((x - x3) / (x1 - x3)) +
                fx2 * ((x - x1) / (x2 - x1))*((x - x3) / (x2 - x3)) +
                fx3 * ((x - x1) / (x3 - x1))*((x - x2) / (x3 - x2));
    }

}
