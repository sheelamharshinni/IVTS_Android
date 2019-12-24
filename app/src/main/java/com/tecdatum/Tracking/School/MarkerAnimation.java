package com.tecdatum.Tracking.School;

import android.os.Handler;
import android.os.SystemClock;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

public class MarkerAnimation {

    public static boolean isMarkerRotating = false;
    static double lat = 0.0;
    static double lng = 0.04;

    public static void animateMarkerToGB(final Marker marker, final LatLng finalPosition, final LatLngInterpolator latLngInterpolator, final GoogleMap googleMap) {

        if (!isMarkerRotating) {
            final LatLng startPosition = marker.getPosition();
            final Handler handler = new Handler();
            final long start = SystemClock.uptimeMillis();
            final Interpolator interpolator = new AccelerateDecelerateInterpolator();
            final float durationInMs = 2000;
            handler.post(new Runnable() {
                long elapsed;
                float t;
                float v;


                @Override
                public void run() {
                    isMarkerRotating = true;

                    // Ca   lculate progress using interpolator
                    elapsed = SystemClock.uptimeMillis() - start;
                    t = elapsed / durationInMs;
                    v = interpolator.getInterpolation(t);

                    marker.setPosition(latLngInterpolator.interpolate(v, startPosition, finalPosition));
//                      marker.setRotation(getBearing(startPosition, finalPosition));
                    googleMap.animateCamera(CameraUpdateFactory.newCameraPosition
                            (new CameraPosition.Builder().target(finalPosition).zoom(16f).build()));


                    // Repeat till progress is complete.
                    if (t < 1) {
                        // Post again 10ms later.
                        handler.postDelayed(this, 10);
                    } else {
                        isMarkerRotating = false;
                    }
                }
            });
        }
    }


    public static float getBearing(LatLng begin, LatLng end) {
        lat = Math.abs(begin.latitude - end.latitude);
        lng = Math.abs(begin.longitude - end.longitude);

        if (begin.latitude < end.latitude && begin.longitude < end.longitude)
            return (float) (Math.toDegrees(Math.atan(lng / lat)));
        else if (begin.latitude >= end.latitude && begin.longitude < end.longitude)
            return (float) ((90 - Math.toDegrees(Math.atan(lng / lat))) + 90);
        else if (begin.latitude >= end.latitude && begin.longitude >= end.longitude)
            return (float) (Math.toDegrees(Math.atan(lng / lat)) + 180);
        else if (begin.latitude < end.latitude && begin.longitude >= end.longitude)
            return (float) ((90 - Math.toDegrees(Math.atan(lng / lat))) + 270);
        return -1;
    }
}

