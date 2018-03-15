package com.cyq7on.avbasepractice;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import com.cyq7on.avbasepractice.view.MyImageView;
import com.cyq7on.avbasepractice.view.MySurfaceView;

public class MainActivity extends AppCompatActivity {
    private ImageView imageView;
    private MySurfaceView surfaceView;
    private MyImageView myImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        imageView = findViewById(R.id.imageView);
        surfaceView = findViewById(R.id.surfaceView);
        myImageView = findViewById(R.id.myImageView);

        showOnImageView();
    }

    void showOnImageView() {
        imageView.setImageResource(R.drawable.android_logo);
        myImageView.setImageResource(R.drawable.android_logo);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
