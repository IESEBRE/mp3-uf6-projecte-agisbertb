package org.example.model.entities;

public class Revisio {
    private String data;
    private String descripcio;

    public Revisio(String data, String descripcio) {
        this.data = data;
        this.descripcio = descripcio;
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
}
