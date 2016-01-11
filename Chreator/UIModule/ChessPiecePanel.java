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

    public ChessPiecePanel(EventCallback eventCallback){
        callback = eventCallback;
    }
}
