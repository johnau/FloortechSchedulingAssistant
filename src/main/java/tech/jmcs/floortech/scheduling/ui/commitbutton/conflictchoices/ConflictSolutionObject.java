package tech.jmcs.floortech.scheduling.ui.commitbutton.conflictchoices;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class ConflictSolutionObject {

    private StringProperty itemName = new SimpleStringProperty("", "itemName");
    private StringProperty existingValue = new SimpleStringProperty("", "existingValue");
    private StringProperty solution = new SimpleStringProperty("", "solution"); // add to, replace, (subtract from, multiply)

    public ConflictSolutionObject(String itemName, String existingValue, String solution) {
        this.setItemName(itemName);
        this.setExistingValue(existingValue);
        this.setSolution(solution);
    }

    public String getItemName() {
        return itemName.get();
    }

    public StringProperty itemNameProperty() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName.set(itemName);
    }

    public String getExistingValue() {
        return existingValue.get();
    }

    public StringProperty existingValueProperty() {
        return existingValue;
    }

    public void setExistingValue(String existingValue) {
        this.existingValue.set(existingValue);
    }

    public String getSolution() {
        return solution.get();
    }

    public StringProperty solutionProperty() {
        return solution;
    }

    public void setSolution(String solution) {
        this.solution.set(solution);
    }
}
