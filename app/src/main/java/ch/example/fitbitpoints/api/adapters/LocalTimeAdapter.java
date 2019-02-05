package ch.example.fitbitpoints.api.adapters;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import org.threeten.bp.LocalTime;

import java.io.IOException;

public class LocalTimeAdapter extends TypeAdapter<LocalTime> {
    @Override
    public void write(final JsonWriter jsonWriter, final LocalTime localTime) throws IOException {
        if (localTime == null) {
            jsonWriter.nullValue();
        } else {
            jsonWriter.value(localTime.toString());
        }
    }

    @Override
    public LocalTime read(final JsonReader jsonReader) throws IOException {
        if (jsonReader.peek() == JsonToken.NULL) {
            return null;
        } else {
            return LocalTime.parse(jsonReader.nextString());
        }
    }
}