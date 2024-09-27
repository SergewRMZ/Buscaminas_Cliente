package modules.game;

public class ActionMessage {
    private Cell cell;
    private String action;
    private String difficulty;
    
    /** Constructor para iniciar juego */
    public ActionMessage (String action, String difficulty) {
        this.action = action;
        this.difficulty = difficulty;
    }
    
    /** Constructor para destapar una celda */
    public ActionMessage (String action, int row, int col) {
        this.action = action;
        cell = new Cell(row, col);
    }
}
