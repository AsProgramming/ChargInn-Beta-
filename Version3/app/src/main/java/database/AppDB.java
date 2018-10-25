package database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.asprogramming.charginn.MapsActivity;
import com.asprogramming.charginn.Profile;

import java.util.ArrayList;

import infos.Borne;
import infos.Client;

public class AppDB extends SQLiteOpenHelper {

    public static final String TAG = "Hello";



    private  Context context;
    private static AppDB db = null;
    private static final String DBNAME = "charg_inn";
    private static final String TABCLIENT = "client";
    private static final String TABHUB = "borne";
    private static final String TABFAV = "favoris";

    private static final String ID = "id";
    private static final String CXID = "cx_id";
    private static final String BORNEID = "borne_id";

    private static final String CLIENTNANE = "name";
    private static final String CLIENTMAX = "distanceMax";
    private static final String CLIENTMIN = "distanceMin";
    private static final String CLIENTNIVEAU = "niveau";
    private static final String CLIENTEMAIL = "email";
    private static final String CLIENTPSSWRD = "psswrd";

    private static final String CHARGNAME = "borne_name";
    private static final String CHARGLONG = "longitude";
    private static final String CHARGLAT = "latitude";
    private static final String CHARGMAX = "niveauMax";
    private static final String CHARGNB = "nombre";
    private static final String CHARGTEL = "telephone";
    private static final String CHARGACTIF = "actif";
    private static final String CHARGFAV = "favori";


    public static AppDB getInstance(Context mContext){
        if(db == null){
            db = new AppDB(mContext.getApplicationContext());
        }
        return db;
    }

