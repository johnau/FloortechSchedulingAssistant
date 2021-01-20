package tech.jmcs.floortech.scheduling.ui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import tech.jmcs.floortech.scheduling.ui.helper.StatusType;

import java.util.ArrayList;
import java.util.LinkedList;

public class StatusHolderFX {

    ObservableList<String> statusWindowBuffer;

    public StatusHolderFX() {
        statusWindowBuffer = FXCollections.observableList(new ArrayList<>());

    }

    public ObservableList<String> getStatusWindowBuffer() {
        return this.statusWindowBuffer;
    }

    public void addStatus(StatusType type, String status) {
//        TextFlow flow = new TextFlow();
//
//        Text text = new Text();
//        text.setText(status);
//
//        if (type == StatusType.IMPORTANT) {
//            text.setFont(Font.font("System", FontWeight.BOLD, 12));
//            text.setFill(Color.web("#0077ff"));
//        } else if (type == StatusType.ERROR) {
//            text.setFont(Font.font("System", FontWeight.BOLD, 12));
//            text.setFill(Color.RED);
//        } else if (type == StatusType.NORMAL) {
//            text.setFont(Font.font("System", FontWeight.BOLD, 12));
//            text.setFill(Color.DARKGRAY);
//        }
//
//        flow.getChildren().add(text);

        statusWindowBuffer.add(status);
    }


}
