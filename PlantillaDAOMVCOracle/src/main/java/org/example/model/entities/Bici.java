package org.example.model.entities;

import java.util.ArrayList;
import java.util.List;

public class Bici {

    public enum TipoBici {
        MTB, CARRETERA, GRAVEL, EBIKE
    }

    public enum Carboni {
        SI, NO
    }

    private Long id;
    private String marca;
    private String modelBici;
    private int anyFabricacio;
    private double pes;
    private TipoBici tipo;
    private Carboni carboni;
    private Propietari propietari;
    private List<Revisio> revisions;

    public Bici(String marca, String modelBici, int anyFabricacio, double pes, TipoBici tipo, Carboni carboni, Propietari propietari) {
        this.marca = marca;
        this.modelBici = modelBici;
        this.anyFabricacio = anyFabricacio;
        this.pes = pes;
        this.tipo = tipo;
        this.carboni = carboni;
        this.propietari = propietari;
        this.revisions = new ArrayList<>();
    }

    // Getters
    public Long getId() {
        return id;
    }

    public String getMarca() {
        return marca;
    }

    public String getModelBici() {
        return modelBici;
    }

    public int getAnyFabricacio() {
        return anyFabricacio;
    }

    public double getPes() {
        return pes;
    }

    public TipoBici getTipo() {
        return tipo;
    }

    public Carboni getCarboni() {
        return carboni;
    }

    public Propietari getPropietari() {
        return propietari;
    }

    public List<Revisio> getRevisions() {
        return revisions;
    }

    // Setters
    public void setId(Long id) {
        this.id = id;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public void setModelBici(String modelBici) {
        this.modelBici = modelBici;
    }

    public void setAnyFabricacio(int anyFabricacio) {
        this.anyFabricacio = anyFabricacio;
    }

    public void setPes(double pes) {
        this.pes = pes;
    }

    public void setTipo(TipoBici tipo) {
        this.tipo = tipo;
    }

    public void setCarboni(Carboni carboni) {
        this.carboni = carboni;
    }

    public void setPropietari(Propietari propietari) {
        this.propietari = propietari;
    }

    public void setRevisions(List<Revisio> revisions) {
        this.revisions = revisions;
    }

    @Override
    public String toString() {
        return marca + " " + modelBici;
    }
}
