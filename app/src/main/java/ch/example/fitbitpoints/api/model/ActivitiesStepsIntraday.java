package ch.example.fitbitpoints.api.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class ActivitiesStepsIntraday {

    @SerializedName("dataset")
    @Expose
    private List<StepsIntraday> dataset = new ArrayList<>();

    public List<StepsIntraday> getDataset() {
        return dataset;
    }

    public void setDataset(List<StepsIntraday> dataset) {
        this.dataset = dataset;
    }
}
