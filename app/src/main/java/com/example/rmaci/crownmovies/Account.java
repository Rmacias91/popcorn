package com.example.rmaci.crownmovies;

import android.util.Log;

/**
 * Created by Rmaci on 12/19/2017.
 */

public class Account implements Comparable<Account> {
         String bday;
         String accountNum;
         boolean used;

         Account(String bday, String accountNum) {
            this.bday = bday;
            this.accountNum = accountNum;
            used = false;
        }

        @Override
        public int compareTo(Account other){
             int returnVal = 0;
             String[] date1 = this.bday.split("/");
             int date1Month = Integer.valueOf(date1[0]);
             int date1Day = Integer.valueOf(date1[1]);

             String[] date2 = other.bday.split("/");
             int date2Month = Integer.valueOf(date2[0]);
             int date2Day = Integer.valueOf(date2[1]);

            //Same month
             if(date1Month == date2Month){
                 //Assuming they never have the same date and moth
                 if(date1Day<date2Day){
                     returnVal = -1;
                 }
                 else {returnVal=1;}
             }
             else if(date1Month<date2Month){
                 returnVal=-1;
             }
             else returnVal = 1;

            Log.d("Account","Month1= "+date1Month +" Day1= "+date1Day);
            Log.d("Account","Month2= "+date2Month +" Day2= "+date2Day);
            Log.d("Account","ReturnVal= "+returnVal);

             return returnVal;
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
