import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Adder {
    ArrayList<Double> tab;

    public Adder(ArrayList<Double> tab) {
        this.tab = tab;
    }

    public ArrayList<Double> getTab() {
        return tab;
    }

    public void showTab()
    {
        System.out.println(tab.toString());
    }

    public void reset()
    {
        tab= new ArrayList<Double>();
        for(int i=0;i<30;i++)
        {
            tab.add(0.0);
        }
    }

    public void setTab(ArrayList<Double> tab) {
        this.tab = tab;
    }

    public void dodaj_na_poczatek(int x1, int x2, double wartosc) {
        tab.set(x1,tab.get(x1)+wartosc);
        for (int i = x1 + 1; i < tab.size(); i++)
            tab.set(i,tab.get(i)+wartosc);
    }

    public void dodaj_na_koniec(int x1, int x2, double wartosc) {
        tab.set(x2,tab.get(x2)+wartosc);
        for (int i = x2 + 1; i < tab.size(); i++)
            tab.set(i,tab.get(i)+wartosc);
    }

    public void dodaj_na_srodek(int x1, int x2, double wartosc) {
        tab.set((x1+x2)/2,tab.get((x1+x2)/2)+wartosc);
        for (int i = (x1 + x2) / 2 + 1; i < tab.size(); i++)
            tab.set(i,tab.get(i)+wartosc);
    }

    public void dodaj_srednia_wartosc_na_przedziale(int x1, int x2, double wartosc) {
        for (int i = x1; i < x2 + 1; i++) {
            tab.set(i,tab.get(i)+wartosc / (double) (x2 - x1 + 1));
            for (int j = i + 1; j < x2 + 1; j++)
                tab.set(j,tab.get(j)+wartosc / (double) (x2 - x1 + 1));
        }
        for (int i = x2 + 1; i < tab.size(); i++)
            tab.set(i,tab.get(i)+wartosc);

    }

    public void rozklad_dowolny(int x1, int x2, double wartosc_oczekiwana, double wariancja, double wartosc) {
	ArrayList<Double> tab_tmp = new ArrayList<Double>();
        for (int i = 0; i < x2 + 1; i++)
            tab_tmp.add(0.0);
        int srodek = (x1 + x2) / 2;
        double delta;
        double poczatek = -3;
        double tmp;
        delta = 3. / (srodek - x1);
        for (int i = x1; i < srodek; i++, poczatek += delta) {
            tmp = rozklad(wartosc_oczekiwana, wariancja, poczatek) * wartosc;
            tab_tmp.set(i,tab_tmp.get(i)+tmp);
            for (int j = i + 1; j < x2 + 1; j++)
                tab_tmp.set(j,tab_tmp.get(j)+tmp);
        }
        tmp = rozklad(wartosc_oczekiwana, wariancja, 0) * wartosc;
        tab_tmp.set(srodek,tab_tmp.get(srodek)+tmp);
        for (int i = srodek + 1; i < x2 + 1; i++)
            tab_tmp.set(i,tab_tmp.get(i)+tmp);
        delta = 3. / (x2 - srodek);
        poczatek = 0 + delta;
        for (int i = srodek + 1; i < x2 + 1; i++, poczatek += delta) {
            tmp = rozklad(wartosc_oczekiwana, wariancja, poczatek) * wartosc;
            tab_tmp.set(i,tab_tmp.get(i)+tmp);
            for (int j = i + 1; j < x2 + 1; j++)
                tab_tmp.set(j,tab_tmp.get(j)+tmp);
        }
        for (int i = 0; i < x2 + 1; i++) {
            tab_tmp.set(i,tab_tmp.get(i)*((wartosc / tab_tmp.get(x2))));
            tab.set(i,tab.get(i)+tab_tmp.get(i));
        }
        for (int i = x2 + 1; i < tab.size(); i++)
            tab.set(i,tab.get(i)+wartosc);
    }

    double rozklad(double wartosc_oczekiwana, double wariancja, double x) {
        return (1. / (wariancja * Math.sqrt(2. * 3.141592654))) * Math.exp(-(x - wartosc_oczekiwana) * (x - wartosc_oczekiwana) / (2 * wariancja * wariancja));
    }


    public void wielomian(int rozmiar, ArrayList<Double> wspolczynniki1, ArrayList<Double> wspolczynniki2, int x1, int x2, double wartosc, int przedzial_x1, int przedzial_x2) {
        ArrayList<Double> tmp3 = wspolczynniki1;
        wspolczynniki1=wspolczynniki2;
        wspolczynniki2=tmp3;
        Collections.reverse(wspolczynniki1);
        Collections.reverse(wspolczynniki2);
	ArrayList<Double> tab_tmp = new ArrayList<Double>();
        for (int i = 0; i < x2 + 1; i++)
            tab_tmp.add(0.0);
        double tmp = przedzial_x1;
        double tmp2 = 0;
        double delta = ((double) przedzial_x2 - (double) przedzial_x1) / ((double) x2 - (double) x1);
        for (int i = x1; i < x2 + 1; i++, tmp += delta) {
            tab_tmp.set(i,tab_tmp.get(i)+wielomian_wartosc(tmp, rozmiar, wspolczynniki1,wspolczynniki2));
            tmp2 += wielomian_wartosc(tmp, rozmiar, wspolczynniki1,wspolczynniki2);
        }
        double iloraz = wartosc / tmp2;
        for (int i = x1; i < x2 + 1; i++)
            tab_tmp.set(i,tab_tmp.get(i)*iloraz);
        for (int i = 0; i < x2 + 1; i++) {
            tab.set(i,tab.get(i)+tab_tmp.get(i));
            for (int j = i + 1; j < x2 + 1; j++)
                tab.set(j,tab.get(j)+tab_tmp.get(i));
        }
        for (int i = x2 + 1; i < tab.size(); i++)
            tab.set(i,tab.get(i)+wartosc);
    }

    double wielomian_wartosc(double x, int rozmiar, ArrayList<Double> wspolczynniki1, ArrayList<Double> wspolczynniki2) {
        double tmp = 0;
        for (int i = rozmiar - 1; i > -1; i--)
            tmp += Math.pow(x - wspolczynniki1.get(i), i) * wspolczynniki2.get(i);
        return tmp;
    }

    public void fibbonaci(int x1, int x2, double wartosc) {
        double tmp = 0;
        double tmp2 = 0;
	ArrayList<Double> tab_tmp2 = new ArrayList<Double>();
        for (int i = 0; i < x2 + 1; i++)
            tab_tmp2.add(0.0);
	ArrayList<Double> tab_tmp = new ArrayList<Double>();
        for (int i = 0; i < x2 - x1 + 1; i++)
            tab_tmp.add(0.0);
        tab_tmp.set(0,1.0);
        tab_tmp.set(1,1.0);
        for (int i = 2; i < x2 - x1 + 1; i++)
            tab_tmp.set(i,tab_tmp.get(i-1)+tab_tmp.get(i-2));
        int j = 0;
        for (int i = x1; i < x2 + 1; i++, j++) {
            tmp = wartosc * tab_tmp.get(j);
            tab_tmp2.set(i,tab_tmp2.get(i)+tmp);
            tmp2 += tmp;
        }
        double iloraz = wartosc / tmp2;
        for (int i = x1; i < x2 + 1; i++, j++) {
            tab.set(i,tab.get(i)+tab_tmp2.get(i) * iloraz);
            for (int z = i + 1; z < x2 + 1; z++)
                tab.set(z,tab.get(z)+tab_tmp2.get(i) * iloraz);
        }
        for (int i = x2 + 1; i < tab.size(); i++)
            tab.set(i,tab.get(i)+wartosc);
    }


    public void fibbonaci_odwrocony(int x1, int x2, double wartosc) {
        double tmp = 0;
        double tmp2 = 0;
	double[] tab_tmp2 = new double[x2 + 1];
        for (int i = x2; i > x1 - 1; i--)
            tab_tmp2[i] = 0;
	double[] tab_tmp = new double[x2 + 1];
        for (int i = x1; i < x2 - x1 + 1; i++)
            tab_tmp[i] = 0;
        tab_tmp[x2] = 1;
        tab_tmp[x2 - 1] = 1;
        for (int i = x2 - 2; i > x1 - 1; i--)
            tab_tmp[i] = tab_tmp[i + 1] + tab_tmp[i + 2];
        for (int i = x1; i < x2 + 1; i++) {
            tmp = wartosc * tab_tmp[i];
            tab_tmp2[i] += tmp;
            tmp2 += tmp;
        }
        double iloraz = wartosc / tmp2;
        for (int i = x1; i < x2 + 1; i++) {
            tab.set(i,tab.get(i)+tab_tmp2[i]*iloraz);
            for (int j = i + 1; j < x2 + 1; j++)
                tab.set(j,tab.get(j)+tab_tmp2[i]*iloraz);
        }
        for (int i = x2 + 1; i < tab.size(); i++)
            tab.set(i,tab.get(i)+wartosc);
    }

    public void fibboaci_wysrodkowany(int x1, int x2, double wartosc) {
        double tmp = 0;
        double tmp2 = 0;
        double[] tab_tmp2 = new double[x2 + 1];
        for (int i = x2; i > x1 - 1; i--)
            tab_tmp2[i] = 0;
        double[] tab_tmp = new double[x2 + 1];
        for (int i = x1; i < x2 - x1 + 1; i++)
            tab_tmp[i] = 0;
        tab_tmp[x1] = 1;
        tab_tmp[x1 + 1] = 1;
        for (int i = x1 + 2; i < (x2 + x1) / 2 + 1; i++)
            tab_tmp[i] = tab_tmp[i - 1] + tab_tmp[i - 2];
        int j = x1;
        for (int i = x2; i > ((x2 + x1) / 2); i--, j++)
            tab_tmp[i] = tab_tmp[j];
        for (int i = x1; i < x2 + 1; i++) {
            tmp = wartosc * tab_tmp[i];
            tab_tmp2[i] += tmp;
            tmp2 += tmp;
        }
        double iloraz = wartosc / tmp2;
        for (int i = x1; i < x2 + 1; i++) {
            tab.set(i, tab.get(i) + tab_tmp2[i] * iloraz);
            for (int z = i + 1; z < x2 + 1; z++)
                tab.set(z, tab.get(z) + tab_tmp2[i] * iloraz);
        }
        for (int i = x2 + 1; i < tab.size(); i++)
            tab.set(i,tab.get(i)+wartosc);
    }

}
