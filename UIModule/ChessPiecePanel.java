package Chreator.UIModule;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.JobAttributes;
import java.awt.ScrollPane;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.ToolTipManager;
import javax.swing.border.EtchedBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import javax.xml.transform.Templates;

import Chreator.ObjectModel.PieceProfile;
import Chreator.ObjectModel.Point;

/**
 * Created by him on 2015/12/20.
 */
public class ChessPiecePanel extends JPanel {
	public interface EventCallback {

	}

	public static String tabName = "Chess Piece";
	public static double sharedPieceHeight = -1, sharedPieceWidth = -1;
	public static Color sharedColor = Color.WHITE;
	private EventCallback callback;
	private JPanel pieceProfileEditorPanel, pieceListAndPreviewPanel, pieceInfoSettingPanel, codeInserterPanel,
			previewImagePanel;
	private int componentCounter = 0;
	private Dimension previewImagePanelSize = new Dimension(200, 200);
	private File imageFile;
	private JList playerSidesList, pieceClassNameList, pieceInitialPointIdList;
	private JRadioButton syncCodeRadioBtn, notSyncCodeRadioBtn, syncSizeRadioBtn;
	private JButton addChessPieceButton, addPlayerSideButton, deleteChessPieceButton, deletePlayerSideButton,
			setPieceSizeButton, addInitialPointIdButton, deleteInitialPointIdButton, deletePiecePicButton,
			defaultRatioButton, addFunction1Button, addFunction2Button, addFunction3Button;
	private JTextField pieceColorGreenTextField, pieceColorBlueTextField, pieceColorRedTextField,
			piecePicHeightTextField, piecePicWidthTextField, pieceClassNameField, piecePicLinkField;
	private SimpleCodeEditor codeEditor;
	private ArrayList<PieceProfile> pieceProfiles;
	private boolean inApplyingProfile = false;
	private Color jtfDefaultBackground = new JTextField().getBackground();
	private JFrame pieceSetSizeWindow, pieceSetInitialPointIDWindow;
	private ChessPieceSetSizeGraphicAreaPanel pieceSetSizePanel;
	private ChessPieceSetInitialPointGraphicAreaPanel pieceSetInitialPointPanel;

	public ChessPiecePanel(EventCallback eventCallback) {
		callback = eventCallback;
		pieceProfiles = new ArrayList<PieceProfile>();
		Dimension screenResolution = UIHandler.getScreenResolution();

		pieceSetSizePanel = new ChessPieceSetSizeGraphicAreaPanel(
				new ChessPieceSetSizeGraphicAreaPanel.OnSetSizeCallBack() {
					@Override
					public void onSetSize(double relativeWidth, double relativeHeight) {
						PieceProfile profile = getSelectedProfile();
						if (profile != null) {
							profile.imageRelativeWidth = relativeWidth;
							profile.imageRelativeHeight = relativeHeight;
							applySelectedProfile();
						}
					}
				});
		pieceSetSizeWindow = new JFrame("Piece Set Size - Chreator");
		pieceSetSizeWindow.add(pieceSetSizePanel);
		pieceSetSizeWindow.setLocation(screenResolution.width / 10, screenResolution.height / 10);
		pieceSetSizeWindow.setSize(screenResolution.width * 4 / 5, screenResolution.height * 4 / 5);

		pieceSetInitialPointPanel = new ChessPieceSetInitialPointGraphicAreaPanel(
				new ChessPieceSetInitialPointGraphicAreaPanel.OnSetInitialPointCallback() {
					@Override
					public void onSetInitialPointCallback(int[] ids) {
						PieceProfile profile = getSelectedProfile();
						int[] idsBackup = new int[profile.initialPointId.size()];

						for (int i = 0; i < profile.initialPointId.getSize(); i++) {
							idsBackup[i] = Integer.parseInt(profile.initialPointId.getElementAt(i));
						}
						profile.initialPointId.clear();

						if (isInitialPointUsed(ids)) {
							int option = JOptionPane.showConfirmDialog(null,
									"There are already another piece using the selected point(s) as its initial point. Do you still want to continue? This may causes unexpected situation during compilation.",
									"Warning", JOptionPane.OK_CANCEL_OPTION);
							if (option != JOptionPane.OK_OPTION) {
								for (int id : idsBackup) {
									profile.initialPointId.addElement(id + "");
								}
								pieceSetInitialPointPanel.updateContent(idsBackup);
								return;
							}
						}

						for (int id : ids) {
							profile.initialPointId.addElement(id + "");
						}
					}
				});
		pieceSetInitialPointIDWindow = new JFrame("Piece Set Initial Placing Point ID - Chreator");
		pieceSetInitialPointIDWindow.add(pieceSetInitialPointPanel);
		pieceSetInitialPointIDWindow.setLocation(screenResolution.width / 10, screenResolution.height / 10);
		pieceSetInitialPointIDWindow.setSize(screenResolution.width * 4 / 5, screenResolution.height * 4 / 5);

		setupLayout();
		addComponentListener(new ComponentAdapter() {
			@Override
			public void componentShown(ComponentEvent e) {
				super.componentResized(e);
				Dimension d = UIHandler.getInstance(null).getChessBoardPanel().getChessBoardPreferredSize();

				if ((sharedPieceWidth < 0 || sharedPieceHeight < 0) && d.getHeight() > 0 && d.getWidth() > 0) {
					if (1.0 * d.getHeight() / d.getWidth() > 1) {
						sharedPieceWidth = 0.1;
						sharedPieceHeight = 0.1 * d.getWidth() / d.getHeight();
					} else {
						sharedPieceHeight = 0.1;
						sharedPieceWidth = 0.1 * d.getHeight() / d.getWidth();
					}
//					addPlayerSideToList("RED");
//					addPlayerSideToList("BLACK");
//					addChessPieceToList("General");
//					addChessPieceToList("Cannon");
//					addChessPieceToList("Advisor");
//					addChessPieceToList("Horse");

					removeComponentListener(this);
				}
			}
		});
	}

