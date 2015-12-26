package Chreator.UIModule;

import javax.swing.JPanel;

/**
 * Created by him on 2015/12/20.
 */
public class ChessPiecePanel extends JPanel {
    public interface EventCallback{

    }
    public static String tabName="Chess Piece";
    private EventCallback callback;
    private UIHandler uiHandler;
    public ChessPiecePanel(EventCallback eventCallback, UIHandler uiHandler){
        callback = eventCallback;
        this.uiHandler = uiHandler;
    }
}
