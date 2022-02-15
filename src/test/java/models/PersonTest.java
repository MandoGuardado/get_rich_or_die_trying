package models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PersonTest {
    private Person player;

    @BeforeEach
    void setUp() {
        player = new Person();
    }

    @Test
    void testGetPrettyNetWorthWithValueOfFiveThousand_shouldReturnCurrencyFormattedStringWithDollarSignAndCommaSeparators() {
        player.setNetWorth(5000);
        assertEquals("$5,000",player.getPrettyNetWorth());
    }

    @Test
    void testGetPrettyNetWorkWithValueOfOneMillion_shouldReturnCurrencyFormattedStringWithDollarSignAndCommaSeparator() {
        player.setNetWorth(1000000);
        assertEquals("$1,000,000", player.getPrettyNetWorth());
    }

    @Test
    void testGetHealthPointsGetter_shouldReturnDefaultValueOfOneHundred () {
        assertEquals(100, player.getHealthPoints());
    }

    @Test
    void testHealthPointValueAfterRemovingTenByEnteringNegativeValueFromDefaultOneHundred_shouldReturnValueOfHealthPointAtNinety() {
        player.adjustHealth(-10);
        assertEquals(player.getHealthPoints(), 90);
    }
    @Test
    void testHealthPointValueAfterSubtractingMoreThanOneHundredFromAValueOfOneHundred_shouldReturnValueOfZero() {
        player.adjustHealth(-165);
        assertEquals(player.getHealthPoints(), 0);
    }
    @Test
    void testHealthPointValueThatItDoesNotReturnValueHigherThanOneHundredWhenAddingMoreThanOneHundred_shouldReturnValueOfOneHundred() {
        player.adjustHealth(165);
        assertEquals(player.getHealthPoints(), 100);
    }
    @Test
    void testHealthPointValueAfterRemovingSixtyFiveAndAddingTenByEnteringNegativeValueFromDefaultOneHundred_healthPointsValueShouldBeNinety() {
        player.adjustHealth(-65);
        player.adjustHealth(10);
        assertEquals(player.getHealthPoints(), 45);
    }

    @Test
    void testAdjustHealthMethodWhenAddingPositiveValue_shouldReturnStringIndictingTheirGain() {
        assertEquals("You have gained 5 health points.", player.adjustHealth(5));
    }
    @Test
    void testAdjustHealthMethodWhenAddingNegativeValue_shouldReturnStringIndictingTheirGain() {
        assertEquals("You have lost 10 health points.", player.adjustHealth(-10));
    }

    @Test
    void testBreakUpMethod_shouldReturnStringMessageWithNameOfPartner() {
        assertEquals("You and Sam have broken up.", player.breakUp());
    }

    @Test
    void testMarryPartnerMethod_ShouldReturnStringMessageIndicatingMarriageAlongWithNameOfPerson() {
        player.addPartner(25000);
        assertEquals("You have married your partner, Sam", player.marryPartner());
    }

    @Test
    void testMarryingPartnerWhenYouDoNotAHavePartner_shouldReturnStringThatStatesYouNeedToHavePartnerBeforeMarriage() {
        assertEquals("You need a partner before you can get married.", player.marryPartner());
    }

    @Test
    void testAddingAChildToPlayer_shouldReturnMessageIndicatingSingularAddition() {
        assertEquals("You have gained 1 child.", player.addChild(1));
    }

    @Test
    void testAddingChildrenToPlayer_shouldReturnMessageIndicatingPluralAddition(){
        assertEquals("You have gained 5 children.", player.addChild(5));
    }

    @Test
    void testAddingAgeValueBeforeTurningFiftyYearsOld_shouldReturnStringMessageStatingAge() {
        assertEquals("You are now 28 years old.", player.addAge(10));
    }

    @Test
    void testAddingAgeValueAfterTurningFiftyYearsOld_shouldReturnAlternativeStringMessage() {
        assertEquals("You are getting older and losing health.\nYou are now 68 years old.", player.addAge(50));
    }

    @Test
    void testAddingDivorceMethod_shouldReturnStringMessageIndicatingDivorceIsCompleted() {
        assertEquals("The divorce to your significant other is now complete, assets have been distributed accordingly.", player.addDivorce());
    }

    @Test
    void testPlayerCurrentInfoStatus_shouldReturnNameAndInitialParameterValuesPlusDefaultValues() {
        Person player = new Person("mando", 1500);
        assertEquals("******************************************************************************************\n" +
                "\tPlayer name: mando\t NetWorth: $1,500.00\t Current Age: 18\t Health Status: 100 \n" +
                "******************************************************************************************\n" ,player.getPlayerInformation() );
    }

    @Test
    void testPlayerCurrentInfoStatus_shouldReturnNameAndInitialParameterValuesPlusAdjustedAge() {
        Person player = new Person("mando", 1500);
        player.addAge(5);
        assertEquals("******************************************************************************************\n" +
                "\tPlayer name: mando\t NetWorth: $1,500.00\t Current Age: 23\t Health Status: 100 \n" +
                "******************************************************************************************\n" ,player.getPlayerInformation());
    }

    @Test
    void testPlayerCurrentInfoStatus_shouldReturnNameAndInitialParameterValuesPlusHealthAdjustment() {
        Person player = new Person("mando", 1500);
        player.adjustHealth(-10);
        assertEquals("******************************************************************************************\n" +
                "\tPlayer name: mando\t NetWorth: $1,500.00\t Current Age: 18\t Health Status: 90 \n" +
                "******************************************************************************************\n" ,player.getPlayerInformation());
    }

    @Test
    void testPlayerCurrentInfoStatus_shouldReturnNameAndInitialParameterValuesPlusHealthAndAgeAdjustment() {
        Person player = new Person("mando", 1500);
        player.addAge(7);
        player.adjustHealth(-10);
        assertEquals("******************************************************************************************\n" +
                "\tPlayer name: mando\t NetWorth: $1,500.00\t Current Age: 25\t Health Status: 90 \n" +
                "******************************************************************************************\n" ,player.getPlayerInformation());
    }
}