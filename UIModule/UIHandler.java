package Chreator.UIModule;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.ToolTipManager;
import javax.swing.UIManager;

/**
 * Created by him on 2015/12/20.
 */
public class UIHandler {
    public interface EventCallback extends
            AIPanel.EventCallback,
            ChessPiecePanel.EventCallback,
            ChessBoardPanel.EventCallback,
            GameRulePanel.EventCallback,
            ProjectSettingPanel.EventCallback
    {}

    public static Dimension screenResolution;
    public static String appName = "Chreator";
    public static double uiScaleRatio = 0.8;
    private static UIHandler uiHandler;
    private EventCallback callback;
    private JFrame mainWindow;
    private JTabbedPane tabPane;

    private AIPanel aiPanel;
    private ChessBoardPanel chessBoardPanel;
    private ChessPiecePanel chessPiecePanel;
    private GameRulePanel gameRulePanel;
    private ProjectSettingPanel projectSettingPanel;

    public static UIHandler getInstance(EventCallback eventCallback){
        if (uiHandler==null){
            uiHandler = new UIHandler();
            screenResolution = Toolkit.getDefaultToolkit().getScreenSize();
            uiHandler.callback = eventCallback;
            uiHandler.prepareObjectInstance();
        }else
            uiHandler.callback = eventCallback;
        return uiHandler;
    }

    private void prepareObjectInstance(){
        ToolTipManager.sharedInstance().setDismissDelay(Integer.MAX_VALUE);
        try {UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());} catch (Exception e) {e.printStackTrace();}
        mainWindow = new JFrame(appName);
        mainWindow.setSize(
                (int) (uiScaleRatio * screenResolution.getWidth()),
                (int) (uiScaleRatio * screenResolution.getHeight())
        );
        tabPane = new JTabbedPane();
        prepareTabPanels();
        mainWindow.add(tabPane);

        mainWindow.addComponentListener(getWindowResizeHandler());
        mainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainWindow.setLocationRelativeTo(null);
        mainWindow.setVisible(true);
    }

    private void prepareTabPanels(){
        aiPanel = new AIPanel(callback);
        chessBoardPanel = new ChessBoardPanel(callback);
        chessPiecePanel = new ChessPiecePanel(callback);
        gameRulePanel = new GameRulePanel(callback);
        projectSettingPanel = new ProjectSettingPanel(callback);

        tabPane.addTab(ProjectSettingPanel.tabName, projectSettingPanel);
        tabPane.addTab(ChessBoardPanel.tabName, chessBoardPanel);
        tabPane.addTab(ChessPiecePanel.tabName, chessPiecePanel);
        tabPane.addTab(GameRulePanel.tabName, gameRulePanel);
        tabPane.addTab(AIPanel.tabName, aiPanel);
    }

    private ComponentAdapter getWindowResizeHandler(){
        return new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                super.componentResized(e);
            }
        };
    }

    public static void refreshWindow(){
        if(uiHandler!=null)uiHandler.mainWindow.repaint();
    }
    public static JFrame getMainWindow(){
        if (uiHandler !=null) return uiHandler.mainWindow;
        else return null;
    }
}
