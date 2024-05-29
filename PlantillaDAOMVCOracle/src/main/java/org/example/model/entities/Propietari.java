package org.example.model.entities;

/**
 * Representa un propietari d'una bicicleta, amb detalls personals com el nom, cognoms, telèfon i email.
 */

public class Propietari {
    private Long id;
    private String nom;
    private String cognoms;
    private String telefon;
    private String email;

    /**
     * Constructor que crea un propietari sense identificador especificat.
     *
     * @param nom El nom del propietari.
     * @param cognoms Els cognoms del propietari.
     * @param telefon El número de telèfon del propietari.
     * @param email L'adreça de correu electrònic del propietari.
     */

    public Propietari(String nom, String cognoms, String telefon, String email) {
        this.nom = nom;
        this.cognoms = cognoms;
        this.telefon = telefon;
        this.email = email;
    }

    /**
     * Constructor que crea un propietari amb un identificador especificat.
     *
     * @param id L'identificador únic del propietari.
     * @param nom El nom del propietari.
     * @param cognoms Els cognoms del propietari.
     * @param telefon El número de telèfon del propietari.
     * @param email L'adreça de correu electrònic del propietari.
     */

    public Propietari(Long id, String nom, String cognoms, String telefon, String email) {
        this.id = id;
        this.nom = nom;
        this.cognoms = cognoms;
        this.telefon = telefon;
        this.email = email;
    }

    // Getters i Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getCognoms() {
        return cognoms;
    }

    public void setCognoms(String cognoms) {
        this.cognoms = cognoms;
    }

    public String getTelefon() {
        return telefon;
    }

    public void setTelefon(String telefon) {
        this.telefon = telefon;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Retorna una representació en cadena del propietari, combinant nom i cognoms.
     *
     * @return Una cadena que representa el propietari amb el seu nom complet.
     */

    @Override
    public String toString() {
        return nom + " " + cognoms;
    }
}
