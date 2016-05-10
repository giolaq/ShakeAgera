package com.laquysoft.acceleratioagera;

import android.hardware.SensorManager;
import android.support.annotation.NonNull;

import com.google.android.agera.BaseObservable;
import com.google.android.agera.Supplier;
import com.google.android.agera.Updatable;

/**
 * Created by joaobiriba on 10/05/16.
 */
public class ShakeRepository extends BaseObservable
        implements Supplier<float[]>, Updatable, AccelerometerListenable.AccelerometerValuesCallback {

    private float[] values;

    private final AccelerometerListenable accelerometerFetcher;

    private float mAccel;
    private float mAccelCurrent;
    private float mAccelLast;

    public ShakeRepository(AccelerometerListenable accelerometerFetcher) {
        super();
        this.accelerometerFetcher = accelerometerFetcher;
        values = new float[3];
        mAccel = 0.00f;
        mAccelCurrent = SensorManager.GRAVITY_EARTH;
        mAccelLast = SensorManager.GRAVITY_EARTH;

    }

    @Override
    public void setAccelerometerValues(float[] values) {
        System.arraycopy(values, 0, this.values, 0, values.length);
        isShaked(values);
    }

    @Override
    protected void observableActivated() {
        // Now that this is activated, we trigger an update to ensure the repository contains up to
        // date data.
        // update();
    }
    @NonNull
    @Override
    public float[] get() {
        return values;
    }

    @Override
    public void update() {
        accelerometerFetcher.getAccelerometerValues(this);
    }

    private void isShaked(float[] values) {
        mAccelLast = mAccelCurrent;
        mAccelCurrent = (float) Math.sqrt(values[0] * values[0] + values[1] * values[1] + values[2] * values[2]);
        float delta = mAccelCurrent - mAccelLast;
        mAccel = mAccel * 0.9f + delta;

        if (mAccel > 3) {
            dispatchUpdate();
        }
    }


}