	private void setupLayout() {
		setLayout(new BorderLayout());
		pieceProfileEditorPanel = new JPanel(new GridBagLayout());
		pieceListAndPreviewPanel = new JPanel(new GridBagLayout());
		pieceListAndPreviewPanel.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
		pieceListAndPreviewPanel.setBackground(Color.lightGray);
		JScrollPane scrollPane = new JScrollPane(pieceListAndPreviewPanel, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		codeEditor = new SimpleCodeEditor();

		prepareChessListAndPreviewPanel();
		preparePieceProfileEditorPanel();

		pieceProfileEditorPanel.setBorder(BorderFactory.createRaisedBevelBorder());
		codeEditor.setBorder(BorderFactory.createLineBorder(Color.GRAY));

		add(scrollPane, BorderLayout.LINE_START);
		JPanel jp = new JPanel();
		jp.setLayout(new BorderLayout());
		jp.add(codeEditor, BorderLayout.CENTER);
		add(new JScrollPane(jp), BorderLayout.CENTER);
		add(pieceProfileEditorPanel, BorderLayout.LINE_END);

		codeEditor.getDocument().addDocumentListener(createDocumentAdapter(codeEditor));

		allComponentInPanelSetEnabled(pieceProfileEditorPanel, false);
		codeEditor.setEnabled(false);
		previewImagePanel.setEnabled(false);
		deletePiecePicButton.setEnabled(false);
	}

	private void prepareChessListAndPreviewPanel() {
		componentCounter = 0;
		previewImagePanel = new JPanel() {
			public void paintComponent(Graphics g) {
				super.paintComponent(g);
				updatePreviewPanelContent(g);
			}

			public void setEnabled(boolean enabled) {
				super.setEnabled(enabled);
				if (enabled)
					setBorder(BorderFactory.createRaisedBevelBorder());
				else
					setBorder(BorderFactory.createLineBorder(Color.GRAY));
			}
		};
		previewImagePanel.setPreferredSize(previewImagePanelSize);
		previewImagePanel.setBackground(Color.lightGray);
		previewImagePanel.addMouseListener(createJPanelMouseListener(previewImagePanel));

		pieceClassNameList = new JList(new DefaultListModel<String>());
		pieceClassNameList.setFixedCellWidth(1);
		pieceClassNameList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		pieceClassNameList.addListSelectionListener(createListSelectionListener(pieceClassNameList));

		playerSidesList = new JList(new DefaultListModel<String>());
		playerSidesList.setFixedCellWidth(1);
		playerSidesList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		playerSidesList.addListSelectionListener(createListSelectionListener(playerSidesList));

		addChessPieceButton = new JButton("Add Chess Piece");
		addPlayerSideButton = new JButton("Add Player Side");
		deletePlayerSideButton = new JButton("Delete Player Side");
		deleteChessPieceButton = new JButton("Delete Chess Piece");
		deletePiecePicButton = new JButton("Delete Piece Image");

		addChessPieceButton.addActionListener(createJButtonActionListener(addChessPieceButton));
		addPlayerSideButton.addActionListener(createJButtonActionListener(addPlayerSideButton));
		deletePlayerSideButton.addActionListener(createJButtonActionListener(deletePlayerSideButton));
		deleteChessPieceButton.addActionListener(createJButtonActionListener(deleteChessPieceButton));
		deletePiecePicButton.addActionListener(createJButtonActionListener(deletePiecePicButton));

		syncCodeRadioBtn = new JRadioButton("Make codes synchronized");
		notSyncCodeRadioBtn = new JRadioButton("Make codes independent");

		syncCodeRadioBtn.addActionListener(createJRadioButtonActionListener(syncCodeRadioBtn));
		notSyncCodeRadioBtn.addActionListener(createJRadioButtonActionListener(notSyncCodeRadioBtn));
		syncCodeRadioBtn.setSelected(true);

		addToPanel(pieceListAndPreviewPanel, new JLabel("Selected Chess Piece Image Preview"),
				GridBagConstraints.CENTER, GridBagConstraints.NONE);
		addToPanel(pieceListAndPreviewPanel, previewImagePanel);
		addToPanel(pieceListAndPreviewPanel, deletePiecePicButton);

		addToPanel(pieceListAndPreviewPanel, new JLabel("<html><br>All Player Side</html>"), GridBagConstraints.CENTER,
				GridBagConstraints.NONE);
		addToPanel(pieceListAndPreviewPanel, new JScrollPane(playerSidesList), GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL);
		addToPanel(pieceListAndPreviewPanel, addPlayerSideButton);
		addToPanel(pieceListAndPreviewPanel, deletePlayerSideButton);

		addToPanel(pieceListAndPreviewPanel, new JLabel("<html><br>All Chess Piece</html>"), GridBagConstraints.CENTER,
				GridBagConstraints.NONE);
		addToPanelFillRemaining(pieceListAndPreviewPanel, new JScrollPane(pieceClassNameList),
				GridBagConstraints.CENTER, GridBagConstraints.BOTH);
		addToPanel(pieceListAndPreviewPanel, syncCodeRadioBtn);
		addToPanel(pieceListAndPreviewPanel, notSyncCodeRadioBtn);
		addToPanel(pieceListAndPreviewPanel, addChessPieceButton);
		addToPanel(pieceListAndPreviewPanel, deleteChessPieceButton);
	}

	private void preparePieceProfileEditorPanel() {
		pieceInfoSettingPanel = new JPanel(new GridBagLayout());
		codeInserterPanel = new JPanel(new GridBagLayout());

		JScrollPane scrollPane = new JScrollPane(codeInserterPanel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		codeInserterPanel.setBorder(
				BorderFactory.createEmptyBorder(0, 0, 0, scrollPane.getVerticalScrollBar().getPreferredSize().width));

		prepareChessInfoSettingPanel();
		prepareCodeInserterPanel();

		componentCounter = 0;
		addToPanel(pieceProfileEditorPanel, pieceInfoSettingPanel);
		addToPanel(pieceProfileEditorPanel, scrollPane);
		addToPanelFillRemaining(pieceProfileEditorPanel, new JLabel(" "), GridBagConstraints.CENTER,
				GridBagConstraints.NONE);
	}

	private void prepareCodeInserterPanel() {
		componentCounter = 0;

		addFunction1Button = new JButton("Next point in (Single direction)");
		addFunction2Button = new JButton("Next point passing through (A set of directions)");
		addFunction3Button = new JButton("All points in (Singel direction)");

		addFunction1Button.addActionListener(createJButtonActionListener(addFunction1Button));
		addFunction2Button.addActionListener(createJButtonActionListener(addFunction2Button));
		addFunction3Button.addActionListener(createJButtonActionListener(addFunction3Button));

		addToPanel(codeInserterPanel, new JLabel("<html><br>Insert code template to editor</html>"),
				GridBagConstraints.CENTER, GridBagConstraints.NONE);
		addToPanel(codeInserterPanel,
				createInserterRow(addFunction1Button, "Get the next point of a specific direction"));
		addToPanel(codeInserterPanel,
				createInserterRow(addFunction2Button, "Get the next point passing through a set of directions"));
		addToPanel(codeInserterPanel,
				createInserterRow(addFunction3Button, "Get all points along a specific direction"));
	}

	private JPanel createInserterRow(JButton jb, String msg) {
		JPanel jp = new JPanel(new BorderLayout());
		JLabel jl = new JLabel("  ?  ");
		jl.setToolTipText(msg);
		jl.setOpaque(true);
		jl.setBackground(jl.getForeground());
		jl.setForeground(jp.getBackground());
		jl.addMouseListener(new MouseAdapter() {
			int showBackupTime, dismissBackupTime;

			@Override
			public void mouseEntered(MouseEvent e) {
				super.mouseEntered(e);
				showBackupTime = ToolTipManager.sharedInstance().getInitialDelay();
				dismissBackupTime = ToolTipManager.sharedInstance().getDismissDelay();
				ToolTipManager.sharedInstance().setInitialDelay(0);
				ToolTipManager.sharedInstance().setDismissDelay(Integer.MAX_VALUE);
			}

			@Override
			public void mouseExited(MouseEvent e) {
				super.mouseExited(e);
				ToolTipManager.sharedInstance().setInitialDelay(showBackupTime);
				ToolTipManager.sharedInstance().setDismissDelay(dismissBackupTime);
			}
		});

		jp.add(jb, BorderLayout.CENTER);
		jp.add(jl, BorderLayout.LINE_END);
		return jp;
	}

	private void prepareChessInfoSettingPanel() {
		pieceInitialPointIdList = new JList(new DefaultListModel<String>());
		setPieceSizeButton = new JButton("Piece set size");
		addInitialPointIdButton = new JButton("Add Point ID");
		deleteInitialPointIdButton = new JButton("Delete Point ID");
		defaultRatioButton = new JButton("Use Default Ratio");
		pieceColorRedTextField = new JTextField();
		pieceColorGreenTextField = new JTextField();
		pieceColorBlueTextField = new JTextField();
		piecePicHeightTextField = new JTextField();
		piecePicWidthTextField = new JTextField();
		pieceClassNameField = new JTextField() {
			public void setEnabled(boolean enabled) {
				super.setEnabled(enabled);
				setBackground(pieceInfoSettingPanel.getBackground());
			}
		};
		piecePicLinkField = new JTextField() {
			public void setEnabled(boolean enabled) {
				super.setEnabled(enabled);
				setBackground(pieceInfoSettingPanel.getBackground());
			}
		};

		pieceInitialPointIdList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		pieceInitialPointIdList.setFixedCellWidth(1);

		pieceClassNameField.setOpaque(true);
		pieceClassNameField.setBackground(pieceInfoSettingPanel.getBackground());
		pieceClassNameField.setOpaque(true);
		piecePicLinkField.setBackground(pieceInfoSettingPanel.getBackground());

		pieceColorBlueTextField.getDocument().addDocumentListener(createDocumentAdapter(pieceColorBlueTextField));
		pieceColorGreenTextField.getDocument().addDocumentListener(createDocumentAdapter(pieceColorGreenTextField));
		pieceColorRedTextField.getDocument().addDocumentListener(createDocumentAdapter(pieceColorRedTextField));
		piecePicHeightTextField.getDocument().addDocumentListener(createDocumentAdapter(piecePicHeightTextField));
		piecePicWidthTextField.getDocument().addDocumentListener(createDocumentAdapter(piecePicWidthTextField));
		((AbstractDocument) piecePicLinkField.getDocument()).setDocumentFilter(createDocumentFilter(piecePicLinkField));
		((AbstractDocument) pieceClassNameField.getDocument())
				.setDocumentFilter(createDocumentFilter(pieceClassNameField));

		addInitialPointIdButton.addActionListener(createJButtonActionListener(addInitialPointIdButton));
		deleteInitialPointIdButton.addActionListener(createJButtonActionListener(deleteInitialPointIdButton));
		setPieceSizeButton.addActionListener(createJButtonActionListener(setPieceSizeButton));
		defaultRatioButton.addActionListener(createJButtonActionListener(defaultRatioButton));

		syncSizeRadioBtn = new JRadioButton("Make all pieces having the same size");
		syncSizeRadioBtn.addActionListener(createJRadioButtonActionListener(syncSizeRadioBtn));
		syncSizeRadioBtn.setSelected(true);

		GridBagConstraints c1 = new GridBagConstraints(), c2 = new GridBagConstraints();
		String t = "    ";
		c1.gridx = 0;
		c1.gridy = 0;
		c1.weightx = 0;
		c1.fill = GridBagConstraints.NONE;
		c1.anchor = GridBagConstraints.CENTER;
		c2.gridx = 1;
		c2.gridy = 0;
		c2.weightx = 1;
		c2.fill = GridBagConstraints.HORIZONTAL;
		c2.anchor = GridBagConstraints.CENTER;

		componentCounter = 0;
		JPanel temp;
		addToPanel(pieceInfoSettingPanel, new JLabel("Piece Name: "));
		addToPanel(pieceInfoSettingPanel, pieceClassNameField);
		addToPanel(pieceInfoSettingPanel, new JLabel("Image Link:"));
		addToPanel(pieceInfoSettingPanel, piecePicLinkField);
		addToPanel(pieceInfoSettingPanel, new JLabel("Piece Color (if no piece image)"), GridBagConstraints.LINE_START,
				GridBagConstraints.NONE);

		temp = new JPanel(new GridBagLayout());
		temp.add(new JLabel(t + "R(0~255):"), c1);
		temp.add(pieceColorRedTextField, c2);
		addToPanel(pieceInfoSettingPanel, temp);

		temp = new JPanel(new GridBagLayout());
		temp.add(new JLabel(t + "G(0~255):"), c1);
		temp.add(pieceColorGreenTextField, c2);
		addToPanel(pieceInfoSettingPanel, temp);

		temp = new JPanel(new GridBagLayout());
		temp.add(new JLabel(t + "B(0~255):"), c1);
		temp.add(pieceColorBlueTextField, c2);
		addToPanel(pieceInfoSettingPanel, temp);

		addToPanel(pieceInfoSettingPanel, new JLabel("Piece Size"), GridBagConstraints.LINE_START,
				GridBagConstraints.HORIZONTAL);

		temp = new JPanel(new GridBagLayout());
		temp.add(syncSizeRadioBtn);
		addToPanel(pieceInfoSettingPanel, temp, GridBagConstraints.LINE_START, GridBagConstraints.NONE);

		temp = new JPanel(new GridBagLayout());
		temp.add(new JLabel(t + "Width(0~1): "), c1);
		addToPanel(pieceInfoSettingPanel, temp, GridBagConstraints.LINE_START, GridBagConstraints.NONE);
		temp = new JPanel(new GridBagLayout());
		temp.add(new JLabel(t), c1);
		temp.add(piecePicWidthTextField, c2);
		addToPanel(pieceInfoSettingPanel, temp);

		temp = new JPanel(new GridBagLayout());
		temp.add(new JLabel(t + "Height(0~1):"), c1);
		addToPanel(pieceInfoSettingPanel, temp, GridBagConstraints.LINE_START, GridBagConstraints.NONE);
		temp = new JPanel(new GridBagLayout());
		temp.add(new JLabel(t), c1);
		temp.add(piecePicHeightTextField, c2);
		addToPanel(pieceInfoSettingPanel, temp);

		temp = new JPanel(new GridBagLayout());
		temp.add(new JLabel(t), c1);
		temp.add(setPieceSizeButton, c2);
		addToPanel(pieceInfoSettingPanel, temp);

		temp = new JPanel(new GridBagLayout());
		temp.add(new JLabel(t), c1);
		temp.add(defaultRatioButton, c2);
		addToPanel(pieceInfoSettingPanel, temp);

		addToPanel(pieceInfoSettingPanel, new JLabel("<html><br>Initial Piece Placing Point Point ID</html>"),
				GridBagConstraints.CENTER, GridBagConstraints.NONE);
		addToPanel(pieceInfoSettingPanel, new JScrollPane(pieceInitialPointIdList), GridBagConstraints.CENTER,
				GridBagConstraints.BOTH);
		addToPanel(pieceInfoSettingPanel, addInitialPointIdButton);
		addToPanel(pieceInfoSettingPanel, deleteInitialPointIdButton);

	}

	private void addToPanelFillRemaining(JPanel jp, Component component, int anchor, int fill) {
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.weightx = 1.0;
		c.gridy = componentCounter;
		c.weighty = 1.0;
		c.anchor = anchor;
		c.fill = fill;
		addToPanel(jp, component, c);
	}

	private void addToPanel(JPanel jp, Component component) {
		addToPanel(jp, component, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL);
	}

	private void addToPanel(JPanel jp, Component component, int anchor, int fill) {
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.weightx = 1.0;
		c.gridy = componentCounter;
		c.anchor = anchor;
		c.fill = fill;
		addToPanel(jp, component, c);
	}

	private void addToPanel(JPanel jp, Component component, GridBagConstraints c) {
		jp.add(component, c);
		componentCounter++;
	}

	private MouseAdapter createJPanelMouseListener(final JPanel jp) {
		if (jp == previewImagePanel)
			return new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					if (!jp.isEnabled())
						return;
					try {
						imageFile = UIUtility.showFileDirectorySelectionDialog(JFileChooser.FILES_ONLY);
						if (imageFile != null) {
							getSelectedProfile().pieceImage = ImageIO.read(imageFile);
							fixPieceImageRatio();
						}
					} catch (Exception ex) {
						ex.printStackTrace();
					}
					jp.repaint();
				}

				@Override
				public void mousePressed(MouseEvent e) {
					if (!jp.isEnabled())
						return;
					jp.setBorder(BorderFactory.createLoweredBevelBorder());
				}

				@Override
				public void mouseReleased(MouseEvent e) {
					if (!jp.isEnabled())
						return;
					jp.setBorder(BorderFactory.createRaisedBevelBorder());
				}
			};

		return null;
	}

