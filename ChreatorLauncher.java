package Chreator;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.JOptionPane;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;

import Chreator.UIModule.UIHandler;

/**
 * Created by him on 2015/12/2.
 */
public class ChreatorLauncher {
    private UIHandler ui;
    private String sourceFolder = "src", binaryFolder = "bin";

    public static void main(String[] args) {
        new ChreatorLauncher();
    }

    public ChreatorLauncher() {
        ui = UIHandler.getInstance(getUIEventCallback());
    }

    private UIHandler.EventCallback getUIEventCallback() {
        return new UIHandler.EventCallback() {

            @Override
            public boolean onSetJDKLocation(String JDKLocation) {
                boolean result = ProjectCompiler.setJDKLocation(JDKLocation);
                if (!result)
                    JOptionPane.showMessageDialog(ui.getMainWindow(),
                            "<html><center>Failed to find JDK with the specified location," +
                                    "<br>source code syntax checking and compilation will not start.</html>",
                            "Set JDK failed - Chreator", JOptionPane.ERROR_MESSAGE);
                return result;
            }

            @Override
            public void onRunGameExecutable(String projectLocation) {
                ProjectCompiler.startGameExecutable(new File(projectLocation).getAbsolutePath() + "/" + binaryFolder);
            }

            @Override
            public void onCompileProject(String projectLocation, ProjectCompiler.AsyncCompilationCallBack callBack) {
                ProjectCompiler.compileProject(new File(projectLocation).getAbsolutePath() + "/" + sourceFolder,
                        new File(projectLocation).getAbsolutePath() + "/" + binaryFolder, callBack);
            }
        };
    }
}