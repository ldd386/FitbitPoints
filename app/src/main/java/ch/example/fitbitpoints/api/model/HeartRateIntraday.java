package ch.example.fitbitpoints.api.model;

import ch.example.fitbitpoints.api.adapters.LocalTimeAdapter;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;
import org.threeten.bp.LocalTime;

public class HeartRateIntraday {


    @SerializedName("time")
    @JsonAdapter(LocalTimeAdapter.class)
    @Expose
    private LocalTime time;

    @SerializedName("value")
    @Expose
    private Integer value;

    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }
}
