package models;


import org.json.JSONObject;
import java.text.NumberFormat;
import java.time.temporal.ValueRange;
import java.util.List;
import java.util.Random;

public class Person {
    //Fields
    private int netWorth = 0, health = 100, age = 18, children = 0;
    private final NumberFormat money = NumberFormat.getCurrencyInstance();
    private int strength = 0, intellect = 0, creativity = 0;
    private Boolean education = false, isMarried = false, hasPrivilege = false;
    private Careers career = Careers.PASSION;
    private Person partner = null;
    private String name;
    private boolean midLifeCrisis;
    private boolean finishedInitialization;
    private boolean isCurrentlyInvesting;
    private JSONObject currentInvestment;
    private int investmentAmount;

    //Constructors
    public Person() {
        money.setMaximumFractionDigits(0);
    }

    public Person(String name, int initialWorth) {
        this();
        setName(name);
        setNetWorth(initialWorth);
    }

    public Person(int netWorth, int health, int age, int children, int strength, int intellect, int creativity, Boolean education, Boolean isMarried, Boolean hasPrivilege, Careers career, Person partner, String name, JSONObject currentInvestment, boolean midLifeCrisis, boolean finishedInitialization, boolean isCurrentlyInvesting, int investmentAmount) {
        this(name, netWorth);
        setHealth(health);
        setAge(age);
        setChildren(children);
        setStrength(strength);
        setIntellect(intellect);
        setCreativity(creativity);
        setEducation(education);
        setMarried(isMarried);
        setPrivilege(hasPrivilege);
        setCareer(career);
        setPartner(partner);
        setCurrentInvestment(currentInvestment);
        setMidLifeCrisis(midLifeCrisis);
        setFinishedInitialization(finishedInitialization);
        setCurrentlyInvesting(isCurrentlyInvesting);
        setInvestmentAmount(investmentAmount);
    }

    //Business Methods

    /**
     * Method to reset Person instance fields default values.
     */
    public void startOver() {
        setNetWorth(0);
        setHealth(100);
        setMidLifeCrisis(false);
        setEducation(false);
        setAge(18);
        setIntellect(0);
        setStrength(0);
        setCreativity(0);
        setCurrentlyInvesting(false);
        setCurrentInvestment(null);
        setInvestmentAmount(0);
    }

    /**
     * Method that returns a Formatted banner with Name, net-worth, age, and health.
     *
     * @return Formatted String
     */
    public String getPlayerInformation() {
        return "**********************************************************************************************************\n\n" +
                "\t" + "Player name: " + getName() + "\t NetWorth: " + getPrettyNetWorth() + "\t Current Age: " + getAge() + "\t Health Status: " + getHealth() + " \n\n" +
                "**********************************************************************************************************\n";
    }

    /**
     * Returns a String value of the Persons netWorth variable in a format that include dollar sign ($)
     * using .format().
     *
     * @return Formatted String
     */
    public String getPrettyNetWorth() {
        return money.format(netWorth);
    }

    /**
     * Returns a String value of the field values based on index passed from List.
     * created internally:
     * [career(String), education (String), partner status(String), privilege (boolean), health > 50 (boolean-String), children > 0 (boolean-String)]
     *
     * @param index value of desired index.
     * @return String value of one of above values in List.
     */
    public String getCategoryValue(int index) {
        index = Math.max(index, 0);
        List<String> fieldValues = List.of(
                career.toString().toLowerCase(),
                education.toString(),
                getPartnerStatus(),
                hasPrivilege.toString(),
                Boolean.toString(health > 50),
                Boolean.toString(children > 0)
        );
        return fieldValues.get(index);
    }

    /**
     * Returns String value of Partner Status.
     * Options: 'single', 'married', 'partner'.
     *
     * @return One String value of above options.
     */
    private String getPartnerStatus() {
        String partnerStatus = "single";

        if (this.partner != null)
            partnerStatus = isMarried ? "married" : "partner";

        return partnerStatus;
    }

