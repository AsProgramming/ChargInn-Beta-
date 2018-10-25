package com.asprogramming.charginn;

import android.app.ListActivity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import infos.Borne;
import infos.BorneAdapter;

public class ListerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lister);

        Boolean bon = getIntent().getBooleanExtra("choix", false);
        ArrayList<Borne> bornes = MapsActivity.getBornes(bon);

        Collections.sort(bornes, new Comparator<Borne>() {
            @Override
            public int compare(Borne b1, Borne b2) {
                return b1.getNom().compareToIgnoreCase(b2.getNom());
            }
        });

        ListView lesBornes = (ListView) findViewById(R.id.activity_lister_listview_afficherBornes);

        BorneAdapter adapteur = new BorneAdapter(this, bornes);

        lesBornes.setAdapter(adapteur);

        lesBornes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //position
            }
        });
    }


}
