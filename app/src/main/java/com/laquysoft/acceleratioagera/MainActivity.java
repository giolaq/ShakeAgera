package com.laquysoft.acceleratioagera;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.android.agera.Updatable;


public class MainActivity extends AppCompatActivity implements Updatable {

    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    AccelerometerListenable observable;

    ShakeRepository shakeRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        observable = new AccelerometerListenable(this);
        shakeRepository = new ShakeRepository(observable);

    }


    @Override
    public void update() {
        Log.d(LOG_TAG, "Shake!");
    }

    @Override
    protected void onResume() {
        super.onResume();
        observable.addUpdatable(shakeRepository);
        shakeRepository.addUpdatable(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        observable.removeUpdatable(shakeRepository);
        shakeRepository.removeUpdatable(this);
    }
}
