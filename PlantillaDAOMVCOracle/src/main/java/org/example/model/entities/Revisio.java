package org.example.model.entities;

/**
 * Representa una revisió realitzada a una bicicleta, incloent detalls com la data, descripció, preu, i la bicicleta associada.
 */

public class Revisio {
    private Long id;
    private String data;
    private String descripcio;
    private double preu;
    private Bici bici;

    /**
     * Constructor per crear una revisió amb els detalls específics.
     *
     * @param data La data en què es va realitzar la revisió.
     * @param descripcio Una descripció del que es va fer durant la revisió.
     * @param preu El cost de la revisió.
     * @param bici L'objecte Bici al qual pertany aquesta revisió.
     */

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


    /**
     * Retorna una representació en cadena de la revisió, utilitzant la representació en cadena de la bicicleta associada.
     *
     * @return Una cadena que representa la revisió, mostrant la bicicleta associada.
     */

    @Override
    public String toString() {
        return bici.toString();
    }
}