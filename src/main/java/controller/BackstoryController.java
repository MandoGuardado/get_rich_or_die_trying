package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import models.Backstory;
import models.BackstoryOption;
import models.Careers;
import java.util.List;
import java.util.Map;

import static controller.Game.getBackStoryScenes;


public class BackstoryController {
    private final List<Backstory> backstories = getBackStoryScenes();

    public BackstoryController() {

    }
    @FXML
    private BorderPane careerPaneCollege;

    @FXML
    private BorderPane careerPaneNon;

    @FXML
    private BorderPane collegePane;

    @FXML
    private ToggleGroup collegeCareer;

    @FXML
    private ToggleGroup collegeSelected;

    @FXML
    private ToggleGroup nonCollegeCareer;

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
        EffectsTranslator.getAttribute(GuiController.getPlayer(), optionSelected.getAttribute());
        backstoryRound++;
        backstoryButton1.setDisable(true);
        backstoryButton2.setDisable(true);
        backstoryButton3.setDisable(true);
        backstoryNext.setDisable(false);
        selected.setSelected(false);

    }

    @FXML
    void CCNextPressed(ActionEvent event) {
        RadioButton selected = (RadioButton) collegeCareer.getSelectedToggle();
        String resp = selected.getText();
        Map<Careers, List<String>> availCareers = Careers.getCollegeCareers();
        topLoop:
        for (Careers career : availCareers.keySet()) {
            for (String specialty : availCareers.get(career)) {
                if (resp.equalsIgnoreCase(specialty)) {
                    GuiController.getPlayer().setCareer(career);
                    break topLoop;
                }
            }
        }
        GuiController.loadScene(event, "mainstory");
    }

    @FXML
    void NCNextPressed(ActionEvent event) {
        RadioButton selected = (RadioButton) nonCollegeCareer.getSelectedToggle();
        String resp = selected.getText();
        Map<Careers, List<String>> availCareers = Careers.getNonCollegeCareers();
        topLoop:
        for (Careers career : availCareers.keySet()) {
            for (String specialty : availCareers.get(career)) {
                if (resp.equalsIgnoreCase(specialty)) {
                    GuiController.getPlayer().setCareer(career);
                    break topLoop;
                }
            }
        }

        GuiController.loadScene(event, "mainstory");
    }

    @FXML
    void collegeNextPressed(ActionEvent event) {
        RadioButton selected = (RadioButton) collegeSelected.getSelectedToggle();
        String resp = selected.getText();
        collegePane.setVisible(false);
        if ("Yes".equals(resp)){
            GuiController.getPlayer().setEducation(true);
            GuiController.getPlayer().adjustNetWorth(-100000);
            careerPaneCollege.setVisible(true);
        } else {
            careerPaneNon.setVisible(true);
            GuiController.getPlayer().setEducation(false);
        }

    }

    @FXML
    void quitPressed(ActionEvent event) {
        System.exit(1);
    }


}