package Chreator.UIModule;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/**
 * Created by him on 2015/12/20.
 */
public class ChessBoardPanel extends JPanel {
    public interface EventCallback {
    }

    public enum PointScaleMode {
        NONE, IMAGE_ALL, PANEL_ALL, IMAGE_COORDINATES, PANEL_COORDINATES;
    }

    public static String tabName = "Chess Board";
    private EventCallback callback;

    private ChessBoardGraphicAreaPanel graphicAreaPanel;
    private JPanel toolAreaPanel;
    private ButtonGroup chessBoardImageOptionGroup, addPointOptionGroup, pointScaleOptionGroup;
    private JRadioButton imageFromFileRadio, blankImageRadio, addSinglePointRadio, addRectGridPointRadio, addTriGridPointRadio,
            scalePointNoneRadio, scalePointAllPanelRadio, scalePointAllImageRadio, scalePointCoordinatesPanelRadio, scalePointCoordinatesImageRadio;
    private JButton browseBoardImageButton, addDirectionButton, deleteDirectionButton, createEdgeForPointsButton, deleteEdgeForPointsButton, addPointsButton, deletePointsButton;
    private JTextField blankImageWidth, blankImageHeight, rowSizeField, colSizeField, firstRowField, triHeightField, diffHeightField;
    private JScrollPane scrollPane;
    private JList edgeDirectionList;

    private final int scrollSpeed = 10;
    private int numBaseComponent = 0, numComponent = 0;
    private File imageFile;
    private String browseDir = null;

    public ChessBoardPanel(EventCallback eventCallback) {
        callback = eventCallback;
        setLayout(new BorderLayout());

        setupToolAreaPanel();
        graphicAreaPanel = new ChessBoardGraphicAreaPanel(this);
        scrollPane = new JScrollPane(toolAreaPanel,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getVerticalScrollBar().setUnitIncrement(scrollSpeed);
        int border_width = scrollPane.getVerticalScrollBar().getPreferredSize().width;
        toolAreaPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, border_width));

