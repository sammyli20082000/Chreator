package Chreator.UIModule;

import javax.swing.JPanel;

/**
 * Created by him on 2015/12/20.
 */
public class GameRulePanel extends JPanel {
    public interface EventCallback {

    }

    public static String tabName = "General Game Rules";
    private EventCallback callback;
    private UIHandler uiHandler;

    public GameRulePanel(EventCallback eventCallback, UIHandler uiHandler) {
        callback = eventCallback;
        this.uiHandler = uiHandler;
    }
}
