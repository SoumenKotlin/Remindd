package com.example.soumendutta.mappsepnavigation.database;

/**
 * Created by soumendutta on 13/10/16.
 */

public class Longtap
{
    int _id;
    private String name;
    private String dec;
    private String redious;
    private String units;
    private String fdate;
    private String tdate;
    private String togglebutton;
    private String log;
    private String lat;
    private String playPauseStatus;


    public Longtap()
    {

    }

    public Longtap(String redious, String name, String dec, String fdate, String tdate,
                   String togglebutton, String lat, String log, String playPauseStatus, String units)
    {
        this.name = name;
        this.dec = dec;
        this.redious = redious;
        this.fdate = fdate;
        this.tdate = tdate;
        this.togglebutton = togglebutton;
        this.lat=lat;
        this.log=log;
        this.playPauseStatus = playPauseStatus;
        this.units = units;
    }

    public Longtap(String redious, String name, String dec, String fdate, String tdate,
                   String togglebutton,  String units)
    {
        this.name = name;
        this.dec = dec;
        this.redious = redious;
        this.fdate = fdate;
        this.tdate = tdate;
        this.togglebutton = togglebutton;
        this.units = units;
    }

    public Longtap(String redious) {
        this.redious = redious;
    }

    public String getPlayPauseStatus()
    {
        return playPauseStatus;
    }

    public void setPlayPauseStatus(String playPauseStatus) {
        this.playPauseStatus = playPauseStatus;
    }

    public String getUnits() {
        return units;
    }

    public void setUnits(String units) {
        this.units = units;
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {

        this.name = name;
    }

    public String getDec()
    {
        return dec;
    }

    public void setDec(String dec)
    {
        this.dec = dec;
    }

    public String getRedious()
    {
        return redious;
    }

    public void setRedious(String redious)
    {
        this.redious = redious;
    }

    public String getFdate()
    {
        return fdate;
    }

    public void setFdate(String fdate)
    {
        this.fdate = fdate;
    }

    public String getTdate()
    {
        return tdate;
    }

    public void setTdate(String tdate)
    {
        this.tdate = tdate;
    }
    public String getTogglebutton()
    {
        return togglebutton;
    }

    public void setTogglebutton(String togglebutton)
    {
        this.togglebutton = togglebutton;
    }

    public String getLat()
    {
        return lat;
    }

    public void setLat(String lat)
    {
        this.lat = lat;
    }
    public String getLog()
    {
        return log;
    }

    public void setLog(String log)
    {
        this.log = log;
    }

}
