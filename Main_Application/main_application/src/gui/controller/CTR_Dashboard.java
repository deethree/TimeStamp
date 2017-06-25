package gui.controller;

import handling.CSV_ClientHandler;
import handling.CSV_ProjectHandler;
import handling.Manager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import main.Main_Application;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by Eike on 20.05.2017.
 */
public class CTR_Dashboard implements Initializable {

    @FXML
    public VBox vbox_projList;
    @FXML
    public BorderPane borderpane;
    @FXML
    public ToggleButton btn_switchMenu;
    @FXML
    HBox left_menu_hbox;
    @FXML
    private Parent em_Menu;
    @FXML
    private CTR_Main_Menu em_MenuController;

    public static Stage stageNewProject;
    private CSV_ProjectHandler csv_projectHandler = new CSV_ProjectHandler();
    private CSV_ClientHandler csv_clientHandler = new CSV_ClientHandler();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //setze den Controller als statische Variable in main ein und setze den Dashboardbutton übern den
        // inludierten Menu Controller auf selected
        Main_Application.setdashboardController(this);
        em_MenuController.menu_dashboard.setSelected(true);
        //Wenn die csv Datei existiert, dann lade sie ins Programm
        if(csv_projectHandler.fileExist("data/trackingData.csv")) {
            try {
                csv_projectHandler.csvLoader();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if(csv_clientHandler.fileExist("data/clients.csv")) {
            try {
                csv_clientHandler.csvLoader();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public CTR_Dashboard() {

    }

    //lädt das NeueProjekt-Fenster
    public void showNewProjectWindow() throws IOException {
        stageNewProject = new Stage();

        //Parent newProject = FXMLLoader.load(getClass().getResource("/gui/fxml/new_project.fxml"));

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/new_project.fxml"));
        CTR_newProject ctr_newProject = new CTR_newProject(this);
        fxmlLoader.setController(ctr_newProject);
        Parent newProject = fxmlLoader.load();

        stageNewProject.setTitle("Neues Projekt anlegen");
        Scene sceneNewProject = new Scene(newProject);
        stageNewProject.initOwner(Main_Application.primaryStage);
        stageNewProject.setScene(sceneNewProject);
        stageNewProject.showAndWait();

    }

    public void showDashboardAtRuntime() throws IOException {
        vbox_projList.getChildren().clear();
        for(Parent projectUI : Manager.projectUIList) {
            vbox_projList.getChildren().add(projectUI);
        }
        borderpane.setCenter(vbox_projList);
    }


    public void removeProject(int index) {
        vbox_projList.getChildren().remove(index);
    }


}
