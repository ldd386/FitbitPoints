package ch.example.fitbitpoints.api.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class ActivitiesHeartRateIntraday {

    @SerializedName("dataset")
    @Expose
    private List<HeartRateIntraday> dataset = new ArrayList<>();

    @SerializedName("datasetInterval")
    @Expose
    private Integer activitiesHeart;

    @SerializedName("datasetType")
    @Expose
    private String datasetType;

    public List<HeartRateIntraday> getDataset() {
        return dataset;
    }

    public void setDataset(List<HeartRateIntraday> dataset) {
        this.dataset = dataset;
    }

    public Integer getActivitiesHeart() {
        return activitiesHeart;
    }

    public void setActivitiesHeart(Integer activitiesHeart) {
        this.activitiesHeart = activitiesHeart;
    }

    public String getDatasetType() {
        return datasetType;
    }

    public void setDatasetType(String datasetType) {
        this.datasetType = datasetType;
    }
}