	private Chreator.UIModule.AbstractModel.DocumentAdapter createDocumentAdapter(final JTextField jtf) {
		if (jtf == pieceColorBlueTextField || jtf == pieceColorRedTextField || jtf == pieceColorGreenTextField
				|| jtf == piecePicHeightTextField || jtf == piecePicWidthTextField)
			return new Chreator.UIModule.AbstractModel.DocumentAdapter() {
				public void editedUpdate(DocumentEvent e) {
					if (!inApplyingProfile && verifyTextFields(jtf)) {
						updateCurrentProfile();

						if (syncSizeRadioBtn.isSelected()
								&& (jtf == piecePicHeightTextField || jtf == piecePicWidthTextField))
							for (int i = 0; i < pieceProfiles.size(); i++) {
								if (jtf == piecePicHeightTextField)
									pieceProfiles.get(i).imageRelativeHeight = Double.parseDouble(jtf.getText());
								else
									pieceProfiles.get(i).imageRelativeWidth = Double.parseDouble(jtf.getText());
							}
					}
					PieceProfile profile = getSelectedProfile();
					if (profile != null) {
						sharedPieceHeight = profile.imageRelativeHeight;
						sharedPieceWidth = profile.imageRelativeWidth;
						sharedColor = profile.pieceColor;
					}
					previewImagePanel.repaint();
				}
			};
		else
			return null;
	}

