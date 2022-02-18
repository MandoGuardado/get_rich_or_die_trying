package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import models.Person;
import models.SceneContainer;
import view.GuiApp;

import java.util.Objects;

public class GuiController {
    private static Person player = new Person();
    private static SceneContainer scenes = new SceneContainer();
    private boolean doesPlayerExist = false;


    @FXML
    private Label loadLabel;

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
            player.setNetWorth(player.getNetWorth() - 25000);
        }else {
            player.setPrivilege(true);
            player.setNetWorth(player.getNetWorth() + 25000);
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
        String resp = loadText.getText();
        if (scenes.getUsers().containsKey(resp)){
            loadLabel.setText("Player Found! You will continue where you left off...");
            player = scenes.getUsers().get(resp);
            doesPlayerExist = true;
        }else {
            loadLabel.setText("Player name was not found! New player record will be created.");
        }
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
            Parent root = FXMLLoader.load(Objects.requireNonNull(GuiApp.class.getResource(sceneChosen + ".fxml")));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root, 1500, 1000);
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
        if (doesPlayerExist){
            loadScene(event, "mainstory");
        } else {
            loadScene(event, "newGame");
        }
    }

    public static SceneContainer getScenes() {
        return scenes;
    }
}