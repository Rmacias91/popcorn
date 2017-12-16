package com.example.rmaci.crownmovies;

import android.util.Log;

import com.opencsv.CSVReader;

import java.io.FileReader;
import java.io.IOException;

/**
 * Created by Rmaci on 12/16/2017.
 */

public class fileLoader {

    public void readCSV() {
        try {
            CSVReader reader = new CSVReader(new FileReader("yourfile.csv"));
            String[] nextLine;
            while ((nextLine = reader.readNext()) != null) {
                // nextLine[] is an array of values from the line
                System.out.println(nextLine[0] + nextLine[1]);
            }
        }catch(IOException e){Log.e("csv",e.getMessage());}
    }

}
