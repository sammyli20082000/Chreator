package Chreator.UIModule;

import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * Created by him on 2015/12/20.
 */
public class AIPanel extends JPanel {
    public interface EventCallback{
    }

    public static String tabName="Artificial Intelligence";
    private EventCallback callback;
    private UIHandler uiHandler;
    public AIPanel(EventCallback eventCallback, UIHandler uiHandler){
        callback = eventCallback;
        this.uiHandler = uiHandler;
    }
}
