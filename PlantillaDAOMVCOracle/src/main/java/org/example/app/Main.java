package org.example.app;

import org.example.controller.MainController;

import javax.swing.*;
import java.util.Locale;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Locale.setDefault(new Locale("ca","ES"));
            new MainController();
        });
    }
}