    private AppDB(Context mContext){
        super(mContext, DBNAME, null, 1);

        context = mContext;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQLcreate(1));
        db.execSQL(SQLcreate(2));
        db.execSQL(SQLcreate(3));
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists " + DBNAME);
        onCreate(db);
    }

    private String SQLcreate(int indice){
        switch(indice){
            case 1:
                return "create table " + TABCLIENT + "(" + ID + " integer primary key autoincrement, " +
                        CLIENTNANE + " text, " +
                        CLIENTMAX + " integer, " +
                        CLIENTMIN + " integer, " +
                        CLIENTEMAIL + " text, " +
                        CLIENTPSSWRD + " text, " +
                        CLIENTNIVEAU + " integer)";
            case 2:
                return "create table " + TABHUB + "(" + ID + " integer primary key autoincrement, " +
                        CHARGNAME + " text, " +
                        CHARGLONG + " real, " +
                        CHARGLAT + " real, " +
                        CHARGMAX + " integer, " +
                        CHARGNB + " integer, " +
                        CHARGTEL + " text, " +
                        CHARGACTIF + " integer, " +
                        CHARGFAV + " integer)";
            default:
                return "create table " + TABFAV + "(" + BORNEID + " integer primary key, " +
                        CXID + " integer)";

        }
    }

    /**
     * Methode qui ajoute le client a la bd
     * @param c client
     */
    public void ajouterClient(Client c){
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = genererValues(c,null);

        long id = db.insert(TABCLIENT, null, values);
        Log.d(TAG, "ajouterClient: ");
        db.close();
    }

    /**
     * Methode qui ajoute la borne a la bd
     * @param b borne
     */
    public void ajouterBorne(Borne b){
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = genererValues(null, b);

        long id = db.insert(TABHUB, null, values);
        Log.d(TAG, "ajout Bornet: ");
        db.close();
    }

    public void ajouterFavoris(Client c, Borne borne){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(CXID, c.getId());
        values.put(BORNEID, borne.getId());
        long id = db.insert(TABFAV, null, values);
        Log.d(TAG, "ajout de favs: ");
        db.close();
    }

    /**
     * Methode qui prend un client/borne et qui le format en execution sql
     * @param c le client
     * @param b la borne
     * @return objet a entrer dans la bd
     */
    private ContentValues genererValues(Client c, Borne b) {
        ContentValues values = new ContentValues();
        if(c != null){
            values.put(CLIENTNANE, c.getNom());
            values.put(CLIENTMAX, c.getDistMax());
            values.put(CLIENTMIN, c.getDistMin());
            values.put(CLIENTEMAIL, c.getEmail());
            values.put(CLIENTPSSWRD, c.getPassword());
            values.put(CLIENTNIVEAU, c.getNiveau());
        }else{
            values.put(CHARGNAME, b.getNom());
            values.put(CHARGLONG, b.getLongitude());
            values.put(CHARGLAT, b.getLatitude());
            values.put(CHARGMAX, b.getNiveau());
            values.put(CHARGNB, b.getNb());
            values.put(CHARGTEL, b.getTelephone());
            values.put(CHARGACTIF, b.getActif());
            values.put(CHARGFAV, b.getFavori());
        }
        return values;
    }

    /**
     * Methode qui renvoi la liste de clients
     * @return liste de clients
     */
    public ArrayList<Client> getClients(){
        ArrayList<Client> lst = new ArrayList<>();

        SQLiteDatabase db = getWritableDatabase();//

        lst = remplirListe(db, lst);

        return lst;
    }

    /**
     * Methode qui renvoi la liste de bornes
     * @return liste de clients
     */
    public ArrayList<Borne> getBornes(){
        ArrayList<Borne> lst = new ArrayList<>();

        SQLiteDatabase db = getWritableDatabase();//

        lst = remplirListeBorne(db, lst);

        return lst;
    }

    public ArrayList<Borne> getFavoris(Client c){
        ArrayList<Borne> lst = new ArrayList<>();

        SQLiteDatabase db = getWritableDatabase();//

        lst = remplirFavoris(db, lst, c);

        return lst;
    }

    private ArrayList<Borne> remplirFavoris(SQLiteDatabase db, ArrayList<Borne> lst, Client c) {

        String request = "SELECT * FROM borne a " +
                "INNER JOIN favoris b ON a.id = b.borne_id WHERE b.cx_id=?";

        Cursor data = db.rawQuery(request, new String[]{c.getId()});
        if (data.moveToFirst()){
            do {
                Borne b = new Borne(data.getInt(0),
                        data.getString(1), data.getInt(4));
                b.setLongitude(data.getFloat(2));
                b.setLatitude(data.getFloat(3));
                b.setTelephone(data.getString(6));
                b.setActif(data.getInt(7));
                b.setFavori(data.getInt(8));
                if(b.isActif()){
                    lst.add(b);
                }
            } while(data.moveToNext());
        }
        data.close();
        db.close();
        return lst;
    }

    /**
     * Methode qui remplit la liste de clients dans la bd
     * @param db la base de donnees
     * @param lst la liste a remplir
     * @return la liste remplit
     */
    private ArrayList<Client> remplirListe(SQLiteDatabase db, ArrayList<Client> lst) {
        String request = "select * from " + TABCLIENT;
        Cursor data = db.rawQuery(request, null);

        if(data.moveToFirst()){
            do{
                Client c = new Client(data.getInt(0));
                c.setNom(data.getString(1));
                c.setEmail(data.getString(4));
                c.setPassword(data.getString(5));
                c.setDistMax(data.getInt(2));
                c.setDistMin(data.getInt(3));
                c.setNiveau(data.getInt(6));
                lst.add(c);
            }while(data.moveToNext());
        }
        data.close();
        db.close();
        return lst;
    }

    /**
     * Methode qui remplit la liste de bornes
     * @param db la bd
     * @param lst la liste a remplir
     * @return la liste remplit
     */
    private ArrayList<Borne> remplirListeBorne(SQLiteDatabase db, ArrayList<Borne> lst) {
        String request = "select * from " + TABHUB;
        Cursor data = db.rawQuery(request, null);

        if(data.moveToFirst()){
            do{
                Borne borne = new Borne(data.getInt(0),
                        data.getString(1), data.getInt(4));
                borne.setLongitude(data.getFloat(2));
                borne.setLatitude(data.getFloat(3));
                borne.setNb(data.getInt(5));
                borne.setTelephone(data.getString(6));
                borne.setActif(data.getInt(7));
                borne.setFavori(data.getInt(8));
                if(borne.isActif()){
                    lst.add(borne);
                }
            }while(data.moveToNext());
        }
        data.close();
        db.close();
        return lst;
    }

    private ArrayList<Borne> remplirListeFavoris(SQLiteDatabase db, ArrayList<Borne> lst) {
        String request = "select * from " + TABHUB;
        Cursor data = db.rawQuery(request, null);

        if(data.moveToFirst()){
            do{
                Borne borne = new Borne(data.getInt(0),
                        data.getString(1), data.getInt(4));
                borne.setLongitude(data.getFloat(2));
                borne.setLatitude(data.getFloat(3));
                borne.setNb(data.getInt(5));
                borne.setTelephone(data.getString(6));
                borne.setActif(data.getInt(7));
                borne.setFavori(data.getInt(8));
                if(borne.isActif()){
                    lst.add(borne);
                }
            }while(data.moveToNext());
        }
        data.close();
        db.close();
        return lst;
    }

    /**
     * Methode qui ajuste le client
     * @param c les nouveaux changements
     * @return si a ete changer
     */
    public boolean ajusterClient(Client c){
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = genererValues(c,null);

        db.update(TABCLIENT, values, ID +" =?", new String[]{c.getId()});
        db.close();
        return true;
    }

    /**
     * Methode qui ajuste la borne
     * @param borne les nouveaux changements
     * @return si a ete changer
     */
    public boolean ajusterBorne(Borne borne, boolean etaitFavori){
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = genererValues(null, borne);

        db.update(TABHUB, values, ID +" =?", new String[]{borne.getId()});
        if(etaitFavori){
            envleverFavori(borne);
        }
        db.close();
        return true;
    }

    public Client verifierClient(String infos){
        String[] verifier = infos.split(" ");
        Client c = null;
        SQLiteDatabase db = getWritableDatabase();

        String request = "SELECT * FROM client WHERE email=? and psswrd=?";

        Cursor data = db.rawQuery(request, new String[]{verifier[0], verifier[1]});
        if(data.moveToFirst()){
            do{
                c = new Client();
                c.setId(data.getInt(0));
                c.setNom(data.getString(1));
                c.setDistMax(data.getInt(2));
                c.setDistMin(data.getInt(3));
                c.setEmail(data.getString(4));
                c.setPassword(data.getString(5));
                c.setNiveau(data.getInt(6));
            }while(data.moveToNext());
        }
        return c;
    }

    /**
     * Methode qui supprime le client
     * @param id l'identifiant
     * @return si le contact a ete supprimer
     */
    public boolean supprimerClient(String id){
        SQLiteDatabase db = getWritableDatabase();
        int rows = db.delete(TABCLIENT, ID + " =?", new String[]{id});
        db.close();
        return true;
    }

    public boolean envleverFavori(Borne infos){
        SQLiteDatabase db = getWritableDatabase();
        int rows = db.delete(TABFAV, ID + " =?", new String[]{infos.getId()});
        db.close();
        return true;
    }

    public void dropAll(){
        SQLiteDatabase db = getWritableDatabase();
/*        String request = "delete from " + TABCLIENT;
        String request2 = "delete from " + TABHUB;
        String request3 = "delete from " + TABFAV;*/
        db.delete(TABCLIENT, null, null);
        db.delete(TABHUB, null, null);
        db.delete(TABFAV, null, null);
        db.close();
    }

}
