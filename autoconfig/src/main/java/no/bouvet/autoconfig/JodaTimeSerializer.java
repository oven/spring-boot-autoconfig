package no.bouvet.autoconfig;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.joda.time.LocalTime;

import java.io.IOException;

public class JodaTimeSerializer extends JsonSerializer<LocalTime> {
    @Override
    public void serialize(LocalTime localTime, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeString(localTime.toString("HH:mm"));
    }
}
