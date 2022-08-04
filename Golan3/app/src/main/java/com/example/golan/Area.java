package com.example.golan;

public class Area {
    private String name;
    private String startFlowering;
    private String thirtyFlowering;
    private String peakFlowering;
    private String biofix;
    private String degree100;
    private int leafHumidity;
    private String rimpro;

    public Area(String name) {
        this.name = name;
        leafHumidity=-432;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStartFlowering() {
        return startFlowering;
    }

    public void setStartFlowering(String startFlowering) {
        this.startFlowering = startFlowering;
    }

    public String getThirtyFlowering() {
        return thirtyFlowering;
    }

    public void setThirtyFlowering(String thirtyFlowering) {
        this.thirtyFlowering = thirtyFlowering;
    }

    public String getPeakFlowering() {
        return peakFlowering;
    }

    public void setPeakFlowering(String peakFlowering) {
        this.peakFlowering = peakFlowering;
    }

    public String getBiofix() {
        return biofix;
    }

    public void setBiofix(String biofix) {
        this.biofix = biofix;
    }

    public String getDegree100() {
        return degree100;
    }

    public void setDegree100(String degree100) {
        this.degree100 = degree100;
    }

    public int getLeafHumidity() {
        return leafHumidity;
    }

    public void setLeafHumidity(int leafHumidity) {
        this.leafHumidity = leafHumidity;
    }

    public String getRimpro() {
        return rimpro;
    }

    public void setRimpro(String rimpro) {
        this.rimpro = rimpro;
    }
}
