package com.example.rmaci.crownmovies;

import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

public class GenerateQR extends AppCompatActivity {
    private String mAccountNum;
    SharedPreferences mSharedPref;
    private int brightness;
    private ContentResolver cResolver;
    private Window window;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.qr_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.reset_account:
                resetUsed();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }

    }

    private void resetUsed(){
        final AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle("Reset Account");
        alertDialog.setMessage("Are You Sure You Want to Reset This Account?");
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "YES",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        mSharedPref.edit().putBoolean(mAccountNum,false).apply();
                        dialog.dismiss();
                        Toast.makeText(getApplicationContext(),"Account "+mAccountNum+" reset", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "NO",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
    }





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generate_qr);
        Intent intent = getIntent();
        //Only Show Action Bar if we are viewing all Items
        if(intent.getBooleanExtra(MainActivity.EXTRA_SHOW,false)) {
            Toolbar toolbar = findViewById(R.id.toolbar);
            toolbar.setTitle("");
            setSupportActionBar(toolbar);
        }

        mSharedPref =  PreferenceManager.getDefaultSharedPreferences(getBaseContext());

        cResolver = getContentResolver();
        window = getWindow();

        try
        {
            // To handle the auto
            Settings.System.putInt(cResolver,
                    Settings.System.SCREEN_BRIGHTNESS_MODE, Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL);
            //Get the current system brightness
            brightness = Settings.System.getInt(cResolver, Settings.System.SCREEN_BRIGHTNESS);
        }
        catch (Settings.SettingNotFoundException e)
        {
            //Throw an error case it couldn't be retrieved
            Log.e("Error", "Cannot access system brightness");
            e.printStackTrace();
        }


        Settings.System.putInt(cResolver, Settings.System.SCREEN_BRIGHTNESS, brightness);
        //Get the current window attributes
        WindowManager.LayoutParams layoutpars = window.getAttributes();
        //Set the brightness of this window
        layoutpars.screenBrightness = 255 / (float)255;
        //Apply attribute changes to this window
        window.setAttributes(layoutpars);

        mAccountNum = intent.getStringExtra(MainActivity.EXTRA);
        TextView tv_account = findViewById(R.id.tv_account);
        tv_account.setText(addSpacesToAccount(mAccountNum));

                QRCodeWriter writer = new QRCodeWriter();
                try{
                    BitMatrix bitMatrix = writer.encode(mAccountNum,BarcodeFormat.QR_CODE,800,500);
                    int width = bitMatrix.getWidth();
                    int height = bitMatrix.getHeight();
                    Bitmap bmp = Bitmap.createBitmap(width,height,Bitmap.Config.ARGB_8888);
                    bmp.setHasAlpha(true);
                    for (int x = 0; x < width; x++) {
                        for (int y = 0; y < height; y++) {
                            bmp.setPixel(x, y, bitMatrix.get(x, y) ? Color.BLACK : Color.TRANSPARENT);

                        }
                    }
//                    BitmapDrawable bd = new BitmapDrawable(bmp);
//                    bd.setAlpha(50);
                    ((ImageView) findViewById(R.id.image_qr)).setImageBitmap(bmp);
                }catch(WriterException e){e.printStackTrace();}
            }

    private String addSpacesToAccount(String account){
        StringBuilder spacedAccount = new StringBuilder();
        while(account.length() > 3) {

            String nextChunk = account.substring(0,4);
            spacedAccount = spacedAccount.append(nextChunk).append(" ");

            account = account.substring(4,account.length());
        }
        spacedAccount = spacedAccount.append(account);
        return spacedAccount.toString();
    }

    }


