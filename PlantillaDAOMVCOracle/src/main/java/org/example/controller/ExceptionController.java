package org.example.controller;

import org.example.model.exceptions.DAOException;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.JOptionPane;

/**
 * Controlador d'excepcions que gestiona els canvis de propietats relacionats amb errors.
 *
 * Aquest controlador escolta els esdeveniments de canvi de propietat específics per a excepcions
 * i mostra un diàleg d'error amb la informació pertinent quan es produeix un canvi.
 *
 * @author Andreu Gisbert Bel
 * @version 1.0
 */

public class ExceptionController implements PropertyChangeListener {
    public static final String PROP_EXCEPCIO = "excepcio";

    /**
     * Gestiona els canvis de propietat quan es llança una excepció.
     *
     * Aquest mètode es crida automàticament quan una propietat de l'objecte observat canvia.
     * Si la propietat canviada és 'excepcio', es mostra un diàleg d'error.
     *
     * @param evt l'esdeveniment de canvi de propietat que conté la informació de la propietat canviada i el nou valor.
     */

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (PROP_EXCEPCIO.equals(evt.getPropertyName())) {
            DAOException e = (DAOException) evt.getNewValue();
            JOptionPane.showMessageDialog(null, "Codi d'error: " + e.getTipo() + "\nMissatge: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
