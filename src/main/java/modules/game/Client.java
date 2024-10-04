package modules.game;
import java.io.*;
import java.net.*;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class Client {   
    private static Client instance;
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private final int port = 5000;
    private final String host = "127.0.0.1";
    private Gson gson;
    
    private Client () { 
        this.gson = new Gson();
    }
    
    public static Client getInstanceClient () {
        if (instance == null) {
            instance = new Client();
        }
        return instance;
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
    
    /** Método para mandar una petición al servidor para revelar la celda con base
     * en la celda presionada 
     * @params int row, col: Fila y columna de la celda
     * @return response: Respuesta JSON del servidor. */
    public String sendMessageRevealCell (int row, int col) {
        ActionMessage revealMessage = new ActionMessage("reveal", row, col);
        String jsonReveal = gson.toJson(revealMessage);
        out.println(jsonReveal);
        
        try {
            String response = in.readLine();
            return response;
        } catch (IOException e) {
            System.err.println("Error al recibir la respuesta del servidor: " + e.getMessage());
        }
        return null;
    }
    
    /** Función para convertir el json del tablero en un arreglo bidimensional.
     * @param jsonResponse: La respuesta del servidor al solicitar revelación de una celda 
     * @return Un arreglo bidimensional que contiene el tablero.*/
    public String[][] getBoardJSON (String jsonResponse) {
        if (jsonResponse != null) {
            JsonObject jsonObject = JsonParser.parseString(jsonResponse).getAsJsonObject();
            JsonArray jsonBoard = jsonObject.getAsJsonArray("board");
            String [][] board = new String[jsonBoard.size()][]; // Crear el tablero
            
            for (int i = 0; i < jsonBoard.size(); i++) {
                JsonArray row = jsonBoard.get(i).getAsJsonArray(); // Obtener la fila como un array
                board[i] = new String[row.size()];
                for (int j = 0; j < row.size(); j++) {
                    board[i][j] = row.get(j).getAsString(); // Obtener los enteros.
                }
            }
            
            System.out.println("Tablero obtenido: ");
            for (int i = 0; i < 9; i++) {
                for (int j = 0; j < 9; j++) {
                    System.out.print(board[i][j]);
                }
                
                System.out.println();
            }
            
            return board;
        }
        
        return null;
    }
    
    /** Función para obtener el win del json si es que el usuario ha ganado.
     * @params jsonResponse: La respuesta del servidor al destapar una celda. */
    public boolean getWin (String jsonResponse) {
        if (jsonResponse != null) {
            JsonObject jsonObject = JsonParser.parseString(jsonResponse).getAsJsonObject();
            if (jsonObject.has("win")) {
                return jsonObject.get("win").getAsBoolean();
            }
        }
        
        return false;
    }
    
    public boolean getLose (String jsonResponse) {
        if (jsonResponse != null) {
            JsonObject jsonObject = JsonParser.parseString(jsonResponse).getAsJsonObject();
            if (jsonObject.has("lose")) {
                return jsonObject.get("lose").getAsBoolean();
            }
        }
        
        return false; 
    }
}
