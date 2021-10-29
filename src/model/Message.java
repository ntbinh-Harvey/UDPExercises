/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * @author Admin
 */
public class Message implements Serializable {

    private int code;
    private String request;
    private Worker w;
    private ArrayList<Worker> lw;
    private ArrayList<String> hometown;

    public Message() {
    }


    public Message(int code) {
        this.code = code;
    }

    public Message(int code, String request) {
        this.code = code;
        this.request = request;
    }

    public Message(int code, String request, Worker w) {
        this.code = code;
        this.request = request;
        this.w = w;
    }

    public Message(int code, String request, Worker w, ArrayList<Worker> lw) {
        this.code = code;
        this.request = request;
        this.w = w;
        this.lw = lw;
    }

    public Message(int code, String request, Worker w, ArrayList<Worker> lw, ArrayList<String> hometown) {
        this.code = code;
        this.request = request;
        this.w = w;
        this.lw = lw;
        this.hometown = hometown;
    }

    public Worker getW() {
        return w;
    }

    public void setW(Worker w) {
        this.w = w;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getRequest() {
        return request;
    }

    public void setRequest(String request) {
        this.request = request;
    }

    public ArrayList<Worker> getLw() {
        return lw;
    }

    public void setLw(ArrayList<Worker> lw) {
        this.lw = lw;
    }
    
    public ArrayList<String> getHometown() {
        return hometown;
    }

    public void setHometown(ArrayList<String> hometown) {
        this.hometown = hometown;
    }

}
