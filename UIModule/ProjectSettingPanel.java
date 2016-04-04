package Chreator.UIModule;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.border.EtchedBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;

import Chreator.ProjectCompiler;
import Chreator.CodeLoader.CodeLoader;
import Chreator.CodeProducer.CodeProducer;
import Chreator.UIModule.AbstractModel.DocumentAdapter;

/**
 * Created by him on 2015/12/20.
 */
public class ProjectSettingPanel extends JPanel {
	public interface EventCallback {
		public boolean onSetJDKLocation(String JDKLocation);

		public void onRunGameExecutable(String projectLocation);

		public void onCompileProject(String projectLocation, ProjectCompiler.AsyncCompilationCallBack callback);
	}

	public enum TemplateType {
		XIANGQI("Xiang Qi");

		public String string;

		TemplateType(String name) {
			string = name;
		}

		public static String[] getAllString() {
			return new String[] { XIANGQI.string };
		}
	}

	public class FontDropDownMenuRenderer extends DefaultListCellRenderer {
		public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected,
				boolean cellHasFocus) {
			Component p = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
			Font f = p.getFont();
			p.setFont(new Font((String) value, f.getStyle(), f.getSize()));
			return p;
		}
	}

	public static String tabName = "Project Settings";
	private EventCallback callback;

	private JPanel basePanel, newProjectPanel;
	private JRadioButton openProjectRadio, newProjectRadio, fromTemplateRadio, customProjectRadio, temp1Radio,
			temp2Radio, temp3Radio;
	private JTextField locationInputField, projectFolderNameInputField, JDKLocationDisplayField,
			codeEditorFontSizeTextField;
	private ButtonGroup startProjectButtonGroup, newProjectButtonGroup;
	private JButton browseLocationButton, executeSettingButton, browseJDKButton, runExecutableButton, compileButton;
	private JComboBox templateDropDownMenu, codeEditorFontDropDownMenu;
	private JLabel projectFolderDirNameLabel;

	private int columnCounter = 0;
	private int editTextColumn = 17;

	private boolean isJDKLocationFieldChangedByProgram = false;

	public ProjectSettingPanel(EventCallback eventCallback) {
		callback = eventCallback;
		setLayout(new GridBagLayout());
		setBackground(Color.DARK_GRAY);
		setupLayout();
		setFontSizeOfCodeEditors(codeEditorFontSizeTextField.getFont().getSize());
		setFontOfCodeEditors(codeEditorFontSizeTextField.getFont().getName());
	}

	private void setupLayout() {
		JPanel baseContainer = new JPanel(new GridBagLayout());
		baseContainer.setBorder(BorderFactory.createRaisedBevelBorder());
		JPanel[] basePanels = new JPanel[3];

		basePanel = new JPanel();
		basePanel.setLayout(new GridBagLayout());
		columnCounter = 0;
		setupProjectSettingTitle();
		setupLocationPanel();
		setupProjectHandleWayPanel();
		setupNewProjectPanel();
		basePanels[0] = basePanel;

		basePanel = new JPanel();
		basePanel.setLayout(new GridBagLayout());
		columnCounter = 0;
		addToBasePanel(createSpaceLabel());
		basePanels[1] = basePanel;

		basePanel = new JPanel();
		basePanel.setLayout(new GridBagLayout());
		columnCounter = 0;
		setupOtherSettingPanel();
		basePanels[2] = basePanel;

		registerEventListener();

		GridBagConstraints c = new GridBagConstraints();
		c.anchor = GridBagConstraints.PAGE_START;
		c.gridx = 0;
		c.gridy = 0;

		for (JPanel jp : basePanels) {
			baseContainer.add(jp, c);
			c.gridx++;
		}
		add(baseContainer);
	}

	private void registerEventListener() {
		openProjectRadio.addActionListener(getRadioButtonsActionListener(openProjectRadio));
		newProjectRadio.addActionListener(getRadioButtonsActionListener(newProjectRadio));
		customProjectRadio.addActionListener(getRadioButtonsActionListener(customProjectRadio));
		fromTemplateRadio.addActionListener(getRadioButtonsActionListener(fromTemplateRadio));
		browseLocationButton.addActionListener(getBrowseButtonActionListener(browseLocationButton));
		browseJDKButton.addActionListener(getBrowseButtonActionListener(browseJDKButton));
		executeSettingButton.addActionListener(getButtonActionListener(executeSettingButton));
		compileButton.addActionListener(getButtonActionListener(compileButton));
		runExecutableButton.addActionListener(getButtonActionListener(runExecutableButton));
		((AbstractDocument) JDKLocationDisplayField.getDocument())
				.setDocumentFilter(getTextFieldDocumentFilter(JDKLocationDisplayField));
		codeEditorFontDropDownMenu.addItemListener(getDropDownMenuItemListener(codeEditorFontDropDownMenu));
		codeEditorFontSizeTextField.getDocument()
				.addDocumentListener(getTextFieldDocumentLister(codeEditorFontSizeTextField));
	}

	private void setupOtherSettingPanel() {
		JDKLocationDisplayField = new JTextField() {
			public void setEnabled(boolean opt) {
				super.setEnabled(opt);
				if (opt)
					setBackground(basePanel.getBackground());
				else
					setBackground(ProjectCompiler.isCompilerReady() ? Color.GREEN : Color.RED);
			}
		};
		runExecutableButton = new JButton("Run executable");
		browseJDKButton = new JButton("Browse");
		compileButton = new JButton("Compile");
		codeEditorFontDropDownMenu = new JComboBox(new DefaultComboBoxModel<String>());
		codeEditorFontSizeTextField = new JTextField();

		JDKLocationDisplayField.setOpaque(true);
		JDKLocationDisplayField.setBackground(ProjectCompiler.isCompilerReady() ? Color.green : Color.red);
		JDKLocationDisplayField.setColumns(editTextColumn);
		codeEditorFontSizeTextField.setOpaque(true);
		codeEditorFontSizeTextField.setBackground(Color.red);
		codeEditorFontSizeTextField.setColumns(editTextColumn);
		codeEditorFontDropDownMenu.setRenderer(new FontDropDownMenuRenderer());

		for (Font f : GraphicsEnvironment.getLocalGraphicsEnvironment().getAllFonts())
			codeEditorFontDropDownMenu.addItem(f.getFontName());

		addToBasePanel(new JLabel("<html><u><b>Other Settings<br></html>"), GridBagConstraints.CENTER,
				GridBagConstraints.NONE);
		addToBasePanel(new JLabel("JDK Location"));
		addToBasePanel(JDKLocationDisplayField, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL);
		addToBasePanel(browseJDKButton, GridBagConstraints.LINE_END, GridBagConstraints.NONE);

		addToBasePanel(new JLabel("<html><br>Font for code editors:</html>"));
		addToBasePanel(codeEditorFontDropDownMenu, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL);

		addToBasePanel(new JLabel("<html><br>Font size for Code</html>"));
		addToBasePanel(codeEditorFontSizeTextField, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL);

		addToBasePanel(createSpaceLabel());

		compileButton.setEnabled(false);
		runExecutableButton.setEnabled(false);

		addToBasePanel(compileButton);
		addToBasePanel(runExecutableButton);
	}

	private void setupProjectSettingTitle() {
		addToBasePanel(new JLabel("<html><u><b>Project Settings<br></html>"), GridBagConstraints.CENTER,
				GridBagConstraints.NONE);
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
		temp3Radio = new JRadioButton("temp radio 3");
		projectFolderDirNameLabel = new JLabel("<html><br>Project folder name</html>");
		templateDropDownMenu = new JComboBox(TemplateType.getAllString());
		executeSettingButton = new JButton("Execute setting");

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
		newProjectPanel.add(createSpaceLabel(), c1);
		newProjectPanel.add(customProjectRadio, c2);

		c1.gridy++;
		c2.gridy++;
		newProjectPanel.add(createSpaceLabel(), c1);
		newProjectPanel.add(fromTemplateRadio, c2);

		c1.gridy++;
		c2.gridy++;
		newProjectPanel.add(createSpaceLabel(), c1);
		c1.gridx++;
		c2.gridx++;
		c2.gridwidth--;
		newProjectPanel.add(createSpaceLabel(), c1);
		newProjectPanel.add(templateDropDownMenu, c2);
		c1.gridx--;
		c2.gridx--;
		c2.gridwidth++;

		c1.gridy++;
		c2.gridy++;
		newProjectPanel.add(createSpaceLabel(), c1);
		newProjectPanel.add(temp1Radio, c2);

		c1.gridy++;
		c2.gridy++;
		newProjectPanel.add(createSpaceLabel(), c1);
		newProjectPanel.add(temp2Radio, c2);

		c1.gridy++;
		c2.gridy++;
		newProjectPanel.add(createSpaceLabel(), c1);
		newProjectPanel.add(temp3Radio, c2);

		c1.gridy++;
		c2.gridy++;
		newProjectPanel.add(createSpaceLabel(), c1);
		newProjectPanel.add(projectFolderDirNameLabel, c2);

		c1.gridy++;
		c2.gridy++;
		newProjectPanel.add(createSpaceLabel(), c1);
		newProjectPanel.add(projectFolderNameInputField, c2);

		addToBasePanel(newProjectPanel, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL);
		addToBasePanel(executeSettingButton, GridBagConstraints.EAST, GridBagConstraints.NONE);
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
		JLabel locationLabel = new JLabel("<html>Project Location</html>");
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
		c.gridy = columnCounter;
		c.anchor = anchor;
		c.fill = fill;
		basePanel.add(p, c);
		columnCounter++;
	}

	private void addToBasePanel(Component p) {
		addToBasePanel(p, GridBagConstraints.WEST, GridBagConstraints.NONE);
	}

	private ItemListener getDropDownMenuItemListener(JComboBox jcb) {
		if (jcb == codeEditorFontDropDownMenu)
			return new ItemListener() {
				@Override
				public void itemStateChanged(ItemEvent e) {
					SimpleCodeEditor.setFontForAllEditors((String) jcb.getSelectedItem(),
							SimpleCodeEditor.getFontForAllEditors().getSize());
				}
			};
		else
			return null;
	}

	private ActionListener getButtonActionListener(JButton jb) {
		if (jb == compileButton)
			return new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					ProjectCompiler.AsyncCompilationCallBack callBack = new ProjectCompiler.AsyncCompilationCallBack() {
						@Override
						public void onCompilationFinished(boolean result) {
							compileProjectFinished(result);
						}
					};

					File dir = new File(locationInputField.getText());
					String proj = projectFolderNameInputField.getText();
					if (ProjectCompiler.isCompilerReady()) {
						compileButton.setEnabled(false);
						compileButton.setText("Compiling...");
						callback.onCompileProject(new File(dir.getAbsolutePath() + "/" + proj).getAbsolutePath(),
								callBack);
					} else
						JOptionPane.showMessageDialog(UIHandler.getMainWindow(),
								"Failed to find JDK, make sure the JDK is installed and the correct path is provided, then try again.",
								"Java compiler not ready - Chreator", JOptionPane.ERROR_MESSAGE);
				}
			};
		else if (jb == runExecutableButton)
			return new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					File dir = new File(locationInputField.getText()),
							proj = new File(projectFolderNameInputField.getText());
					callback.onRunGameExecutable(new File(dir.getAbsolutePath() + "/" + proj).getAbsolutePath());
				}
			};
		else if (jb == executeSettingButton)
			return new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					executeProjectSettings();
				}
			};
		else
			return null;
	}

	private ActionListener getBrowseButtonActionListener(JButton jb) {
		if (jb == browseLocationButton)
			return new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					File dir = UIUtility.showFileDirectorySelectionDialog(JFileChooser.DIRECTORIES_ONLY);
					locationInputField.setText(dir == null ? locationInputField.getText() : dir.getAbsolutePath());
				}
			};
		else if (jb == browseJDKButton)
			return new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					File dir = UIUtility.showFileDirectorySelectionDialog(JFileChooser.DIRECTORIES_ONLY);
					if (dir != null)
						setJDKLocation(dir.getAbsolutePath());
				}
			};
		else
			return null;
	}

	private DocumentFilter getTextFieldDocumentFilter(JTextField jtf) {
		if (jtf == JDKLocationDisplayField)
			return new DocumentFilter() {
				public void replace(DocumentFilter.FilterBypass fb, int offset, int length, String text,
						AttributeSet attrs) throws BadLocationException {
					if (isJDKLocationFieldChangedByProgram)
						super.replace(fb, offset, length, text, attrs);
				}

				public void insertString(DocumentFilter.FilterBypass fb, int offset, String string, AttributeSet attr)
						throws BadLocationException {
					if (isJDKLocationFieldChangedByProgram)
						super.insertString(fb, offset, string, attr);
				}

				public void remove(DocumentFilter.FilterBypass fb, int offset, int length) throws BadLocationException {
					if (isJDKLocationFieldChangedByProgram)
						super.remove(fb, offset, length);
				}
			};
		else
			return null;
	}

	private ActionListener getRadioButtonsActionListener(JRadioButton jrb) {
		if (jrb == newProjectRadio) {
			return new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					fromTemplateRadio.setEnabled(true);
					customProjectRadio.setEnabled(true);
					temp1Radio.setEnabled(true);
					temp2Radio.setEnabled(true);
					temp3Radio.setEnabled(true);
					projectFolderDirNameLabel.setEnabled(true);
					projectFolderNameInputField.setEnabled(true);
					templateDropDownMenu.setEnabled(fromTemplateRadio.isSelected());
					executeSettingButton.setText("Execute setting");
				}
			};
		} else if (jrb == openProjectRadio) {
			return new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					templateDropDownMenu.setEnabled(false);
					fromTemplateRadio.setEnabled(false);
					temp1Radio.setEnabled(false);
					temp2Radio.setEnabled(false);
					temp3Radio.setEnabled(false);
					projectFolderDirNameLabel.setEnabled(false);
					projectFolderNameInputField.setEnabled(false);
					customProjectRadio.setEnabled(false);
					executeSettingButton.setText("Load");
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
		} else
			return null;
	}

	private DocumentListener getTextFieldDocumentLister(JTextField jtf) {
		if (jtf == codeEditorFontSizeTextField)
			return new DocumentAdapter() {
				@Override
				public void editedUpdate(DocumentEvent e) {
					super.editedUpdate(e);
					try {
						int size = Integer.parseInt(codeEditorFontSizeTextField.getText());
						if (size < 1)
							throw new Exception();
						codeEditorFontSizeTextField.setBackground(Color.green);
						SimpleCodeEditor.setFontForAllEditors(SimpleCodeEditor.getFontForAllEditors().getName(), size);
					} catch (Exception ex) {
						codeEditorFontSizeTextField.setBackground(Color.red);
					}
				}
			};
		else
			return null;
	}

	public String getProjectLocationBaseDir() {
		return locationInputField.getText();
	}

	public String getProjectFolderName() {
		return projectFolderNameInputField.getText();
	}

	private JLabel createSpaceLabel() {
		JLabel label = new JLabel("0000");
		label.setForeground(new Color(0f, 0f, 0f, 0f));
		label.setOpaque(true);
		return label;
	}

	public void setJDKLocation(String location) {
		isJDKLocationFieldChangedByProgram = true;
		JDKLocationDisplayField.setText(location);
		isJDKLocationFieldChangedByProgram = false;
		JDKLocationDisplayField.setBackground(callback.onSetJDKLocation(location) ? Color.green : Color.red);
	}

	private boolean loaded = false;
	private String oldLocation = "";
	private void executeProjectSettings() {
		if (newProjectRadio.isSelected()) {
			if (projectFolderNameInputField.getText().equals(""))
				projectFolderNameInputField.setText("Chess_Engine");

			UIHandler uiHandler = UIHandler.getInstance(null);

			CodeProducer codeProducer = new CodeProducer(getProjectLocationBaseDir(), getProjectFolderName(),
					uiHandler.getPointList(), uiHandler.getEdgeDirectionList(), uiHandler.getPlayerSideList(),
					uiHandler.getChessPieceProfiles(), uiHandler.getBoardImage());

			if (loaded && oldLocation.equals(locationInputField.getText())) {
				if (JOptionPane.showConfirmDialog(null, "Old project may be over-written. Are you sure to continue?",
						"New Project Confirmation", JOptionPane.YES_NO_OPTION,
						JOptionPane.WARNING_MESSAGE) == JOptionPane.NO_OPTION) {
					return;
				}
			}

			try {
				codeProducer.produceExecutable();
			} catch (Exception e) {

			}
		} else if (openProjectRadio.isSelected()) {
			CodeLoader codeLoader = new CodeLoader(locationInputField.getText());
			if (CodeLoader.baseDir.equals("")) {
				return;
			}

			try {
				codeLoader.loadCodeIntoChreator();
			} catch (IOException e) {
				return;
			}
		}
		
		CodeLoader.baseDir = "";

		File file = new File(locationInputField.getText());
		oldLocation = file.getParent();
		loaded = true;

		compileButton.setEnabled(true);
		runExecutableButton.setEnabled(true);
	}

	private boolean isFolderNameValid(String dir) {
		String[] banned = { "\\", "/", ":", "*", "?", "\"", "<", ">", "|" };
		for (String s : banned)
			if (dir.contains(s))
				return false;
		return true;
	}

	private void compileProjectFinished(boolean result) {
		compileButton.setEnabled(true);
		compileButton.setText("Compile");
		if (!result) {
			List<Diagnostic<? extends JavaFileObject>> errorList = ProjectCompiler.getErrorMessageForLastCompilation();
			/*
			 * String msg = ""; if (errorList == null) msg =
			 * "Error happened with the compiler"; else for (Diagnostic<?
			 * extends JavaFileObject> diagnostic : errorList) { if
			 * (diagnostic.getKind().equals(Diagnostic.Kind.ERROR)) msg +=
			 * "code ----> " + diagnostic.getCode() + "\n" +
			 * "column number ----> " + diagnostic.getColumnNumber() + "\n" +
			 * "end position ----> " + diagnostic.getEndPosition() + "\n" +
			 * "kind ----> " + diagnostic.getKind() + "\n" +
			 * "line number ----> " + diagnostic.getLineNumber() + "\n" +
			 * "message ----> " + diagnostic.getMessage(null) + "\n" +
			 * "position ----> " + diagnostic.getPosition() + "\n" +
			 * "source ----> " + diagnostic.getSource() + "\n" +
			 * "start position ----> " + diagnostic.getStartPosition() + "\n" +
			 * "<---------->"; } System.out.println(msg);
			 */
			JOptionPane.showMessageDialog(UIHandler.getMainWindow(),
					"<html>Compilation failed:<br>"
							+ (errorList == null ? "Error happened with the compiler" : errorList),
					"Compilation failed - Chreator", JOptionPane.ERROR_MESSAGE);
		}
	}

	public boolean setFontOfCodeEditors(String s) {
		for (int i = 0; i < codeEditorFontDropDownMenu.getModel().getSize(); i++)
			if (codeEditorFontDropDownMenu.getModel().getElementAt(i).equals(s)) {
				codeEditorFontDropDownMenu.setSelectedIndex(i);
				return true;
			}
		return false;
	}

	public void setFontSizeOfCodeEditors(int size) {
		codeEditorFontSizeTextField.setText(size + "");
	}
}
