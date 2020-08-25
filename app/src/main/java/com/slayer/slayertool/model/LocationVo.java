package com.slayer.slayertool.model;

public class LocationVo {
  private Integer id;
  private Long createTime;
  private Double latitude;
  private Double longitude;
  private Double altitude;
  private Float accuracy;
  private Float bearing;
  private Float speed;
  private String provider;

  public LocationVo() {

  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public Long getCreateTime() {
    return createTime;
  }

  public void setCreateTime(Long createTime) {
    this.createTime = createTime;
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

  public Double getAltitude() {
    return altitude;
  }

  public void setAltitude(Double altitude) {
    this.altitude = altitude;
  }

  public Float getAccuracy() {
    return accuracy;
  }

  public void setAccuracy(Float accuracy) {
    this.accuracy = accuracy;
  }

  public Float getBearing() {
    return bearing;
  }

  public void setBearing(Float bearing) {
    this.bearing = bearing;
  }

  public Float getSpeed() {
    return speed;
  }

  public void setSpeed(Float speed) {
    this.speed = speed;
  }

  public String getProvider() {
    return provider;
  }

  public void setProvider(String provider) {
    this.provider = provider;
  }
}

