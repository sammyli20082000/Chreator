package Chreator;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;

import Chreator.UIModule.UIHandler;

/**
 * Created by him on 2015/12/2.
 */
public class ChreatorLauncher {
    private UIHandler ui;

    public static void main(String[] args) {
        //new ChreatorLauncher();

        ProjectCompiler.setJDKLocation("D:/Program Files/Java/jdk1.8.0_74");

        // compile java files in the list,
        // return null if errors with the compilation process;
        // non null empty list for successful compilation;
        // a list with something for errors are found in the java source files
        long start = System.nanoTime(), end;
        boolean result = ProjectCompiler.compileProject("E:/workspace/android_studio/ChessEngine/lib_executable/src/main/java", "E:/Download");
        end = System.nanoTime();

        if (result)
            System.out.println(ProjectCompiler.startGameExecutable("E:/Download"));
        else
            System.out.println(ProjectCompiler.getErrorMessageForLastCompilation());
        System.out.println("Compilation Time: " + ((end - start) / 1000 / 1000) + "ms");
    }

    public ChreatorLauncher() {
        ui = UIHandler.getInstance(getUIEventCallback());
    }

    private UIHandler.EventCallback getUIEventCallback() {
        return new UIHandler.EventCallback() {
        };
    }
}
