package sugelico.postabsugelico.General;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;

import sugelico.postabsugelico.General.Definitions.GPSLocation;

/**
 * Created by hidura on 2/11/2016.
 */
public class GPS {
    public GPSLocation getLocation(Activity activity) {
        GPSLocation loc = new GPSLocation();
        LocationManager lm = (LocationManager) activity.getApplicationContext().getSystemService(Context.LOCATION_SERVICE);


        final LocationListener locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                ;
            }

            public void onProviderDisabled(String arg0) {
                // TODO Auto-generated method stub

            }

            public void onProviderEnabled(String arg0) {
                // TODO Auto-generated method stub

            }

            public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
                // TODO Auto-generated method stub

            }
        };

        if (ActivityCompat.checkSelfPermission(activity,
                Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(activity,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            GPSLocation TODO = null;
            return TODO;
        }
        Location location = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

        lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 2000, 10, locationListener);
        loc.setLongitud(Double.toString(location.getLongitude()));
        loc.setLatitud(Double.toString(location.getLatitude()));
        return loc;
    }
}
