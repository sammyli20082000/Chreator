package Chreator.UIModule;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.ToolTipManager;
import javax.swing.border.EtchedBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import Chreator.ObjectModel.PieceProfile;
import Chreator.ObjectModel.Point;

/**
 * Created by him on 2015/12/20.
 */
public class ChessPiecePanel extends JPanel {
    public interface EventCallback {

    }

    public static String tabName = "Chess Piece";
    public static double sharedPieceHeight = 0.1, sharedPieceWidth = 0.1;
    private EventCallback callback;
    private JPanel pieceContentHolderPanel, pieceListAndPreviewPanel, pieceInfoSettingPanel, codeInserterPanel, previewImagePanel;
    private int componentCounter = 0;
    private Dimension previewImagePanelSize = new Dimension(200, 200);
    private BufferedImage previewImage;
    private Color previewImageColor;
    private String previewImageName;
    private String browseDir;
    private File imageFile;
    private JList playerSideList, pieceClassNameList, pieceInitialPointIdList;
    private JButton addChessPieceButton, addPlayerSideButton, deleteChessPieceButton, deletePlayerSideButton,
            setPieceSizeButton, addInitialPointIdButton, deleteInitialPointIdButton, deletePiecePicButton;
    private JTextField pieceColorGreenTextField, pieceColorBlueTextField, pieceColorRedTextField,
            piecePicHeightTextField, piecePicWidthTextField;
    private SimpleCodeEditor codeEditor;
    private JLabel pieceClassNameLabel, piecePicLinkLabel;
    private ArrayList<PieceProfile> pieceProfiles;
    private boolean autoSave = true;
    private Color jtfDefaultBackground = new JTextField().getBackground();

    public ChessPiecePanel(EventCallback eventCallback) {
        callback = eventCallback;
        pieceProfiles = new ArrayList<PieceProfile>();
        setupLayout();
    }

    private void setupLayout() {
        setLayout(new BorderLayout());
        pieceContentHolderPanel = new JPanel(new BorderLayout());
        pieceListAndPreviewPanel = new JPanel(new GridBagLayout());
        pieceListAndPreviewPanel.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
        pieceListAndPreviewPanel.setBackground(Color.lightGray);
        JScrollPane scrollPane = new JScrollPane(pieceListAndPreviewPanel,
                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        prepareChessListAndPreviewPanel();
        prepareChessContentHolderPanel();
        setupComponentEventListeners();

        pieceInfoSettingPanel.setBorder(BorderFactory.createRaisedBevelBorder());
        codeEditor.setBorder(BorderFactory.createLineBorder(Color.GRAY));

        add(scrollPane, BorderLayout.LINE_START);
        add(pieceContentHolderPanel, BorderLayout.CENTER);
    }

    private void setupComponentEventListeners() {
        previewImagePanel.addMouseListener(createJPanelMouseListener(previewImagePanel));
    }

    private void prepareChessListAndPreviewPanel() {
        componentCounter = 0;
        previewImagePanel = new JPanel() {
            public void paintComponent(Graphics g) {
                super.paintComponent(g);
                updatePreviewPanelContent(g);
            }
        };
        previewImagePanel.setPreferredSize(previewImagePanelSize);
        previewImagePanel.setBackground(Color.lightGray);
        previewImagePanel.setBorder(BorderFactory.createRaisedBevelBorder());

        pieceClassNameList = new JList(new DefaultListModel<String>());
        pieceClassNameList.setFixedCellWidth(1);
        pieceClassNameList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        pieceClassNameList.addListSelectionListener(createListSelectionListener(pieceClassNameList));

        playerSideList = new JList(new DefaultListModel<String>());
        playerSideList.setFixedCellWidth(1);
        playerSideList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        playerSideList.addListSelectionListener(createListSelectionListener(playerSideList));

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

        addToPanel(pieceListAndPreviewPanel, new JLabel("Selected Chess Piece Image Preview"), GridBagConstraints.CENTER, GridBagConstraints.NONE);
        addToPanel(pieceListAndPreviewPanel, previewImagePanel);
        addToPanel(pieceListAndPreviewPanel, deletePiecePicButton);

        addToPanel(pieceListAndPreviewPanel, new JLabel("<html><br>All Player Side</html>"), GridBagConstraints.CENTER, GridBagConstraints.NONE);
        addToPanel(pieceListAndPreviewPanel, new JScrollPane(playerSideList), GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL);
        addToPanel(pieceListAndPreviewPanel, addPlayerSideButton);
        addToPanel(pieceListAndPreviewPanel, deletePlayerSideButton);

        addToPanel(pieceListAndPreviewPanel, new JLabel("<html><br>All Chess Piece</html>"), GridBagConstraints.CENTER, GridBagConstraints.NONE);
        addToPanelFillRemaining(pieceListAndPreviewPanel, new JScrollPane(pieceClassNameList), GridBagConstraints.CENTER, GridBagConstraints.BOTH);
        addToPanel(pieceListAndPreviewPanel, addChessPieceButton);
        addToPanel(pieceListAndPreviewPanel, deleteChessPieceButton);
    }

    private void prepareChessContentHolderPanel() {
        pieceInfoSettingPanel = new JPanel(new GridBagLayout());
        codeInserterPanel = new JPanel(new GridBagLayout());
        codeEditor = new SimpleCodeEditor();

        JScrollPane scrollPane = new JScrollPane(codeInserterPanel,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        codeInserterPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0,
                scrollPane.getVerticalScrollBar().getPreferredSize().width));

        prepareChessInfoSettingPanel();
        prepareCodeInserterPanel();

        pieceContentHolderPanel.add(pieceInfoSettingPanel, BorderLayout.PAGE_START);
        pieceContentHolderPanel.add(scrollPane, BorderLayout.LINE_END);
        pieceContentHolderPanel.add(codeEditor, BorderLayout.CENTER);
    }

