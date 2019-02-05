package ch.example.fitbitpoints.api.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class CaloriesContainer {

    @SerializedName("activities-calories")
    @Expose
    private List<Calories> activitiesCalories = new ArrayList<>();

    @SerializedName("activities-calories-intraday")
    @Expose
    private CaloriesIntradayContainer activitiesCaloriesIntraday;


    public List<Calories> getActivitiesCalories() {
        return activitiesCalories;
    }

    public void setActivitiesCalories(List<Calories> activitiesCalories) {
        this.activitiesCalories = activitiesCalories;
    }

    public CaloriesIntradayContainer getActivitiesCaloriesIntraday() {
        return activitiesCaloriesIntraday;
    }

    public void setActivitiesCaloriesIntraday(CaloriesIntradayContainer activitiesCaloriesIntraday) {
        this.activitiesCaloriesIntraday = activitiesCaloriesIntraday;
    }
}
