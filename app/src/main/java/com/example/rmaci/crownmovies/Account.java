package com.example.rmaci.crownmovies;

/**
 * Created by Rmaci on 12/19/2017.
 */

public class Account {
         String bday;
         String accountNum;
         boolean used;

         Account(String bday, String accountNum) {
            this.bday = bday;
            this.accountNum = accountNum;
            used = false;
        }


    @Override
    public String toString() {
        return "Account{" +
                "bday='" + bday + '\'' +
                ", accountNum='" + accountNum + '\'' +
                ", used=" + used +
                '}';
    }
}