    /**
     * Adds adjustmentAmount value into netWorth variable after being multiplied by a random modifier value.
     *
     * @param adjustmentAmount Amount to be adjusted(negative and positive value are accepted) to netWorth variable.
     * @return Prints a formatted String with modified value after applying modifier.
     */
    public String adjustNetWorth(int adjustmentAmount) {
        double randModifier = new Random().nextDouble() * (1.25d - .75d) + .75d;
        int modifiedAmountToAdd = (int) (adjustmentAmount * randModifier);
        int newNetWorth = getNetWorth() + modifiedAmountToAdd;
        setNetWorth(newNetWorth);
        return String.format("You have %s %s from your choice", (adjustmentAmount < 0 ? "lost" : "gained"), money.format(modifiedAmountToAdd));
    }

    /**
     * Method that makes adjustment to netWorth filed. The adjustment is without any type of modifier.
     *
     * @param adjustmentAmount Amount being adjusted to net worth.
     * @param lostInvestment   True : if adjustment is a loss, false: if adjustment is a gain.
     * @return Formatted string describing the result as a gain or loss.
     */
    public String adjustNetWorth(int adjustmentAmount, boolean lostInvestment) {
        int newNetWorth = getNetWorth() + adjustmentAmount;
        setNetWorth(newNetWorth);
        return String.format("You have %s %s from your investment", (lostInvestment ? "lost" : "gained"), money.format(adjustmentAmount));
    }

    /**
     * Adjusts value to health variable, value is adjusted based on parameter value, positive and negative value are
     * accepted, If parameter value make value over 100, then health value is adjusted to max value of 100.
     * If, parameter value make value under 0, then health value is adjusted to min value of 0.
     *
     * @param value value being added to health variable.
     * @return String Message if Player lost(negative value) or gained(positive value) and value passed in as parameter.
     */
    public String adjustHealth(int value) {
        setHealth(getHealth() + value);
        if (getHealth() > 100) {
            setHealth(Math.min(getHealth(), 100));
        }
        if (getHealth() < 0) {
            setHealth(Math.max(getHealth(), 0));
        }
        return String.format("You have %s %d health points.", value < 0 ? "lost" : "gained", Math.abs(value));
    }

    /**
     * Creates a new Person object that will represent the Partner, He is automatically named 'Sam'.
     *
     * @param value int value that represents the Partners net worth.
     * @return Formatted String message with new partners name.
     */
    public String addPartner(int value) {
        String stringMsg = "";
        if (getPartner() == null) {
            setPartner(new Person("Sam", value));
            stringMsg = String.format("You have a new partner named %s", getPartner().getName());
        }
        return stringMsg;
    }

    /**
     * Setts Person object variable to specific values to indicate marriage is over.
     * partner (Person object) variable to null.
     * isMarried variable set to false.
     *
     * @return String message indicating 'break up' including name of partner.
     */
    public String breakUp() {
        setPartner(null);
        setMarried(false);
        return "You and Sam have broken up.";
    }

    /**
     * Sets Person object variables to specific value to indicate marriage.
     * setMarried variable set to 'true'.
     * partner (Person) object's isMarried variable set to 'true'.
     *
     * @return Formatted String indicating marriage
     */
    public String marryPartner() {
        String msg = "You need a partner before you can get married.";
        if (getPartner() != null) {
            setMarried(true);
            getPartner().setMarried(true);
            msg = "You have married your partner, " + getPartner().getName();
        }
        return msg;
    }

    /**
     * Increments children variable numOfChildren by parameter numOfChildren.
     *
     * @param numOfChildren Number of children being incremented to current children variable numOfChildren.
     * @return String message indicating the number of children added.
     */
    public String addChild(int numOfChildren) {
        setChildren(getChildren() + numOfChildren);
        return String.format("You have gained %d %s.", numOfChildren, numOfChildren > 1 ? "children" : "child");
    }

