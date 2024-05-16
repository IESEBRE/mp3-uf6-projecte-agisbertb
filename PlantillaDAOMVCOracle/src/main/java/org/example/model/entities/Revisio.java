package org.example.model.entities;

public class Revisio {
    private Long id;
    private String data;
    private String descripcio;
    private Long biciId;

    public Revisio(String data, String descripcio, Long biciId) {
        this.data = data;
        this.descripcio = descripcio;
        this.biciId = biciId;
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

    public Long getBiciId() {
        return biciId;
    }

    public void setBiciId(Long biciId) {
        this.biciId = biciId;
    }
}
