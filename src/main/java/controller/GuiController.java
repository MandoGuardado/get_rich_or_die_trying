package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;
import models.Backstory;
import models.BackstoryOption;
import models.Person;
import view.GuiApp;

import java.util.List;

public class GuiController {
    private static Stage stage;
    private static Scene scene;
    private static Person player = new Person();


    @FXML
    private Label loadLabel;

    @FXML
    private Button loadSubmitButton;

    @FXML
    private TextField loadText;


    @FXML
    private ToggleGroup difficulty;

    @FXML
    private Button newGameNext;

    @FXML
    private TextField ngName;

    @FXML
    void newGameNextPressed(ActionEvent event) {
        String name = ngName.getText();
        RadioButton selected = (RadioButton) difficulty.getSelectedToggle();
        String diff = selected.getText();
        player.setName(name);
        if (diff.toLowerCase().startsWith("w")){
            player.setPrivilege(false);
        }else {
            player.setPrivilege(true);
        }
        loadScene(event,"backstory");
    }

    @FXML
    void playAgainPressed(ActionEvent event) {
        player = new Person();
        loadScene(event,"newgame");
    }

    @FXML
    void loadSubmitPressed(ActionEvent event) {

    }


    @FXML
    public void switchToNewGameScene(ActionEvent event) {
        loadScene(event,"newGame");
    }

    @FXML
    public void switchToLoadGameScene(ActionEvent event) {
        loadScene(event,"loadGame");
    }

    static void loadScene(ActionEvent event, String sceneChosen){
        try {
            Parent root = FXMLLoader.load(GuiApp.class.getResource(sceneChosen+".fxml"));
            stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            scene = new Scene(root,1500, 1000);
            stage.setScene(scene);
            stage.show();

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @FXML
    private void handleKeyReleased(){
        String text = ngName.getText();
        boolean disableButton = text.isEmpty() || text.trim().isEmpty();
        newGameNext.setDisable(disableButton);
    }

    @FXML
    void quitPressed(ActionEvent event) {
        System.exit(1);
    }

    public static Person getPlayer() {
        return player;
    }


    public void loadNextPressed(ActionEvent event) {
    }
}