	private Chreator.UIModule.AbstractModel.DocumentAdapter createDocumentAdapter(final SimpleCodeEditor sce) {
		if (sce == codeEditor)
			return new Chreator.UIModule.AbstractModel.DocumentAdapter() {
				public void editedUpdate(DocumentEvent e) {
					if (!inApplyingProfile)
						if (notSyncCodeRadioBtn.isSelected())
							updateCurrentProfile();
						else {
							updateCurrentProfile();

							PieceProfile profile = getSelectedProfile();
							for (int i = 0; i < pieceProfiles.size(); i++) {
								String profile2PieceClassName = pieceProfiles.get(i).pieceClassName;
								String profile2PlayerSide = pieceProfiles.get(i).playerSide;

								if (profile2PieceClassName.equals(profile.pieceClassName)
										&& !profile2PlayerSide.equals(profile.playerSide)) {
									String temp = sce.getText();
									int copyFrom = pieceProfiles.get(i).code.indexOf("Direction[] dirs;");
									int copyTo = temp.indexOf("Direction[] dirs;");

									pieceProfiles.get(i).code = pieceProfiles.get(i).code.substring(0, copyFrom)
											+ temp.substring(copyTo);
								}
							}
						}
				}
			};
		else
			return null;
	}

	private DocumentFilter createDocumentFilter(final JTextField jtf) {
		if (jtf == pieceClassNameField || jtf == piecePicLinkField)
			return new DocumentFilter() {
				public void replace(DocumentFilter.FilterBypass fb, int offset, int length, String text,
						AttributeSet attrs) throws BadLocationException {
					if (inApplyingProfile)
						super.replace(fb, offset, length, text, attrs);
				}

				public void insertString(DocumentFilter.FilterBypass fb, int offset, String string, AttributeSet attr)
						throws BadLocationException {
					if (inApplyingProfile)
						super.insertString(fb, offset, string, attr);
				}

				public void remove(DocumentFilter.FilterBypass fb, int offset, int length) throws BadLocationException {
					if (inApplyingProfile)
						super.remove(fb, offset, length);
				}
			};
		else
			return null;
	}

