package ch.example.fitbitpoints.api.model;

import ch.example.fitbitpoints.api.adapters.LocalTimeAdapter;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;
import org.threeten.bp.LocalTime;

public class CaloriesIntraday {

    @SerializedName("level")
    @Expose
    private Integer level;

    @SerializedName("mets")
    @Expose
    private Integer mets;

    @SerializedName("time")
    @JsonAdapter(LocalTimeAdapter.class)
    @Expose
    private LocalTime time;

    @SerializedName("value")
    @Expose
    private Double value;

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Integer getMets() {
        return mets;
    }

    public void setMets(Integer mets) {
        this.mets = mets;
    }

    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }
}
