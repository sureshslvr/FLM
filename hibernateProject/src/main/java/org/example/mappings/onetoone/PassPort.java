package org.example.mappings.onetoone;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;

@Entity
public class PassPort {

    @Id
    private  int pass_Num;
    private String issuedDate;

    @OneToOne(mappedBy = "passPort")
    private User user;
    public PassPort(int pass_Num, String issuedDate) {
        this.pass_Num = pass_Num;
        this.issuedDate = issuedDate;
    }


    public PassPort(int pass_Num, String issuedDate, User user) {
        this.pass_Num = pass_Num;
        this.issuedDate = issuedDate;
        this.user = user;
    }

    public PassPort() {
    }

    public int getPass_Num() {
        return pass_Num;
    }

    public void setPass_Num(int pass_Num) {
        this.pass_Num = pass_Num;
    }

    public String getIssuedDate() {
        return issuedDate;
    }

    public void setIssuedDate(String issuedDate) {
        this.issuedDate = issuedDate;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "PassPort{" +
                "pass_Num=" + pass_Num +
                ", issuedDate='" + issuedDate + '\'' +
                '}';
    }
}