	private ActionListener createJRadioButtonActionListener(JRadioButton jrb) {
		if (jrb == syncCodeRadioBtn)
			return new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					notSyncCodeRadioBtn.setSelected(false);
					syncCodeRadioBtn.setSelected(true);

					JOptionPane.showMessageDialog(null,
							"After ticking this box, codes will be shared among the same type of pieces (independent of player side).");
				}
			};
		else if (jrb == notSyncCodeRadioBtn)
			return new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					notSyncCodeRadioBtn.setSelected(true);
					syncCodeRadioBtn.setSelected(false);

					JOptionPane.showMessageDialog(null,
							"After ticking this box, codes will NOT be shared among the same type of pieces (dependent of player side).");
				}
			};
		else
			return null;
	}

	private ActionListener createJButtonActionListener(JButton jb) {
		if (jb == addChessPieceButton)
			return new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					if (playerSidesList.getModel().getSize() == 0) {
						JOptionPane.showMessageDialog(UIHandler.getMainWindow(),
								"Please at least create one player side first.", "Error - Add chess piece - Chreator",
								JOptionPane.ERROR_MESSAGE);
					} else {
						String pieceName = UIUtility.showVariableInputDialog("New Chess Piece Model",
								"Input the name of chess piece", "", true);
						if (pieceName != null && !pieceName.toLowerCase().equals("piece")) {
							addChessPieceToList(pieceName);
						}
						if (pieceName != null && pieceName.toLowerCase().equals("piece"))
							JOptionPane.showMessageDialog(UIHandler.getMainWindow(), "Please choose another name.",
									"Name duplicates with super class \"Piece\"", JOptionPane.ERROR_MESSAGE);
					}
				}
			};
		else if (jb == addPlayerSideButton)
			return new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					String playerSide = UIUtility.showVariableInputDialog("New Player Side",
							"Input the name of player side", "Result name will be capitalized.", false);
					if (playerSide != null)
						addPlayerSideToList(playerSide.toUpperCase());
				}
			};
		else if (jb == deleteChessPieceButton)
			return new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					int result = JOptionPane.showConfirmDialog(UIHandler.getMainWindow(),
							"<html><center>Confirm to delete chess piece for all player sides?<br>This action cannot be redone.</html>",
							"Delete Chess Piece - Chreator", JOptionPane.YES_NO_OPTION,
							JOptionPane.INFORMATION_MESSAGE);
					if (result == JOptionPane.YES_OPTION)
						removeChessPieceFromList();
				}
			};
		else if (jb == deletePlayerSideButton)
			return new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					int result = JOptionPane.showConfirmDialog(UIHandler.getMainWindow(),
							"<html><center>Confirm to delete player side?<br>"
									+ "Each chess piece setting about this player side will be deleted and"
									+ "<br>this action cannot be redone.</html>",
							"Delete player side - Chreator", JOptionPane.YES_NO_OPTION);
					if (result == JOptionPane.YES_OPTION)
						removePlayerSideFromList();
				}
			};
		else if (jb == deletePiecePicButton)
			return new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					removePiecePic();
				}
			};
		else if (jb == addInitialPointIdButton)
			return new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					startAddInitialPointIdForPiece();
				}
			};
		else if (jb == deleteInitialPointIdButton)
			return new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					removeInitialPointIdFromList();
				}
			};
		else if (jb == setPieceSizeButton)
			return new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					PieceProfile profile = getSelectedProfile();
					if (profile != null) {
						pieceSetSizeWindow.setVisible(true);
						pieceSetSizePanel.updateContent();
						pieceSetSizePanel.setPiece(profile.imageRelativeWidth, profile.imageRelativeHeight,
								profile.pieceImage, profile.pieceColor, profile.pieceClassName);
						pieceSetSizeWindow.repaint();
					}
				}
			};
		else if (jb == defaultRatioButton)
			return new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					PieceProfile profile = getSelectedProfile();
					if (profile != null) {
						double chessBoardWidth = UIHandler.getInstance(null).getChessBoardPanel()
								.getChessBoardPreferredSize().getWidth(),
								chessBoardHeight = UIHandler.getInstance(null).getChessBoardPanel()
										.getChessBoardPreferredSize().getHeight(),
								piecePixelWidth = profile.imageRelativeWidth * chessBoardWidth,
								piecePixelHeight = profile.imageRelativeHeight * chessBoardHeight,
								fixedRelativeHeight = profile.imageRelativeHeight,
								fixedRelativeWidth = profile.imageRelativeWidth;

						if (profile.pieceImage == null) {
							if (piecePixelHeight > piecePixelWidth)
								fixedRelativeHeight = piecePixelWidth / chessBoardHeight;
							else
								fixedRelativeWidth = piecePixelHeight / chessBoardWidth;
						} else {
							double picCorrectTangent = 1.0 * profile.pieceImage.getHeight()
									/ profile.pieceImage.getWidth();
							if (piecePixelHeight / piecePixelWidth > picCorrectTangent)
								fixedRelativeHeight = piecePixelWidth * picCorrectTangent / chessBoardHeight;
							else
								fixedRelativeWidth = piecePixelHeight / picCorrectTangent / chessBoardWidth;

						}

						piecePicHeightTextField.setText(fixedRelativeHeight + "");
						piecePicWidthTextField.setText(fixedRelativeWidth + "");
					}
				}
			};
		else if (jb == addFunction1Button)
			return new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					PieceProfile profile = getSelectedProfile();
					if (profile != null) {
						int caretPos = codeEditor.getCaretPosition();
						String codeToAdd = "tempPoint = p.getImmediateNextPointAt(Direction.***);\n"
								+ "\t\tvalidMoves.add(tempPoint);\n";

						try {
							codeEditor.getStyledDocument().insertString(caretPos, codeToAdd, null);
						} catch (BadLocationException e1) {

						}
					}
				}
			};
		else if (jb == addFunction2Button)
			return new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					PieceProfile profile = getSelectedProfile();
					if (profile != null) {
						int caretPos = codeEditor.getCaretPosition();
						String codeToAdd = "dirs = new Direction[] {Direction.***, Direction.***, ...};\n"
								+ "\t\ttempPoint = p.getImmediateNextPointAt(dirs);\n"
								+ "\t\tvalidMoves.add(tempPoint);\n";

						try {
							codeEditor.getStyledDocument().insertString(caretPos, codeToAdd, null);
						} catch (BadLocationException e1) {

						}
					}
				}
			};
		else if (jb == addFunction3Button)
			return new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					PieceProfile profile = getSelectedProfile();
					if (profile != null) {
						int caretPos = codeEditor.getCaretPosition();
						String codeToAdd = "tempPointList = p.getPointsAlongDirection(Direction.***);\n"
								+ "\t\tfor (int i = 0; i < tempPointList.size(); i++) {\n"
								+ "\t\t\tvalidMoves.add(tempPointList.get(i));\n" + "\t\t}\n";

						try {
							codeEditor.getStyledDocument().insertString(caretPos, codeToAdd, null);
						} catch (BadLocationException e1) {

						}
					}
				}
			};
		else
			return null;
	}

	private ListSelectionListener createListSelectionListener(JList list) {
		if (list == playerSidesList || list == pieceClassNameList)
			return new ListSelectionListener() {
				@Override
				public void valueChanged(ListSelectionEvent e) {
					applySelectedProfile();
				}
			};
		return null;
	}

	private void removePiecePic() {
		PieceProfile profile = getSelectedProfile();
		profile.pieceImage = null;
		profile.sourcePicLink = "";
		piecePicLinkField.setText(profile.sourcePicLink);
		previewImagePanel.repaint();
	}

	private boolean verifyTextFields(final JTextField jtf) {
		if (jtf == pieceColorBlueTextField || jtf == pieceColorRedTextField || jtf == pieceColorGreenTextField) {
			jtf.setBackground(Color.RED);
			try {
				int result = Integer.parseInt(jtf.getText());
				if (result >= 0 && result <= 255)
					jtf.setBackground(Color.green);
			} catch (Exception ex) {
				return false;
			}
		} else if (jtf == piecePicHeightTextField || jtf == piecePicWidthTextField) {
			jtf.setBackground(Color.red);
			try {
				Double.parseDouble(jtf.getText());
				jtf.setBackground(Color.green);
			} catch (Exception ex) {
				return false;
			}
		}
		return true;
	}

	private void updatePreviewPanelContent(Graphics g) {
		PieceProfile profile = getSelectedProfile();
		if (profile != null) {
			double imageActualHeight = UIHandler.getInstance(null).getChessBoardPanel().getChessBoardPreferredSize()
					.getHeight() * profile.imageRelativeHeight,
					imageActualWidth = UIHandler.getInstance(null).getChessBoardPanel().getChessBoardPreferredSize()
							.getWidth() * profile.imageRelativeWidth;

			int scaledWidth = (int) Math.floor(imageActualWidth / imageActualHeight * previewImagePanel.getHeight()),
					scaledHeight = (int) Math
							.floor(imageActualHeight / imageActualWidth * previewImagePanel.getWidth());
			if (profile.pieceImage != null) {
				if (1.0 * imageActualHeight / imageActualWidth > 1.0 * previewImagePanel.getHeight()
						/ previewImagePanel.getWidth())
					g.drawImage(profile.pieceImage, (previewImagePanel.getWidth() - scaledWidth) / 2, 0, scaledWidth,
							previewImagePanel.getHeight(), null);
				else
					g.drawImage(profile.pieceImage, 0, (previewImagePanel.getHeight() - scaledHeight) / 2,
							previewImagePanel.getWidth(), scaledHeight, null);

			} else {
				g.setColor(profile.pieceColor);
				if (1.0 * imageActualHeight / imageActualWidth > 1.0 * previewImagePanel.getHeight()
						/ previewImagePanel.getWidth())
					g.fillOval((previewImagePanel.getWidth() - scaledWidth) / 2, 0, scaledWidth,
							previewImagePanel.getHeight());
				else
					g.fillOval(0, (previewImagePanel.getHeight() - scaledHeight) / 2, previewImagePanel.getWidth(),
							scaledHeight);

				Color c = profile.pieceColor;
				drawCenteredString(previewImagePanel, g,
						new Color((c.getRed() + 128) % 256, (c.getGreen() + 128) % 256, (c.getBlue() + 128) % 256),
						profile.pieceClassName);
			}
		} else
			drawCenteredString(previewImagePanel, g, Color.BLACK, "No selected chess piece");

	}

	private void drawCenteredString(JPanel jp, Graphics g, Color c, String s) {
		int width = g.getFontMetrics().stringWidth(s), height = g.getFont().getSize();
		g.setColor(c);
		g.drawString(s, (jp.getWidth() - width) / 2, (jp.getHeight() + height) / 2);
	}

	public void addPointIdToList(int id) {
		PieceProfile profile = getSelectedProfile();
		ArrayList<PieceProfile> profiles = getPieceProfiles();

		if (profile == null)
			return;
		if (profile.initialPointId == null)
			profile.initialPointId = new DefaultListModel<String>();
		DefaultListModel<String> listModel = profile.initialPointId;

		for (int i = 0; i < listModel.size(); i++) {
			String s = listModel.getElementAt(i);
			try {
				if (id == Integer.parseInt(s))
					return;
			} catch (Exception e) {
			}
		}

		int[] idBackup = new int[1];
		idBackup[0] = id;
		if (isInitialPointUsed(idBackup)) {
			int option = JOptionPane.showConfirmDialog(null,
					"There are already another piece using the selected point(s) as its initial point. Do you still want to continue? This may causes unexpected situation during compilation.",
					"Warning", JOptionPane.OK_CANCEL_OPTION);
			if (option != JOptionPane.OK_OPTION) {
				return;
			}
		}

		listModel.addElement(id + "");
	}

	public void addPlayerSideToList(String playerSide) {
		DefaultListModel<String> listModel = (DefaultListModel<String>) playerSidesList.getModel();
		for (int i = 0; i < listModel.size(); i++) {
			String s = listModel.getElementAt(i);
			if (s.equals(playerSide))
				return;
		}
		listModel.addElement(playerSide);
		playerSidesList.setSelectedIndex(listModel.getSize() - 1);
		for (int i = 0; i < pieceClassNameList.getModel().getSize(); i++) {
			String className = ((DefaultListModel<String>) pieceClassNameList.getModel()).getElementAt(i);

			String tempPlayerSide = playerSide.substring(0, 1).toUpperCase() + playerSide.substring(1).toLowerCase();
			String tempClassName = className.substring(0, 1).toUpperCase() + className.substring(1).toLowerCase();

			String codes = "" + "package Executable.PieceModel;\n" + "import java.util.*;\n"
					+ "import Executable.BoardModel.Edge.Direction;\n" + "import Executable.BoardModel.Point;\n"
					+ "public class " + tempPlayerSide + tempClassName + " extends Piece {\n" + "\n" + "\tpublic "
					+ tempPlayerSide + tempClassName + "(String s, String l, double w, double h, String n) {\n"
					+ "\t\tsuper(s, l, w, h, n);\n" + "\t}\n" + "\n" + "\t@Override\n"
					+ "\tpublic ArrayList<Point> moveInvolvingOtherPiece(Point p) {\n" + "\n"
					+ "\t\tArrayList<Point> validMoves = new ArrayList<>();\n" + "\t\tPoint tempPoint;\n"
					+ "\t\tArrayList<Point> tempPointList;\n" + "\t\tDirection[] dirs;\n"
					+ "// Do not alter the codes above----------------------------\n" + "\t\t\n" + "\t\t\n" + "\t\t\n"
					+ "// Do not alter the codes below----------------------------\n"
					+ "\t\tfor (int i = 0; i < validMoves.size(); i++) {\n" + "\t\t\tif (validMoves.get(i) == null) {\n"
					+ "\t\t\t\tvalidMoves.remove(i);\n" + "\t\t\t\ti--;\n" + "\t\t\t}\n" + "\t\t}\n" + "\n"
					+ "\t\tfor (int i = 0; i < validMoves.size(); i++) {\n"
					+ "\t\t\tif (validMoves.get(i).getPiece() != null && validMoves.get(i).getPiece().getSide() == this.getSide()) {\n"
					+ "\t\t\t\tvalidMoves.remove(i);\n" + "\t\t\t\ti--;\n" + "\t\t\t}\n" + "\t\t}\n" + "\t\n"
					+ "\treturn validMoves;\n" + "\t}\n" + "}\n";

			addToPieceProfileList(playerSide, className, codes);
		}
	}

	public void startAddInitialPointIdForPiece() {
		String dialogTitle = "Set initial piece placing point - Chreator";
		String[] selectOptions = { "set IDs though dialog", "choose points through GUI", "Cancel" };
		int result = JOptionPane.showOptionDialog(UIHandler.getMainWindow(),
				"<html><center>Select the way to set the initial piece placing points for the piece</html>",
				dialogTitle, JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, selectOptions,
				selectOptions[selectOptions.length - 1]);
		if (result == 0) {
			ArrayList<Integer> ids = showPointIDInputDialog(UIHandler.getInstance(null).getPointList());
			DefaultListModel<String> listModel = (DefaultListModel<String>) pieceInitialPointIdList.getModel();

			if (ids != null) {
				for (int i = ids.size() - 1; i >= 0; i--) {
					for (int j = 0; j < listModel.size(); j++) {
						try {
							if (ids.get(i).intValue() == Integer.parseInt(listModel.getElementAt(j)))
								ids.remove(i);
						} catch (Exception e) {
						}
					}
				}

				ArrayList<Point> pointList = UIHandler.getInstance(null).getPointList();
				for (int i = ids.size() - 1; i >= 0; i--) {
					boolean needTobeDeleted = true;
					for (Point p : pointList) {
						if (p.getId() == ids.get(i).intValue()) {
							needTobeDeleted = false;
							break;
						}
					}
					if (needTobeDeleted)
						ids.remove(i);
				}

				if (ids.size() > 0) {
					String msg = "";
					for (int i = 0; i < ids.size(); i++)
						msg = msg + ids.get(i).intValue() + (i == ids.size() - 1 ? "" : ", ");

					if (JOptionPane
							.showConfirmDialog(UIHandler.getMainWindow(),
									"<html><center>Are you sure to add the initial piece placing point IDs for this chess piece?<br>"
											+ msg + "</html",
									dialogTitle, JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION)
						for (Integer id : ids)
							addPointIdToList(id.intValue());
				} else {
					JOptionPane.showMessageDialog(UIHandler.getMainWindow(),
							"No new input initial piece placing point IDs can be added to the list", dialogTitle,
							JOptionPane.WARNING_MESSAGE);
				}
			}
		} else if (result == 1) {
			PieceProfile profile = getSelectedProfile();
			if (profile != null) {
				if (profile.initialPointId == null)
					profile.initialPointId = new DefaultListModel<String>();
				int[] ids = new int[profile.initialPointId.size()];
				for (int i = 0; i < ids.length; i++)
					try {
						ids[i] = Integer.parseInt(profile.initialPointId.getElementAt(i));
					} catch (Exception e) {
					}
				pieceSetInitialPointIDWindow.setVisible(true);
				pieceSetInitialPointPanel.updateContent(ids);
				pieceSetInitialPointIDWindow.repaint();
			}
		}

	}

	public void addChessPieceToList(String pieceClassName) {
		DefaultListModel<String> listModel = (DefaultListModel<String>) pieceClassNameList.getModel();
		for (int i = 0; i < listModel.size(); i++) {
			String s = listModel.getElementAt(i);
			if (s.equals(pieceClassName))
				return;
		}
		listModel.addElement(pieceClassName);

		pieceClassNameList.setSelectedIndex(listModel.size() - 1);
		for (int i = 0; i < playerSidesList.getModel().getSize(); i++) {
			String playerSide = ((DefaultListModel<String>) playerSidesList.getModel()).getElementAt(i);

			String tempPlayerSide = playerSide.substring(0, 1).toUpperCase() + playerSide.substring(1).toLowerCase();
			String tempClassName = pieceClassName.substring(0, 1).toUpperCase()
					+ pieceClassName.substring(1).toLowerCase();

			String codes = "" + "package Executable.PieceModel;\n" + "import java.util.*;\n"
					+ "import Executable.BoardModel.Edge.Direction;\n" + "import Executable.BoardModel.Point;\n"
					+ "public class " + tempPlayerSide + tempClassName + " extends Piece {\n" + "\n" + "\tpublic "
					+ tempPlayerSide + tempClassName + "(String s, String l, double w, double h, String n) {\n"
					+ "\t\tsuper(s, l, w, h, n);\n" + "\t}\n" + "\n" + "\t@Override\n"
					+ "\tpublic ArrayList<Point> moveInvolvingOtherPiece(Point p) {\n" + "\n"
					+ "\t\tArrayList<Point> validMoves = new ArrayList<>();\n" + "\t\tPoint tempPoint;\n"
					+ "\t\tArrayList<Point> tempPointList;\n" + "\t\tDirection[] dirs;\n"
					+ "// Do not alter the codes above----------------------------\n" + "\t\t\n" + "\t\t\n" + "\t\t\n"
					+ "// Do not alter the codes below----------------------------\n"
					+ "\t\tfor (int i = 0; i < validMoves.size(); i++) {\n" + "\t\t\tif (validMoves.get(i) == null) {\n"
					+ "\t\t\t\tvalidMoves.remove(i);\n" + "\t\t\t\ti--;\n" + "\t\t\t}\n" + "\t\t}\n" + "\n"
					+ "\t\tfor (int i = 0; i < validMoves.size(); i++) {\n"
					+ "\t\t\tif (validMoves.get(i).getPiece() != null && validMoves.get(i).getPiece().getSide() == this.getSide()) {\n"
					+ "\t\t\t\tvalidMoves.remove(i);\n" + "\t\t\t\ti--;\n" + "\t\t\t}\n" + "\t\t}\n" + "\t\n"
					+ "\treturn validMoves;\n" + "\t}\n" + "}\n";

			addToPieceProfileList(playerSide, pieceClassName, codes);
		}
	}

	public void removeInitialPointIdFromList() {
		DefaultListModel<String> listModel = (DefaultListModel<String>) pieceInitialPointIdList.getModel();
		int[] indices = pieceInitialPointIdList.getSelectedIndices();

		for (int index = indices.length - 1; index >= 0; index--)
			listModel.remove(indices[index]);

	}

	public void removePlayerSideFromList() {
		int[] selectedIndices = playerSidesList.getSelectedIndices();

		if (selectedIndices.length > 0) {
			DefaultListModel<String> listModel = (DefaultListModel<String>) playerSidesList.getModel();
			for (int i = selectedIndices.length - 1; i >= 0; i--) {
				for (int j = pieceProfiles.size() - 1; j >= 0; j--)
					if (pieceProfiles.get(j).playerSide.equals(listModel.getElementAt(selectedIndices[i])))
						pieceProfiles.remove(j);
				listModel.remove(selectedIndices[i]);
			}

			if (listModel.size() > 0)
				playerSidesList.setSelectedIndex(listModel.size() - 1);
		}
	}

	public void removeChessPieceFromList() {
		int[] selectedIndices = pieceClassNameList.getSelectedIndices();

		if (selectedIndices.length > 0) {
			DefaultListModel<String> listModel = (DefaultListModel<String>) pieceClassNameList.getModel();
			for (int i = selectedIndices.length - 1; i >= 0; i--) {
				for (int j = pieceProfiles.size() - 1; j >= 0; j--)
					if (pieceProfiles.get(j).pieceClassName.equals(listModel.getElementAt(selectedIndices[i])))
						pieceProfiles.remove(j);
				listModel.remove(selectedIndices[i]);
			}

			if (listModel.size() > 0)
				pieceClassNameList.setSelectedIndex(listModel.size() - 1);
		}
	}

	public ArrayList<PieceProfile> getPieceProfiles() {
		return pieceProfiles;
	}

	public void setPieceProfiles(ArrayList<PieceProfile> profiles) {
		pieceProfiles = profiles;
		((DefaultListModel<String>) playerSidesList.getModel()).clear();
		((DefaultListModel<String>) pieceClassNameList.getModel()).clear();
		for (PieceProfile profile : pieceProfiles) {
			addPlayerSideToList(profile.playerSide);
			addChessPieceToList(profile.pieceClassName);
		}
	}

	private PieceProfile addToPieceProfileList(String playerSide, String pieceClassName, String codes) {
		for (PieceProfile profile : pieceProfiles)
			if (profile.playerSide.equals(playerSide) && profile.pieceClassName.equals(pieceClassName))
				return null;
		PieceProfile profile = new PieceProfile(playerSide, pieceClassName);
		profile.imageRelativeHeight = sharedPieceHeight;
		profile.imageRelativeWidth = sharedPieceWidth;
		profile.pieceColor = sharedColor;
		profile.code = codes;
		pieceProfiles.add(profile);
		return profile;
	}

	public void addToPieceProfileList(PieceProfile profile) {
		DefaultListModel<String> listModel = (DefaultListModel<String>) pieceClassNameList.getModel();

		boolean canAdd = true;
		for (int i = 0; i < listModel.size(); i++) {
			String s = listModel.getElementAt(i);
			if (s.equals(profile.pieceClassName))
				canAdd = false;
		}
		if (canAdd)
			listModel.addElement(profile.pieceClassName);

		pieceProfiles.add(profile);
	}

	public PieceProfile getPieceProfile(String playerSide, String className) {
		for (PieceProfile profile : pieceProfiles)
			if (profile.playerSide.equals(playerSide) && profile.pieceClassName.equals(className))
				return profile;

		String tempPlayerSide = playerSide.substring(0, 1).toUpperCase() + playerSide.substring(1).toLowerCase();
		String tempClassName = className.substring(0, 1).toUpperCase() + className.substring(1).toLowerCase();

		String codes = "" + "package Executable.PieceModel;\n" + "import java.util.*;\n"
				+ "import Executable.BoardModel.Edge.Direction;\n" + "import Executable.BoardModel.Point;\n"
				+ "public class " + tempPlayerSide + tempClassName + " extends Piece {\n" + "\n" + "\tpublic "
				+ tempPlayerSide + tempClassName + "(String s, String l, double w, double h, String n) {\n"
				+ "\t\tsuper(s, l, w, h, n);\n" + "\t}\n" + "\n" + "\t@Override\n"
				+ "\tpublic ArrayList<Point> moveInvolvingOtherPiece(Point p) {\n" + "\n"
				+ "\t\tArrayList<Point> validMoves = new ArrayList<>();\n" + "\t\tPoint tempPoint;\n"
				+ "\t\tArrayList<Point> tempPointList;\n" + "\t\tDirection[] dirs;\n"
				+ "// Do not alter the codes above----------------------------\n" + "\t\t\n" + "\t\t\n" + "\t\t\n"
				+ "// Do not alter the codes below----------------------------\n"
				+ "\t\tfor (int i = 0; i < validMoves.size(); i++) {\n" + "\t\t\tif (validMoves.get(i) == null) {\n"
				+ "\t\t\t\tvalidMoves.remove(i);\n" + "\t\t\t\ti--;\n" + "\t\t\t}\n" + "\t\t}\n" + "\n"
				+ "\t\tfor (int i = 0; i < validMoves.size(); i++) {\n"
				+ "\t\t\tif (validMoves.get(i).getPiece() != null && validMoves.get(i).getPiece().getSide() == this.getSide()) {\n"
				+ "\t\t\t\tvalidMoves.remove(i);\n" + "\t\t\t\ti--;\n" + "\t\t\t}\n" + "\t\t}\n" + "\t\n"
				+ "\treturn validMoves;\n" + "\t}\n" + "}\n";

		return addToPieceProfileList(playerSide, className, codes);
	}

	public PieceProfile getSelectedProfile() {
		if (playerSidesList.getSelectedIndices().length == 0 || pieceClassNameList.getSelectedIndices().length == 0)
			return null;
		else
			return getPieceProfile(
					((DefaultListModel<String>) playerSidesList.getModel())
							.getElementAt(playerSidesList.getSelectedIndices()[0]),
					((DefaultListModel<String>) pieceClassNameList.getModel())
							.getElementAt(pieceClassNameList.getSelectedIndices()[0]));
	}

	private void updateCurrentProfile() {
		try {
			PieceProfile profile = getSelectedProfile();
			if (imageFile != null && profile.sourcePicLink.equals(""))
				profile.sourcePicLink = imageFile.getAbsolutePath();
			profile.pieceColor = new Color(Integer.parseInt(pieceColorRedTextField.getText()),
					Integer.parseInt(pieceColorGreenTextField.getText()),
					Integer.parseInt(pieceColorBlueTextField.getText()));
			profile.imageRelativeHeight = Double.parseDouble(piecePicHeightTextField.getText());
			profile.imageRelativeWidth = Double.parseDouble(piecePicWidthTextField.getText());
			profile.initialPointId = (DefaultListModel<String>) pieceInitialPointIdList.getModel();
			profile.code = codeEditor.getText();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void applySelectedProfile() {
		inApplyingProfile = true;
		if (playerSidesList.getModel().getSize() == 0 || pieceClassNameList.getModel().getSize() == 0
				|| playerSidesList.getSelectedIndices().length == 0
				|| pieceClassNameList.getSelectedIndices().length == 0) {
			pieceClassNameField.setText("");
			piecePicLinkField.setText("");
			pieceColorGreenTextField.setText("");
			pieceColorBlueTextField.setText("");
			pieceColorRedTextField.setText("");
			piecePicWidthTextField.setText("");
			piecePicHeightTextField.setText("");
			codeEditor.setText("");
			pieceInitialPointIdList.setModel(new DefaultListModel<String>());
			pieceColorGreenTextField.setBackground(jtfDefaultBackground);
			pieceColorBlueTextField.setBackground(jtfDefaultBackground);
			pieceColorRedTextField.setBackground(jtfDefaultBackground);
			piecePicHeightTextField.setBackground(jtfDefaultBackground);
			piecePicWidthTextField.setBackground(jtfDefaultBackground);

			allComponentInPanelSetEnabled(pieceProfileEditorPanel, false);
			codeEditor.setEnabled(false);
			deletePiecePicButton.setEnabled(false);
			previewImagePanel.setEnabled(false);
		} else {
			PieceProfile profile = getSelectedProfile();
			pieceClassNameField.setText("(" + profile.playerSide + " side) " + profile.pieceClassName);
			piecePicLinkField.setText(profile.sourcePicLink);
			pieceInitialPointIdList.setModel(profile.initialPointId);
			pieceColorRedTextField.setText(profile.pieceColor.getRed() + "");
			pieceColorGreenTextField.setText(profile.pieceColor.getGreen() + "");
			pieceColorBlueTextField.setText(profile.pieceColor.getBlue() + "");
			piecePicWidthTextField.setText(profile.imageRelativeWidth + "");
			piecePicHeightTextField.setText(profile.imageRelativeHeight + "");
			codeEditor.setText(profile.code);
			verifyTextFields(pieceColorGreenTextField);
			verifyTextFields(pieceColorRedTextField);
			verifyTextFields(pieceColorBlueTextField);
			verifyTextFields(piecePicWidthTextField);
			verifyTextFields(piecePicHeightTextField);

			codeEditor.setEnabled(true);
			allComponentInPanelSetEnabled(pieceProfileEditorPanel, true);
			deletePiecePicButton.setEnabled(true);
			previewImagePanel.setEnabled(true);
		}
		previewImagePanel.repaint();
		inApplyingProfile = false;
	}

	private ArrayList<Integer> showPointIDInputDialog(ArrayList<Point> pointList) {
		String s = JOptionPane.showInputDialog(UIHandler.getMainWindow(),
				"<html><center>Enter the point ID(s) that you would like to place this chess piece to,"
						+ "<br>at the start of the game.</html>",
				"Add initial point ID - Chreator", JOptionPane.QUESTION_MESSAGE);
		return s == null ? null : UIUtility.getPositiveIntegerListFromString(s);
	}

	private void allComponentInPanelSetEnabled(Component component, boolean enabled) {
		if (component instanceof Container)
			for (Component c : ((Container) component).getComponents())
				if (c instanceof Container)
					allComponentInPanelSetEnabled(c, enabled);
				else
					c.setEnabled(enabled);

		component.setEnabled(enabled);
	}

	private void fixPieceImageRatio() {
		PieceProfile profile = getSelectedProfile();
		if (profile.pieceImage == null || profile == null)
			return;
		Dimension boardSize = UIHandler.getInstance(null).getChessBoardPanel().getChessBoardPreferredSize();
		double imaginaryHeight = profile.imageRelativeHeight * boardSize.height,
				imaginaryWidth = profile.imageRelativeWidth * boardSize.width,
				imaginaryTangent = imaginaryHeight / imaginaryWidth,
				previewImageTangent = 1.0 * profile.pieceImage.getHeight() / profile.pieceImage.getWidth(),
				imageActualHeight = imaginaryTangent > previewImageTangent ? imaginaryWidth * previewImageTangent
						: imaginaryHeight,
				imageActualWidth = imaginaryTangent > previewImageTangent ? imaginaryWidth
						: imaginaryHeight / previewImageTangent;
		piecePicWidthTextField.setText((imageActualWidth / boardSize.getWidth()) + "");
		piecePicHeightTextField.setText((imageActualHeight / boardSize.getHeight()) + "");
	}

	private boolean isInitialPointUsed(int[] ids) {
		for (int id : ids) {
			for (PieceProfile pieceProfile : getPieceProfiles()) {
				for (int i = 0; i < pieceProfile.initialPointId.size(); i++) {
					if (pieceProfile.initialPointId.getElementAt(i).equals(id + "")) {
						return true;
					}
				}
			}
		}
		return false;
	}

	public ListModel getPlayerSideList() {
		return playerSidesList.getModel();
	}

	public ListModel getPieceInitialPointIdList() {
		return pieceInitialPointIdList.getModel();
	}
}
