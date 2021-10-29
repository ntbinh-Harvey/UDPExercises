/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.Serializable;

/**
 *
 * @author HarveyNguyen
 */
public class Worker implements Serializable {
    private String fullname;
    private String DoB;
    private String hometown;
    private String gender;
    private float coefficientsSalary;

    public Worker() {
    }

    public Worker(String fullname, String DoB, String hometown, String gender, float coefficientsSalary) {
        this.fullname = fullname;
        this.DoB = DoB;
        this.hometown = hometown;
        this.gender = gender;
        this.coefficientsSalary = coefficientsSalary;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getDoB() {
        return DoB;
    }

    public void setDoB(String DoB) {
        this.DoB = DoB;
    }

    public String getHometown() {
        return hometown;
    }

    public void setHometown(String hometown) {
        this.hometown = hometown;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public float getCoefficientsSalary() {
        return coefficientsSalary;
    }

    public void setCoefficientsSalary(float coefficientsSalary) {
        this.coefficientsSalary = coefficientsSalary;
    }
    
}
