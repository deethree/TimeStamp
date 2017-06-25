package gui.controller;

import handling.Archiv_Handler;
import handling.Manager;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import main.Main_Application;
import object.Report_Object;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;

/**
 * Created by Eike on 11.06.2017.
 */
public class CTR_Report {

    private DatePicker datePicker = new DatePicker(LocalDate.now());
    //private DatePicker datePicker2 = new DatePicker((LocalDate.now()));
    private ArrayList<Report_Object> reports;
    private ScrollPane scrollPane = new ScrollPane();
    private VBox mainVBox;
    private HBox headerHBox;
    private VBox entryVBox;
    private Label label_dayTime;

    public CTR_Report() {

    }

    public void initialize() {
        mainVBox = new VBox();
        headerHBox = new HBox();
        entryVBox = new VBox();
        label_dayTime = new Label();
        ToggleButton expandButton = new ToggleButton("alle ausklappen");
        label_dayTime.setStyle("-fx-font-size: 18px;" +
                "-f-font-weight: bold;");
        Label label_von = new Label("von");
        Label label_bis = new Label("bis");
        headerHBox.getChildren().addAll(datePicker, expandButton);
        mainVBox.getChildren().addAll(headerHBox, entryVBox, label_dayTime);

        datePicker.setOnAction(event -> {
            LocalDate date = datePicker.getValue();
            //datePicker2.setValue(date);
            updateView(date);
        });

        expandButton.setOnAction(event -> {
            expandAll(expandButton);
        });

        /*datePicker2.setOnAction(event -> {
            LocalDate date1 = datePicker.getValue();
            LocalDate date2 = datePicker2.getValue();
            if(date1.isAfter(date2)){
                System.out.println("Zeitraum falsch");
            } else {
                updatePeriodView(date1, date2);
            }
        });*/

        updateView(datePicker.getValue());

        scrollPane.setContent(mainVBox);
        Main_Application.ctr_dashboard.borderpane.setCenter(scrollPane);
    }

    private void updateView(LocalDate date) {
        int dayTimeSeconds = 0;
        reports = new ArrayList<>();
        for(CTR_Project_Module project : Manager.projectList) {
            if(project.getAllDates().contains(date)){
                reports.add(new Report_Object(project.getName(), project.getClient(), project.getMaxTimeHours(), project.getStorageObjects(), reports.size()));
                dayTimeSeconds += project.getSecondsByDate(date);
            }
        }

        for(Report_Object report : Archiv_Handler.archivObjects) {
            if(report.getAllDates().contains(date)){
                reports.add(report);
                dayTimeSeconds += report.getSecondsByDate(date);
            }
        }
        label_dayTime.setText("Du hast heute " + String.valueOf(Manager.printTimeWithoutSec(dayTimeSeconds)) + " gearbeitet.");

        try {
            createUI(reports, date);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void updatePeriodView(LocalDate date1, LocalDate date2) {

    }

    private void createUI(ArrayList<Report_Object> reports, LocalDate date) throws IOException {
        entryVBox.getChildren().clear();
        for(Report_Object report : reports) {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/report_module.fxml"));
            report.setDate(date);
            fxmlLoader.setController(report);
            Parent reportUI = fxmlLoader.load();
            //Manager.projectUIList.add(projectUI);
            entryVBox.getChildren().add(reportUI);
        }
    }

    private void expandAll(ToggleButton btn) {

        for(Report_Object report : reports) {
            report.titlepane.setExpanded(btn.isSelected());
        }
    }

}
