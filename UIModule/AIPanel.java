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

    public AIPanel(EventCallback eventCallback){
        callback = eventCallback;
    }
}
