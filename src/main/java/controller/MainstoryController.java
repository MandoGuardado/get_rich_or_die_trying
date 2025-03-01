package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.BorderPane;
import models.Scene;

import java.util.ArrayList;
import java.util.List;

public class MainstoryController {

    private Scene currentScene;

    @FXML
    private Label effectLabel0;

    @FXML
    private Label effectLabel1;

    @FXML
    private Label effectLabel2;

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
    private Label catLabel;

    @FXML
    private void introNextPressed() {
        currentScene = GuiController.getScenes().getRandomScene(GuiController.getPlayer());
        catLabel.setText(currentScene.getCategory().toUpperCase());
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
    private void mainSubmitPressed() {
        ToggleButton selected = (ToggleButton) mainChoice.getSelectedToggle();
        String resp = selected.getText();
        int index = currentScene.getOptions().indexOf(resp);
        mainLabel2.setText(currentScene.getOutcomes().get(index));
        List<String> effects = new ArrayList<>(currentScene.getEffects().get(index).keySet());
        for (int i = 0; i<effects.size(); i++){
            if (i == 0) {
                effectLabel0.setText(EffectsTranslator.doEffects(GuiController.getPlayer(), effects.get(i), (Integer) currentScene.getEffects().get(index).get(effects.get(i))));
            } else if (i == 1){
                effectLabel1.setText(EffectsTranslator.doEffects(GuiController.getPlayer(), effects.get(i), (Integer) currentScene.getEffects().get(index).get(effects.get(i))));
            } else {
                effectLabel2.setText(EffectsTranslator.doEffects(GuiController.getPlayer(), effects.get(i), (Integer) currentScene.getEffects().get(index).get(effects.get(i))));
            }
        }
        nextButton.setDisable(false);
        selected.setSelected(false);
        mainButton1.setDisable(true);
        mainButton2.setDisable(true);
        mainButton3.setDisable(true);
    }

    @FXML
    private void mainNextPressed(ActionEvent event) {
        GuiController.getPlayer().addAge(5);
        GuiController.getPlayer().addSalary();
        currentScene = GuiController.getScenes().getNewScene(GuiController.getPlayer());
        mainButton3.setVisible(true);
        fiveYearLabel.setVisible(true);
        mainLabel1.setText(currentScene.getPrompt());
        mainButton1.setText(currentScene.getOptions().get(0));
        mainButton2.setText(currentScene.getOptions().get(1));
        statAge.setText(GuiController.getPlayer().getAgeString());
        statChildren.setText(GuiController.getPlayer().getChildrenString());
        statHealth.setText(GuiController.getPlayer().getHealthString());
        statName.setText(GuiController.getPlayer().getName());
        statPartner.setText(GuiController.getPlayer().getPartner() == null ? "none" : GuiController.getPlayer().getPartner().getName());
        statWorth.setText((GuiController.getPlayer().getPrettyNetWorth()));
        mainButton1.setDisable(false);
        mainButton2.setDisable(false);
        mainButton3.setDisable(false);
        submitButton.setDisable(true);
        nextButton.setDisable(true);
        mainLabel2.setText("");
        effectLabel0.setText("");
        effectLabel1.setText("");
        effectLabel2.setText("");
        if (GuiController.getPlayer().getNetWorth() >= 1000000 && GuiController.getPlayer().getHealth() > 0){
            GuiController.loadScene(event, "youwin");
        } else if (GuiController.getPlayer().getHealth() == 0) {
            GuiController.loadScene(event, "youlose");
        } else if ("midlifeCrisis".equals(currentScene.getCategory())){
            catLabel.setText("MID-LIFE CRISIS");
            mainButton3.setVisible(false);
            GuiController.getPlayer().setMidLifeCrisis(true);
        } else {
            catLabel.setText(currentScene.getCategory().toUpperCase());
            mainButton3.setText(currentScene.getOptions().get(2));
        }

    }

    @FXML
    private void mainButtonPressed() {
        submitButton.setDisable(false);
    }

    @FXML
    private void quitPressed() {
        GuiController.getScenes().saveUsers(GuiController.getPlayer());
        System.exit(1);
    }

    @FXML
    private void resetPressed(ActionEvent event) {
        GuiController.loadScene(event, "newGame");
    }

}


