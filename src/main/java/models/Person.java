package models;

import java.text.NumberFormat;
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
    //Constructors
    public Person() {
        money.setMaximumFractionDigits(0);
    }

    public Person(String name, int initialWorth) {
        this.name = name;
        this.netWorth = initialWorth;
    }

    public Person(int netWorth, int health, int age, int children, int strength, int intellect, int creativity, Boolean education, Boolean isMarried, Boolean hasPrivilege, Careers career, Person partner, String name) {
        this.netWorth = netWorth;
        this.health = health;
        this.age = age;
        this.children = children;
        this.strength = strength;
        this.intellect = intellect;
        this.creativity = creativity;
        this.education = education;
        this.isMarried = isMarried;
        this.hasPrivilege = hasPrivilege;
        this.career = career;
        this.partner = partner;
        this.name = name;
    }

    public Person(int netWorth, int health, int age, int children, int strength, int intellect, int creativity, Boolean education, Boolean isMarried, Boolean hasPrivilege, Careers career, String name) {
        this.netWorth = netWorth;
        this.health = health;
        this.age = age;
        this.children = children;
        this.strength = strength;
        this.intellect = intellect;
        this.creativity = creativity;
        this.education = education;
        this.isMarried = isMarried;
        this.hasPrivilege = hasPrivilege;
        this.career = career;
        this.name = name;
    }

    //Business Methods

    /**
     * Method that returns a Formatted banner with Name, net-worth, age, and health.
     * @return Formatted String
     */
    public String getPlayerInformation(){
        return "******************************************************************************************\n" +
                "\t" + "Player name: " + getName()   + "\t NetWorth: " + getPrettyNetWorth() + "\t Current Age: " + getAge() + "\t Health Status: " + getHealth() + " \n" +
                "******************************************************************************************\n" ;
    }

    /**
     * Returns a String value of the Persons netWorth variable in a format that include dollar sign ($)
     * using .format().
     * @return Formatted String
     */
    public String getPrettyNetWorth() {
        return money.format(netWorth);
    }

    /**
     * Returns a String value of the field values based on index passed from List.
     * created internally:
     * [career(String), education (String), partner status(String), privilege (boolean), health > 50 (boolean-String), children > 0 (boolean-String)]
     * @param index value of desired index.
     * @return String value of one of above values in List.
     */
    public String getCategoryValue(int index) {

        if (index < 0)
            index = 0;

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
     * @param adjustmentAmount Amount to be adjusted(negative and positive value are accepted) to netWorth variable.
     * @return   Prints a formatted String with modified value after applying modifier.
     */
    public String adjustNetWorth(int adjustmentAmount) {
        double randModifier = new Random().nextDouble() * (1.25d - .75d) + .75d;
        int modifiedAmountToAdd = (int) (adjustmentAmount * randModifier);
        netWorth += modifiedAmountToAdd;
        return String.format("You have %s %s from your choice", (adjustmentAmount < 0 ? "lost" : "gained"), money.format(modifiedAmountToAdd));
    }

    /**
     * Adjusts value to health variable, value is adjusted based on parameter value, positive and negative value are
     * accepted, If parameter value make value over 100, then health value is adjusted to max value of 100.
     * If, parameter value make value under 0, then health value is adjusted to min value of 0.
     * @param value value being added to health variable.
     * @return String Message if Player lost(negative value) or gained(positive value) and value passed in as parameter.
     */
    public String adjustHealth(int value) {
        health += value;
        if (health > 100){
            health = 100;
        }
        if (health < 0){
            health = 0;
        }

        String gained = value < 0 ? "lost" : "gained";
        return String.format("You have %s %d health points.", gained, Math.abs(value));
    }

    /**
     * Creates a new Person object that will represent the Partner, He is automatically named 'Sam'.
     * @param value int value that represents the Partners net worth.
     * @return Formatted String message with new partners name.
     */
    public String addPartner(int value) {
        String stringMsg ="";
        if (partner == null) {
            partner = new Person("Sam", value);
            stringMsg = String.format("You have a new partner named %s", partner.name);
        }
        return stringMsg;
    }

    /**
     * Setts Person object variable to specific values to indicate marriage is over.
     * partner (Person object) variable to null.
     * isMarried variable set to false.
     * @return String message indicating 'break up' including name of partner.
     */
    public String breakUp() {
        this.partner = null;
        this.isMarried = false;
        return "You and Sam have broken up.";
    }

    /**
     * Sets Person object variables to specific value to indicate marriage.
     * setMarried variable set to 'true'.
     * partner (Person) object's isMarried variable set to 'true'.
     * @return Formatted String indicating marriage
     */
    public String marryPartner() {
        String msg="You need a partner before you can get married.";
        if (partner != null) {
            this.setMarried(true);
            partner.setMarried(true);
            msg = "You have married your partner, Sam";
        }
        return msg;
    }

    /**
     * Increments children variable numOfChildren by parameter numOfChildren.
     * @param numOfChildren Number of children being incremented to current children variable numOfChildren.
     * @return String message indicating the number of children added.
     */
    public String addChild(int numOfChildren) {
        children += numOfChildren;
        String some = numOfChildren > 1 ? "children" : "child";
        return String.format("You have gained %d %s.", numOfChildren, some);
    }

    /**
     * Adjusts Person (Person Object) career variable to another value based on parameter value.
     * Used Careers ENUM class.
     * Prints message indicating old value and new value.
     * @param value new career value
     * @return String message indicating the career change.
     */
    public String changeCareer(int value) {
        String oldCareer = career.name();
        career = Careers.values()[value];
        return String.format("Your career has changed from %s to %s", oldCareer, career.toString());
    }

    /**
     * Adds parameter(i) value to age variable
     * If age value after addition is more than 50, then random amount is deducted from health variable.
     * @param i value of age increase
     */
    public String addAge(int i) {
        age += i;
        StringBuilder msg = new StringBuilder();
        if (age > 50) {
            Random rand = new Random();
            int amountHealthToDecrease = -(rand.nextInt(15) + 1);
            msg.append("You are getting older and losing health.\n");
            adjustHealth(amountHealthToDecrease);
        }

        String msg2 = String.format("You are now %d years old.", age);
        msg.append(msg2);
        return msg.toString();
    }

    /**
     *Adds to netWorth: Five times Person object salary
     * times(*) education multiplier (1.5 or 1)
     * times (*) income multiplier using incomeMultiplier() return value.
     * @return Sting representation 5 year summary after adding a sum to total netWorth.
     */
    public String addSalary() {
        int amountToAdd = career.getSalaryAmount() * 5;
        double educationMultiplier = hasEducation() ? 1.5 : 1;
        double incomeMultiplier = getIncomeMultiplier();
        int sum = (int) (amountToAdd * educationMultiplier * incomeMultiplier);
        int oldNetWorth = netWorth;
        netWorth = sum + netWorth;

        String netWorthSummary = "Your current net worth: " + money.format(netWorth);
        System.out.println(netWorthSummary);

        return "\nYou have earned " + money.format(sum) + " in the last 5 years from your job.\n" +
                "\nNet worth breakdown: " +
                "\nBase yearly salary: " + money.format(career.getSalaryAmount()) +
                "\nYearly salary * 5 years: " + money.format(amountToAdd) +
                "\nEducation Multiplier: " + educationMultiplier +
                "\nIncome Multiplier from " + getAttributeFromCareer() + ": " + incomeMultiplier +
                "\nTotal: (Yearly Salary * 5 years * education multiplier * income multiplier): " + money.format(sum) + " + Previous net worth: " + money.format(oldNetWorth) + "=" + money.format(netWorth);
    }

    /**
     * Returns attribute value based on career (Career ENUM) variable value
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
     * @return Value of income multiplier
     */
    private double getIncomeMultiplier() {
        //1 - 1.5

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
     * @param i value of increase to creativity variable
     */
    public void addCreativity(int i) {
        this.creativity += i;
    }

    /**
     * Increments intellect variable by parameter (i) passed in
     * @param i value of increase to intellect variable
     */
    public void addIntellect(int i) {
        this.intellect += i;
    }

    /**
     * Increments strength variable by parameter (i) passed in
     * @param i value of increase to strength variable
     */
    public void addStrength(int i) {
        this.strength += i;
    }

    /**
     * Sets partner field (Person object) to null.
     */
    public void removePartner() {
        partner = null;
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

    public int getHealth() {
        return health;
    }

    public Boolean getEducation() {
        return education;
    }

    public Boolean getMarried() {
        return isMarried;
    }

    public Boolean getHasPrivilege() {
        return hasPrivilege;
    }

    public int getNetWorth() {
        return netWorth;
    }

    public void setNetWorth(int netWorth) {
        this.netWorth = netWorth;
    }

    public int getHealthPoints() {
        return this.health;
    }

    public int getAge() {
        return this.age;
    }

    public int getChildren() {
        return this.children;
    }

    public int getStrength() {
        return this.strength;
    }

    public int getIntellect() {
        return this.intellect;
    }

    public int getCreativity() {
        return this.creativity;
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
                '}';
    }
}
