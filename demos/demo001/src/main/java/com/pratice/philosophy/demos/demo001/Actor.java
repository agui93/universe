package com.pratice.philosophy.demos.demo001;

public class Actor {

    private Integer id;
    private String firstName;
    private String lastName;

    public Actor() {
    }

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }


    public String getFirstName() {
        return this.firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Override
    public String toString() {
        return "{" + this.id + ";" + this.firstName + ";" + this.lastName + "}";

    }

}