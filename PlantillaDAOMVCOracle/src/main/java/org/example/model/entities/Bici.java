package org.example.model.entities;

import java.util.ArrayList;
import java.util.List;

/**
 * Representa una bicicleta amb atributs específics i relacions associades amb propietaris i revisions.
 *
 * La classe inclou diferents tipus de bicicleta i si està feta de carboni o no,
 * a més de mantenir una llista de revisions associades.
 */

public class Bici {

    /**
     * Enumera els possibles tipus de bicicleta.
     */

    public enum TipoBici {
        MTB, CARRETERA, GRAVEL, EBIKE
    }

    /**
     * Enumera les opcions de construcció amb carboni de la bicicleta.
     */

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


    /**
     * Constructor per a crear una instància de Bici amb detalls específics.
     *
     * @param marca Marca de la bicicleta.
     * @param modelBici Model de la bicicleta.
     * @param anyFabricacio Any de fabricació de la bicicleta.
     * @param pes Pes de la bicicleta en kilograms.
     * @param tipo Tipus de bicicleta.
     * @param carboni Indica si la bicicleta està feta de carboni.
     * @param propietari El propietari de la bicicleta.
     */

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

    /**
     * Retorna una representació en cadena de la bicicleta, mostrant la marca i el model.
     *
     * @return Una cadena que representa la bicicleta amb la seva marca i model.
     */

    @Override
    public String toString() {
        return marca + " " + modelBici;
    }
}
