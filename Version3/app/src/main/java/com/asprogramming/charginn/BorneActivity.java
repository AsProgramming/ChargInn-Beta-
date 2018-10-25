package com.asprogramming.charginn;

import android.content.Intent;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import infos.Borne;
import infos.Client;

public class BorneActivity extends AppCompatActivity {

    private String aVerifier;
    private EditText txtNom;
    private EditText txtCivic;
    private EditText txtRue;
    private EditText txtCP;
    private EditText txtTel;
    Spinner spinnerNb;
    Spinner spinnerLvl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_borne);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        invalidateOptionsMenu();

        spinnerNb = (Spinner) findViewById(R.id.activity_borne_spinner_nb);
        spinnerLvl = (Spinner) findViewById(R.id.activity_borne_spinner_level);
        txtNom = (EditText) findViewById(R.id.activity_borne_editText_nom);
        txtCivic = (EditText) findViewById(R.id.activity_borne_editText_numero);
        txtCP = (EditText) findViewById(R.id.activity_borne_editText_code);
        txtRue = (EditText) findViewById(R.id.activity_borne_editText_rue);
        txtTel = (EditText) findViewById(R.id.activity_borne_editText_tel);

        ArrayAdapter<CharSequence> adapteur = ArrayAdapter.createFromResource(this,
                R.array.nombres, android.R.layout.simple_spinner_item);
        adapteur.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerNb.setAdapter(adapteur);

        ArrayAdapter<CharSequence> adapteurNiveau = ArrayAdapter.createFromResource(this,
                R.array.niveau, android.R.layout.simple_spinner_item);
        adapteurNiveau.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerLvl.setAdapter(adapteurNiveau);

        Button btnAjout = (Button) findViewById(R.id.activity_borne_btnAjout);

        btnAjout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(champsValider()){
                    verifierAddress();
                }
            }
        });

        spinnerLvl.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }

    /**
     * Methode qui verifie les champs dentrees
     * @return
     */
    public boolean champsValider() {
        int compteur = 0;

        if(Informations.nonValide(txtNom, Informations.NOM)){
            compteur++;
        }
        if(Informations.nonValide(txtCivic, Informations.NUMERO)){
            compteur++;
        }
        if(Informations.nonValide(txtCP, Informations.CODEPOSTAL)){
            compteur++;
        }
        if(Informations.nonValide(txtRue, Informations.RUE)){
            compteur++;
        }
        if(Informations.nonValide(txtTel, Informations.NUMEROTEL)){
            compteur++;
        }
        return compteur == 0;
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
            Intent in = new Intent(this, Profile.class);
            Client c = Profile.getClient();
            in.putExtra("client", c);
            startActivity(in);
        }else{
            Intent in  = new Intent(this, MapsActivity.class);
            startActivity(in);
        }
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        try {
            menu.findItem(R.id.action_signin).setVisible(true);
            menu.findItem(R.id.action_borne).setVisible(false);
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    private void verifierAddress() {
        Geocoder coder = new Geocoder(getApplicationContext(), Locale.getDefault());

        aVerifier = txtCivic.getText().toString().trim()
                + " " + txtRue.getText().toString().trim() + " " + txtCP.getText().toString().trim();
        try {

            List<Address> add = coder.getFromLocationName(aVerifier, 1);

            if(add.size() > 0){
                addBorne(add);
                Toast.makeText(this, "La borne est maintenant disponible",
                        Toast.LENGTH_LONG).show();
                Intent in = new Intent(this, MapsActivity.class);
                startActivity(in);
            }else{
                Toast.makeText(this,"L'addresse est invalide",
                        Toast.LENGTH_SHORT).show();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void addBorne(List<Address>  add) {
        Borne nouv = new Borne();
        nouv.setNom(txtNom.getText().toString().trim());
        nouv.setNb(spinnerNb.getSelectedItemPosition()+1);
        nouv.setTelephone(txtTel.getText().toString().trim());
        nouv.setNiveau(spinnerLvl.getSelectedItemPosition()+1);
        nouv.setLatitude(add.get(0).getLatitude());
        nouv.setLongitude(add.get(0).getLongitude());
        nouv.setActif(1);
        MapsActivity.ajoutBorne(nouv);
    }

}
