package tech.jmcs.floortech.scheduling.app.datasource.model;

import tech.jmcs.floortech.scheduling.app.types.EndCapType;

import java.util.ArrayList;
import java.util.List;

/**
 * Data object for storing values from Truss Listing
 * ** Note on adding fields:
 * ! Default values should be provided for all fields for older truss lists that do not supply that data.
 */
public class TrussData {

    private String trussId = "NA";

    private Long qty = 0l;

    private Long length = 0l;

    private String type = "NA";

    private EndCapType leftEndcap = EndCapType.STANDARD;

    private EndCapType rightEndcap = EndCapType.STANDARD;

    private boolean airConPeno = false;

    private List<Integer> penetrationWebCuts = new ArrayList();

    private Integer packingGroup = 1;

    public TrussData() {
    }

    public String getTrussId() {
        return trussId;
    }

    public void setTrussId(String trussId) {
        this.trussId = trussId;
    }

    public Long getQty() {
        return qty;
    }

    public void setQty(Long qty) {
        this.qty = qty;
    }

    public Long getLength() {
        return length;
    }

    public void setLength(Long length) {
        this.length = length;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public EndCapType getLeftEndcap() {
        return leftEndcap;
    }

    public void setLeftEndcap(EndCapType leftEndcap) {
        this.leftEndcap = leftEndcap;
    }

    public EndCapType getRightEndcap() {
        return rightEndcap;
    }

    public void setRightEndcap(EndCapType rightEndcap) {
        this.rightEndcap = rightEndcap;
    }

    public boolean hasAirconPenetration() {
        return airConPeno;
    }

    public void setHasAirconPenetration(boolean airConPeno) {
        this.airConPeno = airConPeno;
    }

    public List<Integer> getPenetrationWebCuts() {
        return penetrationWebCuts;
    }

    public void setPenetrationWebCuts(List<Integer> penetrationWebCuts) {
        this.penetrationWebCuts = penetrationWebCuts;
    }

    public Integer getPackingGroup() {
        return packingGroup;
    }

    public void setPackingGroup(Integer packingGroup) {
        this.packingGroup = packingGroup;
    }
}
