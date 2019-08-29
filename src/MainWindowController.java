import javafx.embed.swing.SwingFXUtils;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.WritableImage;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import javax.imageio.ImageIO;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class MainWindowController {

    @FXML
    private Button interpAdd;

    @FXML
    private Button interpDelete;

    @FXML
    private Button transformAdd;

    @FXML
    private Button transformDelete;

    @FXML
    private Button chooseFile;

    @FXML
    private Button interpGo;

    @FXML
    private LineChart<Double,Double> chart;

    @FXML
    private Button doChanges;

    @FXML
    private Button exportFile;

    @FXML
    private Button exportChart;

    @FXML
    private TableView<InterpolationData> interpTable;

    @FXML
    private TableColumn<InterpolationData, String> startTempColumn;

    @FXML
    private TableColumn<InterpolationData, String> endTempColumn;

    @FXML
    private TableColumn<InterpolationData, String> typeColumn;

    @FXML
    private TableView<ChangeData> entalpTable;

    @FXML
    private TableColumn<InterpolationData, String> entalpStartTemp;

    @FXML
    private TableColumn<InterpolationData, String> entalpEndtemp;

    @FXML
    private TableColumn<InterpolationData, String> entalpMethod;

    @FXML
    private TableColumn<InterpolationData, String> entalpEffect;

    @FXML
    private TableColumn<InterpolationData, String> entalpExpected;

    @FXML
    private TableColumn<InterpolationData, String> entalpVariance;

    @FXML
    private TableView<ChangeData> allTable;

    @FXML
    private TableColumn<InterpolationData, String> allStartTemp;

    @FXML
    private TableColumn<InterpolationData, String> allEndtemp;

    @FXML
    private TableColumn<InterpolationData, String> allMethod;

    @FXML
    private TableColumn<InterpolationData, String> allEffect;

    @FXML
    private TableColumn<InterpolationData, String> allExpected;

    @FXML
    private TableColumn<InterpolationData, String> allVariance;

    @FXML
    private Button unmark;

    @FXML
    private  Button best;

    private ArrayList<Double> temps = new ArrayList<Double>();
    private ArrayList<Double> cP = new ArrayList<Double>();
    private final FileChooser fileChooser = new FileChooser();
    private final Interp interpolation = new Interp();
    int selectedIndex;
    private ArrayList<Double> entalpyInterpolated = new ArrayList<Double>();
    private ArrayList<Double> entalpySaveCopy = new ArrayList<Double>();
    private boolean fileLoaded = false;
    private ArrayList<Double> entalpyDifference = new ArrayList<Double>();
    private Adder adder = new Adder(entalpyInterpolated);
    private ArrayList<Double> fulltemps = new ArrayList<Double>();
    private boolean interpolated = false;


    public void reset()
    {
        temps=new ArrayList<Double>();
        cP = new ArrayList<Double>();
        entalpyInterpolated = new ArrayList<Double>();
        fileLoaded = false;
        entalpyDifference = new ArrayList<Double>();
        fulltemps = new ArrayList<Double>();
        chart.getData().remove(0,chart.getData().size());
        interpTable.getItems().remove(0,interpTable.getItems().size());
        entalpTable.getItems().remove(0,entalpTable.getItems().size());
        interpolated = false;
        adder = new Adder(entalpyInterpolated);
        this.allTable.getItems().remove(0,allTable.getItems().size());
    }

    @FXML
    public void initialize() {

        chart.getXAxis().setLabel("Temperatura [deg]");
        chart.getYAxis().setLabel("Entalpia [kJ/kg]");
        chart.setCreateSymbols(false);
        chart.setLegendVisible(false);
        chart.setAnimated(false);
        interpAdd.setOnAction((e)->{
            if(!fileLoaded)
            {
                this.noFile();
                return;
            }
            Parent root;
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("interpolation.fxml"));


                InterpolationController interpController = new InterpolationController();
                interpController.setTableView(interpTable);
                interpController.setcP(this.cP);
                interpController.setTemperatures(this.temps);
                //InterpolationController interpolationController = fxmlLoader.<InterpolationController>getController();
                //interpolationController.setListView(interpolations);
                //interpolationController.setcP(this.cP);
                //interpolationController.setTemperatures(this.temps);
                fxmlLoader.setController(interpController);

                root = (Parent)fxmlLoader.load();
                Stage stage = new Stage();
                stage.setTitle("Wybierz interpolacje na przedziale");
                stage.initModality(Modality.WINDOW_MODAL);
                stage.initOwner(chooseFile.getScene().getWindow());
                stage.setScene(new Scene(root,450,450));


                stage.show();

                // Hide this current window (if this is what you want)
                //((Node)(e.getSource())).getScene().getWindow().hide();
            }
            catch (IOException a) {
                a.printStackTrace();
            }

        });

        chooseFile.setOnAction((e)->{
            Stage stage = (Stage) chooseFile.getScene().getWindow();
            File file = fileChooser.showOpenDialog(stage);
            if (file != null) {
                System.out.println(file.toString());


                ArrayList<String[]> words = new ArrayList<String[]>();
                try (BufferedReader br = Files.newBufferedReader(Paths.get(file.toString()))) {

                    String line;
                    int line_number=0;

                    while ((line = br.readLine()) != null) {
                        if(line_number>4)
                        {
                            words.add(line.split("\\s"));
                        }
                        line_number++;
                    }
                    reset();
                    fileLoaded=true;


                } catch (IOException exe) {
                    System.err.format("IOException: %s%n", exe);
                }

                ArrayList<Double> data = new ArrayList<Double>();
                for(int i=0;i<words.size();i++)
                {
                    data.add(Double.parseDouble(words.get(i)[0]));
                    data.add(Double.parseDouble(words.get(i)[1]));
                }


                for(int i=0;i<data.size();i++)
                {
                    if(i%2==0)
                    {
                        temps.add(data.get(i));
                    }
                    else
                    {
                        cP.add(data.get(i));
                    }
                }

                entalpyDifference.add(0.0);
                for(int i=1;i<temps.size();i++)
                {
                    entalpyDifference.add(entalpyDifference.get(i-1)+((cP.get(i)+ cP.get(i-1))/2)*(temps.get(i)-temps.get(i-1)));
                }
                System.out.println("Added");
                /*
                double xstart = data.get(0);
                double xfinish = data.get(data.size()-2);
                double fxstart = data.get(1);
                double fxfinish = data.get(data.size()-1);
                for(Double i=data.get(0);i<=data.get(data.size()-2);i++)
                {
                    System.out.println(i+ " : "+ interpolation.interpolatin_linear(i,xstart,xfinish,fxstart,fxfinish));
                }
                */
                chart.getData().remove(0,chart.getData().size());
                XYChart.Series series = new XYChart.Series();
                for(int i=0;i<temps.size();i++)
                {
                    //series.getData().add(new XYChart.Data(this.temps.get(0)+i, entalpyInterpolated.get(i)));
                    series.getData().add(new XYChart.Data(temps.get(i), entalpyDifference.get(i)));
                    //System.out.println("Temp:" + (this.temps.get(0)+i) + " entalpia: " + entalpyInterpolated.get(i));
                }
                chart.getData().remove(0,chart.getData().size());
                chart.getData().add(series);
                chart.setCreateSymbols(true);

            }
        });

        interpDelete.setOnAction((event)->{
           if(interpTable.getSelectionModel().getSelectedIndex()!=-1)
           {
               interpTable.getItems().remove(interpTable.getSelectionModel().getSelectedIndex());
           }
        });

        best.setOnAction((event)->{
            if(!fileLoaded)
            {
                this.noFile();
                return;
            }
            if(interpolated)
            {
                alreadyInterpolated();
                return;
            }
            interpolated=true;
            chart.getData().remove(0,chart.getData().size());
            chart.setCreateSymbols(false);
            double start=temps.get(0);
            double finish=temps.get(1);
            double fstart = entalpyDifference.get(0);
            double ffinish = entalpyDifference.get(1);
            ArrayList<Double> tempTmp = new ArrayList<>();
            ArrayList<Double> entropyTmp = new ArrayList<>();
            for(int i=0;i<temps.size()-1;i++)
            {
                start=temps.get(i);
                finish=temps.get(i+1);
                fstart=entalpyDifference.get(i);
                ffinish=entalpyDifference.get(i+1);
                for(Double j=temps.get(i);j<temps.get(i+1);j++)
                {
                    entalpyInterpolated.add(interpolation.interpolatin_linear(j, start, finish, fstart, ffinish));
                    tempTmp.add(j);
                    fulltemps.add(j);
                    entropyTmp.add(interpolation.interpolatin_linear(j,start,finish,fstart,ffinish));
                }
            }
            entalpyInterpolated.add(entalpyDifference.get(entalpyDifference.size()-1));
            tempTmp.add(temps.get(temps.size()-1));
            fulltemps.add(temps.get(temps.size()-1));
            entropyTmp.add(entalpyDifference.get(entalpyDifference.size()-1));
            drawChart(tempTmp,entropyTmp);
            tempTmp.clear();
            entropyTmp.clear();
            //Solidus likwidus
            Parent root;
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("solidusLikwidus.fxml"));


                SolidusController solidusController = new SolidusController();
                solidusController.setChangeTable(entalpTable);
                solidusController.setTemperatures(this.temps);
                solidusController.setHistory(allTable);
                fxmlLoader.setController(solidusController);

                root = (Parent)fxmlLoader.load();
                Stage stage = new Stage();
                stage.setTitle("Wybierz przemiane");
                stage.initModality(Modality.WINDOW_MODAL);
                stage.initOwner(chooseFile.getScene().getWindow());
                stage.setScene(new Scene(root,450,450));
                stage.onCloseRequestProperty().setValue(new EventHandler<WindowEvent>() {
                    @Override
                    public void handle(WindowEvent event) {
                        event.consume();
                    }
                });


                stage.show();

                // Hide this current window (if this is what you want)
                //((Node)(e.getSource())).getScene().getWindow().hide();
            }
            catch (IOException a) {
                a.printStackTrace();
            }

        });

        interpGo.setOnAction((event)->{

            if(!fileLoaded)
            {
                this.noFile();
                return;
            }
            boolean isOk = true;
            if(interpolated)
            {
                alreadyInterpolated();
                return;
            }
            chart.getData().remove(0,chart.getData().size());
            chart.setCreateSymbols(false);
            interpTable.getItems().sort((first,second)->{
                if(first.getStartTemp()<second.getStartTemp())
                {
                    return -1;
                }
                return 1;
            });
            Double startTemp = this.temps.get(0);

            if(interpTable.getItems().size()==0)
            {
               isOk=false;
            }

            for(int i=0;i<interpTable.getItems().size();i++)
            {
                if(this.interpTable.getItems().get(i).getStartTemp()!=startTemp)
                {
                    isOk=false;
                    break;
                }
                startTemp=this.interpTable.getItems().get(i).getEndTemp()+1;
                if(i==interpTable.getItems().size()-1)   //ostatni element
                {
                    if(this.interpTable.getItems().get(i).getEndTemp()!=this.temps.get(this.temps.size()-1))
                    {
                        isOk=false;
                        break;
                    }
                }
            }


            if(!isOk)
            {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.getDialogPane().setMinWidth(400);
                alert.setTitle("Błąd");
                alert.setHeaderText("Nie pełny zakres interpolacji");
                alert.setContentText("Zakresy interpolacji muszą pokrywać cały podany obszar");
                //alert.setResizable(true);


                alert.showAndWait();
                return;
            }
            else
            {
                for(int i=0;i<interpTable.getItems().size();i++) //Po interpolacjach
                {
                    ArrayList<Double> tempTmp = new ArrayList<>();
                    ArrayList<Double> entropyTmp = new ArrayList<>();
                    if(interpTable.getItems().get(i).getInterpolationType()== Interpolations.Liniowa)
                    {
                        //set start
                        double start = interpTable.getItems().get(i).getStartTemp();
                        double fstart=0;
                        for(int k=0;k<temps.size();k++)
                        {
                            if(temps.get(k)==start)
                            {
                                fstart=entalpyDifference.get(k);
                                break;
                            }
                            if(temps.get(k)>start)
                            {
                                start=temps.get(k-1);
                                fstart=entalpyDifference.get(k-1);
                                break;
                            }
                        }
                        //Set finish
                        double finish = interpTable.getItems().get(i).getEndTemp();
                        double ffinish=0;
                        for(int k=temps.size()-1;k>=0;k--)
                        {
                            if(temps.get(k)==finish)
                            {
                                ffinish=entalpyDifference.get(k);
                                break;
                            }
                            if(temps.get(k)<finish)
                            {
                                finish=temps.get(k+1);
                                ffinish=entalpyDifference.get(k+1);
                                break;
                            }
                        }

                        for(Double j=interpTable.getItems().get(i).getStartTemp();j<=interpTable.getItems().get(i).getEndTemp();j++)
                        {
                            entalpyInterpolated.add(interpolation.interpolatin_linear(j,start,finish,fstart,ffinish));
                            tempTmp.add(j);
                            fulltemps.add(j);
                            entropyTmp.add(interpolation.interpolatin_linear(j,start,finish,fstart,ffinish));
                        }
                    }
                    else if(interpTable.getItems().get(i).getInterpolationType()==Interpolations.Kwadratowa)
                    {
                        //set start
                        double start = interpTable.getItems().get(i).getStartTemp();
                        double fstart=0;
                        for(int k=0;k<temps.size();k++)
                        {
                            if(temps.get(k)==start)
                            {
                                fstart=entalpyDifference.get(k);
                                break;
                            }
                            if(temps.get(k)>start)
                            {
                                start=temps.get(k-1);
                                fstart=entalpyDifference.get(k-1);
                                break;
                            }
                        }
                        //Set finish
                        double finish = interpTable.getItems().get(i).getEndTemp();
                        double ffinish=0;
                        for(int k=temps.size()-1;k>=0;k--)
                        {
                            if(temps.get(k)==finish)
                            {
                                ffinish=entalpyDifference.get(k);
                                break;
                            }
                            if(temps.get(k)<finish)
                            {
                                finish=temps.get(k+1);
                                ffinish=entalpyDifference.get(k+1);
                                break;
                            }
                        }

                        //setmid
                        int numberOfDataInInterval=0;
                        for(int z=0;z<this.temps.size();z++)
                        {
                            if(this.temps.get(z)>interpTable.getItems().get(i).getStartTemp() && this.temps.get(z)<interpTable.getItems().get(i).getEndTemp())
                            {
                                numberOfDataInInterval++;
                            }
                        }
                        if(numberOfDataInInterval>0)
                        {
                            numberOfDataInInterval = (int)Math.ceil(numberOfDataInInterval/2.0);
                            int tmp=0;
                            for(int k=0;k<temps.size();k++)
                            {
                                if(temps.get(k)==interpTable.getItems().get(i).getStartTemp())
                                {
                                    tmp=k;
                                }
                            }
                            double mid = this.temps.get(tmp+numberOfDataInInterval);
                            double fmid=this.entalpyDifference.get(tmp+numberOfDataInInterval);

                            for(Double j=interpTable.getItems().get(i).getStartTemp();j<=interpTable.getItems().get(i).getEndTemp();j++)
                            {
                                entalpyInterpolated.add(interpolation.interpolacja_square(j,start,mid,finish,fstart,fmid,ffinish));
                                tempTmp.add(j);
                                fulltemps.add(j);
                                entropyTmp.add(interpolation.interpolatin_linear(j,start,finish,fstart,ffinish));
                            }
                        }
                        else
                        {
                            //Uzyj liniowej bo nie da sie kwadratowej
                            for(Double j=interpTable.getItems().get(i).getStartTemp();j<=interpTable.getItems().get(i).getEndTemp();j++)
                            {
                                entalpyInterpolated.add(interpolation.interpolatin_linear(j,start,finish,fstart,ffinish));
                                tempTmp.add(j);
                                fulltemps.add(j);
                                entropyTmp.add(interpolation.interpolatin_linear(j,start,finish,fstart,ffinish));
                            }
                        }
                    }
                    else
                    {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.getDialogPane().setMinWidth(400);
                        alert.setTitle("Błąd");
                        alert.setHeaderText("Nie isnieje wybrany typ interpolacji");
                        alert.setContentText("Wybrany tym interpolacji nie istnieje");
                        //alert.setResizable(true);


                        alert.showAndWait();
                        return;
                    }
                    System.out.println("END");
                    drawChart(tempTmp,entropyTmp);
                    tempTmp.clear();
                    entropyTmp.clear();
                    interpolated=true;
                    Parent root;
                    try {
                        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("solidusLikwidus.fxml"));


                        SolidusController solidusController = new SolidusController();
                        solidusController.setChangeTable(entalpTable);
                        solidusController.setTemperatures(this.temps);
                        solidusController.setHistory(allTable);
                        fxmlLoader.setController(solidusController);

                        root = (Parent)fxmlLoader.load();
                        Stage stage = new Stage();
                        stage.setTitle("Wybierz przemiane");
                        stage.initModality(Modality.WINDOW_MODAL);
                        stage.initOwner(chooseFile.getScene().getWindow());
                        stage.setScene(new Scene(root,450,450));
                        stage.onCloseRequestProperty().setValue(new EventHandler<WindowEvent>() {
                            @Override
                            public void handle(WindowEvent event) {
                                event.consume();
                            }
                        });


                        stage.show();

                        // Hide this current window (if this is what you want)
                        //((Node)(e.getSource())).getScene().getWindow().hide();
                    }
                    catch (IOException a) {
                        a.printStackTrace();
                    }
                }

            }
        });

        transformAdd.setOnAction((event)->{
            if(!fileLoaded)
            {
                this.noFile();
                return;
            }
            if(!interpolated)
            {
                this.notInterpolated();
                return;
            }
            Parent root;
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("change.fxml"));


                ChangeController changeController = new ChangeController();
                changeController.setChangeTable(entalpTable);
                changeController.setTemperatures(this.temps);
                changeController.setHistory(allTable);
                fxmlLoader.setController(changeController);

                root = (Parent)fxmlLoader.load();
                Stage stage = new Stage();
                stage.setTitle("Wybierz przemiane");
                stage.initModality(Modality.WINDOW_MODAL);
                stage.initOwner(chooseFile.getScene().getWindow());
                stage.setScene(new Scene(root,450,450));


                stage.show();

                // Hide this current window (if this is what you want)
                //((Node)(e.getSource())).getScene().getWindow().hide();
            }
            catch (IOException a) {
                a.printStackTrace();
            }
        });
        transformDelete.setOnAction((event)->{
            if(entalpTable.getSelectionModel().getSelectedIndex()!=-1)
            {
                if(entalpTable.getItems().get(entalpTable.getSelectionModel().getSelectedIndex()).isDeletable())
                {
                    entalpTable.getItems().remove(entalpTable.getSelectionModel().getSelectedIndex());
                }
                else
                {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.getDialogPane().setMinWidth(400);
                    alert.setTitle("Akcja niemożliwa");
                    alert.setHeaderText("Nie można usunąć przemiany");
                    alert.setContentText("Tej przemiany nie można usunąć");
                    alert.showAndWait();
                }
            }
        });

        doChanges.setOnAction((event)->{
            if(!interpolated)
            {
                this.notInterpolated();
                return;
            }
            entalpTable.getSelectionModel().clearSelection();
            for (int i=0;i<entalpTable.getItems().size();i++)
            {
                adder.setTab(entalpyInterpolated);
                switch (entalpTable.getItems().get(i).getChangeType())
                {
                    case Dodaj_na_poczatek:
                        adder.dodaj_na_poczatek((int)entalpTable.getItems().get(i).getStartTemp() - this.temps.get(0).intValue(),(int)entalpTable.getItems().get(i).getEndTemp()- this.temps.get(0).intValue(),entalpTable.getItems().get(i).getValue());
                        reDraw(this.fulltemps,this.entalpyInterpolated);
                        break;
                    case dodaj_na_srodek:
                        adder.dodaj_na_srodek((int)entalpTable.getItems().get(i).getStartTemp() - this.temps.get(0).intValue(),(int)entalpTable.getItems().get(i).getEndTemp()- this.temps.get(0).intValue(),entalpTable.getItems().get(i).getValue());
                        reDraw(this.fulltemps,this.entalpyInterpolated);
                        break;
                    case dodaj_na_koniec:
                        adder.dodaj_na_koniec((int)entalpTable.getItems().get(i).getStartTemp()- this.temps.get(0).intValue(),(int)entalpTable.getItems().get(i).getEndTemp()- this.temps.get(0).intValue(),entalpTable.getItems().get(i).getValue());
                        reDraw(this.fulltemps,this.entalpyInterpolated);
                        break;
                    case rozklad_dowolny:
                        adder.rozklad_dowolny((int)entalpTable.getItems().get(i).getStartTemp()- this.temps.get(0).intValue(),(int)entalpTable.getItems().get(i).getEndTemp()- this.temps.get(0).intValue(),entalpTable.getItems().get(i).getExpected(),entalpTable.getItems().get(i).getVariance(),entalpTable.getItems().get(i).getValue());
                        reDraw(this.fulltemps,this.entalpyInterpolated);
                        break;
                    case dodaj_srednia_wartosc_na_przedziale:
                        adder.dodaj_srednia_wartosc_na_przedziale((int)entalpTable.getItems().get(i).getStartTemp()- this.temps.get(0).intValue(),(int)entalpTable.getItems().get(i).getEndTemp()- this.temps.get(0).intValue(),entalpTable.getItems().get(i).getValue());
                        reDraw(this.fulltemps,this.entalpyInterpolated);
                        break;
                    case wielomian:
                        adder.wielomian(entalpTable.getItems().get(i).getSize(),entalpTable.getItems().get(i).getCoefficients1(),entalpTable.getItems().get(i).getCoefficients2(),(int)entalpTable.getItems().get(i).getStartTemp()- this.temps.get(0).intValue(),(int)entalpTable.getItems().get(i).getEndTemp()- this.temps.get(0).intValue(),entalpTable.getItems().get(i).getValue(),entalpTable.getItems().get(i).getPrzedzial_x1(),entalpTable.getItems().get(i).getPrzedzial_x2());
                        reDraw(this.fulltemps,this.entalpyInterpolated);
                        break;
                    case fibbonaci:
                        adder.fibbonaci((int)entalpTable.getItems().get(i).getStartTemp()- this.temps.get(0).intValue(),(int)entalpTable.getItems().get(i).getEndTemp()- this.temps.get(0).intValue(),entalpTable.getItems().get(i).getValue());
                        reDraw(this.fulltemps,this.entalpyInterpolated);
                        break;
                    case fibbonaci_odwrocony:
                        adder.fibbonaci_odwrocony((int)entalpTable.getItems().get(i).getStartTemp()- this.temps.get(0).intValue(),(int)entalpTable.getItems().get(i).getEndTemp()- this.temps.get(0).intValue(),entalpTable.getItems().get(i).getValue());
                        reDraw(this.fulltemps,this.entalpyInterpolated);
                        break;
                    case fibboaci_wysrodkowany:
                        adder.fibboaci_wysrodkowany((int)entalpTable.getItems().get(i).getStartTemp()- this.temps.get(0).intValue(),(int)entalpTable.getItems().get(i).getEndTemp()- this.temps.get(0).intValue(),entalpTable.getItems().get(i).getValue());
                        reDraw(this.fulltemps,this.entalpyInterpolated);
                        break;
                }
            }

            for(int i=0;i<entalpTable.getItems().size();i++)
            {
                allTable.getItems().add(entalpTable.getItems().get(i));
            }
            entalpTable.getItems().remove(0,entalpTable.getItems().size());
        });

        exportFile.setOnAction((event)->{
            if(!interpolated)
            {
                notInterpolated();
                return;
            }
            try{

                List<String> lines = new ArrayList<String>();
                for(int i=0;i<entalpyInterpolated.size();i++)
                {
                    lines.add(fulltemps.get(i).toString()+"\t"+entalpyInterpolated.get(i).toString());
                }
                FileChooser myFile = new FileChooser();
                Stage stage = (Stage) chooseFile.getScene().getWindow();
                File filee = fileChooser.showSaveDialog(null);
                System.out.println(filee);
                Path file = Paths.get(filee.getPath()+".txt");
                Files.write(file, lines, Charset.forName("UTF-8"));
            }
            catch (IOException e)
            {
                System.out.println("Nie udalo sie zapisac do pliku");
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.getDialogPane().setMinWidth(400);
                alert.setTitle("Błąd");
                alert.setHeaderText("Nie udalo sie zapisac do pliku");
                alert.setContentText("");
                alert.showAndWait();
            }
        });
        exportChart.setOnAction((event)->{
            if(!interpolated)
            {
                notInterpolated();
                return;
            }
            chart.setAnimated(false);
            WritableImage image= chart.snapshot(new SnapshotParameters(),null);
            FileChooser myFile = new FileChooser();
            Stage stage = (Stage) chooseFile.getScene().getWindow();
            File imageFile = fileChooser.showSaveDialog(null);

            try {
                ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", new File(imageFile.getPath()+".png"));
            } catch (IOException e) {

            }
        });
        //First table
        startTempColumn.setCellValueFactory(
                new PropertyValueFactory<InterpolationData,String>("startTemp")
        );

        endTempColumn.setCellValueFactory(
                new PropertyValueFactory<InterpolationData,String>("endTemp")
        );

        typeColumn.setCellValueFactory(
                new PropertyValueFactory<InterpolationData,String>("interpolationType")
        );

        //Second table
        entalpStartTemp.setCellValueFactory(
                new PropertyValueFactory<InterpolationData,String>("startTemp")
        );
        entalpEndtemp.setCellValueFactory(
                new PropertyValueFactory<InterpolationData,String>("EndTemp")
        );
        entalpMethod.setCellValueFactory(
                new PropertyValueFactory<InterpolationData,String>("changeType")
        );
        entalpEffect.setCellValueFactory(
                new PropertyValueFactory<InterpolationData,String>("value")
        );
        entalpExpected.setCellValueFactory(
                new PropertyValueFactory<InterpolationData,String>("expected")
        );
        entalpVariance.setCellValueFactory(
                new PropertyValueFactory<InterpolationData,String>("variance")
        );

        allStartTemp.setCellValueFactory(
                new PropertyValueFactory<InterpolationData,String>("startTemp")
        );
        allEndtemp.setCellValueFactory(
                new PropertyValueFactory<InterpolationData,String>("EndTemp")
        );
        allMethod.setCellValueFactory(
                new PropertyValueFactory<InterpolationData,String>("changeType")
        );
        allEffect.setCellValueFactory(
                new PropertyValueFactory<InterpolationData,String>("value")
        );
        allExpected.setCellValueFactory(
                new PropertyValueFactory<InterpolationData,String>("expected")
        );
        allVariance.setCellValueFactory(
                new PropertyValueFactory<InterpolationData,String>("variance")
        );

        entalpTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if(oldSelection==null)
            {
                System.out.println("Create original copy");
                toSaveCopy();
                System.out.println(entalpyInterpolated);
                System.out.println(entalpySaveCopy);
            }
            if (newSelection != null) {
                System.out.println("Get save copy to start new simulation");
                fromSaveCopy();
                adder.setTab(entalpyInterpolated);
                switch (newSelection.getChangeType())
                {
                    case Dodaj_na_poczatek:
                        adder.dodaj_na_poczatek((int)newSelection.getStartTemp() - this.temps.get(0).intValue(),(int)newSelection.getEndTemp()- this.temps.get(0).intValue(),newSelection.getValue());
                        reDraw(this.fulltemps,this.entalpyInterpolated);
                        break;
                    case dodaj_na_srodek:
                        adder.dodaj_na_srodek((int)newSelection.getStartTemp() - this.temps.get(0).intValue(),(int)newSelection.getEndTemp()- this.temps.get(0).intValue(),newSelection.getValue());
                        reDraw(this.fulltemps,this.entalpyInterpolated);
                        break;
                    case dodaj_na_koniec:
                        adder.dodaj_na_koniec((int)newSelection.getStartTemp()- this.temps.get(0).intValue(),(int)newSelection.getEndTemp()- this.temps.get(0).intValue(),newSelection.getValue());
                        reDraw(this.fulltemps,this.entalpyInterpolated);
                        break;
                    case rozklad_dowolny:
                        adder.rozklad_dowolny((int)newSelection.getStartTemp()- this.temps.get(0).intValue(),(int)newSelection.getEndTemp()- this.temps.get(0).intValue(),newSelection.getExpected(),newSelection.getVariance(),newSelection.getValue());
                        reDraw(this.fulltemps,this.entalpyInterpolated);
                        break;
                    case dodaj_srednia_wartosc_na_przedziale:
                        adder.dodaj_srednia_wartosc_na_przedziale((int)newSelection.getStartTemp()- this.temps.get(0).intValue(),(int)newSelection.getEndTemp()- this.temps.get(0).intValue(),newSelection.getValue());
                        reDraw(this.fulltemps,this.entalpyInterpolated);
                        break;
                    case wielomian:
                        adder.wielomian(newSelection.getSize(),newSelection.getCoefficients1(),newSelection.getCoefficients2(),(int)newSelection.getStartTemp()- this.temps.get(0).intValue(),(int)newSelection.getEndTemp()- this.temps.get(0).intValue(),newSelection.getValue(),newSelection.getPrzedzial_x1(),newSelection.getPrzedzial_x2());
                        reDraw(this.fulltemps,this.entalpyInterpolated);
                        break;
                    case fibbonaci:
                        adder.fibbonaci((int)newSelection.getStartTemp()- this.temps.get(0).intValue(),(int)newSelection.getEndTemp()- this.temps.get(0).intValue(),newSelection.getValue());
                        reDraw(this.fulltemps,this.entalpyInterpolated);
                        break;
                    case fibbonaci_odwrocony:
                        adder.fibbonaci_odwrocony((int)newSelection.getStartTemp()- this.temps.get(0).intValue(),(int)newSelection.getEndTemp()- this.temps.get(0).intValue(),newSelection.getValue());
                        reDraw(this.fulltemps,this.entalpyInterpolated);
                        break;
                    case fibboaci_wysrodkowany:
                        adder.fibboaci_wysrodkowany((int)newSelection.getStartTemp()- this.temps.get(0).intValue(),(int)newSelection.getEndTemp()- this.temps.get(0).intValue(),newSelection.getValue());
                        reDraw(this.fulltemps,this.entalpyInterpolated);
                        break;
                }
                System.out.println(entalpyInterpolated);
                System.out.println(entalpySaveCopy);
            }
            if(newSelection==null)
            {
                System.out.println("Get save copy to finish");
                fromSaveCopy();
                System.out.println(entalpyInterpolated);
                System.out.println(entalpySaveCopy);
                reDraw(this.fulltemps,this.entalpyInterpolated);
            }
        });

        unmark.setOnAction((event)->{
            entalpTable.getSelectionModel().clearSelection();

        });


    }

    private void noFile()
    {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.getDialogPane().setMinWidth(400);
        alert.setTitle("Błąd");
        alert.setHeaderText("Nie załadowano pliku");
        alert.setContentText("Prosze wybrac plik");
        alert.showAndWait();
    }

    private void drawChart(ArrayList<Double> temp, ArrayList<Double> entalp)
    {
        XYChart.Series series = new XYChart.Series();
        for(int i=0;i<temp.size();i++)
        {
            //series.getData().add(new XYChart.Data(this.temps.get(0)+i, entalpyInterpolated.get(i)));
            series.getData().add(new XYChart.Data(temp.get(i), entalp.get(i)));
            //System.out.println("Temp:" + (this.temps.get(0)+i) + " entalpia: " + entalpyInterpolated.get(i));
        }
        chart.getData().add(series);

    }
    private void reDraw(ArrayList<Double> temp, ArrayList<Double> entalp)
    {
        chart.getData().remove(0,chart.getData().size());
        XYChart.Series series = new XYChart.Series();
        for(int i=0;i<temp.size();i++)
        {
            //series.getData().add(new XYChart.Data(this.temps.get(0)+i, entalpyInterpolated.get(i)));
            series.getData().add(new XYChart.Data(temp.get(i), entalp.get(i)));
            //System.out.println("Temp:" + (this.temps.get(0)+i) + " entalpia: " + entalpyInterpolated.get(i));
        }
        chart.getData().remove(0,chart.getData().size());
        chart.getData().add(series);
    }

    private void notInterpolated()
    {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.getDialogPane().setMinWidth(400);
        alert.setTitle("Błąd");
        alert.setHeaderText("Nie zinterpolowano przedziału");
        alert.setContentText("Prosze najpierw wykonac interpolacje");
        alert.showAndWait();
    }

    private void alreadyInterpolated()
    {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.getDialogPane().setMinWidth(400);
        alert.setTitle("Błąd");
        alert.setHeaderText("Już zinterpolowano dane");
        alert.setContentText("Aby dokonać ponownej interpolacji proszę ponownie wybrać plik wejściowy");
        alert.showAndWait();
    }

    public void toSaveCopy()
    {
        entalpySaveCopy=new ArrayList<Double>(entalpyInterpolated);
    }

    public void fromSaveCopy()
    {
        entalpyInterpolated = new ArrayList<Double>(entalpySaveCopy);
    }
}
