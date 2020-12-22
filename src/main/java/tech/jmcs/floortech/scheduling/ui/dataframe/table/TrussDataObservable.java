package tech.jmcs.floortech.scheduling.ui.dataframe.table;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import tech.jmcs.floortech.scheduling.app.types.EndCapType;

import java.util.ArrayList;
import java.util.List;

public class TrussDataObservable {
    private StringProperty trussId = new SimpleStringProperty("", "trussId");

    private LongProperty qty = new SimpleLongProperty(0L, "qty");

    private LongProperty length = new SimpleLongProperty(0L, "length");

    private StringProperty type = new SimpleStringProperty("", "type");

    private ObjectProperty<EndCapType> leftEndcap = new SimpleObjectProperty<>(EndCapType.STANDARD, "leftEndcap");

    private ObjectProperty<EndCapType> rightEndcap = new SimpleObjectProperty<>(EndCapType.STANDARD, "rightEndcap");

    private BooleanProperty airConPeno = new SimpleBooleanProperty(false, "airConPeno");

    private ObservableList<Integer> penetrationWebCuts = FXCollections.observableArrayList(new ArrayList<>());

    private IntegerProperty packingGroup = new SimpleIntegerProperty(0, "packingGroup");

    public TrussDataObservable() {
    }

    public String getTrussId() {
        return trussId.get();
    }

    public StringProperty trussIdProperty() {
        return trussId;
    }

    public void setTrussId(String trussId) {
        this.trussId.set(trussId);
    }

    public long getQty() {
        return qty.get();
    }

    public LongProperty qtyProperty() {
        return qty;
    }

    public void setQty(long qty) {
        this.qty.set(qty);
    }

    public long getLength() {
        return length.get();
    }

    public LongProperty lengthProperty() {
        return length;
    }

    public void setLength(long length) {
        this.length.set(length);
    }

    public String getType() {
        return type.get();
    }

    public StringProperty typeProperty() {
        return type;
    }

    public void setType(String type) {
        this.type.set(type);
    }

    public EndCapType getLeftEndcap() {
        return leftEndcap.get();
    }

    public ObjectProperty<EndCapType> leftEndcapProperty() {
        return leftEndcap;
    }

    public void setLeftEndcap(EndCapType leftEndcap) {
        this.leftEndcap.set(leftEndcap);
    }

    public EndCapType getRightEndcap() {
        return rightEndcap.get();
    }

    public ObjectProperty<EndCapType> rightEndcapProperty() {
        return rightEndcap;
    }

    public void setRightEndcap(EndCapType rightEndcap) {
        this.rightEndcap.set(rightEndcap);
    }

    public boolean isAirConPeno() {
        return airConPeno.get();
    }

    public BooleanProperty airConPenoProperty() {
        return airConPeno;
    }

    public void setAirConPeno(boolean airConPeno) {
        this.airConPeno.set(airConPeno);
    }

    public ObservableList<Integer> getPenetrationWebCuts() {
        return penetrationWebCuts;
    }

    public void setPenetrationWebCuts(List<Integer> penetrationWebCuts) {
        this.penetrationWebCuts.clear();
        this.penetrationWebCuts.addAll(penetrationWebCuts);
    }

    public int getPackingGroup() {
        return packingGroup.get();
    }

    public IntegerProperty packingGroupProperty() {
        return packingGroup;
    }

    public void setPackingGroup(int packingGroup) {
        this.packingGroup.set(packingGroup);
    }
}
