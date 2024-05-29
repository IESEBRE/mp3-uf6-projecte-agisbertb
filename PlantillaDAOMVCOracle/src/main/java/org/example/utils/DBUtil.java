package org.example.utils;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Utilitat per a la gestió de connexions a la base de dades.
 *
 * Aquesta classe proporciona una manera centralitzada d'obtenir connexions a la base de dades
 * utilitzant propietats carregades des d'un fitxer de configuració.
 */

public class DBUtil {
    private static final Properties properties = new Properties();

    // Bloc d'inicialització estàtic per carregar les propietats de configuració de la base de dades.

    static {
        try (InputStream input = DBUtil.class.getClassLoader().getResourceAsStream("db.properties")) {
            if (input == null) {
                throw new RuntimeException("Fitxer de propietats no trobat");
            }
            properties.load(input);
        } catch (IOException e) {

            // Rellança l'excepció com una excepció d'inicialització no comprovada per alertar de l'error de configuració.

            throw new RuntimeException("Error carregant el fitxer de propietats de la base de dades", e);
        }
    }

    /**
     * Obtenir una connexió a la base de dades basada en les propietats especificades.
     *
     * Utilitza les propietats carregades per establir una connexió amb la base de dades.
     * Aquest mètode pot llençar una SQLException si la connexió no es pot establir.
     *
     * @return Una nova connexió a la base de dades.
     * @throws SQLException Si ocorre un error al obtenir la connexió a la base de dades.
     */

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(
                properties.getProperty("db.url"),
                properties.getProperty("db.user"),
                properties.getProperty("db.password")
        );
    }
}
