package models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SceneContainerTest {
    private SceneContainer container;

    @BeforeEach
    void setUp() {
        container = new SceneContainer();
    }

    @Test
    void testSceneContainerLoadUsersWhenExternalDoesNotContainAnyPreviousUsers_ShouldHaveAnEmptyMap() {
        SceneContainer otherContainer = new SceneContainer();
        otherContainer.loadUsers("testingEmpty.json");
        assertTrue(otherContainer.getUsers().isEmpty());
    }

    @Test
    void testSceneContainerLoadUserWhenExistingFileContainsTwoPreviousUsers_shouldReturnTwoAsSizeOfMap(){
        container.loadUsers("testing.json");
        assertEquals(2, container.getUsers().size());

    }

    @Test
    void testSceneContainerUserMapSizeAfterLoadingExternalFileWithExistingFilesAndAddingAnotherUser_shouldReturnSizeOfThree() {
        container.loadUsers("testing.json");

        container.saveUsers(new Person("Armando", 1500));
        assertEquals(3,container.getUsers().size());
    }

    @Test
    void testGetMidLifeCrisisSceneWhenPlayerMeetsCriteria_shouldReturnFalseScenePrompt() {
        assertEquals("You're feeling a bit down and begin to assess all aspects of your life. From achievements, career, family, and character. You may be at the beginning of a major midlife crisis. Do you seek help or try to handle it yourself?", container.getMidLifeCrisisScene("false").getPrompt());
    }

    @Test
    void testGetMidLifeCrisisWhenPlayerMeetsCriteria_shouldReturnFalseSceneOptions() {
        String [] options = {"seek help", "handle it yourself"};
        ArrayList<String> midlifeCrisisOptions = new ArrayList<>(List.of(options));
        assertEquals(midlifeCrisisOptions, container.getMidLifeCrisisScene("false").getOptions());
    }

    @Test
    void testGetMidLifeCrisisWhenPlayerMeetsCriteria_shouldReturnFalseSceneOutcomes() {
        String [] options = {"The decision to seek profession help and to take medical leave from your job was for the best. Not only did you drastically improved you mental health but your overall health improved and your employer was very supportive." ,
                "Unfortunately you mental health is not improving. You continue to try to handle it yourself but everything you do doesn't seem to work. You are hanging by a thread at work and feel like you will be fired soon. You go like this for two years."};
        ArrayList<String> midlifeCrisisOptions = new ArrayList<>(List.of(options));
        assertEquals(midlifeCrisisOptions, container.getMidLifeCrisisScene("false").getOutcomes());
    }

    @Test
    void testGetMidLifeCrisisSceneWhenPlayerMeetsCriteria_shouldReturnTrueScenePrompt() {
        String prompt = "The last time you encountered a midlife crisis the decision you made early on determined the duration of the crisis episode.\n Your are recognizing the tale tail signs of another episode and must now determine if you want to be proactive or reactive?";
        assertEquals(prompt, container.getMidLifeCrisisScene("true").getPrompt());

    }

    @Test
    void testGetMidLifeCrisisWhenPlayerMeetsCriteria_shouldReturnTrueSceneOptions() {
        String [] options = {"proactive", "reactive"};
        ArrayList<String> midlifeCrisisOptions = new ArrayList<>(List.of(options));
        assertEquals(midlifeCrisisOptions, container.getMidLifeCrisisScene("true").getOptions());
    }

    @Test
    void testGetMidLifeCrisisWhenPlayerMeetsCriteria_shouldReturnTrueSceneOutcomes() {
        String [] options = {"That's awesome! Being proactive not only saves you money but your health improved." ,
                "While you decided not to be proactive the latest crisis proved not to be so bad. You were able to handle it but it was a bit costly because of the days off you had to take and your overall health also took a hit."};
        ArrayList<String> midlifeCrisisOptions = new ArrayList<>(List.of(options));
        assertEquals(midlifeCrisisOptions, container.getMidLifeCrisisScene("true").getOutcomes());
    }

}