import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SolidusController {


    @FXML
    Button accept;


    @FXML
    TextField tempStart;

    @FXML
    TextField tempFinish;

    @FXML
    ComboBox<Change> changeMethod;

    @FXML
    TextField changeValue;

    TableView<ChangeData> changeTable;

    TableView<ChangeData> history;

    @FXML
    Label expected;

    @FXML
    Label variance;

    @FXML
    Label wsp1;

    @FXML
    Label wsp2;

    @FXML
    Label x1;

    @FXML
    Label x2;

    @FXML
    TextField intervalX1;

    @FXML
    TextField intervalX2;

    @FXML
    TextField expectedValue;

    @FXML
    TextField varianceValue;

    @FXML
    TextField w1;

    @FXML
    TextField w2;

    ArrayList<Double> temperatures;

    public void setChangeTable(TableView<ChangeData> changeTable) {
        this.changeTable = changeTable;
    }

    public void setTemperatures(ArrayList<Double> temperatures) {
        this.temperatures = temperatures;
    }

    public void setHistory(TableView<ChangeData> history) {
        this.history = history;
    }

    @FXML
    public void initialize()
    {


        accept.setOnAction((event)->{
            if(tempFinish.getCharacters().length()>0 && tempStart.getCharacters().length()>0 && changeMethod.valueProperty().get()!=null && changeValue.getCharacters().length()>0)
            {
                Double start = Double.parseDouble(tempStart.getCharacters().toString());
                Double finish = Double.parseDouble(tempFinish.getCharacters().toString());
                Double value = Double.parseDouble(changeValue.getCharacters().toString());
                if(start>=temperatures.get(0) && finish<=temperatures.get(temperatures.size()-1))
                {
                    //Walidacja przedzialu
                    boolean validated = true;
                    for (int i = 0; i < changeTable.getItems().size(); i++) {
                        if (start >= changeTable.getItems().get(i).getStartTemp() && start <= changeTable.getItems().get(i).getEndTemp()) {
                            validated = false;
                        }
                        if (finish <= changeTable.getItems().get(i).getEndTemp() && finish >= changeTable.getItems().get(i).getStartTemp()) {
                            validated = false;
                        }
                    }
                    for (int i = 0; i < history.getItems().size(); i++) {
                        if (start >= history.getItems().get(i).getStartTemp() && start <= history.getItems().get(i).getEndTemp()) {
                            validated = false;
                        }
                        if (finish <= history.getItems().get(i).getEndTemp() && finish >= history.getItems().get(i).getStartTemp()) {
                            validated = false;
                        }
                    }
                    if(validated)
                    {
                        if (start < finish) {

                            if (changeMethod.getValue() == Change.rozklad_dowolny) {
                                if (varianceValue.getCharacters().length() > 0 && expectedValue.getCharacters().length() > 0) {
                                    Double expe = Double.parseDouble(expectedValue.getCharacters().toString());
                                    Double vari = Double.parseDouble(varianceValue.getCharacters().toString());
                                    ChangeData changeData = new ChangeData(Double.parseDouble(tempStart.getCharacters().toString()), Double.parseDouble(tempFinish.getCharacters().toString()), changeMethod.getValue(), value, expe, vari);
                                    changeData.setDeletable(false);
                                    changeTable.getItems().add(changeData);
                                    Stage stage = (Stage) accept.getScene().getWindow();
                                    stage.close();
                                } else {
                                    Alert alert = new Alert(Alert.AlertType.WARNING);
                                    alert.setTitle("Błąd");
                                    alert.setHeaderText("Nie uzupelniono wspolczynnikow wymaganych do wybranej metody");
                                    alert.setContentText("Uzupelnij dane i ponów próbę");

                                    alert.showAndWait();
                                }
                            } else if (changeMethod.getValue() == Change.wielomian) {
                                if (w1.getCharacters().length() > 0 && w2.getCharacters().length() > 0 && intervalX1.getCharacters().length() > 0 && intervalX2.getCharacters().length() > 0) {
                                    ArrayList<Double> wsp1 = new ArrayList<Double>();
                                    ArrayList<Double> wsp2 = new ArrayList<Double>();
                                    String s1 = w1.getCharacters().toString();
                                    String s2 = w2.getCharacters().toString();
                                    Integer przedzialX1 = Integer.parseInt(intervalX1.getCharacters().toString());
                                    Integer przedzialX2 = Integer.parseInt(intervalX2.getCharacters().toString());

                                    String[] stringArray1 = s1.trim().split("\\s+");
                                    String[] stringArray2 = s2.trim().split("\\s+");
                                    List<String> stringList1 = Arrays.asList(stringArray1);
                                    List<String> stringList2 = Arrays.asList(stringArray2);
                                    if (stringList1.size() == stringList2.size()) {
                                        for (int i = 0; i < stringList1.size(); i++) {
                                            wsp1.add(Double.parseDouble(stringList1.get(i)));
                                        }
                                        for (int i = 0; i < stringList2.size(); i++) {
                                            wsp2.add(Double.parseDouble(stringList2.get(i)));
                                        }
                                        ChangeData changeData = new ChangeData(Double.parseDouble(tempStart.getCharacters().toString()), Double.parseDouble(tempFinish.getCharacters().toString()), changeMethod.getValue(), value, wsp1, wsp2, wsp1.size(), przedzialX1, przedzialX2);
                                        changeData.setDeletable(false);
                                        changeTable.getItems().add(changeData);
                                        Stage stage = (Stage) accept.getScene().getWindow();
                                        stage.close();
                                    } else {
                                        Alert alert = new Alert(Alert.AlertType.WARNING);
                                        alert.setTitle("Błąd");
                                        alert.setHeaderText("Liczba wspolczynnikow musi byc rowna");
                                        alert.setContentText("Uzupelnij dane i ponów próbę");

                                        alert.showAndWait();
                                    }


                                } else {
                                    Alert alert = new Alert(Alert.AlertType.WARNING);
                                    alert.setTitle("Błąd");
                                    alert.setHeaderText("Nie uzupelniono wspolczynnikow wymaganych do wybranej metody");
                                    alert.setContentText("Uzupelnij dane i ponów próbę");

                                    alert.showAndWait();
                                }
                            } else {
                                ChangeData changeData = new ChangeData(Double.parseDouble(tempStart.getCharacters().toString()), Double.parseDouble(tempFinish.getCharacters().toString()), changeMethod.getValue(), value);
                                changeData.setDeletable(false);
                                changeTable.getItems().add(changeData);
                                Stage stage = (Stage) accept.getScene().getWindow();
                                stage.close();
                            }

                        } else {
                            Alert alert = new Alert(Alert.AlertType.WARNING);
                            alert.setTitle("Błąd");
                            alert.setHeaderText("Występił błąd z wprowadzonymi temperaturami");
                            alert.setContentText("Upewnij sie że temperatura końcowa jest mniejsza niż początkowa");

                            alert.showAndWait();
                        }
                    }
                    else
                    {
                        Alert alert = new Alert(Alert.AlertType.WARNING);
                        alert.setTitle("Błąd");
                        alert.setHeaderText("Występił błąd z wprowadzonymi temperaturami");
                        alert.setContentText("Przedziały przemian nachodzą na siebie");

                        alert.showAndWait();
                    }
                }
                else
                {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Błąd");
                    alert.setHeaderText("Temperatura początkowa lub końcowa znajduje się poza zakresem danych");
                    alert.setContentText("Zmień wartość temperatury początkowej lub końcowej");

                    alert.showAndWait();
                }
            }
            else
            {
                //Alert
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Błąd");
                alert.setHeaderText("Występił błąd z przyjęciem danych");
                alert.setContentText("Uzupełnij brakujące dane");

                alert.showAndWait();
            }
        });

        tempStart.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if(newValue.matches("[-]"))
                {
                    return;
                }
                if (!newValue.matches("\\d*") && !newValue.matches("\\d*[.]\\d*") && !newValue.matches("[-]\\d*") && !newValue.matches("[-]\\d*[.]\\d*")) {
                    tempStart.setText(newValue.substring(0, newValue.length() - 1));
                }
                else if(newValue.matches("[.]"))
                {
                    tempStart.setText(newValue.substring(0, newValue.length() - 1));
                }
            }
        });

        tempFinish.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if(newValue.matches("[-]"))
                {
                    return;
                }
                if (!newValue.matches("\\d*") && !newValue.matches("\\d*[.]\\d*")  && !newValue.matches("[-]\\d*") && !newValue.matches("[-]\\d*[.]\\d*")) {
                    tempFinish.setText(newValue.substring(0, newValue.length() - 1));
                }
                else if(newValue.matches("[.]"))
                {
                    tempStart.setText(newValue.substring(0, newValue.length() - 1));
                }
            }
        });

        changeValue.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if(newValue.matches("[-]"))
                {
                    return;
                }
                if (!newValue.matches("\\d*") && !newValue.matches("\\d*[.]\\d*") && !newValue.matches("[-]\\d*") && !newValue.matches("[-]\\d*[.]\\d*")) {
                    changeValue.setText(newValue.substring(0, newValue.length() - 1));
                }
                else if(newValue.matches("[.]"))
                {
                    changeValue.setText(newValue.substring(0, newValue.length() - 1));
                }
            }
        });
        changeMethod.getItems().addAll(
                Change.values()
        );

        expectedValue.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if(newValue.matches("[-]"))
                {
                    return;
                }
                if (!newValue.matches("\\d*") && !newValue.matches("\\d*[.]\\d*")  && !newValue.matches("[-]\\d*") && !newValue.matches("[-]\\d*[.]\\d*")) {
                    expectedValue.setText(newValue.substring(0, newValue.length() - 1));
                }
                else if(newValue.matches("[.]"))
                {
                    expectedValue.setText(newValue.substring(0, newValue.length() - 1));
                }
            }
        });

        w1.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if(newValue.matches("[-]"))
                {
                    return;
                }
                if (newValue.matches("(.*)[a-zA-Z](.*)")){//"\\d*.,-") && !newValue.matches("\\d*[.]\\d*")  && !newValue.matches("[-]\\d*") && !newValue.matches("[-]\\d*[.]\\d*")) {
                    w1.setText(newValue.substring(0, newValue.length() - 1));
                }
            }
        });

        w2.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if(newValue.matches("[-]"))
                {
                    return;
                }
                if (newValue.matches("(.*)[a-zA-Z](.*)")){//"\\d*.,-") && !newValue.matches("\\d*[.]\\d*")  && !newValue.matches("[-]\\d*") && !newValue.matches("[-]\\d*[.]\\d*")) {
                    w2.setText(newValue.substring(0, newValue.length() - 1));
                }
            }
        });

        varianceValue.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if(newValue.matches("[-]"))
                {
                    return;
                }
                if (!newValue.matches("\\d*") && !newValue.matches("\\d*[.]\\d*")  && !newValue.matches("[-]\\d*") && !newValue.matches("[-]\\d*[.]\\d*")) {
                    varianceValue.setText(newValue.substring(0, newValue.length() - 1));
                }
                else if(newValue.matches("[.]"))
                {
                    varianceValue.setText(newValue.substring(0, newValue.length() - 1));
                }
            }
        });


        intervalX1.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if(newValue.matches("[-]"))
                {
                    return;
                }
                if (!newValue.matches("\\d*") && !newValue.matches("\\d*[.]\\d*")  && !newValue.matches("[-]\\d*") && !newValue.matches("[-]\\d*[.]\\d*")) {
                    intervalX1.setText(newValue.substring(0, newValue.length() - 1));
                }
                else if(newValue.matches("[.]"))
                {
                    intervalX1.setText(newValue.substring(0, newValue.length() - 1));
                }
            }
        });

        intervalX2.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if(newValue.matches("[-]"))
                {
                    return;
                }
                if (!newValue.matches("\\d*") && !newValue.matches("\\d*[.]\\d*")  && !newValue.matches("[-]\\d*") && !newValue.matches("[-]\\d*[.]\\d*")) {
                    intervalX2.setText(newValue.substring(0, newValue.length() - 1));
                }
                else if(newValue.matches("[.]"))
                {
                    intervalX2.setText(newValue.substring(0, newValue.length() - 1));
                }
            }
        });

        expectedValue.setDisable(true);
        varianceValue.setDisable(true);
        variance.setDisable(true);
        expected.setDisable(true);
        w1.setDisable(true);
        w2.setDisable(true);
        wsp1.setDisable(true);
        wsp2.setDisable(true);
        intervalX1.setDisable(true);
        intervalX2.setDisable(true);
        x1.setDisable(true);
        x2.setDisable(true);

        changeMethod.valueProperty().addListener((event)->{
            if(changeMethod.valueProperty().get()==Change.rozklad_dowolny)
            {
                variance.setDisable(false);
                expected.setDisable(false);
                varianceValue.setDisable(false);
                expectedValue.setDisable(false);
                w1.setDisable(true);
                w2.setDisable(true);
                wsp1.setDisable(true);
                wsp2.setDisable(true);
                w1.setText("");
                w2.setText("");
                intervalX1.setDisable(true);
                intervalX2.setDisable(true);
                x1.setDisable(true);
                x2.setDisable(true);
                intervalX1.setText("");
                intervalX2.setText("");
            }
            else if(changeMethod.valueProperty().get()==Change.wielomian)
            {
                variance.setDisable(true);
                varianceValue.setText("");
                expected.setDisable(true);
                expectedValue.setText("");
                varianceValue.setDisable(true);
                expectedValue.setDisable(true);
                w1.setDisable(false);
                w2.setDisable(false);
                wsp1.setDisable(false);
                wsp2.setDisable(false);
                intervalX1.setDisable(false);
                intervalX2.setDisable(false);
                x1.setDisable(false);
                x2.setDisable(false);
            }
            else
            {
                variance.setDisable(true);
                varianceValue.setText("");
                expected.setDisable(true);
                expectedValue.setText("");
                varianceValue.setDisable(true);
                expectedValue.setDisable(true);
                w1.setDisable(true);
                w1.setText("");
                w2.setText("");
                w2.setDisable(true);
                wsp1.setDisable(true);
                wsp2.setDisable(true);
                intervalX1.setDisable(true);
                intervalX2.setDisable(true);
                x1.setDisable(true);
                x2.setDisable(true);
                intervalX1.setText("");
                intervalX2.setText("");
            }
        });

    }

}
