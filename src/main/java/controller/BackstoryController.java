package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import models.Backstory;
import models.BackstoryOption;
import models.Person;
import view.GuiApp;

import java.util.List;

import static controller.Game.getBackStoryScenes;

public class BackstoryController {
    private Person player;
    private final List<Backstory> backstories = getBackStoryScenes();

    public BackstoryController() {

    }

    @FXML
    private Label backstoryLabel1;

    @FXML
    private Label backstoryLabel2;


    @FXML
    private ToggleButton backstoryButton1;

    @FXML
    private ToggleButton backstoryButton2;

    @FXML
    private ToggleButton backstoryButton3;

    @FXML
    private ToggleGroup backstoryChoice;

    @FXML
    private Button backstorySubmit;

    @FXML
    private Button backstoryNext;

    @FXML
    void backstoryNextPressed(ActionEvent event) {
        if (backstoryRound < 4){
            backstoryLabel1.setText(backstories.get(backstoryRound).getPrompt());
            backstoryButton1.setText(backstories.get(backstoryRound).getOptions().get(0).getText());
            backstoryButton2.setText(backstories.get(backstoryRound).getOptions().get(1).getText());
            backstoryButton3.setText(backstories.get(backstoryRound).getOptions().get(2).getText());
            backstoryLabel2.setText("");
            backstoryButton1.setDisable(false);
            backstoryButton2.setDisable(false);
            backstoryButton3.setDisable(false);
            backstorySubmit.setDisable(true);
            backstoryNext.setDisable(true);
        } else {
            GuiController.loadScene(event,"career");
        }
    }

    @FXML
    void buttonPressed(ActionEvent event) {
        backstorySubmit.setDisable(false);
    }

    int backstoryRound = 0;

    @FXML
    private void BSSubmitPressed(ActionEvent event) {
        ToggleButton selected = (ToggleButton) backstoryChoice.getSelectedToggle();
        String resp = selected.getText();
        BackstoryOption optionSelected = null;
            for (BackstoryOption option : backstories.get(backstoryRound).getOptions()) {
                if (option.getText().contains(resp)) {
                    optionSelected = option;
                    break;
                }
            }
        backstoryLabel2.setText(optionSelected.getOutcome());
        EffectsTranslator.getAttribute(Game.getPlayer(), optionSelected.getAttribute());
        backstoryRound++;
        backstoryButton1.setDisable(true);
        backstoryButton2.setDisable(true);
        backstoryButton3.setDisable(true);
        backstoryNext.setDisable(false);

    }


}