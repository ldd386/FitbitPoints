package ch.example.fitbitpoints.api.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class HeartRatesContainer {

    @SerializedName("activities-heart")
    @Expose
    private List<Object> activitiesHeartRate = new ArrayList<>();

    @SerializedName("activities-heart-intraday")
    @Expose
    private ActivitiesHeartRateIntraday activitiesHeartRateIntraday;

    public List<Object> getActivitiesHeartRate() {
        return activitiesHeartRate;
    }

    public void setActivitiesHeartRate(List<Object> activitiesHeartRate) {
        this.activitiesHeartRate = activitiesHeartRate;
    }

    public ActivitiesHeartRateIntraday getActivitiesHeartRateIntraday() {
        return activitiesHeartRateIntraday;
    }

    public void setActivitiesHeartRateIntraday(ActivitiesHeartRateIntraday activitiesHeartRateIntraday) {
        this.activitiesHeartRateIntraday = activitiesHeartRateIntraday;
    }
}
