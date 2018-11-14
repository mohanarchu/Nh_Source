package com.develop.app.myapplication;

/**
 * Created by Karthi K on 5/26/2018.
 */

class OrderList {

    String order_number,name,requested_by,contact_no,status,status_code;


    public OrderList(String order_number, String name, String requested_by, String contact_no, String status, String status_code) {
        this.order_number = order_number;
        this.name = name;
        this.requested_by = requested_by;
        this.contact_no = contact_no;
        this.status = status;
        this.status_code = status_code;
    }

    public String getOrder_number() {
        return order_number;
    }

    public void setOrder_number(String order_number) {
        this.order_number = order_number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRequested_by() {
        return requested_by;
    }

    public void setRequested_by(String requested_by) {
        this.requested_by = requested_by;
    }

    public String getContact_no() {
        return contact_no;
    }

    public void setContact_no(String contact_no) {
        this.contact_no = contact_no;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus_code() {
        return status_code;
    }

    public void setStatus_code(String status_code) {
        this.status_code = status_code;
    }
}
