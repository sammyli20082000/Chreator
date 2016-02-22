package Chreator;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import Chreator.UIModule.UIHandler;
import Chreator.UIModule.UIUtility;

/**
 * Created by him on 2015/12/2.
 */
public class ChreatorLauncher {
    private UIHandler ui;

    public static void main(String[] args) {
        new ChreatorLauncher();
    }

    public ChreatorLauncher() {
        ui = UIHandler.getInstance(getUIEventCallback());
    }

    private UIHandler.EventCallback getUIEventCallback() {
        return new UIHandler.EventCallback() {
        };
    }
}
