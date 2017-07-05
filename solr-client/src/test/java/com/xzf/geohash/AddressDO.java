package com.xzf.geohash;

/**
 * Created by Administrator on 2017/7/5.
 */
public class AddressDO {

    private Double latitude;

    private Double longitude;

    private String geoCode;

    private Double distance;

    public Double getDistance() {
        return distance;
    }

    public void setDistance(Double distance) {
        this.distance = distance;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getGeoCode() {
        return geoCode;
    }

    public void setGeoCode(String geoCode) {
        this.geoCode = geoCode;
    }

    @Override
    public String toString() {
        return "AddressDO{" +
                "latitude=" + latitude +
                ", longitude=" + longitude +
                ", geoCode='" + geoCode + '\'' +
                ", distance=" + distance +
                '}';
    }
}
