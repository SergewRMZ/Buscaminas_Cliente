package modules.views;

import java.awt.Color;
import java.awt.BorderLayout;
import javax.swing.BoxLayout;
import javax.swing.Box;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.OverlayLayout;
import javax.swing.Timer;
import javax.swing.border.EmptyBorder;
import modules.game.Client;
import modules.views.components.CustomButton;
import modules.views.utils.Colors;

public class Game extends javax.swing.JFrame {
    private static Game instanceGame;
    private String[][] board;
    private CustomButton[][] buttons;
    private int rows;
    private int cols;
    private String difficulty;

    private String gameEasyBgPath = "/resources/bg_game_easy.jpg";
    private String crono = "/resources/cronometer.png"; 
    private String score = "/resources/score.png"; 
    
    private BoardPanel tablero;
    private JLabel clockLabel; // Etiqueta para mostrar el reloj.
    private Timer timer; // Temporizador.
    private int timeElapsed;
    
    private Game(String[][] board, String difficulty) {
        this.board = board;
        this.rows = board.length;
        this.cols = board[0].length;
        this.timeElapsed = 0;
        this.difficulty = difficulty;
        
        // Establecer el color del marco
        setUndecorated(false); 
        getRootPane().setBorder(javax.swing.BorderFactory.createLineBorder(Color.BLACK, 20));
        
        startClock();
        createGame();
        setLocationRelativeTo(null);
        setVisible(true);
    }
        
    /** Patrón singleton para generar el juego.
     * @param board[][]: Arreglo bidimensional que contiene el tablero del juego.
     * @param difficulty: Dificultad del juego*/
    public static Game getInstanceGame (String[][] board, String difficulty) {
        if (instanceGame == null) {
            instanceGame = new Game(board, difficulty);
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
        tablero = new BoardPanel(this.gameEasyBgPath);
        tablero.setLayout(new GridLayout(this.rows, this.cols));
        buttons = new CustomButton[rows][cols];
        int x = 80;
        int y = 80;
        
        if (this.difficulty.equals("HARD")) {
            x = 40;
            y = 40;
        }
        
        else if (this.difficulty.equals("NORMAL")) {
            x = 40;
            y = 40;
        }
                  
        for (int i = 0; i < this.rows; i++) {
            for (int j = 0; j < this.cols; j++) {
                buttons[i][j] = new CustomButton(x, y);               
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
        
        JPanel panelContainer = new JPanel(new BorderLayout());
        panelContainer.setBorder(new EmptyBorder(20, 20, 20, 0)); // Margin de 20
        panelContainer.add(tablero, BorderLayout.CENTER);

        // Diseño del cronómetro calabaza
        JPanel clockPanel = new JPanel(new BorderLayout());
        ImageIcon pumpkinIcon = new ImageIcon(getClass().getResource(crono));
        Image pumpkinImage = pumpkinIcon.getImage().getScaledInstance(300, 300, Image.SCALE_SMOOTH);
        JLabel clockImageLabel = new JLabel(new ImageIcon(pumpkinImage)); // Nueva imagen redimensionada

        // Establecer el alineamiento del JLabel de la imagen
        clockImageLabel.setHorizontalAlignment(JLabel.CENTER);
        clockImageLabel.setVerticalAlignment(JLabel.TOP);

        // Crear un JPanel transparente para el tiempo digital
        JPanel overlayPanel = new JPanel();
        overlayPanel.setOpaque(false);
        overlayPanel.setLayout(new BorderLayout());

        // Configurar el JLabel del tiempo
        clockLabel = new JLabel();
        clockLabel.setFont(new Font("Arial", Font.BOLD, 40));
        clockLabel.setHorizontalAlignment(JLabel.CENTER);
        clockLabel.setBorder(new EmptyBorder(150, 30, 30, 30));
        overlayPanel.add(clockLabel, BorderLayout.NORTH);

        // Añadir el overlayPanel sobre la imagen del reloj
        clockImageLabel.setLayout(new BorderLayout());
        clockImageLabel.add(overlayPanel, BorderLayout.CENTER);

        // Crear el score y añadirlo debajo del cronómetro
        ImageIcon scoreIcon = new ImageIcon(getClass().getResource(score));
        Image scoreImage = scoreIcon.getImage().getScaledInstance(300, 500, Image.SCALE_SMOOTH); 
        JLabel scoreImageLabel = new JLabel(new ImageIcon(scoreImage));
        scoreImageLabel.setLayout(new OverlayLayout(scoreImageLabel));
        scoreImageLabel.setBorder(null);
       
        Client client = Client.getInstanceClient();
        JLabel scoreText = new JLabel(client.requestRanking()); // Solicitar ranking del server
        scoreText.setHorizontalAlignment(JLabel.CENTER);
        scoreText.setFont(new Font("Harrington", Font.BOLD, 12));
        scoreText.setForeground(Colors.ORANGE);
        scoreText.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Espacio alrededor del texto
        scoreImageLabel.add(scoreText);
        
        JPanel clockAndScorePanel = new JPanel();
        clockAndScorePanel.setLayout(new BoxLayout(clockAndScorePanel, BoxLayout.Y_AXIS));
        clockAndScorePanel.add(clockImageLabel);
        clockAndScorePanel.add(Box.createVerticalStrut(3)); 
        clockAndScorePanel.add(scoreImageLabel);

        // Añadir el panel que contiene el cronómetro y el score al contenedor principal
        panelContainer.add(clockAndScorePanel, BorderLayout.EAST);

        // Limpiar y añadir el nuevo contenido
        getContentPane().removeAll(); // Limpiar cualquier contenido previo
        add(panelContainer, BorderLayout.NORTH);
        pack(); // Ajustar tamaño ventana

        revalidate(); 
        repaint();
    }
    
    /** Método para iniciar el cronómetro. */
    private void startClock() {
        timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                timeElapsed++;
                updateClock(); // Actualizar el reloj
            }
        });
        timer.start(); // Iniciar el temporizador
    }
    
