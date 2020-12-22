package tech.jmcs.floortech.scheduling.ui.commitbutton.nomatchchoices;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import tech.jmcs.floortech.scheduling.app.util.ExcelCellAddress;

public class NoMatchSolutionObject {

    private StringProperty itemName = new SimpleStringProperty("", "itemName");
    private StringProperty solution = new SimpleStringProperty("", "solution");
    private ObjectProperty<ExcelCellAddress> location = new SimpleObjectProperty(ExcelCellAddress.EMPTY, "location");

    public NoMatchSolutionObject() {
    }

    public NoMatchSolutionObject(String itemName, String solution) {
        this.setItemName(itemName);
        this.setSolution(solution);
        this.setLocation(ExcelCellAddress.EMPTY);
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

    public String getSolution() {
        return solution.get();
    }

    public StringProperty solutionProperty() {
        return solution;
    }

    public void setSolution(String solution) {
        this.solution.set(solution);
    }

    public ExcelCellAddress getLocation() {
        return location.get();
    }

    public ObjectProperty<ExcelCellAddress> locationProperty() {
        return location;
    }

    public void setLocation(ExcelCellAddress location) {
        this.location.set(location);
    }
}
