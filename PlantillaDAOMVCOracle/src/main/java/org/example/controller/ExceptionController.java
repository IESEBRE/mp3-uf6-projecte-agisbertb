package org.example.controller;

import org.example.model.exceptions.DAOException;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JOptionPane;

public class ExceptionController implements PropertyChangeListener {
    public static final String PROP_EXCEPCIO = "excepcio";

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (PROP_EXCEPCIO.equals(evt.getPropertyName())) {
            DAOException e = (DAOException) evt.getNewValue();
            JOptionPane.showMessageDialog(null, "Codi d'error: " + e.getTipo() + "\nMissatge: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
