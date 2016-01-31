package Chreator.UIModule;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.util.ArrayList;

import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.ToolTipManager;
import javax.swing.UIManager;

import Chreator.ObjectModel.PieceProfile;
import Chreator.ObjectModel.Point;

/**
 * Created by him on 2015/12/20.
 */
public class UIHandler {
    public interface EventCallback extends
            AIPanel.EventCallback,
            ChessPiecePanel.EventCallback,
            ChessBoardPanel.EventCallback,
            GameRulePanel.EventCallback,
            ProjectSettingPanel.EventCallback {
    }

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

    public static UIHandler getInstance(EventCallback eventCallback) {
        if (uiHandler == null) {
            uiHandler = new UIHandler();
            screenResolution = Toolkit.getDefaultToolkit().getScreenSize();
            uiHandler.callback = eventCallback;
            uiHandler.prepareObjectInstance();
        } else
            uiHandler.callback = eventCallback;
        return uiHandler;
    }

    private void prepareObjectInstance() {
        ToolTipManager.sharedInstance().setDismissDelay(Integer.MAX_VALUE);
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        mainWindow = new JFrame(appName);
        mainWindow.setSize(
                (int) (uiScaleRatio * screenResolution.getWidth()),
                (int) (uiScaleRatio * screenResolution.getHeight())
        );
        tabPane = new JTabbedPane();
        prepareTabPanels();
        mainWindow.add(tabPane);
        debugBar();

        mainWindow.addComponentListener(getWindowResizeHandler());
        mainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainWindow.setLocationRelativeTo(null);
        mainWindow.setVisible(true);
    }

    private void debugBar() {
        JMenuBar bar = new JMenuBar();
        JButton menu = new JButton("print debug");
        menu.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                for (PieceProfile profile : chessPiecePanel.getPieceProfiles()) {
                    System.out.print(profile.pieceClassName +
                                    "; " + profile.playerSide +
                                    "; " + profile.sourcePicLink +
                                    "; " + profile.pieceColor.toString() +
                                    "; " + profile.imageWidth + " x " + profile.imageHeight +
                                    "; "
                    );
                    for (int i = 0; i < profile.initialPointId.size(); i++) {
                        System.out.print(profile.initialPointId.getElementAt(i) + ", ");
                    }
                    System.out.println();
                }
                System.out.println();
            }
        });
        bar.add(menu);
        mainWindow.setJMenuBar(bar);
    }

    private void prepareTabPanels() {
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

    private ComponentAdapter getWindowResizeHandler() {
        return new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                super.componentResized(e);
            }
        };
    }

    public static void refreshWindow() {
        if (uiHandler != null) uiHandler.mainWindow.repaint();
    }

    public static JFrame getMainWindow() {
        if (uiHandler != null) return uiHandler.mainWindow;
        else return null;
    }
    
    public ArrayList<Point> getPointList() {
    	return chessBoardPanel.getPointList();
    }

    public static String showVariableInputDialog(String title, String majorMessage, String minorMessage) {
        String s;
        do {
            s = JOptionPane.showInputDialog(UIHandler.getMainWindow(), "<html><center>" + majorMessage + "<br>" +
                    "Name must only contain English alphabet, arabic numerals, dollar sign ($) or underscore (_).<br>" +
                    "Name cannot start with arabic numerals.<br>" + minorMessage + "</html>", title, JOptionPane.QUESTION_MESSAGE);
            if (s == null) return null;
            if (s.matches("^[A-Za-z0-9_$]+$") && !((s.charAt(0) + "").matches("[0-9]")))
                break;
            JOptionPane.showMessageDialog(UIHandler.getMainWindow(), "<html><center>Input error.<br>" +
                    "Name must only contain English alphabet, arabic numerals, dollar sign ($) or underscore (_).<br>" +
                    "Name cannot start with arabic numerals.</html>", "ERROR - " + title, JOptionPane.ERROR_MESSAGE);
        } while (true);
        return s;
    }

    public void setChessPieceProfiles(ArrayList<PieceProfile> pieceProfiles) {
        chessPiecePanel.setPieceProfiles(pieceProfiles);
    }

    public ArrayList<PieceProfile> getChessPieceProfile() {
        return chessPiecePanel.getPieceProfiles();
    }
}
