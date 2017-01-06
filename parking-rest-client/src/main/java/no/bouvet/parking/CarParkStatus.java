package no.bouvet.parking;

import java.util.Date;

public class CarParkStatus {
    private final String name;
    private final int vacantSpaces;
    private final Date timestamp;

    public CarParkStatus(String name, int vacantSpaces, Date timestamp) {
        this.name = name;
        this.vacantSpaces = vacantSpaces;
        this.timestamp = timestamp;
    }

    public String getName() {
        return name;
    }

    public int getVacantSpaces() {
        return vacantSpaces;
    }

    public Date getTimestamp() {
        return timestamp;
    }
}
