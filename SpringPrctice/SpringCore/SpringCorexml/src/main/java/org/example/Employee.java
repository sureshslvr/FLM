package org.example;

import java.util.Map;

public class Employee {
    private int id;
    private String name;
    private Map<Integer,String> skills;

    public Employee(){

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map<Integer, String> getSkills() {
        return skills;
    }

    public void setSkills(Map<Integer, String> skills) {
        this.skills = skills;
    }

    @Override
    public String toString() {
        return "Employee{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", skills=" + skills +
                '}';
    }
}
