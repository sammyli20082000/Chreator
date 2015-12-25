package Chreator.UIModule;

import javax.swing.JPanel;

/**
 * Created by him on 2015/12/20.
 */
public class ChessBoardPanel extends JPanel {
    public interface EventCallback{
    }

    public static String tabName="Chess Board";
    private EventCallback callback;
    public ChessBoardPanel(EventCallback eventCallback){
        callback = eventCallback;
    }
}