    private void stopClock () {
        if (timer != null && timer.isRunning()) {
            timer.stop();
        }
    }

    /** Método para actualizar la etiqueta del reloj. */
    private void updateClock() {
        int minutes = timeElapsed / 60;
        int seconds = timeElapsed % 60;

        // Tiempo en formato MM:SS
        String formattedTime = String.format("%02d:%02d", minutes, seconds);
        this.clockLabel.setText(formattedTime);
    }
    
    /** Método para revelar una celda del juego, se manda la solicitud al servidor y
     regresa la respuesta como JSON. */
    private void revealCell(int coordX, int coordY) {
        // Desactivar el botón para que no se pueda hacer clic de nuevo
        buttons[coordX][coordY].setEnabled(false);
        Client socketClient = Client.getInstanceClient();// Obtener la instancia de cliente.
        String jsonResponse = socketClient.sendMessageRevealCell(coordX, coordY); //Solicita datos al cliente.
        this.board = socketClient.getBoardJSON(jsonResponse);
        
        for (int i = 0; i < this.rows; i++) {
            for (int j = 0; j < this.cols; j++) {
                if (!board[i][j].equals("-")) {
                    Integer cellValue = Integer.parseInt(this.board[i][j].toString());
                
                    switch (cellValue) {
                        case -1:
                            this.buttons[i][j].updateCellMine();
                            break;
                            
                        case 0:
                            this.buttons[i][j].updateCellZero();
                            break;
                            
                        case 1:
                            this.buttons[i][j].updateCellOne();
                            break;

                        case 2:
                            this.buttons[i][j].updateCellTwo();
                            break;

                        case 3:
                            this.buttons[i][j].updateCellThree();
                            break;

                        case 4:
                            this.buttons[i][j].updateCellFour();
                            break;

                        case 5:
                            this.buttons[i][j].updateCellFive();
                            break;

                        default:
                            break;
                    }
                }
            }
        }
        
        if (socketClient.getWin(jsonResponse)) {
            // Mostrar ventana de que ha ganado
            showWinImage();
            disableAllButtons();
            stopClock();
        }
        
        else if (socketClient.getLose(jsonResponse)) {
            showLoseImage();
            disableAllButtons();
            stopClock();
        }
        
        tablero.revalidate();
        tablero.repaint();
    }
    
    private void showWinImage () {
        WinScreen.getInstanceWin().setVisible(true);
        Client client = Client.getInstanceClient();
        client.disconnect();
        this.setVisible(false);
        instanceGame = null;
    }
    
    private void showLoseImage() {
        LoseScreen.getInstanceGameOverScreen().setVisible(true);
        Client client = Client.getInstanceClient();
        client.disconnect(); // Desconectar el cliente
        this.setVisible(false);
        instanceGame = null;
    }
    
    private void disableAllButtons () {
        for (int i = 0; i < this.rows; i++) {
            for (int j = 0; j < this.cols; j++) {
                buttons[i][j].setEnabled(false);
            }
        }
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