    /**
     * Adjusts Person (Person Object) career variable to another value based on parameter value.
     * Used Careers ENUM class.
     * Prints message indicating old value and new value.
     *
     * @param value new career value
     * @return String message indicating the career change.
     */
    public String changeCareer(int value) {
        String msg = "Valid career not found, career has not changed";
        ValueRange valueRange = ValueRange.of(0, 2);
        if (valueRange.isValidValue(value)) {
            String oldCareer = getCareer().name();
            setCareer(Careers.values()[value]);
            msg = String.format("Your career has changed from %s to %s", oldCareer, getCareer().toString());
        }
        return msg;

    }

    /**
     * Adds parameter(i) value to age variable
     * If age value after addition is more than 50, then random amount is deducted from health variable.
     *
     * @param i value of age increase
     */
    public String addAge(int i) {
        setAge(getAge() + i);
        StringBuilder msg = new StringBuilder();
        if (age > 50) {
            Random rand = new Random();
            int amountHealthToDecrease = Math.negateExact(rand.nextInt(15) + 1);
            msg.append("You are getting older and losing health.\n");
            adjustHealth(amountHealthToDecrease);
        }

        String msg2 = String.format("You are now %d years old.", age);
        msg.append(msg2);
        return msg.toString();
    }

    /**
     * Adds to netWorth: Five times Person object salary
     * times(*) education multiplier (1.5 or 1)
     * times (*) income multiplier using incomeMultiplier() return value.
     *
     * @return Sting representation 5 year summary after adding a sum to total netWorth.
     */
    public String addSalary() {
        int amountToAdd = getCareer().getSalaryAmount() * 5;
        double educationMultiplier = hasEducation() ? 1.5 : 1;
        double incomeMultiplier = getIncomeMultiplier();
        int sum = (int) (amountToAdd * educationMultiplier * incomeMultiplier);
        int oldNetWorth = getNetWorth();
        setNetWorth(sum + getNetWorth());

        String netWorthSummary = "Your current net worth: " + getPrettyNetWorth();
        System.out.println(netWorthSummary);

        return "\nYou have earned " + money.format(sum) + " in the last 5 years from your job.\n" +
                "\nNet worth breakdown: " +
                "\nBase yearly salary: " + money.format(getCareer().getSalaryAmount()) +
                "\nYearly salary * 5 years: " + money.format(amountToAdd) +
                "\nEducation Multiplier: " + educationMultiplier +
                "\nIncome Multiplier from " + getAttributeFromCareer() + ": " + incomeMultiplier +
                "\nTotal: (Yearly Salary * 5 years * education multiplier * income multiplier): " + money.format(sum) + " + Previous net worth: " + money.format(oldNetWorth) + "=" + money.format(netWorth);
    }

    /**
     * Returns attribute value based on career (Career ENUM) variable value
     *
     * @return String attribute value.
     */
    private String getAttributeFromCareer() {
        switch (career) {
            case DANGER:
                return "strength";
            case KNOWLEDGE:
                return "intellect";
            case PASSION:
                return "creativity";
            default:
                return "none";
        }
    }

    /**
     * Returns double value based on career(type: Career Enum) variable type and attribute value
     *
     * @return Value of income multiplier
     */
    private double getIncomeMultiplier() {
        switch (career) {
            case DANGER:
                return (10.0 + strength) / 10;
            case KNOWLEDGE:
                return (10.0 + intellect) / 10;
            case PASSION:
                return (10.0 + creativity) / 10;
            default:
                return 1;
        }
    }

    /**
     * Increments creativity variable by parameter (i) passed in
     *
     * @param i value of increase to creativity variable
     */
    public void addCreativity(int i) {
        setCreativity(getCreativity() + i);
    }

    /**
     * Increments intellect variable by parameter (i) passed in
     *
     * @param i value of increase to intellect variable
     */
    public void addIntellect(int i) {
        setIntellect(getIntellect() + i);
    }

    /**
     * Increments strength variable by parameter (i) passed in
     *
     * @param i value of increase to strength variable
     */
    public void addStrength(int i) {
        setStrength(getStrength() + i);
    }

