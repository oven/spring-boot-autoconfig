package no.bouvet.parking;

import com.google.gson.Gson;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class ParkingClient {

    private URI endpoint;
    private Gson gson = new Gson();
    private final static DateFormat df = new SimpleDateFormat("HH:mm");

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
                result[i] = new CarParkStatus(deserialized[i].navn, deserialized[i].ledigePlasser, df.parse(deserialized[i].oppdatert));
            }
            return result;
        } catch (ParseException e) {
            throw new RuntimeException(e);
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
