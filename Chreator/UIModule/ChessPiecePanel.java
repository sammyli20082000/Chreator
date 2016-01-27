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
import javax.swing.ToolTipManager;
import javax.swing.border.EtchedBorder;


/**
 * Created by him on 2015/12/20.
 */
public class ChessPiecePanel extends JPanel {
    public interface EventCallback {

    }

    public static String tabName = "Chess Piece";
    private EventCallback callback;
    private JPanel pieceContentHolderPanel, pieceListAndPreviewPanel, pieceInfoSettingPanel, codeInserterPanel;
    private JPanel previewImagePanel;
    private int componentCounter = 0;
    private Dimension previewImagePanelSize = new Dimension(200, 200);
    private BufferedImage previewImage;
    private Color previewImageColor;
    private String previewImageName;
    private String browseDir;
    private File imageFile;
    private JList playerSideList, chessPieceList, piecePlayerSideList, pieceInitialPointIdList;
    private JButton addChessPieceButton, addPlayerSideButton, deleteChessPieceButton, deletePlayerSideButton,
            setPieceSizeButton, addInitialPointIdButton, deleteInitialPointIdButton;
    private JTextField pieceClassNameTextField, pieceColorGreenTextField, pieceColorBlueTextField, pieceColorRedTextField,
            piecePicHeightTextField, piecePicWidthTextField;
    private SimpleCodeEditor codeEditor;
    private JLabel piecePlayerSideLabel;

    public ChessPiecePanel(EventCallback eventCallback) {
        callback = eventCallback;
        setupLayout();
    }

    private void setupLayout() {
        setLayout(new BorderLayout());
        pieceContentHolderPanel = new JPanel(new BorderLayout());
        pieceListAndPreviewPanel = new JPanel(new GridBagLayout());
        pieceListAndPreviewPanel.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
        pieceListAndPreviewPanel.setBackground(Color.lightGray);

        prepareChessListAndPreviewPanel();
        prepareChessContentHolderPanel();
        setupComponentEventListeners();

        pieceInfoSettingPanel.setBorder(BorderFactory.createRaisedBevelBorder());
        codeEditor.setBorder(BorderFactory.createLineBorder(Color.GRAY));

        add(pieceContentHolderPanel, BorderLayout.CENTER);
        add(pieceListAndPreviewPanel, BorderLayout.LINE_START);
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

        chessPieceList = new JList(new DefaultListModel<String>());
        chessPieceList.setFixedCellWidth(1);
        chessPieceList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        playerSideList = new JList(new DefaultListModel<String>());
        playerSideList.setFixedCellWidth(1);
        playerSideList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        addChessPieceButton = new JButton("Add Chess Piece");
        addPlayerSideButton = new JButton("Add Player Side");
        deletePlayerSideButton = new JButton("Delete Player Side");
        deleteChessPieceButton = new JButton("Delete Chess Piece");

        addChessPieceButton.addActionListener(createJButtonActionListener(addChessPieceButton));
        addPlayerSideButton.addActionListener(createJButtonActionListener(addPlayerSideButton));
        deletePlayerSideButton.addActionListener(createJButtonActionListener(deletePlayerSideButton));
        deleteChessPieceButton.addActionListener(createJButtonActionListener(deleteChessPieceButton));

        addToPanel(pieceListAndPreviewPanel, new JLabel("Selected Chess Piece Image Preview"), GridBagConstraints.CENTER, GridBagConstraints.NONE);
        addToPanel(pieceListAndPreviewPanel, previewImagePanel);

        addToPanel(pieceListAndPreviewPanel, new JLabel("<html><br>All Player Side</html>"), GridBagConstraints.CENTER, GridBagConstraints.NONE);
        addToPanel(pieceListAndPreviewPanel, new JScrollPane(playerSideList), GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL);
        addToPanel(pieceListAndPreviewPanel, addPlayerSideButton);
        addToPanel(pieceListAndPreviewPanel, deletePlayerSideButton);

        addToPanel(pieceListAndPreviewPanel, new JLabel("<html><br>All Chess Piece</html>"), GridBagConstraints.CENTER, GridBagConstraints.NONE);
        addToPanelFillRemaining(pieceListAndPreviewPanel, new JScrollPane(chessPieceList), GridBagConstraints.CENTER, GridBagConstraints.BOTH);
        addToPanel(pieceListAndPreviewPanel, addChessPieceButton);
        addToPanel(pieceListAndPreviewPanel, deleteChessPieceButton);
    }

