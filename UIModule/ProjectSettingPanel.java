package Chreator.UIModule;

import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Objects;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.border.EtchedBorder;

/**
 * Created by him on 2015/12/20.
 */
public class ProjectSettingPanel extends JPanel {
    public interface EventCallback {

    }

    public enum TemplateType {
        XIANGQI("Xiang Qi");

        public String string;

        TemplateType(String name) {
            string = name;
        }

        public static String[] getAllString() {
            return new String[]{
                    XIANGQI.string
            };
        }
    }

    public static String tabName = "Project Settings";
    private EventCallback callback;

    private JPanel basePanel, newProjectPanel;
    private JRadioButton openProjectRadio, newProjectRadio, fromTemplateRadio, customProjectRadio, temp1Radio, temp2Radio, temp3Radio;
    private JTextField locationInputField, projectFolderNameInputField;
    private ButtonGroup startProjectButtonGroup, newProjectButtonGroup;
    private JButton browseLocationButton, executeButton, compileButton, runButton;
    private JComboBox templateDropDownMenu;
    private JLabel projectFolderDirNameLabel;

    private int componentCounter = 0;
    private int editTextColumn = 17;

    public ProjectSettingPanel(EventCallback eventCallback) {
        callback = eventCallback;
        setLayout(new GridBagLayout());
        setBackground(Color.DARK_GRAY);
        setupLayout();
    }

    private void setupLayout() {
        basePanel = new JPanel();
        basePanel.setLayout(new GridBagLayout());
        basePanel.setBorder(BorderFactory.createRaisedBevelBorder());
        setupLocationPanel();
        setupProjectHandleWayPanel();
        setupNewProjectPanel();
        setupBottomButtons();
        registerEventListener();

        GridBagConstraints c = new GridBagConstraints();
        c.anchor = GridBagConstraints.CENTER;
        add(basePanel, c);
    }

    private void registerEventListener() {
        openProjectRadio.addActionListener(getRadioButtonsActionListener(openProjectRadio));
        newProjectRadio.addActionListener(getRadioButtonsActionListener(newProjectRadio));
        customProjectRadio.addActionListener(getRadioButtonsActionListener(customProjectRadio));
        fromTemplateRadio.addActionListener(getRadioButtonsActionListener(fromTemplateRadio));
        browseLocationButton.addActionListener(getBrowseButtonActionListener());
    }

    private void setupBottomButtons() {
        executeButton = new JButton("Execute setting");
        compileButton = new JButton("Compile project");
        runButton = new JButton("Run executable");

        addToBasePanel(executeButton, GridBagConstraints.EAST, GridBagConstraints.NONE);
        addToBasePanel(new JLabel(" "));
        addToBasePanel(compileButton);
        addToBasePanel(runButton);
    }

