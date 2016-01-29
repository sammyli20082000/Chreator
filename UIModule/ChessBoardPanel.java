package Chreator.UIModule;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.DefaultListCellRenderer;
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
import javax.swing.SwingConstants;
import javax.swing.ToolTipManager;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import Chreator.ObjectModel.Point;

/**
 * Created by him on 2015/12/20.
 */
public class ChessBoardPanel extends JPanel {
    public interface EventCallback {
    }

    public enum PointScaleMode {
        NONE, IMAGE_ALL, PANEL_ALL, IMAGE_COORDINATES, PANEL_COORDINATES;
    }

    public static class CustomListCellRenderer extends DefaultListCellRenderer {
        private ArrayList<Color> colorList;

        public CustomListCellRenderer() {
            colorList = new ArrayList<Color>();
        }

        public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            Component c = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            if (index < colorList.size()) c.setBackground(colorList.get(index));
            return c;
        }

        public Color getBackground(int i) {
            return colorList.get(i);
        }

        public void addBackground(Color c) {
            colorList.add(c);
        }

        public void removeBackground(int i) {
            colorList.remove(i);
        }
    }

    public static String tabName = "Chess Board";
    private EventCallback callback;

    private ChessBoardGraphicAreaPanel graphicAreaPanel;
    private JPanel toolAreaPanel;
    private ButtonGroup chessBoardImageOptionGroup, addPointOptionGroup, pointScaleOptionGroup;
    private JRadioButton imageFromFileRadio, blankImageRadio, addSinglePointRadio, addRectGridPointRadio, addTriGridPointRadio,
            scalePointNoneRadio, scalePointAllPanelRadio, scalePointAllImageRadio, scalePointCoordinatesPanelRadio, scalePointCoordinatesImageRadio;
    private JButton browseBoardImageButton, addDirectionButton, deleteDirectionButton, createEdgeForPointsButton, deleteEdgeForPointsButton,
            addPointsButton, deletePointsButton, copyAndPastePointEdgeButton;
    private JTextField blankImageWidth, blankImageHeight, rectRowSizeField, rectColSizeField, triFirstRowField, triLastRowField, nextPointIdField;
    private JScrollPane scrollPane;
    private JList edgeDirectionList;
    private JLabel helpHotKeyLabel;

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

        helpHotKeyLabel = new JLabel("Move here for hot key tips");
        helpHotKeyLabel.addMouseListener(new MouseAdapter() {
            int defaultInitialDelay;

            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);
                defaultInitialDelay = ToolTipManager.sharedInstance().getInitialDelay();
                ToolTipManager.sharedInstance().setInitialDelay(0);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                super.mouseExited(e);
                ToolTipManager.sharedInstance().setInitialDelay(defaultInitialDelay);
            }
        });
        helpHotKeyLabel.setToolTipText("<html>Any mouse action on the chess board activates the<br>" +
                "hot key edit function.<br>" +
                "a key - add points<br>" +
                "d key - delete selected points<br>" +
                "c key - copy and paste points<br>" +
                "e key - create edge for selected points<br>" +
                "r key - delete all edges for points<br>" +
                "ctrl key - multiple selection mode, same as normal file management</html>");

        addToPanel(toolAreaPanel, helpHotKeyLabel, GridBagConstraints.CENTER, GridBagConstraints.NONE);
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
        copyAndPastePointEdgeButton.addActionListener(getButtonActionListener(copyAndPastePointEdgeButton));
        createEdgeForPointsButton.addActionListener(getButtonActionListener(createEdgeForPointsButton));
        deleteEdgeForPointsButton.addActionListener(getButtonActionListener(deleteEdgeForPointsButton));
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
        copyAndPastePointEdgeButton = new JButton("Copy selected points");
        deletePointsButton = new JButton("Delete selected points");
        rectRowSizeField = new JTextField();
        rectColSizeField = new JTextField();
        triFirstRowField = new JTextField();
        triLastRowField = new JTextField();
        nextPointIdField = new JTextField();

        addPointsButton.setToolTipText("<html>If single point is selected, the point will be directly added to the chess board;<br>" +
                "If point grid is selected, drag on the chess board to specify the grid size first,<br>" +
                "then points and edges will be added. Click to dismiss this message.</html>");
        deletePointsButton.setToolTipText("Delete all the selected points. All edges connected to those points will also be removed");
        copyAndPastePointEdgeButton.setToolTipText("Selected points and all their edges will be copied and pasted");

        addSinglePointRadio.setSelected(true);
        rectRowSizeField.setColumns(5);
        rectColSizeField.setColumns(5);
        triFirstRowField.setColumns(5);
        triLastRowField.setColumns(5);
        nextPointIdField.setColumns(5);
        nextPointIdField.setText("0");

        addPointOptionGroup.add(addSinglePointRadio);
        addPointOptionGroup.add(addRectGridPointRadio);
        addPointOptionGroup.add(addTriGridPointRadio);

        numComponent = 0;
        JPanel addPointPanel = new JPanel(new GridBagLayout());
        addPointPanel.setBorder(BorderFactory.createLoweredBevelBorder());

        JPanel flowPanel;
        JLabel jLabel;
        addToPanel(addPointPanel, addSinglePointRadio);

        //--------------------------------------------------
        addToPanel(addPointPanel, addRectGridPointRadio);

        flowPanel = new JPanel(new GridLayout(2, 2));
        jLabel = new JLabel("Row: ");
        jLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        flowPanel.add(jLabel);
        flowPanel.add(rectRowSizeField);

        jLabel = new JLabel("Column: ");
        jLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        flowPanel.add(jLabel);
        flowPanel.add(rectColSizeField);

        addToPanel(addPointPanel, flowPanel, GridBagConstraints.LINE_END, GridBagConstraints.NONE);

        //--------------------------------------------------
        addToPanel(addPointPanel, addTriGridPointRadio);

        flowPanel = new JPanel(new GridLayout(2, 2));
        jLabel = new JLabel("First row: ");
        jLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        flowPanel.add(jLabel);
        flowPanel.add(triFirstRowField);

        jLabel = new JLabel("Last row: ");
        jLabel.setHorizontalAlignment(SwingConstants.RIGHT);

        flowPanel.add(jLabel);
        flowPanel.add(triLastRowField);
        addToPanel(addPointPanel, flowPanel, GridBagConstraints.LINE_END, GridBagConstraints.NONE);

        //--------------------------------------------------
        flowPanel = new JPanel(new FlowLayout());
        flowPanel.add(new JLabel("Next point ID:"));
        flowPanel.add(nextPointIdField);
        addToPanel(addPointPanel, flowPanel, GridBagConstraints.LINE_START, GridBagConstraints.NONE);

        addToPanel(toolAreaPanel, new JLabel("<html><br>Piece placing points</html>"), GridBagConstraints.CENTER, GridBagConstraints.NONE);
        addToPanel(toolAreaPanel, addPointPanel);
        addToPanel(toolAreaPanel, addPointsButton);
        addToPanel(toolAreaPanel, deletePointsButton);
        addToPanel(toolAreaPanel, copyAndPastePointEdgeButton);
    }

    private void setupEdgeDirectionSettingPanel() {
        JPanel edgeDirectionSettingPanel = new JPanel();
        edgeDirectionSettingPanel.setLayout(new BoxLayout(edgeDirectionSettingPanel, BoxLayout.Y_AXIS));
        edgeDirectionSettingPanel.setBorder(BorderFactory.createLoweredBevelBorder());

        addToPanel(toolAreaPanel, new JLabel("<html><br>Edge direction between points</html>"), GridBagConstraints.CENTER, GridBagConstraints.NONE);

        edgeDirectionList = new JList();
        edgeDirectionList.setCellRenderer(new CustomListCellRenderer());
        edgeDirectionList.setFixedCellWidth(1);
        edgeDirectionList.setModel(new DefaultListModel<String>());
        edgeDirectionList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        edgeDirectionSettingPanel.add(new JScrollPane(edgeDirectionList));
        addToPanel(toolAreaPanel, edgeDirectionSettingPanel);
        //------------------------------
        addDirectionButton = new JButton("Add new direction");
        addDirectionButton.setToolTipText("<html>Add a new edge direction to the list.<br>" +
                "Edge direction name must only contain English alphabet, arabic numerals,<br>dollar sign ($) or underscore (_).<br>" +
                "Click to dismiss this message.</html>");
        addToPanel(toolAreaPanel, addDirectionButton);

        //------------------------------
        deleteDirectionButton = new JButton("Delete direction");
        deleteDirectionButton.setToolTipText("<html>Delete selected edge direction from list</html>");
        addToPanel(toolAreaPanel, deleteDirectionButton);
        //------------------------------
        createEdgeForPointsButton = new JButton("Create edge for points");
        createEdgeForPointsButton.setToolTipText("<html>Create edge for the selected points in between each neighbour point,<br>" +
                "the neighbour point sequence depends on the points selection order.<br>" +
                "See the red string under the chess board graph to confirm the edge<br>" +
                "adding order. Click to dismiss this message.</html>");
        addToPanel(toolAreaPanel, createEdgeForPointsButton);
        //------------------------------
        deleteEdgeForPointsButton = new JButton("Delete all edges for points");
        deleteEdgeForPointsButton.setToolTipText("<html>Delete edges among all selected points</html>");
        addToPanel(toolAreaPanel, deleteEdgeForPointsButton);
    }

    private void setupChessBoardImageSettingPanel() {
        JPanel chessBoardImageSettingPanel = new JPanel();
        chessBoardImageSettingPanel.setLayout(new GridBagLayout());
        numComponent = 0;

        addToPanel(toolAreaPanel, new JLabel(" "));
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
                    addEdgeDirection(s);
                }
            };
        else if (jb == deleteDirectionButton)
            return new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    int[] indices = edgeDirectionList.getSelectedIndices();

                    for (int i = indices.length - 1; i >= 0; i--) {
                        ((DefaultListModel<String>) edgeDirectionList.getModel()).removeElementAt(indices[i]);
                        ((CustomListCellRenderer) edgeDirectionList.getCellRenderer()).removeBackground(indices[i]);
                        break;
                    }
                    if (indices.length > 0) {
                        int index = (indices[0] < edgeDirectionList.getModel().getSize() ? indices[0] : edgeDirectionList.getModel().getSize() - 1);
                        edgeDirectionList.setSelectedIndex(index);
                    }
                }
            };
        else if (jb == addPointsButton)
            return new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    addPointAction();
                }
            };
        else if (jb == deletePointsButton) return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deletePointButtonAction();
            }
        };
        else if (copyAndPastePointEdgeButton == jb)
            return new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    copyAndPastePointButtonAction();
                }
            };
        else if (jb == createEdgeForPointsButton)
            return new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    createEdgeForPointsButtonAction();
                }
            };
        else if (jb == deleteEdgeForPointsButton)
            return new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    deleteEdgeForPointsButtonAction();
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

    public void setNextPointIdField(int id) {
        nextPointIdField.setText(id + "");
    }

    private void addEdgeDirection(String s) {
        s = s.toUpperCase();
        DefaultListModel<String> listModel = (DefaultListModel<String>) edgeDirectionList.getModel();
        for (int i = 0; i < listModel.getSize(); i++)
            if (s.equals(listModel.getElementAt(i))) return;
        listModel.addElement(s);

        Random ran = new Random();
        ((CustomListCellRenderer) edgeDirectionList.getCellRenderer()).
                addBackground(new Color(ran.nextInt(128) + 64, ran.nextInt(128) + 64, ran.nextInt(128) + 64));
        edgeDirectionList.setSelectedIndex(listModel.getSize() - 1);
    }

    public Color getEdgeDirectionColor(String dir) {
        DefaultListModel listModel = (DefaultListModel) edgeDirectionList.getModel();
        CustomListCellRenderer listCellRenderer = (CustomListCellRenderer) edgeDirectionList.getCellRenderer();
        for (int i = 0; i < listModel.size(); i++) {
            if (listModel.getElementAt(i).equals(dir))
                return listCellRenderer.getBackground(i);
        }
        return null;
    }

    public void addPointAction() {
        int id, row, height;
        try {
            id = Integer.parseInt(nextPointIdField.getText());
            if (addTriGridPointRadio.isSelected()) {
                row = Integer.parseInt(triFirstRowField.getText());
                height = Integer.parseInt(triLastRowField.getText());
            } else if (addRectGridPointRadio.isSelected()) {
                row = Integer.parseInt(rectRowSizeField.getText());
                height = Integer.parseInt(rectColSizeField.getText());
            } else {
                row = 1;
                height = 1;
            }
            if (row < 1 || height < 1 || id < 0) throw new Exception();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(UIHandler.getMainWindow(),
                    "Make sure the related fields are all filled with positive integer, only ID of first point field accepts 0 value.",
                    "Chreator - Add new point failed",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        boolean result;
        if (addRectGridPointRadio.isSelected())
            result = graphicAreaPanel.addRectGrid(id, row, height);
        else if (addTriGridPointRadio.isSelected())
            result = graphicAreaPanel.addTriGrid(id, row, height);
        else
            result = graphicAreaPanel.addSinglePoint(id);

        if (!result)
            JOptionPane.showMessageDialog(UIHandler.getMainWindow(),
                    "ID entered is already used, please use another unique integer as ID.",
                    "Chreator - Add new point failed",
                    JOptionPane.ERROR_MESSAGE);
        else {
            if (addRectGridPointRadio.isSelected()) {
                addEdgeDirection("EAST");
                addEdgeDirection("SOUTH");
                addEdgeDirection("WEST");
                addEdgeDirection("NORTH");
            } else if (addTriGridPointRadio.isSelected()) {
                addEdgeDirection("ANGLE_0");
                addEdgeDirection("ANGLE_60");
                addEdgeDirection("ANGLE_120");
                addEdgeDirection("ANGLE_180");
                addEdgeDirection("ANGLE_240");
                addEdgeDirection("ANGLE_300");
            }
        }
    }

    public void deletePointButtonAction() {
        graphicAreaPanel.deletePoints();
    }

    public void copyAndPastePointButtonAction() {
        graphicAreaPanel.copyAndPasteSelectedPoint();
    }

    public void createEdgeForPointsButtonAction() {
        int[] selectedIndices = edgeDirectionList.getSelectedIndices();
        if (selectedIndices.length > 0)
            graphicAreaPanel.createEdgesForSelectedPoints(((DefaultListModel<String>) edgeDirectionList.getModel()).getElementAt(selectedIndices[0]));
        else
            JOptionPane.showMessageDialog(UIHandler.getMainWindow(), "No selected edge direction.", "Error - Chreator", JOptionPane.ERROR_MESSAGE);
    }

    public void deleteEdgeForPointsButtonAction(){
        graphicAreaPanel.deleteEdgesForSelectedPoints();
    }

	public ArrayList<Point> getPointList() {
		return graphicAreaPanel.getPointList();
	}
}