        add(graphicAreaPanel, BorderLayout.CENTER);
        add(scrollPane, BorderLayout.LINE_END);
    }

    private void setupToolAreaPanel() {
        toolAreaPanel = new JPanel();
        toolAreaPanel.setLayout(new GridBagLayout());
        toolAreaPanel.setBackground(Color.lightGray);

        setupChessBoardImageSettingPanel();
        setupAddPointPanel();
        setupEdgeDirectionSettingPanel();
        setupPointUpdateSizeMethodPanel();
        fillRemaining();

        setupEventListeners();
    }

    public void setupEventListeners() {
        blankImageHeight.getDocument().addDocumentListener(getTextFieldDocumentListener(blankImageHeight));
        blankImageWidth.getDocument().addDocumentListener(getTextFieldDocumentListener(blankImageWidth));
        browseBoardImageButton.addActionListener(getButtonActionListener(browseBoardImageButton));
        blankImageRadio.addActionListener(getRadioButtonActionListener(blankImageRadio));
        imageFromFileRadio.addActionListener(getRadioButtonActionListener(imageFromFileRadio));
        addDirectionButton.addActionListener(getButtonActionListener(addDirectionButton));
        deleteDirectionButton.addActionListener(getButtonActionListener(deleteDirectionButton));
        addPointsButton.addActionListener(getButtonActionListener(addPointsButton));
        scalePointNoneRadio.addActionListener(getRadioButtonActionListener(scalePointNoneRadio));
        scalePointAllImageRadio.addActionListener(getRadioButtonActionListener(scalePointAllImageRadio));
        scalePointAllPanelRadio.addActionListener(getRadioButtonActionListener(scalePointAllPanelRadio));
        scalePointCoordinatesImageRadio.addActionListener(getRadioButtonActionListener(scalePointCoordinatesImageRadio));
        scalePointCoordinatesPanelRadio.addActionListener(getRadioButtonActionListener(scalePointCoordinatesPanelRadio));
        deletePointsButton.addActionListener(getButtonActionListener(deletePointsButton));
    }

    public void setupPointUpdateSizeMethodPanel() {
        addToPanel(toolAreaPanel, new JLabel("<html><br>Point scaling method</html>"), GridBagConstraints.CENTER, GridBagConstraints.NONE);

        JPanel pointScaleOptionPanel = new JPanel(new GridBagLayout());
        numComponent = 0;
        pointScaleOptionPanel.setBorder(BorderFactory.createLoweredBevelBorder());

        pointScaleOptionGroup = new ButtonGroup();
        scalePointNoneRadio = new JRadioButton("<html>Do not scale</html>");
        scalePointAllPanelRadio = new JRadioButton("<html>Scale all<br>by display panel</html>");
        scalePointAllImageRadio = new JRadioButton("<html>Scale all<br>by chess board image</html>");
        scalePointCoordinatesImageRadio = new JRadioButton("<html>Scale only coordinates<br>by chess board image</html>");
        scalePointCoordinatesPanelRadio = new JRadioButton("<html>Scale only coordinates<br>by display panel</html>");
        scalePointAllImageRadio.setSelected(true);

        pointScaleOptionGroup.add(scalePointNoneRadio);
        pointScaleOptionGroup.add(scalePointAllPanelRadio);
        pointScaleOptionGroup.add(scalePointAllImageRadio);
        pointScaleOptionGroup.add(scalePointCoordinatesPanelRadio);
        pointScaleOptionGroup.add(scalePointCoordinatesImageRadio);

        addToPanel(pointScaleOptionPanel, scalePointAllImageRadio, GridBagConstraints.WEST, GridBagConstraints.NONE);
        addToPanel(pointScaleOptionPanel, scalePointAllPanelRadio, GridBagConstraints.WEST, GridBagConstraints.NONE);
        addToPanel(pointScaleOptionPanel, scalePointCoordinatesImageRadio, GridBagConstraints.WEST, GridBagConstraints.NONE);
        addToPanel(pointScaleOptionPanel, scalePointCoordinatesPanelRadio, GridBagConstraints.WEST, GridBagConstraints.NONE);
        addToPanel(pointScaleOptionPanel, scalePointNoneRadio, GridBagConstraints.WEST, GridBagConstraints.NONE);

        addToPanel(toolAreaPanel, pointScaleOptionPanel);
    }

    private void setupAddPointPanel() {
        addPointOptionGroup = new ButtonGroup();
        addSinglePointRadio = new JRadioButton("Single point");
        addTriGridPointRadio = new JRadioButton("Triangular Grid");
        addRectGridPointRadio = new JRadioButton("Rectangular Grid");
        addPointsButton = new JButton("Add points");
        deletePointsButton = new JButton("Delete selected points");
        rowSizeField = new JTextField();
        colSizeField = new JTextField();
        firstRowField = new JTextField();
        diffHeightField = new JTextField();
        triHeightField = new JTextField();

        addSinglePointRadio.setSelected(true);
        rowSizeField.setColumns(5);
        colSizeField.setColumns(5);
        firstRowField.setColumns(5);
        diffHeightField.setColumns(5);
        triHeightField.setColumns(5);

        addPointOptionGroup.add(addSinglePointRadio);
        addPointOptionGroup.add(addRectGridPointRadio);
        addPointOptionGroup.add(addTriGridPointRadio);

        numComponent = 0;
        JPanel addPointPanel = new JPanel(new GridBagLayout());
        addPointPanel.setBorder(BorderFactory.createLoweredBevelBorder());

        JPanel flowPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        flowPanel.add(addSinglePointRadio);
        addToPanel(addPointPanel, flowPanel);

        //--------------------------------------------------

        flowPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        flowPanel.add(addRectGridPointRadio);
        addToPanel(addPointPanel, flowPanel);

        flowPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        flowPanel.add(new JLabel("Row:"));
        flowPanel.add(rowSizeField);
        addToPanel(addPointPanel, flowPanel);

        flowPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        flowPanel.add(new JLabel("Column:"));
        flowPanel.add(colSizeField);
        addToPanel(addPointPanel, flowPanel);
        //--------------------------------------------------

        flowPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        flowPanel.add(addTriGridPointRadio);
        addToPanel(addPointPanel, flowPanel);

        flowPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        flowPanel.add(new JLabel("First row size:"));
        flowPanel.add(firstRowField);
        addToPanel(addPointPanel, flowPanel);

        flowPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        flowPanel.add(new JLabel("Grid height:"));
        flowPanel.add(triHeightField);
        addToPanel(addPointPanel, flowPanel);

        flowPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        flowPanel.add(new JLabel("Row difference:"));
        flowPanel.add(diffHeightField);
        addToPanel(addPointPanel, flowPanel);

        addToPanel(toolAreaPanel, new JLabel("<html><br>Piece placing points</html>"), GridBagConstraints.CENTER, GridBagConstraints.NONE);
        addToPanel(toolAreaPanel, addPointPanel);
        addToPanel(toolAreaPanel, addPointsButton);
        addToPanel(toolAreaPanel, deletePointsButton);
    }

    private void setupEdgeDirectionSettingPanel() {
        JPanel edgeDirectionSettingPanel = new JPanel();
        edgeDirectionSettingPanel.setLayout(new BoxLayout(edgeDirectionSettingPanel, BoxLayout.Y_AXIS));
        edgeDirectionSettingPanel.setBorder(BorderFactory.createLoweredBevelBorder());

        addToPanel(toolAreaPanel, new JLabel("<html><br>Edge direction between points</html>"), GridBagConstraints.CENTER, GridBagConstraints.NONE);

        edgeDirectionList = new JList();
        edgeDirectionList.setFixedCellWidth(1);
        edgeDirectionList.setModel(new DefaultListModel<String>());
        edgeDirectionList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        edgeDirectionSettingPanel.add(new JScrollPane(edgeDirectionList));
        addToPanel(toolAreaPanel, edgeDirectionSettingPanel);
        //------------------------------
        addDirectionButton = new JButton("Add new direction");
        addToPanel(toolAreaPanel, addDirectionButton);
        //------------------------------
        deleteDirectionButton = new JButton("Delete direction");
        addToPanel(toolAreaPanel, deleteDirectionButton);
        //------------------------------
        createEdgeForPointsButton = new JButton("Create edge for points");
        addToPanel(toolAreaPanel, createEdgeForPointsButton);
        //------------------------------
        deleteEdgeForPointsButton = new JButton("Delete all edges for points");
        addToPanel(toolAreaPanel, deleteEdgeForPointsButton);
    }

    private void setupChessBoardImageSettingPanel() {
        JPanel chessBoardImageSettingPanel = new JPanel();
        chessBoardImageSettingPanel.setLayout(new GridBagLayout());
        numComponent = 0;

        addToPanel(toolAreaPanel, new JLabel("Chess Board Image"), GridBagConstraints.CENTER, GridBagConstraints.NONE);
        imageFromFileRadio = new JRadioButton("From file");
        blankImageRadio = new JRadioButton("Blank image");
        browseBoardImageButton = new JButton("Browse");
        browseBoardImageButton.setEnabled(false);

        blankImageRadio.setSelected(true);
        blankImageRadio.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                browseBoardImageButton.setEnabled(false);
                blankImageWidth.setEditable(true);
                blankImageHeight.setEditable(true);
            }
        });
        imageFromFileRadio.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                browseBoardImageButton.setEnabled(true);
                blankImageWidth.setEditable(false);
                blankImageHeight.setEditable(false);
            }
        });

        chessBoardImageOptionGroup = new ButtonGroup();
        chessBoardImageOptionGroup.add(imageFromFileRadio);
        chessBoardImageOptionGroup.add(blankImageRadio);

        JPanel temp = new JPanel(new FlowLayout(FlowLayout.LEFT));
        temp.add(imageFromFileRadio);
        temp.add(browseBoardImageButton);

        addToPanel(chessBoardImageSettingPanel, temp);

        temp = new JPanel(new FlowLayout(FlowLayout.LEFT));
        temp.add(blankImageRadio);
        addToPanel(chessBoardImageSettingPanel, temp);

        temp = new JPanel(new FlowLayout(FlowLayout.LEFT));
        blankImageWidth = new JTextField();
        blankImageWidth.setColumns(5);
        temp.add(new JLabel("Width: "));
        temp.add(blankImageWidth);
        temp.add(new JLabel("px"));
        addToPanel(chessBoardImageSettingPanel, temp);

        temp = new JPanel(new FlowLayout(FlowLayout.LEFT));
        blankImageHeight = new JTextField();
        blankImageHeight.setColumns(5);
        temp.add(new JLabel("Height: "));
        temp.add(blankImageHeight);
        temp.add(new JLabel("px"));
        addToPanel(chessBoardImageSettingPanel, temp);

        chessBoardImageSettingPanel.setBorder(BorderFactory.createLoweredBevelBorder());

        addToPanel(toolAreaPanel, chessBoardImageSettingPanel);
    }

    private void fillRemaining() {
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = numBaseComponent;
        c.weighty = 1.0;
        c.anchor = GridBagConstraints.WEST;
        c.fill = GridBagConstraints.NONE;
        addToPanel(toolAreaPanel, new JLabel(" "), c);
    }

    private void addToPanel(JPanel jp, Component cp) {
        addToPanel(jp, cp, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL);
    }

    private void addToPanel(JPanel jp, Component cp, int anchor, int fill) {
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.weightx = 1.0;
        c.gridy = (jp == toolAreaPanel ? numBaseComponent : numComponent);
        c.anchor = anchor;
        c.fill = fill;
        addToPanel(jp, cp, c);
    }

    private void addToPanel(JPanel jp, Component cp, GridBagConstraints c) {
        jp.add(cp, c);
        if (jp == toolAreaPanel) numBaseComponent++;
        else numComponent++;
    }

    public void setTextFieldImageSize(int width, int height) {
        blankImageHeight.setText(height + "");
        blankImageWidth.setText(width + "");
    }

    private DocumentListener getTextFieldDocumentListener(JTextField jtf) {
        if (jtf == blankImageWidth || jtf == blankImageHeight)
            return new DocumentAdapter() {
                public void editedUpdate(DocumentEvent de) {
                    setBlankImage();
                }
            };
        else
            return null;
    }

    private ActionListener getButtonActionListener(final JButton jb) {
        if (jb == browseBoardImageButton)
            return new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (jb.isEnabled()) {
                        JFileChooser fileChooser = new JFileChooser();
                        if (browseDir != null) fileChooser.setCurrentDirectory(new File(browseDir));
                        int returnValue = fileChooser.showOpenDialog(null);
                        if (returnValue == JFileChooser.APPROVE_OPTION) {
                            imageFile = fileChooser.getSelectedFile();
                            browseDir = imageFile.getParent();
                            if (!graphicAreaPanel.setBoardImage(imageFile)) {
                                JOptionPane.showMessageDialog(UIHandler.getMainWindow(),
                                        "<html><center>Failed to read image file.<br>File type is not supported, file content is corrupted or file path cannot be accessed.</html>",
                                        "Failed to read image file", JOptionPane.ERROR_MESSAGE);

                            }
                        }

                    }
                }
            };
        else if (jb == addDirectionButton)
            return new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String s;
                    do {
                        s = JOptionPane.showInputDialog(UIHandler.getMainWindow(), "<html><center>Enter the name of new edge direction.<br>" +
                                "Name must only contain English alphabet, arabic numerals, dollar sign ($) or underscore (_).<br>" +
                                "Name cannot start with arabic numerals. The result name will be automatically capitalized.", "New edge direction", JOptionPane.QUESTION_MESSAGE);
                        if (s == null) return;
                        if (s.matches("^[A-Za-z0-9_$]+$") && !((s.charAt(0) + "").matches("[0-9]")))
                            break;
                        JOptionPane.showMessageDialog(UIHandler.getMainWindow(), "<html><center>Input error.<br>" +
                                "Name must only contain English alphabet, arabic numerals, dollar sign ($) or underscore (_).<br>" +
                                "Name cannot start with arabic numerals. The result name will be automatically capitalized.", "ERROR - New edge direction", JOptionPane.ERROR_MESSAGE);
                    } while (true);
                    s = s.toUpperCase();
                    DefaultListModel<String> listModel = (DefaultListModel<String>) edgeDirectionList.getModel();
                    for (int i = 0; i < listModel.getSize(); i++)
                        if (s.equals(listModel.getElementAt(i))) return;
                    listModel.addElement(s);
                }
            };
        else if (jb == deleteDirectionButton)
            return new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    int[] indices = edgeDirectionList.getSelectedIndices();

                    for (int i = indices.length - 1; i >= 0; i--) {
                        ((DefaultListModel<String>) edgeDirectionList.getModel()).removeElementAt(indices[i]);
                    }
                }
            };
        else if (jb == addPointsButton)
            return new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    graphicAreaPanel.addSinglePoint();
                }
            };
        else if (jb ==deletePointsButton) return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                graphicAreaPanel.deletePoints();
            }
        };
        else
            return null;
    }

    private ActionListener getRadioButtonActionListener(JRadioButton jrb) {
        if (jrb == imageFromFileRadio)
            return new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (imageFile != null) graphicAreaPanel.setBoardImage(imageFile);
                }
            };
        else if (jrb == blankImageRadio)
            return new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    setBlankImage();
                }
            };
        else if (jrb == scalePointNoneRadio ||
                jrb == scalePointAllImageRadio ||
                jrb == scalePointAllPanelRadio ||
                jrb == scalePointCoordinatesImageRadio ||
                jrb == scalePointCoordinatesPanelRadio)
            return new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    graphicAreaPanel.updateAllPoints();
                }
            };
        else return null;
    }

    private void setBlankImage() {
        blankImageWidth.setBackground(Color.GREEN);
        blankImageHeight.setBackground(Color.GREEN);

        if (imageFromFileRadio.isSelected()) return;

        try {
            if (!graphicAreaPanel.setBoardImage(
                    Integer.parseInt(blankImageWidth.getText()),
                    Integer.parseInt(blankImageHeight.getText()))) throw new Exception();
        } catch (Exception e) {
            blankImageWidth.setBackground(Color.RED);
            blankImageHeight.setBackground(Color.RED);
        }
    }

    public PointScaleMode getPointScaleMode() {
        if (scalePointAllImageRadio.isSelected())
            return PointScaleMode.IMAGE_ALL;
        else if (scalePointAllPanelRadio.isSelected())
            return PointScaleMode.PANEL_ALL;
        else if (scalePointCoordinatesImageRadio.isSelected())
            return PointScaleMode.IMAGE_COORDINATES;
        else if (scalePointCoordinatesPanelRadio.isSelected())
            return PointScaleMode.PANEL_COORDINATES;
        else
            return PointScaleMode.NONE;
    }
}