    private void prepareCodeInserterPanel() {
        componentCounter = 0;
        addToPanel(codeInserterPanel, new JLabel("Insert functional code:"), GridBagConstraints.CENTER, GridBagConstraints.NONE);
        addToPanel(codeInserterPanel, createInserterRow(new JButton("Function 1"), "Test description for function 1"));
        addToPanel(codeInserterPanel, createInserterRow(new JButton("Function 2"), "Test description for function 2"));

        addToPanelFillRemaining(codeInserterPanel, new JLabel(" "), GridBagConstraints.CENTER, GridBagConstraints.VERTICAL);
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
        JPanel container;
        pieceInitialPointIdList = new JList(new DefaultListModel<String>());
        GridBagConstraints c = new GridBagConstraints();
        setPieceSizeButton = new JButton("Piece set size");
        addInitialPointIdButton = new JButton("Add Point ID");
        deleteInitialPointIdButton = new JButton("Delete Point ID");
        pieceColorRedTextField = new JTextField();
        pieceColorGreenTextField = new JTextField();
        pieceColorBlueTextField = new JTextField();
        piecePicHeightTextField = new JTextField();
        piecePicWidthTextField = new JTextField();
        pieceClassNameLabel = new JLabel("Piece Name: ");
        piecePicLinkLabel = new JLabel("Image Link:");

        pieceInitialPointIdList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        pieceInitialPointIdList.setFixedCellWidth(1);
        pieceColorRedTextField.setColumns(3);
        pieceColorBlueTextField.setColumns(3);
        pieceColorGreenTextField.setColumns(3);
        piecePicHeightTextField.setColumns(15);
        piecePicWidthTextField.setColumns(15);

        pieceColorBlueTextField.getDocument().addDocumentListener(createDocumentAdapter(pieceColorBlueTextField));
        pieceColorGreenTextField.getDocument().addDocumentListener(createDocumentAdapter(pieceColorGreenTextField));
        pieceColorRedTextField.getDocument().addDocumentListener(createDocumentAdapter(pieceColorRedTextField));
        piecePicHeightTextField.getDocument().addDocumentListener(createDocumentAdapter(piecePicHeightTextField));
        piecePicWidthTextField.getDocument().addDocumentListener(createDocumentAdapter(piecePicWidthTextField));

        addInitialPointIdButton.addActionListener(createJButtonActionListener(addInitialPointIdButton));
        deleteInitialPointIdButton.addActionListener(createJButtonActionListener(deleteInitialPointIdButton));

        container = new JPanel(new GridBagLayout());
        componentCounter = 0;
        addToPanel(container, pieceClassNameLabel, GridBagConstraints.LINE_START, GridBagConstraints.NONE);
        addToPanel(container, piecePicLinkLabel, GridBagConstraints.LINE_START, GridBagConstraints.NONE);
        addToPanel(container, new JLabel("Piece Color (if no piece image)"), GridBagConstraints.LINE_START, GridBagConstraints.NONE);

        JPanel temp = new JPanel(new FlowLayout());
        temp.add(new JLabel("R(0~255):"));
        temp.add(pieceColorRedTextField);
        temp.add(new JLabel("    G(0~255):"));
        temp.add(pieceColorGreenTextField);
        temp.add(new JLabel("    B(0~255):"));
        temp.add(pieceColorBlueTextField);
        addToPanel(container, temp, GridBagConstraints.LINE_START, GridBagConstraints.NONE);
        addToPanel(container, new JLabel("Piece Size"), GridBagConstraints.LINE_START, GridBagConstraints.NONE);

        temp = new JPanel(new FlowLayout());
        temp.add(new JLabel("Width(0~1): "));
        temp.add(piecePicWidthTextField);
        addToPanel(container, temp, GridBagConstraints.LINE_START, GridBagConstraints.NONE);

        temp = new JPanel(new FlowLayout());
        temp.add(new JLabel("Height(0~1):"));
        temp.add(piecePicHeightTextField);
        addToPanel(container, temp, GridBagConstraints.LINE_START, GridBagConstraints.NONE);

        addToPanel(container, setPieceSizeButton, GridBagConstraints.LINE_START, GridBagConstraints.NONE);

        c.gridx = 0;
        c.gridy = 0;
        c.insets = new Insets(5, 5, 5, 5);
        c.weightx = 0;
        c.anchor = GridBagConstraints.FIRST_LINE_START;
        pieceInfoSettingPanel.add(container, c);

        container = new JPanel(new GridBagLayout());
        componentCounter = 0;
        addToPanel(container, new JLabel("Initial Piece Placing Point Point ID"), GridBagConstraints.CENTER, GridBagConstraints.NONE);
        addToPanel(container, new JScrollPane(pieceInitialPointIdList), GridBagConstraints.CENTER, GridBagConstraints.BOTH);
        addToPanel(container, addInitialPointIdButton, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL);
        addToPanel(container, deleteInitialPointIdButton, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL);

        c.gridx++;
        c.weightx = 1.0;
        c.anchor = GridBagConstraints.FIRST_LINE_START;
        c.fill = GridBagConstraints.NONE;
        pieceInfoSettingPanel.add(container, c);
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
                    JFileChooser fileChooser = new JFileChooser();
                    if (browseDir != null) fileChooser.setCurrentDirectory(new File(browseDir));
                    int returnValue = fileChooser.showOpenDialog(null);
                    if (returnValue == JFileChooser.APPROVE_OPTION) {
                        imageFile = fileChooser.getSelectedFile();
                        browseDir = imageFile.getParent();
                        try {
                            previewImage = ImageIO.read(imageFile);
                        } catch (Exception ex) {
                            previewImage = null;
                            ex.printStackTrace();
                        }
                        UIHandler.refreshWindow();
                    }
                }

