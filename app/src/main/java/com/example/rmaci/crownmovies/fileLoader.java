package com.example.rmaci.crownmovies;


import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rmaci on 12/16/2017.
 */

public class fileLoader {
    private Context mContext;
    private ArrayList<Account> mAccounts;

    public fileLoader(Context context){
        mContext = context;
        mAccounts = new ArrayList<>();
        }

        public ArrayList<Account> readAccounts() {
            final InputStream input = mContext.getResources().openRawResource(R.raw.accounts);
            BufferedReader bufferedReader = new BufferedReader(
                    new InputStreamReader(input, Charset.forName("UTF-8"))
            );

            String line = "";

            try {
                while((line = bufferedReader.readLine())!= null ){
                    String[] tokens = line.split(",");
                    Account account = new Account(tokens[0],tokens[1]);
                    mAccounts.add(account);
                    Log.d("fileLoader", account.toString());
                }
            } catch (IOException e) {
                Log.wtf("fileLoader","Error reading file on line " + line, e);
                e.printStackTrace();
            }
            return mAccounts;
        }

}
