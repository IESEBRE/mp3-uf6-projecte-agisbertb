package org.example.model.exceptions;

import java.util.HashMap;
import java.util.Map;

/**
 * Excepció personalitzada per gestionar errors específics relacionats amb les operacions de la base de dades.
 * Inclou codis d'error específics que descriuen diferents tipus d'errors que poden ocórrer durant les operacions de la base de dades.
 */

public class DAOException extends Exception {

    private static final Map<Integer, String> missatges = new HashMap<>();

    // Bloc d'inicialització estàtic per emplenar el mapa de missatges d'errors amb descripcions específiques.

    static {
        missatges.put(0, "Error al connectar a la BD!!");
        missatges.put(1, "Restricció d'integritat violada - clau primària duplicada");
        missatges.put(904, "Nom de columna no vàlid");
        missatges.put(936, "Falta expressió en l'ordre SQL");
        missatges.put(942, "La taula o la vista no existeix");
        missatges.put(1000, "S'ha superat el nombre màxim de cursors oberts");
        missatges.put(1400, "Inserció de valor nul en una columna que no permet nuls");
        missatges.put(1403, "No s'ha trobat cap dada");
        missatges.put(1722, "Ha fallat la conversió d'una cadena de caràcters a un número");
        missatges.put(1747, "El nombre de columnes de la vista no coincideix amb el nombre de columnes de les taules subjacents");
        missatges.put(4091, "Modificació d'un procediment o funció en execució actualment");
        missatges.put(6502, "Error numèric o de valor durant l'execució del programa");
        missatges.put(12154, "No s'ha pogut resoldre el nom del servei de la base de dades Oracle o l'identificador de connexió");
        missatges.put(2292, "S'ha violat la restricció d'integritat -  s'ha trobat un registre fill");

        // Missatges de validació

        missatges.put(11,"Nom no vàlid! Ha de començar per majúscula i seguir amb minúscules, sense números ni caràcters especials.");
        missatges.put(12,"Cognoms no vàlids! Ha de començar per majúscula i seguir amb minúscules, sense números ni caràcters especials.");
        missatges.put(13,"Telèfon no vàlid! Ha de començar per 6 o 9 i seguir amb 8 dígits més.");
        missatges.put(14,"Email no vàlid! Ha de tenir un format vàlid (propietari@example.com)");

        missatges.put(21, "La marca de la bicicleta és invàlida! Ha de començar per majúscula i seguir amb minúscules, sense números ni caràcters especials.");
        missatges.put(22, "El model de la bicicleta és invàlid! Ha de començar per majúscula i seguir amb minúscules, sense números ni caràcters especials.");
        missatges.put(23, "L'any de la bicicleta és invàlid! Ha de ser entre 1990 i l'any actual.");
        missatges.put(24, "El pes de la bicicleta és invàlid! Ha de ser un valor positiu.");

        missatges.put(31, "La data de la revisió és invàlida! El format correcte és yyyy-MM-dd");
        missatges.put(32, "La descripció de la revisió és invàlida! Ha de començar per majúscula i seguir amb minúscules.");
        missatges.put(33, "El preu de la revisió és invàlid! Ha de ser un valor positiu.");
    }

    private int tipo; // Codi d'error específic d'aquesta excepció.

    /**
     * Constructor per a excepcions amb un codi d'error, utilitzant un missatge predeterminat basat en el codi.
     *
     * @param tipo El codi d'error que identifica el tipus d'error ocorregut.
     */

    public DAOException(int tipo) {
        super(missatges.get(tipo));
        this.tipo = tipo;
    }

    /**
     * Constructor per a excepcions amb un codi d'error i un missatge d'error personalitzat.
     *
     * @param tipo El codi d'error que identifica el tipus d'error ocorregut.
     * @param message El missatge personalitzat que descriu l'error.
     */

    public DAOException(int tipo, String message) {
        super(message);
        this.tipo = tipo;
    }

    /**
     * Constructor per a excepcions amb un codi d'error, un missatge d'error personalitzat i una causa.
     *
     * @param tipo El codi d'error que identifica el tipus d'error ocorregut.
     * @param message El missatge personalitzat que descriu l'error.
     * @param cause La causa original de l'excepció (una excepció que va causar aquesta).
     */

    public DAOException(int tipo, String message, Throwable cause) {
        super(message, cause);
        this.tipo = tipo;
    }

    /**
     * Obté el codi d'error associat amb aquesta excepció.
     *
     * @return El codi d'error.
     */

    public int getTipo() {
        return tipo;
    }

    /**
     * Obté el missatge d'error associat amb el codi d'error d'aquesta excepció.
     *
     * @return El missatge d'error corresponent al codi d'error.
     */

    public String getCodi() {
        return missatges.get(tipo);
    }
}
