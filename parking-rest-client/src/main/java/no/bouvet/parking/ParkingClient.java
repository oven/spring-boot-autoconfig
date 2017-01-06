package no.bouvet.parking;

import com.google.gson.Gson;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.joda.time.LocalTime;
import org.joda.time.format.DateTimeFormat;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;

public class ParkingClient {

    private URI endpoint;
    private Gson gson = new Gson();

    public CarParkStatus[] getData() throws IOException {
        HttpGet get = new HttpGet(endpoint);
        try (
                CloseableHttpClient httpClient = HttpClients.createDefault();
                CloseableHttpResponse response = httpClient.execute(get)
        ) {
            HttpEntity entity = response.getEntity();
            Parkeringshusstatus[] deserialized = gson.fromJson(new InputStreamReader(entity.getContent()), Parkeringshusstatus[].class);
            CarParkStatus[] result = new CarParkStatus[deserialized.length];
            for (int i = 0; i < deserialized.length; i++) {
                result[i] = new CarParkStatus(deserialized[i].navn, deserialized[i].ledigePlasser, LocalTime.parse(deserialized[i].oppdatert, DateTimeFormat.forPattern("HH:mm")));
            }
            return result;
        }
    }

    public void setEndpoint(URI endpoint) {
        this.endpoint = endpoint;
    }

    static class Parkeringshusstatus {
        public String navn, oppdatert;
        public int ledigePlasser;
    }
}
