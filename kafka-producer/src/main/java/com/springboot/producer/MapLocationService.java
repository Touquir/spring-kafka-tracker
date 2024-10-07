package com.springboot.producer;

public class MapLocationService {

    public ObjectCoordinates getCurrentLocation(String id) {
        ObjectCoordinates coordinates = new ObjectCoordinates();
        coordinates.setId(id);
        coordinates.setLat((int)(Math.random()*1000));
        coordinates.setLng((int)(Math.random()*1000));
        return coordinates;
    }
}
