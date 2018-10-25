package com.asprogramming.charginn;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

import database.AppDB;
import infos.Borne;
import infos.BorneAdapter;
import infos.Client;
import infos.ServiceGPS;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private final int LOCATION_CODE = 1234;
    private final float DEFAULT_ZOOM = 12.5f;
    private static AppDB db;
    private FrameLayout infoBox;
    private TextView nomBorne;
    private TextView numTel;
    private Button btnPlusInfos;
    private FloatingActionButton btnDetails;
    private GoogleMap mMap;
    private Location location;
    private ServiceGPS position;
    private static ArrayList<Borne> lstBornes;
    private static ArrayList<Borne> lstFavs;
    private Client client;
    private Borne borne;
    private boolean visible;
    private boolean permission;
    private boolean counteur;
    ImageView imgNiv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        imgNiv = (ImageView) findViewById(R.id.activity_maps_imageView_niveau);
        infoBox = (FrameLayout) findViewById(R.id.map_info);
        nomBorne = (TextView) findViewById(R.id.activity_maps_textView_nom);
        numTel = (TextView) findViewById(R.id.activity_maps_textView_tel);
        btnDetails = (FloatingActionButton) findViewById(R.id.activity_maps_floatingActionBtn_fav);
        btnPlusInfos = (Button) findViewById(R.id.activity_maps_btn_details);

        position = new ServiceGPS(this);
        db = AppDB.getInstance(this);

        invalidateOptionsMenu();
/* FOR TESTING PURPOSES ONLY

        if(!counteur){
            db.dropAll();
            counteur = true;
            Client test = new Client("abc@abc.com", "a", "nom", 100, 80, 2);
            Borne borne4 = new Borne("Mcharge", -73.6603913, 45.5364653, 2, 4, "5148225656", true, false);
            Borne borne2 = new Borne("Bdebcity", -73.673061, 45.536482, 1, 4, "5142889699", true, false);
            Borne borne3 = new Borne("Hq", -73.6282682, 45.5353481, 3, 4, "5142889699", true, false);

            db.ajouterClient(test);
            db.ajouterBorne(borne4);
            db.ajouterBorne(borne2);
            db.ajouterBorne(borne3);
        }
*/


       //
        lstBornes = db.getBornes();

        obtenirPermission();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int res_id = item.getItemId();
        if(res_id == R.id.action_signin){
            Client c = null;
            if(Profile.signedIn){
                Intent in = new Intent(MapsActivity.this, Profile.class);
                c = Profile.getClient();
                in.putExtra("client", c);
                startActivity(in);
            }
            else{
                Intent in = new Intent(MapsActivity.this, SignIn.class);
                in.putExtra("client", c);
                startActivity(in);
            }

        }else if(res_id == R.id.action_borne){
            Intent in = new Intent(this, BorneActivity.class);
            startActivity(in);
        }
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        try {
            if(Profile.signedIn){
                menu.findItem(R.id.action_borne).setVisible(true);
            }else{
                menu.findItem(R.id.action_borne).setVisible(false);
            }
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mettreBornes();

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                infoBox.setVisibility(View.GONE);
            }
        });

            mMap.setOnCameraMoveStartedListener(new GoogleMap.OnCameraMoveStartedListener() {
                @Override
                public void onCameraMoveStarted(int i) {
                    if (i == GoogleMap.OnCameraMoveStartedListener.REASON_GESTURE) {
                              infoBox.setVisibility(View.GONE);
                    }
                }
            });


        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                Integer id = (Integer) marker.getTag();
                borne = lstBornes.get(id);
                imgNiv.setImageResource(BorneAdapter.getImages(lstBornes.get(id).getNiveau() - 1));
                nomBorne.setText(lstBornes.get(id).getNom());
                numTel.setText(lstBornes.get(id).getTelephone());
                if(Profile.signedIn && lstFavs.size() > 0){
                    for (int i = 0; i < lstFavs.size(); i++) {
                        if(lstFavs.get(i).getId().equals(borne.getId())){
                            btnDetails.show();
                        }
                    }
                }
                infoBox.setVisibility(View.VISIBLE);
                return false;
            }
        });

        btnPlusInfos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(MapsActivity.this, Informations.class);
                in.putExtra("borne", borne);
                startActivity(in);
            }
        });
    }
    /**
     * Methode qui associe met les bornes
     */
    private void mettreBornes() {

        mMap.getUiSettings().setMapToolbarEnabled(false);

        for (int i = 0; i < lstBornes.size(); i++) {
            if(lstBornes.get(i).isActif()){
                LatLng marque = new LatLng(lstBornes.get(i).getLatitude(),
                        lstBornes.get(i).getLongitude());
                Marker marker = mMap.addMarker(new MarkerOptions().position(marque)
                        .icon(BitmapDescriptorFactory.fromResource(BorneAdapter.
                                getImages(lstBornes.get(i).getNiveau() - 1))));
                marker.setTag(i);
            }
        }
        bougerCamera(new LatLng(location.getLatitude(), location.getLongitude()), DEFAULT_ZOOM);
    }
    /**
     * Methode quifait le map
     */
    private void initMap() {
        location = position.getPosition();
        if(ServiceGPS.nonLocaliser()){
            Toast.makeText(this, "Votre position n<a pu etre localiser",
                    Toast.LENGTH_SHORT).show();
        }
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        getSupportActionBar().setTitle("Charg-Inn");
        getSupportActionBar().setIcon(R.drawable.imageedit_1);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
    }

    @SuppressLint("MissingPermission")
    private void bougerCamera(LatLng pos, float zoom){
        mMap.setMyLocationEnabled(true);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(pos, zoom));
    }
    /**
     * Methode qui obteient erm user
     */
    private void obtenirPermission() {
        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION};
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_COARSE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED) {
            permission = true;
            initMap();

        } else {
            ActivityCompat.requestPermissions(this, permissions, LOCATION_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        permission = false;
        switch (requestCode) {
            case LOCATION_CODE:
                verifierPermissions(grantResults);
                break;
        }
    }
    /**
     * Methode qui verifie les permission de luser
     */
    private void verifierPermissions(int[] grantResults) {
        int addition = 0;
        if (grantResults.length > 0) {
            for (int i = 0; i < grantResults.length; i++) {
                if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                    addition++;
                }
            }
            if(addition == grantResults.length){
                finish();
            }else{
                permission = true;
                initMap();
            }
        }
    }

    public static Client verifierUser(String infos){
        return db.verifierClient(infos);
    }

    public static void ajoutUser(Client c){
         db.ajouterClient(c);
    }

    public static void ajoutFav(Client c, Borne b){
        db.ajouterFavoris(c, b);
    }

    public static ArrayList getBornes(boolean fav){ return fav ? lstFavs : lstBornes; }

    public static void enleverBorne(Borne b, boolean etaitFav){  db.ajusterBorne(b, etaitFav); }

    public static void getFavs(Client c){
        lstFavs = db.getFavoris(c);
    }

    public static void modificationBorne(Borne b){
        db.ajusterBorne(b, b.isFavori());
    }

    public static void ajoutBorne(Borne b){
        db.ajouterBorne(b);
    }

    public void rafraichirLst(){
        lstBornes = db.getBornes();
    }

}