    /**
     * Sets partner field (Person object) to null.
     */
    public void removePartner() {
        setPartner(null);
    }

    /**
     * Calls on removePartner() and sets married field to 'false'.
     */
    public String addDivorce() {
        removePartner();
        setMarried(false);
        return "The divorce to your significant other is now complete, assets have been distributed accordingly.";
    }


    //Setter and Getters
    public int getNetWorth() {
        return netWorth;
    }

    public void setNetWorth(int netWorth) {
        this.netWorth = netWorth;
    }

    public int getHealth() {
        return this.health;
    }

    public String getHealthString() {
        return String.valueOf(health);
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public int getAge() {
        return this.age;
    }

    public String getAgeString() {
        return String.valueOf(this.age);
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getChildren() {
        return this.children;
    }

    public String getChildrenString() {
        return String.valueOf(this.children);
    }

    public void setChildren(int children) {
        this.children = children;
    }

    public int getStrength() {
        return this.strength;
    }

    public void setStrength(int strength) {
        this.strength = strength;
    }

    public int getIntellect() {
        return this.intellect;
    }

    public void setIntellect(int intellect) {
        this.intellect = intellect;
    }

    public int getCreativity() {
        return this.creativity;
    }

    public void setCreativity(int creativity) {
        this.creativity = creativity;
    }

    public boolean hasEducation() {
        return education;
    }

    public void setEducation(boolean b) {
        this.education = b;
    }

    public boolean isMarried() {
        return isMarried;
    }

    private void setMarried(boolean b) {
        this.isMarried = b;
    }

    public Boolean getHasPrivilege() {
        return hasPrivilege;
    }

    public void setPrivilege(boolean b) {
        this.hasPrivilege = b;
    }

    public Careers getCareer() {
        return this.career;
    }

    public void setCareer(Careers career) {
        this.career = career;
    }

    public Person getPartner() {
        return partner;
    }

    public void setPartner(Person partner) {
        this.partner = partner;
    }

    public String getName() {
        return name;
    }

    public void setName(String playerName) {
        this.name = playerName;
    }

    public boolean isMidLifeCrisis() {
        return midLifeCrisis;
    }

    public void setMidLifeCrisis(boolean midLifeCrisis) {
        this.midLifeCrisis = midLifeCrisis;
    }

    public boolean isFinishedInitialization() {
        return finishedInitialization;
    }

    public void setFinishedInitialization(boolean finishedInitialization) {
        this.finishedInitialization = finishedInitialization;
    }

    public boolean isCurrentlyInvesting() {
        return isCurrentlyInvesting;
    }

    public void setCurrentlyInvesting(boolean currentlyInvesting) {
        isCurrentlyInvesting = currentlyInvesting;
    }

    public JSONObject getCurrentInvestment() {
        return currentInvestment;
    }

    public void setCurrentInvestment(JSONObject currentInvestment) {
        this.currentInvestment = currentInvestment;
    }

    public int getInvestmentAmount() {
        return investmentAmount;
    }

    public void setInvestmentAmount(int investmentAmount) {
        this.investmentAmount = investmentAmount;
    }


    @Override
    public String toString() {
        return "Person{" +
                "netWorth=" + netWorth +
                ", health=" + health +
                ", age=" + age +
                ", children=" + children +
                ", money=" + money +
                ", strength=" + strength +
                ", intellect=" + intellect +
                ", creativity=" + creativity +
                ", education=" + education +
                ", isMarried=" + isMarried +
                ", hasPrivilege=" + hasPrivilege +
                ", career=" + career +
                ", partner=" + partner +
                ", name='" + name + '\'' +
                ", midLifeCrisis=" + midLifeCrisis +
                ", finishedInitialization=" + finishedInitialization +
                ", isCurrentlyInvesting=" + isCurrentlyInvesting +
                ", currentInvestment=" + currentInvestment +
                ", investmentAmount=" + investmentAmount +
                '}';
    }
}
