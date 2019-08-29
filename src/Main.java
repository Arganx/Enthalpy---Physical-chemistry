import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.ArrayList;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        ArrayList<Double> test = new ArrayList<Double>();
        for(int i=0;i<30;i++)
        {
            test.add(0.0);
        }
        Adder adder = new Adder(test);

        System.out.println("Poczatkowa tablica");
        adder.showTab();

        adder.dodaj_na_poczatek(5,15,10);
        System.out.println("Dodaj na poczatek 5 15 10");
        adder.showTab();
        adder.reset();

        System.out.println("Dodaj na Koniec 5 15 10");
        adder.dodaj_na_koniec(5,15,10);
        adder.showTab();
        adder.reset();

        System.out.println("Dodaj na Koniec 5 15 10");
        adder.dodaj_na_koniec(5,15,10);
        adder.showTab();
        adder.reset();

        System.out.println("Dodaj na srodek 5 15 10");
        adder.dodaj_na_srodek(5,15,10);
        adder.showTab();
        adder.reset();

        System.out.println("dodaj_srednia_wartosc_na_przedziale 5 15 10");
        adder.dodaj_srednia_wartosc_na_przedziale(5,15,10);
        adder.showTab();
        adder.reset();

        System.out.println("rozklad_dowolny 5 15 5 2 10");
        adder.rozklad_dowolny(5,15,5,2,10);
        adder.showTab();
        adder.reset();


        ArrayList<Double> wspolczynniki1 = new ArrayList<Double>();
        wspolczynniki1.add(1.0);
        wspolczynniki1.add(0.0);
        ArrayList<Double> wspolczynniki2 = new ArrayList<Double>();
        wspolczynniki2.add(0.0);
        wspolczynniki2.add(0.0);
        System.out.println("wielomian 3, wspolczynniki, 5,15,10,0,6");
        adder.wielomian(2,wspolczynniki1,wspolczynniki2,5,15,10,0,6);
        adder.showTab();
        adder.reset();


        System.out.println("fibbonaci 5 15 10");
        adder.fibbonaci(5,15,10);
        adder.showTab();
        adder.reset();


        System.out.println("fibbonaci_odwrocony 5 15 10");
        adder.fibbonaci_odwrocony(5,15,10);
        adder.showTab();
        adder.reset();

        System.out.println("fibboaci_wysrodkowany 5 15 10");
        adder.fibboaci_wysrodkowany(5,15,10);
        adder.showTab();
        adder.reset();



        Parent root = FXMLLoader.load(getClass().getResource("mainWindow.fxml"));
        primaryStage.setTitle("Fizykochemia projekt");
        primaryStage.setScene(new Scene(root, 1980, 960));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
