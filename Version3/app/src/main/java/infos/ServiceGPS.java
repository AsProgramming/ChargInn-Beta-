package infos;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;


public class ServiceGPS extends Service implements LocationListener {

    private Context mContext;
    private boolean gpsActif;
    private boolean reseauActif;
    private  static boolean nonLocaliser;
    private Location position;
    private LocationManager posManager;


    public ServiceGPS(Context context){
        mContext = context;
    }

    public Location getPosition(){
        try{
            posManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
            gpsActif = posManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            reseauActif = posManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if(ContextCompat.checkSelfPermission(mContext,
                    Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                    || ContextCompat.checkSelfPermission(mContext,
                    Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                    if(reseauActif && position == null){
                    posManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,0,
                            0,this);
                        if(posManager!=null){
                            position = posManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        }
                    }
                else if(gpsActif && position == null){
                    posManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,1,
                            1,this);
                    if(posManager!=null){
                        position = posManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        if(position == null){
            nonLocaliser = true;
            position = new Location("dummyprovier");
            position.setLatitude(45.5381464);
            position.setLongitude(-73.6756627);
        }
        return position;
    }

    public static boolean verifierAddrress(String infos){

        return true;
    }

    public static boolean nonLocaliser(){
        return nonLocaliser;
    }

    @Override
    public void onLocationChanged(Location location) {
        position.setLatitude(location.getLatitude());
        position.setLongitude(location.getLongitude());
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
