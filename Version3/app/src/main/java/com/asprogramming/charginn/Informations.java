package com.asprogramming.charginn;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.lang.reflect.MalformedParameterizedTypeException;
import java.util.regex.Pattern;

import infos.Borne;
import infos.BorneAdapter;
import infos.Client;

public class Informations extends AppCompatActivity {

    public static String NUMEROTEL = "^[1-9][0-9]{9}";
    public static String NOM = "^[a-zA-Z]{1}[a-z]*";
    public static String NOMBRES = "^[1-8]{1}";
    public static String NUMERO = "^[1-9][0-9]{0,4}";
    public static String CODEPOSTAL = "[a-zA-Z]{1}[1-9]{1}" +
            "[a-zA-Z]{1}[-]?[0-9]{1}[a-zA-Z]{1}[0-9]{1}";
    public static String RUE = "[a-zA-Z]{2,}[-| ]?[a-zA-Z]*";
    public static String EMAIL = "^[a-zA-Z]{1}[a-zA-Z0-9]*[@]{1}[a-z0-9]{2,}[.]{1}[a-zA-Z]{2,4}";

    private EditText txtNom;
    private EditText txtnNumero;
    private EditText txtRue;
    private EditText txtCodepostal;
    private EditText txtNbs;
    private ImageView txtNiv;
    private Button btnOK;
    private Borne borne;
    private FloatingActionButton btnFav;
    private FloatingActionButton btnDesactiver;
    private boolean etaitFav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_informations);
        associer();

        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(champsValider()){
                    sauvergarder();
                }
            }
        });

        btnFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!borne.isFavori()){
                    ajoutFavori();
                    Toast.makeText(Informations.this,
                            "Cette borne a ete ajouter a vos favoris", Toast.LENGTH_SHORT).show();
                    Intent in = new Intent(Informations.this, MapsActivity.class);
                    startActivity(in);
                }else
                    Toast.makeText(Informations.this,
                            "Cette borne est deja dans vos favoris", Toast.LENGTH_SHORT).show();
            }
        });

        btnDesactiver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                desactiver();
                Intent in = new Intent(Informations.this, MapsActivity.class);
                startActivity(in);
            }
        });
    }

    private void sauvergarder() {
        borne.setNom(txtNom.getText().toString().trim());
        borne.setTelephone(txtnNumero.getText().toString().trim());
        borne.setNb(Integer.valueOf(txtNbs.getText().toString().trim()));
        MapsActivity.modificationBorne(borne);
        Intent in = new Intent(Informations.this, MapsActivity.class);
        startActivity(in);
    }

    private void associer() {
        txtNom = (EditText) findViewById(R.id.activity_infos_editText_nom);
        txtnNumero = (EditText) findViewById(R.id.activity_infos_editText_numero);
        txtNbs  = (EditText) findViewById(R.id.activity_infos_editText_nb);
        txtNiv = (ImageView) findViewById(R.id.activity_infos_imageView_niv);
        btnOK = (Button) findViewById(R.id.activity_infos_btn_ok);
        btnFav = (FloatingActionButton) findViewById(R.id.activity_infos_floatingBtn_fav);
        btnDesactiver = (FloatingActionButton) findViewById(R.id.activity_infos_floatingBtn_desactiver);
        mettreInfos();
    }

    private void mettreInfos() {
        borne = (Borne) getIntent().getSerializableExtra("borne");

        txtNiv.setImageResource(BorneAdapter.getImages(borne.getNiveau()-1));
        txtNom.setText(borne.getNom());
        //txtNiv.setText(Integer.toString(borne.getNiveau()));
        txtnNumero.setText(borne.getTelephone());
        txtNbs.setText(Integer.toString(borne.getNb()));
        if(Profile.signedIn){
            etaitFav = borne.isFavori();
            btnFav.setVisibility(View.VISIBLE);
        }else
            btnFav.setVisibility(View.GONE);
    }


    private void ajoutFavori() {
        borne.setFavori(1);
        Client c = Profile.getClient();
        MapsActivity.ajoutFav(c, borne);
    }

    private void desactiver() {
        borne.setActif(0);
        MapsActivity.enleverBorne(borne, etaitFav);
        Toast.makeText(Informations.this,
                "Cette borne est maintenant desactiver", Toast.LENGTH_SHORT).show();
    }

    /**
     * Methode qui verifie les champs de donnees
     * @param verifier
     * @param regex
     * @return
     */
    public static boolean nonValide(EditText verifier, String regex){
        String aVerifier = verifier.getText().toString().trim();
        if(aVerifier.isEmpty()){
            verifier.setError("Les informations ne peuvent etre vide");
            return true;
        }else if(!verifierEntre(aVerifier, regex)){
            verifier.setError("Les informations sont invalide");
            return true;
        }else{
            verifier.setError(null);
            return false;
        }
    }

    public static  boolean verifierEntre(String verifier, String regex){
        return Pattern.compile(regex).matcher(verifier).matches();
    }

    /**
     * Methode qui verifie les champs dentrees
     * @return
     */
    public boolean champsValider() {
        int compteur = 0;
        if(nonValide(txtNom,NOM)){
            compteur++;
        }
        if(nonValide(txtNbs, NOMBRES)){
            compteur++;
        }
        if(nonValide(txtnNumero, NUMEROTEL)){
            compteur++;
        }
        return compteur == 0;
    }

}
