package no.bouvet.parking;

import org.joda.time.LocalTime;

public class CarParkStatus {
    private final String name;
    private final int vacantSpaces;
    private final LocalTime timestamp;

    public CarParkStatus(String name, int vacantSpaces, LocalTime timestamp) {
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

    public LocalTime getTimestamp() {
        return timestamp;
    }
}
