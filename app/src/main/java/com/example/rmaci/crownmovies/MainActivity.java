package com.example.rmaci.crownmovies;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    public static final String EXTRA = "accountNum";
    ListView mListView;
    ArrayList<Account> mListarray;
    AccountAdapter adapter;
    SharedPreferences mSharedPref;
    DateChecker mDateChecker;
    fileLoader mLoader;


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.show_all:
                showAllAccounts();
                return true;
            case R.id.show_available:
                showAvaibleAccounts();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mSharedPref = this.getPreferences(Context.MODE_PRIVATE);
        mDateChecker = new DateChecker();

        mListView = findViewById(R.id.listView);
        mListarray = new ArrayList<>();
        mLoader = new fileLoader(this);
        mListarray = mLoader.readAccounts();
        Log.d("Main","ListArray size first load:" + mListarray.size());
        mListarray = removeUsedAccounts();
        Log.d("Main","ListArray size After removed:" + mListarray.size());



        adapter = new AccountAdapter(this,mListarray);

        mListView.setAdapter(adapter);



        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Account account = (Account)adapterView.getItemAtPosition(position);
                Intent i = new Intent(getApplicationContext(),GenerateQR.class);
                i.putExtra(EXTRA,account.accountNum);
                startActivity(i);
            }
        });

    }

    private void showAllAccounts(){
        mListarray.clear();
        mListarray.addAll(mLoader.readAccounts());
        adapter.notifyDataSetChanged();
        Log.d("showAll",""+ mListarray.size());

    }

    private void showAvaibleAccounts(){

        mListarray.addAll(removeUsedAccounts());
        Log.d("showAll","avail accounts: "+ mListarray.size());
        adapter.notifyDataSetChanged();
    }


    //removes
    private ArrayList<Account> removeUsedAccounts(){
        ArrayList<Account> validAccounts = new ArrayList<>();
        Log.d("MainAct","Size of list Array before removal "+mListarray.size());
        for(Account account: mListarray){
            //remove if Used or not in bday Range
            if(mSharedPref.getBoolean(account.accountNum,false)|
                    (!mDateChecker.inBdayRange(account.bday))){
                Log.d("MainAct","Removed " + account.bday);
            }
            else validAccounts.add(account);
        }
        Log.d("MainAct","Size of Valid"+validAccounts.size());
        mListarray.clear();
        return validAccounts;
    }


    public void removeUsedAccount(Account account){
        mListarray.remove(account);
        adapter.notifyDataSetChanged();
    }




    public class AccountAdapter extends ArrayAdapter<Account> {
        public AccountAdapter(Context context, ArrayList<Account> users) {
            super(context, 0, users);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // Get the data item for this position
            Account account = getItem(position);
            // Check if an existing view is being reused, otherwise inflate the view
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_layout, parent, false);
            }
            // Lookup view for data population
            TextView bday = convertView.findViewById(R.id.bday);
            // Populate the data into the template view using the data object
            bday.setText(account.bday);
            // Return the completed view to render on screen

            //https://stackoverflow.com/questions/5291726/what-is-the-main-purpose-of-settag-gettag-methods-of-view
            //Explains setting tags in views/buttons can easily set one listener to multiple buttons.


            //For Use Button
            Button UseButton = convertView.findViewById(R.id.useBut);
            //identify each button with its position
            UseButton.setTag(position);
            //Could have made a db or csv file but wanted to try sharedpref. Made each key an account num
            UseButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    int position=(Integer)arg0.getTag();
                    Account account = getItem(position);
                    mSharedPref.edit().putBoolean(account.accountNum,true).apply();
                    removeUsedAccount(account);
                }
            });
            return convertView;
        }
    }

}