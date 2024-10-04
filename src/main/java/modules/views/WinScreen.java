/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modules.views;

import javax.swing.*;
import java.awt.*;

/**
 *
 * @author Ale
 */
public class WinScreen extends javax.swing.JFrame{
    private static WinScreen WinGame;
    public WinScreen() {
        setLocationRelativeTo(null);
        setImageLabel(FondoLabel, "/resources/bg.jpg");
    }
    
    public static WinScreen getInstanceWin () {
        if (WinGame == null) {
            WinGame = new WinScreen();
        }
        return WinGame;
    }
    
    private void setImageLabel(JLabel label, String root) {
        ImageIcon imageIcon = new ImageIcon(getClass().getResource(root));

        if (imageIcon.getIconWidth() == -1) {
            System.err.println("Error: la imagen no se pudo cargar desde la ruta: " + root);
        } else {

            // Escalar la imagen al tamaño del JLabel
            Image image = imageIcon.getImage();
            Image scaledImage = image.getScaledInstance(label.getWidth(), label.getHeight(), Image.SCALE_SMOOTH);

            Icon icon = new ImageIcon(scaledImage);
            label.setIcon(icon);
            label.repaint(); // Repinta el JLabel para mostrar la nueva imagen
        }
    }
    
    private javax.swing.JLabel FondoLabel;
}

