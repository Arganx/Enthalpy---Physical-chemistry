import java.util.ArrayList;

public class ChangeData {
    private double startTemp;
    private double endTemp;
    private Change changeType;
    private Double value;
    private Double expected;
    private Double variance;
    private ArrayList<Double> coefficients1;
    private ArrayList<Double> coefficients2;
    private int size;
    private int przedzial_x1;
    private int przedzial_x2;
    private boolean deletable;

    public ChangeData(double startTemp, double endTemp, Change changeType, Double value) {
        this.startTemp = startTemp;
        this.endTemp = endTemp;
        this.changeType = changeType;
        this.value = value;
        this.deletable=true;
    }

    public ChangeData(double startTemp, double endTemp, Change changeType, Double value, Double expected, Double variance) {
        this.startTemp = startTemp;
        this.endTemp = endTemp;
        this.changeType = changeType;
        this.value = value;
        this.expected = expected;
        this.variance = variance;
        this.deletable=true;
    }

    public ChangeData(double startTemp, double endTemp, Change changeType, Double value, ArrayList<Double> coefficients1, ArrayList<Double> coefficients2, int size, int przedzial_x1, int przedzial_x2) {
        this.startTemp = startTemp;
        this.endTemp = endTemp;
        this.changeType = changeType;
        this.value = value;
        this.coefficients1 = coefficients1;
        this.coefficients2 = coefficients2;
        this.size = size;
        this.przedzial_x1 = przedzial_x1;
        this.przedzial_x2 = przedzial_x2;
        this.deletable=true;
    }

    public boolean isDeletable() {
        return deletable;
    }

    public void setDeletable(boolean deletable) {
        this.deletable = deletable;
    }

    public double getStartTemp() {
        return startTemp;
    }

    public void setStartTemp(double startTemp) {
        this.startTemp = startTemp;
    }

    public double getEndTemp() {
        return endTemp;
    }

    public void setEndTemp(double endTemp) {
        this.endTemp = endTemp;
    }

    public Change getChangeType() {
        return changeType;
    }

    public void setChangeType(Change changeType) {
        this.changeType = changeType;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public Double getExpected() {
        return expected;
    }

    public void setExpected(Double expected) {
        this.expected = expected;
    }

    public Double getVariance() {
        return variance;
    }

    public void setVariance(Double variance) {
        this.variance = variance;
    }

    public ArrayList<Double> getCoefficients1() {
        return coefficients1;
    }

    public void setCoefficients1(ArrayList<Double> coefficients1) {
        this.coefficients1 = coefficients1;
    }

    public ArrayList<Double> getCoefficients2() {
        return coefficients2;
    }

    public void setCoefficients2(ArrayList<Double> coefficients2) {
        this.coefficients2 = coefficients2;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getPrzedzial_x1() {
        return przedzial_x1;
    }

    public void setPrzedzial_x1(int przedzial_x1) {
        this.przedzial_x1 = przedzial_x1;
    }

    public int getPrzedzial_x2() {
        return przedzial_x2;
    }

    public void setPrzedzial_x2(int przedzial_x2) {
        this.przedzial_x2 = przedzial_x2;
    }

    @Override
    public String toString() {

        if(changeType==Change.Dodaj_na_poczatek || changeType==Change.dodaj_na_srodek || changeType==Change.dodaj_na_koniec || changeType==Change.dodaj_srednia_wartosc_na_przedziale || changeType==Change.fibbonaci || changeType==Change.fibbonaci_odwrocony || changeType==Change.fibboaci_wysrodkowany)
        {
            return "Temperatura poczatkowa=" + startTemp +
                    ", Temperatura końcowa=" + endTemp +
                    ", Rodzaj przemiany=" + changeType +
                    ", Wartość=" + value
                    ;
        }
        else if(changeType==Change.rozklad_dowolny)
        {
            return "Temperatura poczatkowa=" + startTemp +
                    ", Temperatura końcowa=" + endTemp +
                    ", Rodzaj przemiany=" + changeType +
                    ", Wartość=" + value +
                    ", Wartość oczekiwana=" + expected +
                    ", Wariancja=" + variance;
        }
        else if(changeType==Change.wielomian)
        {
            return "Temperatura poczatkowa=" + startTemp +
                    ", Temperatura końcowa=" + endTemp +
                    ", Rodzaj przemiany=" + changeType +
                    ", Wartość=" + value +
                    ", Rozmiar=" + size +
                    ", wspolczynniki1="+ coefficients1.toString() +
                    ", wspolczynniki2="+ coefficients2.toString() +
                    ", przedzial_x1=" + przedzial_x1 +
                    ", przedzial_x2=" + przedzial_x2;

        }
        else
        {
            return "Nie rozpoznano przemiany";
        }
    }


}
