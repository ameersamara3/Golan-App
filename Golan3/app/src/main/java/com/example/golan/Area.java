package com.example.golan;

public class Area implements Comparable<Area>{
    private String Name;
    private double distance;
    private double longitude;
    private double latitide;

    public double getDistance() {
        return distance;
    }

    public double getLongitude() {
        return longitude;
    }

    public double getLatitide() {
        return latitide;
    }

    public Area(String name, double distance, double longitude, double latitide) {
        Name = name;
        this.distance = distance;
        this.longitude = longitude;
        this.latitide = latitide;
    }

    public static double distance(double lat1, double lon1, double lat2, double lon2) {
        double radlat1 = Math.PI * lat1/180;
        double radlat2 = Math.PI * lat2/180;
        double theta = lon1-lon2;
        double radtheta = Math.PI * theta/180;
        double dist = Math.sin(radlat1) * Math.sin(radlat2) + Math.cos(radlat1) * Math.cos(radlat2) * Math.cos(radtheta);
        dist = Math.acos(dist);
        dist = dist * 180/Math.PI;
        dist = dist * 60 * 1.1515;
        dist = dist * 1.609344 ;
        return dist;
    }


    public String getName() {
        return Name;
    }

    @Override
    public int compareTo(Area o) {
        if (this.distance>o.getDistance()){
            return 1;
        }
        return -1;
    }

}
