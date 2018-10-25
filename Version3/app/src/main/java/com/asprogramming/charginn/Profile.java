package com.asprogramming.charginn;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import infos.Client;

public class Profile extends AppCompatActivity {

    public static boolean signedIn;
    private static Client client;
    private Button btnOK;
    private Button btnLstAlpha;
    private Button btnLstFav;
    private EditText editNom;
    private EditText editEmailCreer;
    private EditText editPassCreer;
    private TextView titreSorte;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        associer();
        invalidateOptionsMenu();
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
        if(res_id == R.id.action_borne){

            Intent in = new Intent(this, BorneActivity.class);
            startActivity(in);
        }else{
            Intent in = new Intent(this, MapsActivity.class);
            startActivity(in);
        }
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        try {
            if(signedIn){
                menu.findItem(R.id.action_signin).setVisible(false);
                menu.findItem(R.id.action_borne).setVisible(true);
            }else{
                menu.findItem(R.id.action_signin).setVisible(false);
                menu.findItem(R.id.action_borne).setVisible(false);
            }
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        return true;
    }
    /**
     * Methode qui associe les items
     */
    private void associer() {
        editNom = (EditText) findViewById(R.id.profile_activity_editText_afficheNom);
        editEmailCreer = (EditText) findViewById(R.id.profile_activity_editText_afficheEmail);
        editPassCreer = (EditText) findViewById(R.id.profile_activity_editText_creerPass);
        btnOK = (Button) findViewById(R.id.profile_activity_btn_ok);
        btnLstAlpha = (Button) findViewById(R.id.profile_activity_btn_listerAlpha);
        btnLstFav = (Button) findViewById(R.id.profile_activity_btn_listerFav);
        titreSorte = (TextView) findViewById(R.id.profile_activity_textView_annonce);
        setListenners();
    }

    private void setListenners() {
        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(verifierChamps()){
                    creerClient();
                };
            }
        });

        btnLstAlpha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(Profile.this, ListerActivity.class);
                in.putExtra("choix",false);
                startActivity(in);
            }
        });

        btnLstFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(Profile.this, ListerActivity.class);
                in.putExtra("choix",true);
                startActivity(in);
            }
        });
        verifierPaquet();
    }

    private void verifierPaquet() {
        client = (Client) getIntent().getSerializableExtra("client");
        if(client != null){
            montrerProfile();
        }
    }

    private boolean verifierChamps() {
        int compteur = 0;

        if(Informations.nonValide(editNom, Informations.NOM)){
            compteur++;
        }
        if(Informations.nonValide(editEmailCreer, Informations.EMAIL)){
            compteur++;
        }
        if(Informations.nonValide(editNom, Informations.NOM)){
            compteur++;
        }
        return compteur == 0;
    }

    private void montrerProfile() {
        invalidateOptionsMenu();
        signedIn = true;
        btnOK.setVisibility(View.GONE);
        LinearLayout lay = (LinearLayout) findViewById(R.id.layout_lister);
        lay.setVisibility(View.VISIBLE);
        titreSorte.setVisibility(View.VISIBLE);
        editPassCreer.setVisibility(View.GONE);
        editEmailCreer.setText(client.getEmail());
        editEmailCreer.setEnabled(false);
        editNom.setText(client.getNom());
        MapsActivity.getFavs(getClient());
    }

    private void creerClient() {
        Client c = new Client(editEmailCreer.getText().toString().trim(),
                editPassCreer.getText().toString(), editNom.getText().toString().trim());
        MapsActivity.ajoutUser(c);
        Intent in = new Intent(this, SignIn.class);
        startActivity(in);
    }

    public static Client getClient(){
        return client;
    }
}
