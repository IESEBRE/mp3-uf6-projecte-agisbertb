package org.example.model.entities;

public class Revisio {
    private Long id;
    private String data;
    private String descripcio;
    private double preu;
    private Bici bici;

    public Revisio(String data, String descripcio, double preu, Bici bici) {
        this.data = data;
        this.descripcio = descripcio;
        this.preu = preu;
        this.bici = bici;
    }

    // Getters i setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getDescripcio() {
        return descripcio;
    }

    public void setDescripcio(String descripcio) {
        this.descripcio = descripcio;
    }

    public double getPreu() {
        return preu;
    }

    public void setPreu(double preu) {
        this.preu = preu;
    }

    public Bici getBici() {
        return bici;
    }

    public void setBici(Bici bici) {
        this.bici = bici;
    }

    @Override
    public String toString() {
        return bici.toString();
    }
}