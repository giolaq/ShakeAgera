package com.laquysoft.acceleratioagera;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.annotation.NonNull;

import com.google.android.agera.ActivationHandler;
import com.google.android.agera.Observable;
import com.google.android.agera.Observables;
import com.google.android.agera.Updatable;
import com.google.android.agera.UpdateDispatcher;

/**
 * Created by joaobiriba on 04/05/16.
 */
public class AccelerometerListenable implements Observable {

    private final UpdateDispatcher updateDispatcher;
    private Context mContext;
    private float[] values;


    public AccelerometerListenable(Context context) {
        values = new float[3];
        mContext = context;
        updateDispatcher = Observables.updateDispatcher(new Bridge());
    }


    @Override
    public void addUpdatable(@NonNull Updatable updatable) {
        updateDispatcher.addUpdatable(updatable);
    }

    @Override
    public void removeUpdatable(@NonNull Updatable updatable) {
        updateDispatcher.removeUpdatable(updatable);
    }


    private final class Bridge implements ActivationHandler, SensorEventListener {
        private SensorManager sensorManager;

        public Bridge() {
            sensorManager = (SensorManager) mContext.getSystemService(Context.SENSOR_SERVICE);
        }


        @Override
        public void observableActivated(UpdateDispatcher caller) {
            sensorManager.registerListener(this,
                    sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                    SensorManager.SENSOR_DELAY_NORMAL);
        }

        @Override
        public void observableDeactivated(UpdateDispatcher caller) {
            sensorManager.unregisterListener(this);
        }


        @Override
        public void onSensorChanged(SensorEvent event) {
            if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
                values = getAccelerometer(event);
            }
            updateDispatcher.update();
        }

        private float[] getAccelerometer(SensorEvent event) {
            return event.values;
        }


        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }


    }

    public void getAccelerometerValues(AccelerometerValuesCallback callback) {
        callback.setAccelerometerValues(values);
    }

    public interface AccelerometerValuesCallback {
        void setAccelerometerValues(float[] values);
    }
}
