package com.sheenline.ehm.server.entity;

import java.util.Date;

public class HistoryStatusData {
    private String id;

    private String devicenum;

    private String statuscode;

    private String statusvalue;

    private Date datetime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getDevicenum() {
        return devicenum;
    }

    public void setDevicenum(String devicenum) {
        this.devicenum = devicenum == null ? null : devicenum.trim();
    }

    public String getStatuscode() {
        return statuscode;
    }

    public void setStatuscode(String statuscode) {
        this.statuscode = statuscode == null ? null : statuscode.trim();
    }

    public String getStatusvalue() {
        return statusvalue;
    }

    public void setStatusvalue(String statusvalue) {
        this.statusvalue = statusvalue == null ? null : statusvalue.trim();
    }

    public Date getDatetime() {
        return datetime;
    }

    public void setDatetime(Date datetime) {
        this.datetime = datetime;
    }
}