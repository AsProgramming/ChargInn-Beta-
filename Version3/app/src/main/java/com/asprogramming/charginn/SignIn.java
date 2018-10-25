package com.asprogramming.charginn;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.menu.ActionMenuItem;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import infos.Client;

public class SignIn extends AppCompatActivity {

    private LinearLayout connection;
    private EditText editEmail;
    private EditText editPsswrd;
    private Button btnSignin;
    private Button btnIns;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        invalidateOptionsMenu();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        associer();
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
            menu.findItem(R.id.action_signin).setVisible(false);
            menu.findItem(R.id.action_borne).setVisible(false);
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
        connection = (LinearLayout) findViewById(R.id.layout_signin);
        editEmail = (EditText) findViewById(R.id.signin_activity_editText_email);
        editPsswrd = (EditText) findViewById(R.id.signin_activity_editText_psswrd);
        btnSignin= (Button) findViewById(R.id.signin_activity_btn_signin);
        btnIns = (Button) findViewById(R.id.signin_activity_btn_signup);
        setListenners();
    }

    private void setListenners() {
        btnSignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(champsValider()){
                    essaiConnection();
                }
            }
        });

        btnIns.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inscrire();
            }
        });
    }

    private void inscrire() {
        Intent in = new Intent(this, Profile.class);
        Client c = null;
        in.putExtra("client", c);
        startActivity(in);
    }

    private void essaiConnection() {
        String infos = editEmail.getText().toString()+ " " + editPsswrd.getText().toString();
        Client client = MapsActivity.verifierUser(infos);
        if(client != null){
            Intent in = new Intent(this, Profile.class);
            in.putExtra("client", client);
            startActivity(in);
        }else{
            Toast.makeText(this, "Mauvais email/password",Toast.LENGTH_SHORT).show();
        }
    }
    /**
     * Methode qui verifie les champs dentrees
     * @return
     */
    public boolean champsValider() {
        int compteur = 0;
        if(Informations.nonValide(editEmail, Informations.EMAIL)){
            compteur++;
        }
        if(editPsswrd.getText().toString().equals("")){
            compteur++;
        }
        return compteur == 0;
    }

}
