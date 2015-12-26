package Chreator.UIModule;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;

/**
 * Created by him on 2015/12/20.
 */
public class ChessBoardPanel extends JPanel {
    public interface EventCallback {
    }

    public static String tabName = "Chess Board";
    private EventCallback callback;
    private ChessBoardGraphicAreaPanel graphicAreaPanel;
    private JPanel toolAreaPanel;
    private int numBaseComponent = 0, numComponent =0;
    private ButtonGroup chessBoardImageOptionGroup;
    private JRadioButton imageFromFileRadio, blankImageRadio;
    private JButton browseBoardImageButton;
    private JTextField blankImageWidth, blankImageHeight;
    private JScrollPane scrollPane;

    private final int scrollSpeed = 10;
    private UIHandler uiHandler;

    public ChessBoardPanel(EventCallback eventCallback, UIHandler uiHandler) {
        callback = eventCallback;
        setLayout(new BorderLayout());
        this.uiHandler = uiHandler;

        graphicAreaPanel = new ChessBoardGraphicAreaPanel(this);
        setupToolAreaPanel();
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
        setupEdgeDirectionSettingPanel();
        fillRemaining();
    }

    private void setupEdgeDirectionSettingPanel(){
        JPanel edgeDirectionSettingPanel = new JPanel();
        edgeDirectionSettingPanel.setLayout(new GridBagLayout());
        numComponent = 0;
        edgeDirectionSettingPanel.setBorder(BorderFactory.createLoweredBevelBorder());

        addToPanel(toolAreaPanel, new JLabel("<html><br>Point Edge Direction</html>"));

        addToPanel(toolAreaPanel, edgeDirectionSettingPanel, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL);
    }

    private void setupChessBoardImageSettingPanel() {
        JPanel chessBoardImageSettingPanel = new JPanel();
        chessBoardImageSettingPanel.setLayout(new GridBagLayout());
        numComponent = 0;

        addToPanel(toolAreaPanel, new JLabel("Chess Board Image"));
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

        addToPanel(chessBoardImageSettingPanel, temp, GridBagConstraints.CENTER, GridBagConstraints.NONE);

        temp = new JPanel(new FlowLayout(FlowLayout.LEFT));
        temp.add(blankImageRadio);
        addToPanel(chessBoardImageSettingPanel, temp);

        temp = new JPanel(new FlowLayout(FlowLayout.LEFT));
        blankImageWidth = new JTextField();
        blankImageWidth.setColumns(5);
        temp.add(new JLabel("Width: "));
        temp.add(blankImageWidth);
        temp.add(new JLabel("px"));
        addToPanel(chessBoardImageSettingPanel, temp, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL);

        temp = new JPanel(new FlowLayout(FlowLayout.LEFT));
        blankImageHeight = new JTextField();
        blankImageHeight.setColumns(5);
        temp.add(new JLabel("Height: "));
        temp.add(blankImageHeight);
        temp.add(new JLabel("px"));
        addToPanel(chessBoardImageSettingPanel, temp, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL);

        chessBoardImageSettingPanel.setBorder(BorderFactory.createLoweredBevelBorder());

        addToPanel(toolAreaPanel, chessBoardImageSettingPanel, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL);
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
        addToPanel(jp, cp, GridBagConstraints.WEST, GridBagConstraints.NONE);
    }

    private void addToPanel(JPanel jp, Component cp, int anchor, int fill) {
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = (jp == toolAreaPanel ? numBaseComponent : numComponent);
        c.anchor = anchor;
        c.fill = fill;
        addToPanel(jp, cp, c);
    }

    private void addToPanel(JPanel jp, Component cp, GridBagConstraints c) {
        jp.add(cp, c);
        if (jp==toolAreaPanel)numBaseComponent++;else numComponent++;
    }
}