                @Override
                public void mousePressed(MouseEvent e) {
                    jp.setBorder(BorderFactory.createLoweredBevelBorder());
                }

                @Override
                public void mouseReleased(MouseEvent e) {
                    jp.setBorder(BorderFactory.createRaisedBevelBorder());
                }
            };

        return null;
    }

    private DocumentAdapter createDocumentAdapter(final JTextField jtf) {
        if (jtf == pieceColorBlueTextField || jtf == pieceColorRedTextField || jtf == pieceColorGreenTextField
                || jtf == piecePicHeightTextField || jtf == piecePicWidthTextField)
            return new DocumentAdapter() {
                public void editedUpdate(DocumentEvent e) {
                    if (autoSave && verifyTextFields(jtf)) updateCurrentProfile();
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
                    if (playerSideList.getModel().getSize() == 0) {
                        JOptionPane.showMessageDialog(UIHandler.getMainWindow(),
                                "Please at least create one player side first.",
                                "Error - Add chess piece - Chreator",
                                JOptionPane.ERROR_MESSAGE);
                    } else {
                        String pieceName = UIHandler.showVariableInputDialog("New Chess Piece Model", "Input the name of chess piece", "");
                        if (pieceName != null) addChessPieceToList(pieceName);
                    }
                }
            };
        else if (jb == addPlayerSideButton)
            return new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String playerSide = UIHandler.showVariableInputDialog("New Player Side", "Input the name of player side", "Result name will be capitalized.");
                    if (playerSide != null) addPlayerSideToList(playerSide.toUpperCase());
                }
            };
        else if (jb == deleteChessPieceButton)
            return new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    int result = JOptionPane.showConfirmDialog(UIHandler.getMainWindow(),
                            "<html><center>Confirm to delete chess piece for all player sides?<br>This action cannot be redone.</html>",
                            "Delete Chess Piece - Chreator",
                            JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE);
                    if (result == JOptionPane.YES_OPTION) removeChessPieceFromList();
                }
            };
        else if (jb == deletePlayerSideButton)
            return new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    int result = JOptionPane.showConfirmDialog(UIHandler.getMainWindow(),
                            "<html><center>Confirm to delete player side?<br>" +
                                    "Each chess piece setting about this player side will be deleted and" +
                                    "<br>this action cannot be redone.</html>",
                            "Delete player side - Chreator", JOptionPane.YES_NO_OPTION);
                    if (result == JOptionPane.YES_OPTION) removePlayerSideFromList();
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
                    addInitialPointIdToList();
                }
            };
        else if (jb == deleteInitialPointIdButton)
            return new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    removeInitialPointIdFromList();
                }
            };
        else
            return null;
    }

    private ListSelectionListener createListSelectionListener(JList list) {
        if (list == playerSideList || list == pieceClassNameList)
            return new ListSelectionListener() {
                @Override
                public void valueChanged(ListSelectionEvent e) {
                    applySelectedProfile();
                }
            };
        return null;
    }

    private void removePiecePic() {
        previewImage = null;
        UIHandler.refreshWindow();
    }

    private boolean verifyTextFields(final JTextField jtf) {
        if (jtf == pieceColorBlueTextField || jtf == pieceColorRedTextField || jtf == pieceColorGreenTextField) {
            jtf.setBackground(Color.RED);
            try {
                int result = Integer.parseInt(jtf.getText());
                if (result >= 0 && result <= 255) jtf.setBackground(Color.green);
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
        if (previewImage != null) {
            if (1.0 * previewImage.getHeight() / previewImage.getWidth() > 1.0 * previewImagePanel.getHeight() / previewImagePanel.getWidth()) {
                int scaledWidth = previewImagePanel.getHeight() * previewImage.getWidth() / previewImage.getHeight();
                g.drawImage(previewImage, (previewImagePanel.getWidth() - scaledWidth) / 2, 0, scaledWidth, previewImagePanel.getHeight(), null);
            } else {
                int scaledHeight = previewImagePanel.getWidth() * previewImage.getHeight() / previewImage.getWidth();
                g.drawImage(previewImage, 0, (previewImagePanel.getHeight() - scaledHeight) / 2, previewImagePanel.getWidth(), scaledHeight, null);
            }
        } else if (!(previewImageName == null || previewImageColor == null)) {
            g.setColor(previewImageColor);
            g.fillOval(0, 0, previewImagePanel.getWidth(), previewImagePanel.getHeight());
            drawCenteredString(previewImagePanel, g, Color.BLACK, previewImageName);
        } else
            drawCenteredString(previewImagePanel, g, Color.BLACK, "No selected chess piece");

    }

    private void drawCenteredString(JPanel jp, Graphics g, Color c, String s) {
        int width = g.getFontMetrics().stringWidth(s), height = g.getFont().getSize();
        g.setColor(c);
        g.drawString(s, (jp.getWidth() - width) / 2, (jp.getHeight() + height) / 2);
    }

    public void addPlayerSideToList(String playerSide) {
        DefaultListModel<String> listModel = (DefaultListModel<String>) playerSideList.getModel();
        for (int i = 0; i < listModel.size(); i++) {
            String s = listModel.getElementAt(i);
            if (s.equals(playerSide)) return;
        }
        listModel.addElement(playerSide);
        playerSideList.setSelectedIndex(listModel.getSize() - 1);
        for (int i = 0; i < pieceClassNameList.getModel().getSize(); i++) {
            String className = ((DefaultListModel<String>) pieceClassNameList.getModel()).getElementAt(i);
            addToPieceProfileList(playerSide, className);
        }
    }

    public void addInitialPointIdToList() {
        Integer id = showPointIDInputDialog(UIHandler.getInstance((UIHandler.EventCallback) callback).getPointList());
        if (id == null) return;
        DefaultListModel<String> listModel = (DefaultListModel<String>) pieceInitialPointIdList.getModel();
        for (int i = 0; i < listModel.size(); i++) {
            try {
                if (Integer.parseInt(listModel.getElementAt(i)) == id.intValue()) return;
            } catch (Exception e) {
            }
        }
        listModel.addElement(id + "");
    }

    public void removeInitialPointIdFromList() {
        DefaultListModel<String> listModel = (DefaultListModel<String>) pieceInitialPointIdList.getModel();
        int[] indices = pieceInitialPointIdList.getSelectedIndices();

        for (int index = indices.length - 1; index >= 0; index--)
            listModel.remove(indices[index]);

    }

    public void removePlayerSideFromList() {
        int[] selectedIndices = playerSideList.getSelectedIndices();

        if (selectedIndices.length > 0) {
            DefaultListModel<String> listModel = (DefaultListModel<String>) playerSideList.getModel();
            for (int i = selectedIndices.length - 1; i >= 0; i--) {
                for (int j = pieceProfiles.size() - 1; j >= 0; j--)
                    if (pieceProfiles.get(j).playerSide.equals(listModel.getElementAt(selectedIndices[i])))
                        pieceProfiles.remove(j);
                listModel.remove(selectedIndices[i]);
            }

            if (listModel.size() > 0)
                playerSideList.setSelectedIndex(listModel.size() - 1);
        }
    }

    public void addChessPieceToList(String pieceClassName) {
        DefaultListModel<String> listModel = (DefaultListModel<String>) pieceClassNameList.getModel();
        for (int i = 0; i < listModel.size(); i++) {
            String s = listModel.getElementAt(i);
            if (s.equals(pieceClassName)) return;
        }
        listModel.addElement(pieceClassName);
        pieceClassNameList.setSelectedIndex(listModel.size() - 1);
        for (int i = 0; i < playerSideList.getModel().getSize(); i++) {
            String playerSide = ((DefaultListModel<String>) playerSideList.getModel()).getElementAt(i);
            addToPieceProfileList(playerSide, pieceClassName);
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
        ((DefaultListModel<String>) playerSideList.getModel()).clear();
        ((DefaultListModel<String>) pieceClassNameList.getModel()).clear();
        for (PieceProfile profile : pieceProfiles) {
            addPlayerSideToList(profile.playerSide);
            addChessPieceToList(profile.pieceClassName);
        }
    }

    public PieceProfile addToPieceProfileList(String playerSide, String pieceClassName) {
        for (PieceProfile profile : pieceProfiles)
            if (profile.playerSide.equals(playerSide) && profile.pieceClassName.equals(pieceClassName))
                return null;
        PieceProfile profile = new PieceProfile(playerSide, pieceClassName);
        pieceProfiles.add(profile);
        return profile;
    }

    public PieceProfile getPieceProfile(String playerSide, String className) {
        for (PieceProfile profile : pieceProfiles)
            if (profile.playerSide.equals(playerSide) && profile.pieceClassName.equals(className))
                return profile;
        return addToPieceProfileList(playerSide, className);
    }

    private void updateCurrentProfile() {
        try {
            PieceProfile profile = getPieceProfile(
                    ((DefaultListModel<String>) playerSideList.getModel()).getElementAt(playerSideList.getSelectedIndices()[0]),
                    ((DefaultListModel<String>) pieceClassNameList.getModel()).getElementAt(pieceClassNameList.getSelectedIndices()[0])
            );
            profile.sourcePicLink = imageFile == null ? "" : imageFile.getAbsolutePath();
            profile.pieceColor = new Color(
                    Integer.parseInt(pieceColorRedTextField.getText()),
                    Integer.parseInt(pieceColorGreenTextField.getText()),
                    Integer.parseInt(pieceColorBlueTextField.getText())
            );
            profile.imageHeight = Double.parseDouble(piecePicHeightTextField.getText());
            profile.imageWidth = Double.parseDouble(piecePicWidthTextField.getText());
            profile.initialPointId = (DefaultListModel<String>) pieceInitialPointIdList.getModel();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void applySelectedProfile() {
        autoSave = false;
        if (playerSideList.getModel().getSize() == 0
                || pieceClassNameList.getModel().getSize() == 0
                || playerSideList.getSelectedIndices().length == 0
                || pieceClassNameList.getSelectedIndices().length == 0) {
            pieceClassNameLabel.setText("Piece Name: ");
            piecePicLinkLabel.setText("Image Link: ");
            pieceColorGreenTextField.setText("");
            pieceColorBlueTextField.setText("");
            pieceColorRedTextField.setText("");
            piecePicWidthTextField.setText("");
            piecePicHeightTextField.setText("");
            pieceInitialPointIdList.setModel(new DefaultListModel<String>());
            pieceColorGreenTextField.setBackground(jtfDefaultBackground);
            pieceColorBlueTextField.setBackground(jtfDefaultBackground);
            pieceColorRedTextField.setBackground(jtfDefaultBackground);
            piecePicHeightTextField.setBackground(jtfDefaultBackground);
            piecePicWidthTextField.setBackground(jtfDefaultBackground);
        } else {
            PieceProfile profile = getPieceProfile(
                    ((DefaultListModel<String>) playerSideList.getModel()).getElementAt(playerSideList.getSelectedIndices()[0]),
                    ((DefaultListModel<String>) pieceClassNameList.getModel()).getElementAt(pieceClassNameList.getSelectedIndices()[0])
            );
            pieceClassNameLabel.setText("Piece Name: " + profile.pieceClassName + " @ " + profile.playerSide + " side");
            piecePicLinkLabel.setText("Image Link: " + profile.sourcePicLink);
            pieceInitialPointIdList.setModel(profile.initialPointId);
            pieceColorRedTextField.setText(profile.pieceColor.getRed() + "");
            pieceColorGreenTextField.setText(profile.pieceColor.getGreen() + "");
            pieceColorBlueTextField.setText(profile.pieceColor.getBlue() + "");
            piecePicWidthTextField.setText(profile.imageWidth + "");
            piecePicHeightTextField.setText(profile.imageHeight + "");
            verifyTextFields(pieceColorGreenTextField);
            verifyTextFields(pieceColorRedTextField);
            verifyTextFields(pieceColorBlueTextField);
            verifyTextFields(piecePicWidthTextField);
            verifyTextFields(piecePicHeightTextField);
        }
        autoSave = true;
    }

    private Integer showPointIDInputDialog(ArrayList<Point> pointList) {
        String s;
        Integer i = null;
        do {
            s = JOptionPane.showInputDialog(UIHandler.getMainWindow(), "<html><center>Enter the point ID that you would like to place this chess piece to,<br>at the start of the game.</html>",
                    "Add initial point ID - Chreator", JOptionPane.QUESTION_MESSAGE);
            if (s == null) return null;
            try {
                i = new Integer(Integer.parseInt(s));
                for (Point p : pointList) {
                    if (p.getId() == i.intValue())
                        return i;
                }
            } catch (Exception e) {
            }
            JOptionPane.showMessageDialog(UIHandler.getMainWindow(), "<html><center>Input error.<br>" +
                    "Please check if the input point ID exists or not.<br>" +
                    "You can find all the point IDs in the chess board tab.</html>", "ERROR - Add initial point ID - Chreator", JOptionPane.ERROR_MESSAGE);
        } while (true);
    }
}
