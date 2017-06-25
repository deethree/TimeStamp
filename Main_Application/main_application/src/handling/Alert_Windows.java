package handling;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextInputDialog;
import main.Main_Application;

import java.util.Optional;

/**
 * Created by Eike on 20.05.2017.
 */
public class Alert_Windows {

    public static String inputBox(String title, String header, String text){
        TextInputDialog dialog = new TextInputDialog("");
        dialog.setTitle(title);
        dialog.setHeaderText(header);
        dialog.setContentText(text);
        dialog.initOwner(Main_Application.primaryStage);

        Optional<String> result = dialog.showAndWait();

        if(result.isPresent()) {
            return result.get();
        }else{
            return null;
        }
    }

    public static boolean confirmDialog(String title, String header, String text) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(text);
        alert.initOwner(Main_Application.primaryStage);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK){
            return true;
        } else {
            return false;
        }
    }

}
