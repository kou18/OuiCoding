package com.example.ouicoding.Service;

public class ContratResponse {
    private int id;
    private String type;
    private String debut;
    private String fin;
    private int iduser;
    private float joursConge;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDebut() {
        return debut;
    }

    public void setDebut(String debut) {
        this.debut = debut;
    }

    public String getFin() {
        return fin;
    }

    public void setFin(String fin) {
        this.fin = fin;
    }

    public int getIduser() {
        return iduser;
    }

    public void setIduser(int iduser) {
        this.iduser = iduser;
    }

    public float getJoursConge() {
        return joursConge;
    }

    public void setJoursConge(int joursCongé) {
        this.joursConge = joursCongé;
    }
}
