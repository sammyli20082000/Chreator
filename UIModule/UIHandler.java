package Chreator.UIModule;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;
import javax.swing.ListModel;
import javax.swing.ToolTipManager;
import javax.swing.UIManager;

import Chreator.CodeProducer.CodeProducer;
import Chreator.ObjectModel.PieceProfile;
import Chreator.ObjectModel.Point;

/**
 * Created by him on 2015/12/20.
 */
public class UIHandler {
	public interface EventCallback extends AIPanel.EventCallback, ChessPiecePanel.EventCallback,
			ChessBoardPanel.EventCallback, GameRulePanel.EventCallback, ProjectSettingPanel.EventCallback {
	}

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
		if (uiHandler == null && eventCallback != null) {
			uiHandler = new UIHandler();
			if (eventCallback == null || !(eventCallback instanceof UIHandler.EventCallback))
				return uiHandler;
			uiHandler.callback = eventCallback;
			uiHandler.prepareObjectInstance();
		}
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
		mainWindow.setSize((int) (uiScaleRatio * getScreenResolution().getWidth()),
				(int) (uiScaleRatio * getScreenResolution().getHeight()));
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
//				for (PieceProfile profile : chessPiecePanel.getPieceProfiles()) {
//					System.out.print(profile.pieceClassName + "; " + profile.playerSide + "; " + profile.sourcePicLink
//							+ "; " + profile.pieceColor.toString() + "; " + profile.imageRelativeWidth + " x "
//							+ profile.imageRelativeHeight + "; ");
//					for (int i = 0; i < profile.initialPointId.size(); i++) {
//						System.out.print(profile.initialPointId.getElementAt(i) + ", ");
//					}
//					System.out.println();
//				}
//				System.out.println();
//				for(int i = 0; i < chessPiecePanel.getPieceInitialPointIdList().getSize(); i++) {
//					System.out.println(chessPiecePanel.getPieceInitialPointIdList().getElementAt(i));
//				}

				CodeProducer codeProducer = new CodeProducer(getProjectLocationBaseDir(), getProjectFolderName(),
						getPointList(), getEdgeDirectionList(), getPlayerSideList(), getPieceProfiles());

				try {
					codeProducer.produceExecutable();
				} catch (Exception e1) {
				}

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

	public static JFrame getMainWindow() {
		if (uiHandler != null)
			return uiHandler.mainWindow;
		else
			return null;
	}

	public ArrayList<Point> getPointList() {
		return chessBoardPanel.getPointList();
	}

	public void setChessPieceProfiles(ArrayList<PieceProfile> pieceProfiles) {
		chessPiecePanel.setPieceProfiles(pieceProfiles);
	}

	public ArrayList<PieceProfile> getChessPieceProfile() {
		return chessPiecePanel.getPieceProfiles();
	}

	ChessBoardPanel getChessBoardPanel() {
		return chessBoardPanel;
	}

	ChessPiecePanel getChessPiecePanel() {
		return chessPiecePanel;
	}

	public String getProjectLocationBaseDir() {
		return projectSettingPanel.getProjectLocationBaseDir();
	}

	public Color getEdgeInfoTriangleColorByDirection(String direction) {
		return chessBoardPanel.getEdgeDirectionColor(direction);
	}

	public String getProjectFolderName() {
		return projectSettingPanel.getProjectFolderName();
	}

	public ListModel<String> getEdgeDirectionList() {
		return chessBoardPanel.getEdgeDirectionList();
	}

	public static Dimension getScreenResolution() {
		return Toolkit.getDefaultToolkit().getScreenSize();
	}

	public ListModel<String> getPlayerSideList() {
		return chessPiecePanel.getPlayerSideList();
	}
	
	public ArrayList<PieceProfile> getPieceProfiles() {
		return chessPiecePanel.getPieceProfiles();
	}
}
