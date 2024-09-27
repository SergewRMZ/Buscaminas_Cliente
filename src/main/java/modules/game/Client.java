package modules.game;
import java.io.*;
import java.net.*;
import com.google.gson.Gson;

public class Client {
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private final int port = 5000;
    private final String host = "127.0.0.1";
    private Gson gson;
    
    public Client () { 
        this.gson = new Gson();
    }
    
    public void connect () {
        try {
            socket = new Socket(this.host, this.port);
            System.out.println("Conectado al servidor " + this.host + ":" + this.port);   
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            
            
        }   catch (IOException e) {
            System.err.println("Error al conectar con el servidor: " + e.getMessage());
        }
    }
    
    public String sendMessageInitGame (String difficulty) {
        difficulty = difficulty.toLowerCase();
        ActionMessage startMessage = new ActionMessage("start", difficulty);
        String jsonStart = gson.toJson(startMessage);
        out.println(jsonStart);
        
        try {
            String response = in.readLine();
            return response;
        } catch (IOException e) {
            System.err.println("Error al recibir la respuesta del servidor: " + e.getMessage());
        }
        return null;
    }
    
    public void sendMessageRevealCell (int row, int col) {
        ActionMessage revealMessage = new ActionMessage("reveal", row, col);
        String jsonReveal = gson.toJson(revealMessage);
        out.println(jsonReveal);
        
        try {
            String response = in.readLine();
            System.out.println(response);
        } catch (IOException e) {
            System.err.println("Error al recibir la respuesta del servidor: " + e.getMessage());
        }
    }
}
