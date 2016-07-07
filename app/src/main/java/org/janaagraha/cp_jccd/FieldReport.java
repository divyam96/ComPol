package org.janaagraha.cp_jccd;

/**
 * Created by TUSHAR PC on 05-07-2016.
 */
public class FieldReport {
    public String PoliceStation;
    public String BeatArea;
    public String OfficerName;
    public String ActivityName;
    public String ActivityPlan;

    public FieldReport(){
    }

    public FieldReport(String policeStation,String beatArea){
        this.PoliceStation = policeStation;
        this.BeatArea =beatArea;
    }

    public FieldReport(String officerName){
        this.OfficerName = officerName;
    }


    public FieldReport(String policeStation,String beatArea,String officerName, String activityName, String activityPlan){
        this.PoliceStation = policeStation;
        this.BeatArea = beatArea;
        this.OfficerName = officerName;
        this.ActivityName = activityName;
        this.ActivityPlan = activityPlan;
    }

    public String getPoliceStation(){
        return PoliceStation;
    }

    public String getBeatArea(){
        return BeatArea;
    }

    public  String getOfficerName(){
        return OfficerName;
    }

    public String getActivityName(){
        return ActivityName;
    }

    public String getActivityPlan(){
        return ActivityPlan;
    }


    /*
    public String getPoliceStation(){
        return PoliceStation;
    }

    public void setPoliceStation(String policeStation){
        this.PoliceStation = PoliceStation;
    }

    public String getBeatArea(){
        return BeatArea;
    }

    public void setBeatArea(String beatArea){
        this.BeatArea = BeatArea;
    }

    public String getOfficerName(){
        return OfficerName;
    }

    public void setOfficerName(String officerName){
        this.OfficerName = OfficerName;
    }

    public String getActivityName(){
        return ActivityName ;
    }

    public void setActivityName(String activityName){
        this.ActivityName = ActivityName;
    }

    public String getActivityPlan(){
        return ActivityPlan;
    }

    public void setActivityPlan(String activityPlan){
        this.ActivityPlan = ActivityPlan;
    }

    */

}
