package id.co.gsd.mybirawa.util;

/**
 * Created by Gilang on 13-Apr-16.
 */

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

//public class UserLocationManager extends Service implements LocationListener {
public class UserLocationManager extends Service {

    private final Context _context;

    //boolean isGPSEnable = false;
    //boolean isNetworkEnable = false;
    //boolean canGetLocation = false;

    /*
    Location location;
    double latitude;
    double longitude;
    */

    // GPS akan update ketika jarak sudah berubah lebih dari 10 meter
    //private static final long MIN_JARAK_GPS_UPDATE = 10;                // meter

    // GPS akan update pada waktu interval
    //private static final long MIN_WAKTU_GPS_UPDATE = 1000 * 1 * 1;

    //protected LocationManager locManager;
    //private LocationRequest mLocationRequest;

    public UserLocationManager(Context context) {
        _context = context;
        //checkProvider();

        /*mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(10 * 1000)        // 10 seconds, in milliseconds
                .setFastestInterval(1 * 1000); // 1 second, in milliseconds
        */
    }

    /*public boolean checkProvider() {
        boolean canGetLocation = false;
        try {
            LocationManager locManager = (LocationManager) _context.getSystemService(LOCATION_SERVICE);
            boolean isGPSEnable = locManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            boolean isNetworkEnable = locManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (isGPSEnable || isNetworkEnable) {
                canGetLocation = true;
            } else {
                canGetLocation = false;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return canGetLocation;

    }

    public float calculateDistance(Location from, Location target) {

        return from.distanceTo(target);
    }

    public String[] getAddress(double lat, double lon){
        String[] result;
        result = new String[6];
        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(_context);
        try{
            addresses = geocoder.getFromLocation(lat, lon, 1);
            result[0] = addresses.get(0).getAddressLine(0);
            result[1] = addresses.get(0).getLocality();
            result[2] = addresses.get(0).getAdminArea();
            result[3] = addresses.get(0).getCountryName();
            result[4] = addresses.get(0).getPostalCode();
            result[5] = addresses.get(0).getFeatureName();
        }catch (Exception e){
            System.out.println(e);
        }
        return result;
    }

    /*
    public LatLng getPosition() {
        try {
            //locManager = (LocationManager) _context.getSystemService(LOCATION_SERVICE);
            if (isNetworkEnable) {

                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                   // return a;
                }
                locManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_WAKTU_GPS_UPDATE,
                        MIN_JARAK_GPS_UPDATE, this);
                //locManager.requestLocationUpdates(locManager.NETWORK_PROVIDER, MIN_WAKTU_GPS_UPDATE,MIN_JARAK_GPS_UPDATE,this);


                if (locManager != null) {
                    location = locManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                    if (location != null) {
                        latitude = location.getLatitude();
                        longitude = location.getLongitude();
                    }
                }
            }

            // jika gps bisa digunakan
            if (isGPSEnable) {
                if (location == null) {
                    // ambil posisi berdasar GPS
                    locManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_WAKTU_GPS_UPDATE,
                            MIN_JARAK_GPS_UPDATE, this);
                    if (locManager != null) {
                        // dapatkan posisi terakhir user menggunakan GPS
                        location = locManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                        if (location != null) {
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();
                        }
                    }
                }
            }


                    if (isGPSEnable && isNetworkEnable) {
                        if (location == null) {
                            // ambil posisi berdasar GPS
                            locManager.requestLocationUpdates(LocationManager., MIN_WAKTU_GPS_UPDATE,
                                    MIN_JARAK_GPS_UPDATE, this);
                            if (locManager != null) {
                                // dapatkan posisi terakhir user menggunakan GPS
                                location = locManager.getLastKnownLocation();
                                // jika lokasi berhasil didapat
                                if (location != null) {
                                    // ambil latitude
                                    latitude = location.getLatitude();
                                    // ambil longitude
                                    longitude = location.getLongitude();
                                }
                            }
                        }
                    }
        } catch (Exception e) {
            e.printStackTrace();
        }

        LatLng pos = new LatLng(latitude,longitude);

        return pos;
    }

    */

    /*
    @Override
    public void onLocationChanged(Location location) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onProviderDisabled(String provider) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onProviderEnabled(String provider) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // TODO Auto-generated method stub

    }

    */
    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        return null;
    }
    /*
    public double getLatitude() {
        if (location != null){
            latitude = location.getLatitude();
        }
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        if (location != null){
            longitude = location.getLongitude();
        }

        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public boolean canGetLocation() {
        return this.canGetLocation;
    }

    public void stopUsingGPS() {
        if (locManager != null){
            //locManager.removeUpdates(MapsActivity.this);
        }

    }
    */
}

