package Chreator.UIModule;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ToolTipManager;
import javax.swing.border.EtchedBorder;

/**
 * Created by him on 2015/12/20.
 */
public class GameRulePanel extends JPanel {
    public interface EventCallback {

    }

    public static String tabName = "General Game Rules";
    private EventCallback callback;
    private JPanel codeInserterPanel, gameRulesEditorPanel;
    private int componentCounter = 0;
    private JButton addFunction1Button, addFunction2Button;
    private SimpleCodeEditor codeEditor;

    public GameRulePanel(EventCallback eventCallback) {
        callback = eventCallback;
        setupLayout();
    }

	private void setupLayout() {
		setLayout(new BorderLayout());
		gameRulesEditorPanel = new JPanel(new GridBagLayout());
		gameRulesEditorPanel.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
		gameRulesEditorPanel.setBackground(Color.lightGray);
		
		codeEditor = new SimpleCodeEditor();
		
		prepareGameRulesEditorPanel();
		
		add(new JScrollPane(codeEditor), BorderLayout.CENTER);
		add(gameRulesEditorPanel, BorderLayout.LINE_END);
		
		allComponentInPanelSetEnabled(gameRulesEditorPanel, true);
	}


	private void prepareGameRulesEditorPanel() {
		componentCounter = 0;
		
		addFunction1Button = new JButton("Add Function 1");
		addFunction2Button = new JButton("Add Function 2");
		
		addFunction1Button.addActionListener(createJButtonActionListener(addFunction1Button));
		addFunction2Button.addActionListener(createJButtonActionListener(addFunction2Button));
		
		addToPanel(gameRulesEditorPanel, createInserterRow(addFunction1Button, "Test description for function 1"));
		addToPanel(gameRulesEditorPanel, createInserterRow(addFunction2Button, "Test description for function 2"));
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

	private void addToPanel(JPanel jp, Component component) {
		addToPanel(jp, component, GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL);
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

	private ActionListener createJButtonActionListener(JButton jb) {
		if (jb == addFunction1Button) {
			return new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					
				}
			};
		} else if (jb == addFunction2Button) {
			return new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					
				}
			}; 
		} else {
			return null;			
		}
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
}
