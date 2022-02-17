package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.BorderPane;
import models.Scene;
import models.SceneContainer;

public class MainstoryController {
    private SceneContainer sceneCont = new SceneContainer();
    private Scene currentScene;

    @FXML
    private Label fiveYearLabel;

    @FXML
    private BorderPane introPane;

    @FXML
    private BorderPane mainPane;

    @FXML
    private Button nextButton;

    @FXML
    private Button submitButton;

    @FXML
    private ToggleButton mainButton1;

    @FXML
    private ToggleButton mainButton2;

    @FXML
    private ToggleButton mainButton3;

    @FXML
    private ToggleGroup mainChoice;

    @FXML
    private Label mainLabel1;

    @FXML
    private Label mainLabel2;

    @FXML
    private Label statAge;

    @FXML
    private Label statChildren;

    @FXML
    private Label statHealth;

    @FXML
    private Label statName;

    @FXML
    private Label statPartner;

    @FXML
    private Label statWorth;

    @FXML
    void introNextPressed(ActionEvent event) {
        currentScene = sceneCont.getRandomScene(GuiController.getPlayer());
        mainLabel1.setText(currentScene.getPrompt());
        mainButton1.setText(currentScene.getOptions().get(0));
        mainButton2.setText(currentScene.getOptions().get(1));
        mainButton3.setText(currentScene.getOptions().get(2));
        introPane.setVisible(false);
        mainPane.setVisible(true);
        statAge.setText(GuiController.getPlayer().getAgeString());
        statChildren.setText(GuiController.getPlayer().getChildrenString());
        statHealth.setText(GuiController.getPlayer().getHealthString());
        statName.setText(GuiController.getPlayer().getName());
        statPartner.setText(GuiController.getPlayer().getPartner() == null ? "none" : GuiController.getPlayer().getPartner().getName());
        statWorth.setText((GuiController.getPlayer().getPrettyNetWorth()));
    }

    @FXML
    void mainButtonPressed(ActionEvent event) {
        submitButton.setDisable(false);
    }

    @FXML
    void mainSubmitPressed(ActionEvent event) {
        ToggleButton selected = (ToggleButton) mainChoice.getSelectedToggle();
        String resp = selected.getText();
        int index = currentScene.getOptions().indexOf(resp);
        mainLabel2.setText(currentScene.getOutcomes().get(index));
        EffectsTranslator.doEffects(GuiController.getPlayer(),currentScene.getEffects().get(index));
        nextButton.setDisable(false);
        selected.setSelected(false);
        mainButton1.setDisable(true);
        mainButton2.setDisable(true);
        mainButton3.setDisable(true);
    }

    @FXML
    void mainNextPressed(ActionEvent event) {
        GuiController.getPlayer().addAge(5);
        GuiController.getPlayer().addSalary();
        currentScene = sceneCont.getNewScene(GuiController.getPlayer());
        if (GuiController.getPlayer().getNetWorth() >= 1000000 && GuiController.getPlayer().getHealth() > 0){
            GuiController.loadScene(event, "youwin");
        } else if (GuiController.getPlayer().getHealth() == 0) {
            GuiController.loadScene(event, "youlose");
        } else if ("midlifeCrisis".equals(currentScene.getCategory())){
            fiveYearLabel.setVisible(true);
            mainLabel1.setText(currentScene.getPrompt());
            mainButton1.setText(currentScene.getOptions().get(0));
            mainButton2.setText(currentScene.getOptions().get(1));
            mainButton3.setVisible(false);
            statAge.setText(GuiController.getPlayer().getAgeString());
            statChildren.setText(GuiController.getPlayer().getChildrenString());
            statHealth.setText(GuiController.getPlayer().getHealthString());
            statName.setText(GuiController.getPlayer().getName());
            statPartner.setText(GuiController.getPlayer().getMarried().toString());
            statWorth.setText((GuiController.getPlayer().getPrettyNetWorth()));
            mainButton1.setDisable(false);
            mainButton2.setDisable(false);
            submitButton.setDisable(true);
            nextButton.setDisable(true);
            mainLabel2.setText("");
        } else {
            mainButton3.setVisible(true);
            fiveYearLabel.setVisible(true);
            mainLabel1.setText(currentScene.getPrompt());
            mainButton1.setText(currentScene.getOptions().get(0));
            mainButton2.setText(currentScene.getOptions().get(1));
            mainButton3.setText(currentScene.getOptions().get(2));
            statAge.setText(GuiController.getPlayer().getAgeString());
            statChildren.setText(GuiController.getPlayer().getChildrenString());
            statHealth.setText(GuiController.getPlayer().getHealthString());
            statName.setText(GuiController.getPlayer().getName());
            statPartner.setText(GuiController.getPlayer().getMarried().toString());
            statWorth.setText((GuiController.getPlayer().getPrettyNetWorth()));
            mainButton1.setDisable(false);
            mainButton2.setDisable(false);
            mainButton3.setDisable(false);
            submitButton.setDisable(true);
            nextButton.setDisable(true);
            mainLabel2.setText("");
        }

    }

    @FXML
    void quitPressed(ActionEvent event) {

    }

}


