package ch.example.fitbitpoints.api.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class CaloriesIntradayContainer {


    @SerializedName("dataset")
    @Expose
    private List<CaloriesIntraday> dataset = new ArrayList<>();

    public List<CaloriesIntraday> getDataset() {
        return dataset;
    }

    public void setDataset(List<CaloriesIntraday> dataset) {
        this.dataset = dataset;
    }
}
