package com.swiftsynq.medmanager.Model;

/**
 * Created by popoolaadebimpe on 31/03/2018.
 */

public class Medication {
    public String drugName;
    public String decsription;
    public String interval;
    public String Startdate;
    public String enddate;

    public String getDrugName() {
        return drugName;
    }

    public void setDrugName(String drugName) {
        this.drugName = drugName;
    }

    public String getDecsription() {
        return decsription;
    }

    public void setDecsription(String decsription) {
        this.decsription = decsription;
    }

    public String getInterval() {
        return interval;
    }

    public void setInterval(String interval) {
        this.interval = interval;
    }

    public String getStartdate() {
        return Startdate;
    }

    public void setStartdate(String startdate) {
        Startdate = startdate;
    }

    public String getEnddate() {
        return enddate;
    }

    public void setEnddate(String enddate) {
        this.enddate = enddate;
    }
}
