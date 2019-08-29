public class InterpolationData {
    private double startTemp;
    private double endTemp;
    private Interpolations interpolationType;

    public InterpolationData(double startTemp, double endTemp, Interpolations interpolationType) {
        this.startTemp = startTemp;
        this.endTemp = endTemp;
        this.interpolationType = interpolationType;
    }

    public InterpolationData() {

    }

    public double getStartTemp() {
        return startTemp;
    }

    public double getEndTemp() {
        return endTemp;
    }

    public Interpolations getInterpolationType() {
        return interpolationType;
    }

    public void setStartTemp(double startTemp) {
        this.startTemp = startTemp;
    }

    public void setEndTemp(double endTemp) {
        this.endTemp = endTemp;
    }

    public void setInterpolationType(Interpolations interpolationType) {
        this.interpolationType = interpolationType;
    }

    public void showData()
    {
        System.out.println(startTemp);
        System.out.println(endTemp);
        System.out.println(interpolationType);
    }

    @Override
    public String toString() {
        return "Temperatura początkowa=" + startTemp +
                ", Temperatura końcowa=" + endTemp +
                ", Rodzaj interpolacji=" + interpolationType;
    }
}
