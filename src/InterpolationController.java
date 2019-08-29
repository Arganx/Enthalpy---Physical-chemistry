import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.util.ArrayList;

public class InterpolationController {

    @FXML
    Button accept;

    @FXML
    Button cancel;

    @FXML
    TextField tempStart;

    @FXML
    TextField tempFinish;

    @FXML
    ComboBox<Interpolations> interpolationMethod;

    TableView<InterpolationData> tableView;

    ArrayList<Double> temperatures;
    ArrayList<Double> cP;

    public void setTableView(TableView<InterpolationData> tableView) {
        this.tableView = tableView;
    }


    @FXML
    public void initialize()
    {


        cancel.setOnAction((event)->
        {
            Stage stage = (Stage) cancel.getScene().getWindow();
            stage.close();
        });

        accept.setOnAction((event)->{
            if(tempFinish.getCharacters().length()>0 && tempStart.getCharacters().length()>0 && interpolationMethod.valueProperty().get()!=null)
            {
                Double start = Double.parseDouble(tempStart.getCharacters().toString());
                Double finish = Double.parseDouble(tempFinish.getCharacters().toString());
                if(start<finish)
                {
                    if(start>=temperatures.get(0) && finish<=temperatures.get(temperatures.size()-1))
                    {
                        boolean validated = true;
                        for (int i = 0; i < tableView.getItems().size(); i++) {
                            if (start >= tableView.getItems().get(i).getStartTemp() && start <= tableView.getItems().get(i).getEndTemp()) {
                                validated = false;
                            }
                            if (finish <= tableView.getItems().get(i).getEndTemp() && finish >= tableView.getItems().get(i).getStartTemp()) {
                                validated = false;
                            }
                        }
                        if (validated) {
                            tableView.getItems().add(new InterpolationData(Double.parseDouble(tempStart.getCharacters().toString()), Double.parseDouble(tempFinish.getCharacters().toString()), interpolationMethod.getValue()));
                            Stage stage = (Stage) accept.getScene().getWindow();
                            stage.close();
                        } else {
                            Alert alert = new Alert(Alert.AlertType.WARNING);
                            alert.setTitle("Błąd");
                            alert.setHeaderText("Występił błąd z wprowadzonymi temperaturami");
                            alert.setContentText("Przedziały interpolacji nachodzą na siebie");

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
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Błąd");
                    alert.setHeaderText("Występił błąd z wprowadzonymi temperaturami");
                    alert.setContentText("Upewnij sie że temperatura końcowa jest mniejsza niż początkowa");

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

        interpolationMethod.getItems().addAll(
                Interpolations.values()
        );

        /*
        tempStartBox.getItems().addAll(
                temperatures
        );

        tempFinishBox.getItems().addAll(
                temperatures
        );
        */


    }

    public void setTemperatures(ArrayList<Double> temperatures) {
        this.temperatures = temperatures;
    }

    public void setcP(ArrayList<Double> cP) {
        this.cP = cP;
    }
}
