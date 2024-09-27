import modules.game.Client;
import modules.views.Index;
public class App {
    public static void main (String[] args) {
        //Client client = new Client(); 
        //client.connect();
        //client.sendMessageInitGame("easy");
        Index.getInstanceIndex().setVisible(true);
        
    }
}
