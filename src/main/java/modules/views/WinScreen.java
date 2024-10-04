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
public class WinScreen extends JFrame{
    public WinScreen() {
        setSize(600, 400); 
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        // Crear un panel para la imagen de victoria
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                ImageIcon winImage = new ImageIcon(getClass().getResource("/resources/win.jpg"));
                g.drawImage(winImage.getImage(), 0, 0, getWidth(), getHeight(), this);
            }
        };
        add(panel);
        setVisible(true);
    } 
}