    private void setupNewProjectPanel() {
        newProjectPanel = new JPanel();
        newProjectPanel.setLayout(new GridBagLayout());
        newProjectPanel.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));

        newProjectButtonGroup = new ButtonGroup();
        projectFolderNameInputField = new JTextField();
        customProjectRadio = new JRadioButton("Custom project");
        fromTemplateRadio = new JRadioButton("From template");
        temp1Radio = new JRadioButton("temp radio 1");
        temp2Radio = new JRadioButton("temp radio 2");
        temp3Radio = new JRadioButton("temp radio3");
        projectFolderDirNameLabel = new JLabel("<html><br>Project folder name</html>");
        templateDropDownMenu = new JComboBox(TemplateType.getAllString());

        templateDropDownMenu.setEditable(false);
        fromTemplateRadio.setSelected(true);

        newProjectButtonGroup.add(customProjectRadio);
        newProjectButtonGroup.add(fromTemplateRadio);
        newProjectButtonGroup.add(temp1Radio);
        newProjectButtonGroup.add(temp2Radio);
        newProjectButtonGroup.add(temp3Radio);

        GridBagConstraints c1 = new GridBagConstraints(), c2 = new GridBagConstraints();
        c1.gridx = 0;
        c1.gridy = 0;
        c2.gridx = 1;
        c2.gridy = 0;
        c2.weightx = 1.0;
        c2.gridwidth = 2;
        c2.fill = GridBagConstraints.HORIZONTAL;
        newProjectPanel.add(makeSpaceLabel(), c1);
        newProjectPanel.add(customProjectRadio, c2);

        c1.gridy++;
        c2.gridy++;
        newProjectPanel.add(makeSpaceLabel(), c1);
        newProjectPanel.add(fromTemplateRadio, c2);

        c1.gridy++;
        c2.gridy++;
        newProjectPanel.add(makeSpaceLabel(), c1);
        c1.gridx++;
        c2.gridx++;
        c2.gridwidth--;
        newProjectPanel.add(makeSpaceLabel(), c1);
        newProjectPanel.add(templateDropDownMenu, c2);
        c1.gridx--;
        c2.gridx--;
        c2.gridwidth++;

        c1.gridy++;
        c2.gridy++;
        newProjectPanel.add(makeSpaceLabel(), c1);
        newProjectPanel.add(projectFolderDirNameLabel, c2);

        c1.gridy++;
        c2.gridy++;
        newProjectPanel.add(makeSpaceLabel(), c1);
        newProjectPanel.add(projectFolderNameInputField, c2);

        addToBasePanel(newProjectPanel, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL);
    }

    private void setupProjectHandleWayPanel() {
        startProjectButtonGroup = new ButtonGroup();
        openProjectRadio = new JRadioButton("Open old project");
        newProjectRadio = new JRadioButton("Build new project");
        newProjectRadio.setSelected(true);

        startProjectButtonGroup.add(openProjectRadio);
        startProjectButtonGroup.add(newProjectRadio);

        addToBasePanel(new JLabel("<html><br>Task to do with the project location</html>"));
        addToBasePanel(openProjectRadio);
        addToBasePanel(newProjectRadio);
    }

    private void setupLocationPanel() {
        JLabel locationLabel = new JLabel("Project Location");
        locationInputField = new JTextField();
        browseLocationButton = new JButton("Browse");

        locationInputField.setColumns(editTextColumn);

        addToBasePanel(locationLabel);
        addToBasePanel(locationInputField, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL);
        addToBasePanel(browseLocationButton, GridBagConstraints.EAST, GridBagConstraints.NONE);
    }

    private void addToBasePanel(Component p, int anchor, int fill) {
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = componentCounter;
        c.anchor = anchor;
        c.fill = fill;
        basePanel.add(p, c);
        componentCounter++;
    }

    private void addToBasePanel(Component p) {
        addToBasePanel(p, GridBagConstraints.WEST, GridBagConstraints.NONE);
    }

    private ActionListener getBrowseButtonActionListener() {
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                int result = fileChooser.showOpenDialog(null);
                if (result == JFileChooser.APPROVE_OPTION)
                    locationInputField.setText(fileChooser.getSelectedFile().getAbsolutePath());
            }
        };
    }

    private ActionListener getRadioButtonsActionListener(JRadioButton jrb) {
        if (jrb == newProjectRadio) {
            return new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    fromTemplateRadio.setEnabled(true);
                    customProjectRadio.setEnabled(true);
                    projectFolderDirNameLabel.setEnabled(true);
                    projectFolderNameInputField.setEnabled(true);
                    templateDropDownMenu.setEnabled(fromTemplateRadio.isSelected());
                }
            };
        } else if (jrb == openProjectRadio) {
            return new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    templateDropDownMenu.setEnabled(false);
                    fromTemplateRadio.setEnabled(false);
                    projectFolderDirNameLabel.setEnabled(false);
                    projectFolderNameInputField.setEnabled(false);
                    customProjectRadio.setEnabled(false);
                }
            };
        } else if (jrb == fromTemplateRadio) {
            return new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    templateDropDownMenu.setEnabled(true);
                }
            };
        } else if (jrb == customProjectRadio) {
            return new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    templateDropDownMenu.setEnabled(false);
                }
            };
        } else return null;
    }

    public String getProjectLocationBaseDir() {
        return locationInputField.getText();
    }

    public String getProjectFolderName() {
        return projectFolderNameInputField.getText();
    }

    private JLabel makeSpaceLabel() {
        JLabel label = new JLabel("0000");
        label.setForeground(new Color(0f, 0f, 0f, 0f));
        label.setOpaque(true);
        return label;
    }
}
