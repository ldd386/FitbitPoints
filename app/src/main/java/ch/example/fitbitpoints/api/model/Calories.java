package ch.example.fitbitpoints.api.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Calories {

    @SerializedName("dateTime")
    @Expose
    private String dateTime;
    @SerializedName("value")
    @Expose
    private String value;

    /**
     * @return The dateTime
     */
    public String getDateTime() {
        return dateTime;
    }

    /**
     * @param dateTime The dateTime
     */
    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    /**
     * @return The value
     */
    public String getValue() {
        return value;
    }

    /**
     * @param value The value
     */
    public void setValue(String value) {
        this.value = value;
    }
}
