package infos;

import java.io.Serializable;

public class Client implements Serializable {

    private int id;
    private String email;
    private String password;
    private String nom;
    private int distMax;
    private int distMin;
    private int niveau;


    public Client(){ }

    public Client(String email, String password, String nom, int distMax, int distMin, int niveau) {
        this.email = email;
        this.password = password;
        this.nom = nom;
        this.distMax = distMax;
        this.distMin = distMin;
        this.niveau = niveau;
    }

    public Client(int Lid) {
        id = Lid;
    }

    public Client(String email, String password, String leNom) {
        nom = leNom;
        this.email = email;
        this.password = password;
    }

    public Client(int Lid, String leNom, String email, String password) {
        id = Lid;
        this.email = email;
        this.password = password;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setDistMax(int distMax) {
        this.distMax = distMax;
    }

    public void setDistMin(int distMin) {
        this.distMin = distMin;
    }

    public void setNiveau(int niveau) {
        this.niveau = niveau;
    }

    public String getId() {
        return Integer.toString(id);
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getNom() {
        return nom;
    }

    public int getDistMax() {
        return distMax;
    }

    public int getDistMin() {
        return distMin;
    }

    public int getNiveau() {
        return niveau;
    }
}

