package modules.views;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.border.EmptyBorder;
import modules.game.Client;
import modules.views.components.CustomButton;

public class Game extends javax.swing.JFrame {
    private static Game instanceGame;
    private String[][] board;
    private JButton[][] buttons;
    private int rows;
    private int cols;
    
    private String gameEasyBgPath = "/resources/bg_game_easy.jpg";
    private String celdaClosePath = "/resources/celda_close.png";
    private String Bomba = "/resources/bomba.png";
    private String Abierta = "/resources/abierta.png";
    
    private JLabel clockLabel; // Etiqueta para mostrar el reloj.
    private Timer timer; // Temporizador.
    private int timeElapsed;
    
    private Game(String[][] board) {
        this.board = board;
        this.rows = board.length;
        this.cols = board[0].length;
        this.timeElapsed = 0;
        createGame();
        setLocationRelativeTo(null);
        setVisible(true);
        startClock();
    }
        
    public static Game getInstanceGame (String[][] board) {
        if (instanceGame == null) {
            instanceGame = new Game(board);
        }
        return instanceGame;
    }
    
    /** Clase que hereda de JPanel para establecer una imagen de fondo para el juego. */
    private class BoardPanel extends JPanel {
        private Image backgroundImg;
        public BoardPanel(String imagePath) {
            this.backgroundImg = new ImageIcon(getClass().getResource(imagePath)).getImage();
        }
        
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(this.backgroundImg, 0, 0, getWidth(), getHeight(), this);
        }
    }
    
    /** Función para generar el tablero del juego. */
    private void createGame () {
        BoardPanel tablero = new BoardPanel(this.gameEasyBgPath);
        tablero.setLayout(new GridLayout(this.rows, this.cols));
        buttons = new CustomButton[rows][cols];
        String imagePath = this.celdaClosePath;
        
        for (int i = 0; i < this.rows; i++) {
            for (int j = 0; j < this.cols; j++) {
                buttons[i][j] = new CustomButton(imagePath, 100, 100);               
                int coordX = i;
                int coordY = j;
                
                buttons[i][j].addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        revealCell(coordX, coordY);
                    }
                });
                
                tablero.add(buttons[i][j]);
            }
        }
        
        tablero.revalidate();
        tablero.repaint();
        JPanel panelContainer = new JPanel(new BorderLayout());
        panelContainer.setBorder(new EmptyBorder(20, 20, 20, 20)); // Margin de 20
        panelContainer.add(tablero, BorderLayout.CENTER);
        
        // Diseño del cronómetro
        JPanel clockPanel = new JPanel();
        clockLabel = new JLabel();
        clockLabel.setFont(new Font("Arial", Font.BOLD, 24));
        clockPanel.add(this.clockLabel);
        
        // Agregar clockPanel
        panelContainer.add(clockPanel, BorderLayout.EAST);
        panelContainer.repaint();
        
        // getContentPane().removeAll(); // Limpiar cualquier contenido previo
        add(panelContainer, BorderLayout.CENTER);
        pack(); // Ajustar tamaño ventana
        
        revalidate(); // Refrescar la interfaz gráfica
        repaint();
    }
    
    /** Método para iniciar el cronómetro. */
    private void startClock() {
        timer = new Timer(1000, new ActionListener() { // Crear un temporizador que se dispara cada segundo
            @Override
            public void actionPerformed(ActionEvent e) {
                timeElapsed++;
                updateClock(); // Actualizar el reloj
            }
        });
        timer.start(); // Iniciar el temporizador
    }

    /** Método para actualizar la etiqueta del reloj. */
    private void updateClock() {
        this.clockLabel.setText("Tiempo: " + timeElapsed);
    }
    
    /** Método para revelar una celda del juego, se manda la solicitud al servidor y
     regresa la respuesta como JSON. */
    private void revealCell(int coordX, int coordY) {
        Client socketClient = Client.getInstanceClient();// Obtener la instancia de cliente.
        String jsonResponse = socketClient.sendMessageRevealCell(coordX, coordY); //Solicita datos al cliente.
        this.board = socketClient.getBoardJSON(jsonResponse);
        
        /* En this.board ya tienes el tablero actualizado, ahora harás un for
        para iterar entre las celdas e ir actualizando las imágenes
        
        for (int i = 0; i < this.rows; i++) {
            for (int j = 0; j < this.cols; j++) {
                if (this.board[i][j] == 1) {
                    poner imagen de celda 1
                }
        
            else if (this.board[i][j] == 2) poner imagen de 2
            else if (this.board[i][j] == -1) imagen de bomba
            else deja la imagen default
            }
        }*/
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(0, 0, 0));
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        pack();
    }// </editor-fold>//GEN-END:initComponents

    

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
