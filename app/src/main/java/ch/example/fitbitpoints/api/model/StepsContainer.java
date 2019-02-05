package ch.example.fitbitpoints.api.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class StepsContainer {

    @SerializedName("activities-steps")
    @Expose
    private List<Steps> activitiesSteps = new ArrayList<>();

    @SerializedName("activities-steps-intraday")
    @Expose
    private ActivitiesStepsIntraday activitiesStepsIntraday;

    public List<Steps> getActivitiesSteps() {
        return activitiesSteps;
    }

    public void setActivitiesSteps(List<Steps> activitiesSteps) {
        this.activitiesSteps = activitiesSteps;
    }

    public ActivitiesStepsIntraday getActivitiesStepsIntraday() {
        return activitiesStepsIntraday;
    }

    public void setActivitiesStepsIntraday(ActivitiesStepsIntraday activitiesStepsIntraday) {
        this.activitiesStepsIntraday = activitiesStepsIntraday;
    }
}