    private void prepareChessContentHolderPanel() {
        pieceInfoSettingPanel = new JPanel(new GridBagLayout());
        codeInserterPanel = new JPanel(new GridBagLayout());
        codeEditor = new SimpleCodeEditor();

        prepareChessInfoSettingPanel();
        prepareCodeInserterPanel();

        pieceContentHolderPanel.add(pieceInfoSettingPanel, BorderLayout.PAGE_START);
        pieceContentHolderPanel.add(codeInserterPanel, BorderLayout.LINE_END);
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
        piecePlayerSideList = new JList(new DefaultListModel<String>());
        GridBagConstraints c = new GridBagConstraints();
        setPieceSizeButton = new JButton("Piece set size");
        addInitialPointIdButton = new JButton("Add Point ID");
        deleteInitialPointIdButton = new JButton("Delete Point ID");
        pieceClassNameTextField = new JTextField();
        pieceColorRedTextField = new JTextField();
        pieceColorGreenTextField = new JTextField();
        pieceColorBlueTextField = new JTextField();
        piecePicHeightTextField = new JTextField();
        piecePicWidthTextField = new JTextField();
        piecePlayerSideLabel = new JLabel();

        pieceInitialPointIdList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        piecePlayerSideList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        pieceInitialPointIdList.setFixedCellWidth(1);
        piecePlayerSideList.setFixedCellWidth(1);
        pieceColorRedTextField.setColumns(3);
        pieceColorBlueTextField.setColumns(3);
        pieceColorGreenTextField.setColumns(3);
        piecePicHeightTextField.setColumns(15);
        piecePicWidthTextField.setColumns(15);

        container = new JPanel(new GridBagLayout());
        componentCounter = 0;
        addToPanel(container, new JLabel("Piece Name"), GridBagConstraints.LINE_START, GridBagConstraints.NONE);
        addToPanel(container, pieceClassNameTextField, GridBagConstraints.LINE_START, GridBagConstraints.HORIZONTAL);
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
        addToPanel(container, new JLabel("Initial Piece Placing Point ID"), GridBagConstraints.CENTER, GridBagConstraints.NONE);
        addToPanel(container, new JScrollPane(pieceInitialPointIdList), GridBagConstraints.CENTER, GridBagConstraints.BOTH);
        addToPanel(container, addInitialPointIdButton, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL);
        addToPanel(container, deleteInitialPointIdButton, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL);

        c.gridx++;
        c.weightx = 0;
        c.fill = GridBagConstraints.HORIZONTAL;
        pieceInfoSettingPanel.add(container, c);

        container = new JPanel(new GridBagLayout());
        componentCounter = 0;
        piecePlayerSideLabel = new JLabel("");
        addToPanel(container, new JLabel("Profile for player side"), GridBagConstraints.CENTER, GridBagConstraints.NONE);
        addToPanel(container, new JScrollPane(piecePlayerSideList), GridBagConstraints.CENTER, GridBagConstraints.BOTH);
        addToPanel(container, new JLabel("<html>Current profile is editing<br>for player side:</html>"));
        addToPanel(container, piecePlayerSideLabel, GridBagConstraints.CENTER, GridBagConstraints.NONE);

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

    private ActionListener createJButtonActionListener(JButton jb) {
        if (jb == addChessPieceButton)
            return new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String pieceName = showVariableInputDialog("New Chess Piece Model", "Input the name of chess piece", "");
                    if (pieceName != null) addChessPieceToList(pieceName);
                }
            };
        else if (jb == addPlayerSideButton)
            return new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String playerSide = showVariableInputDialog("New Player Side", "Input the name of player side", "Result name will be capitalized.");
                    if (playerSide != null) addPlayerSideToList(playerSide.toUpperCase());
                }
            };
        else if (jb == deleteChessPieceButton)
            return new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    removeChessPieceFromList();
                }
            };
        else if (jb == deletePlayerSideButton)
            return new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    removePlayerSideFromList();
                }
            };
        else
            return null;
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
    }

    public void removePlayerSideFromList() {
        int[] selectedIndices = playerSideList.getSelectedIndices();

        if (selectedIndices.length > 0) {
            DefaultListModel<String> listModel = (DefaultListModel<String>) playerSideList.getModel();
            for (int i = selectedIndices.length - 1; i >= 0; i--)
                listModel.remove(selectedIndices[i]);
        }
    }

    public void addChessPieceToList(String chessPiece) {
        DefaultListModel<String> listModel = (DefaultListModel<String>) chessPieceList.getModel();
        for (int i = 0; i < listModel.size(); i++) {
            String s = listModel.getElementAt(i);
            if (s.equals(chessPiece)) return;
        }
        listModel.addElement(chessPiece);
    }

    public void removeChessPieceFromList() {
        int[] selectedIndices = chessPieceList.getSelectedIndices();

        if (selectedIndices.length > 0) {
            DefaultListModel<String> listModel = (DefaultListModel<String>) chessPieceList.getModel();
            for (int i = selectedIndices.length - 1; i >= 0; i--)
                listModel.remove(selectedIndices[i]);
        }
    }

    public String showVariableInputDialog(String title, String majorMessage, String minorMessage) {
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
}
