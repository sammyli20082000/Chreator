package Chreator;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.tools.Diagnostic;
import javax.tools.DiagnosticCollector;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;

import Chreator.UIModule.UIUtility;

public abstract class ProjectCompiler {

    public interface AsyncCompilationCallBack {
        public void onCompilationFinished(boolean result);
    }

    public static class LogWindow extends JFrame {
        private ArrayList<String> logs = new ArrayList<String>(), queue;
        private Font font;
        private JScrollBar verticalBar;
        private JTextArea showLogPanel;
        private boolean pushed = false, modified = false, setTextByProgram = false;
        private Thread updateThread;
        private JScrollPane scrollPane;
        private JPanel logPanel;

        public LogWindow(Component component, String title, Font font, Dimension windowSize) {
            setTitle(title);
            logPanel = new JPanel(new BorderLayout());

            addComponentListener(new ComponentAdapter() {
                @Override
                public void componentResized(ComponentEvent e) {
                    super.componentResized(e);
                    updateContent();
                }
            });

            this.font = font;

            verticalBar = new JScrollBar(JScrollBar.VERTICAL, 0, 0, 0, 0);
            verticalBar.addAdjustmentListener(new AdjustmentListener() {
                @Override
                public void adjustmentValueChanged(AdjustmentEvent e) {
                    updateContent();
                }
            });

            showLogPanel = new JTextArea();
            showLogPanel.setBackground(Color.BLACK);
            showLogPanel.setForeground(Color.WHITE);
            showLogPanel.setFont(font);
            showLogPanel.setFocusable(false);
            showLogPanel.addMouseWheelListener(new MouseWheelListener() {
                @Override
                public void mouseWheelMoved(MouseWheelEvent e) {
                    int value = verticalBar.getValue() + e.getScrollAmount() * e.getWheelRotation();
                    if (value < verticalBar.getMinimum()) value = verticalBar.getMinimum();
                    if (value > verticalBar.getMaximum()) value = verticalBar.getMaximum();
                    verticalBar.setValue(value);
                    updateContent();
                }
            });
            scrollPane = new JScrollPane(showLogPanel, JScrollPane.VERTICAL_SCROLLBAR_NEVER, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
            logPanel.add(scrollPane, BorderLayout.CENTER);
            logPanel.add(verticalBar, BorderLayout.LINE_END);

            add(logPanel);
            setSize(windowSize);
            setLocationRelativeTo(component);
            setVisible(true);
        }

        private void updateContent() {
            modified = true;
            if (updateThread == null || !updateThread.isAlive()) {
                updateThread = new Thread() {
                    public void run() {
                        while (modified) {
                            modified = false;
                            boolean getData = pushed;
                            pushed = false;
                            Graphics g = showLogPanel.getGraphics();
                            int numRowCanShow =
                                    (scrollPane.getHeight() -
                                            scrollPane.getHorizontalScrollBar()
                                                    .getHeight()) /
                                            (g != null ? g.getFontMetrics(font).getHeight() : font.getSize());

                            if (getData) {
                                ArrayList<String> backup = queue;
                                queue = null;
                                if (backup != null) {
                                    long start = System.nanoTime();
                                    for (String s : backup) {
                                        logs.add(s);
                                        if (logs.size() > 262144) logs.remove(0);
                                        if (System.nanoTime() - start > 25000000) {
                                            logs.add("[Too much output to process... Skipped " + backup.size() + " outputs]");
                                            break;
                                        }
                                    }
                                }
                            }

                            verticalBar.setMaximum(logs.size() - numRowCanShow < 0 ? 0 : logs.size() - numRowCanShow);
                            if (getData) verticalBar.setValue(verticalBar.getMaximum());

                            String msg = "";
                            for (int i = verticalBar.getValue(); i < logs.size() && i - verticalBar.getValue() < numRowCanShow; i++)
                                msg += logs.get(i) + "\n";
                            setTextByProgram = true;
                            showLogPanel.setText(msg);
                            setTextByProgram = false;
                        }
                    }
                };
                updateThread.start();
            }
        }

        synchronized public void pushLog(String line) {
            if (queue == null) queue = new ArrayList<String>();
            if (line == null)
                queue.add("null");
            else {
                ArrayList<String> choppedLines = UIUtility.splitStringAfterCondition(line, UIUtility.JavaLineTerminator),
                        filteredLines = new ArrayList<String>();
                for (String s : choppedLines) {
                    for (String terminator : UIUtility.JavaLineTerminator)
                        s = s.replace(terminator, "");
                    filteredLines.add(s);
                }
                for (String s : filteredLines)
                    queue.add(s);

            }
            pushed = true;
            updateContent();
        }
    }

    private static List<Diagnostic<? extends JavaFileObject>> errorMessageList;
    private static Thread compilationThread;
    private static Runnable upcomingCompilationTask;

    public static void compileProject(final ArrayList<File> filesNeedToBeCompiled, final String inDirPath, final String outDirPath, final AsyncCompilationCallBack callBack) {
        upcomingCompilationTask = new Runnable() {
            @Override
            public void run() {
                try {
                    errorMessageList = null;
                    File outDir = new File(outDirPath);

                    if (!isCompilerReady()) {
                        errorMessageList.add(createCustomDiagnosticMessage(
                                "Failed to find JDK with the provided path, please fix it and try again."
                        ));
                        if (callBack != null) callBack.onCompilationFinished(false);
                        return;
                    }
                    if (filesNeedToBeCompiled.size() == 0) {
                        errorMessageList = new ArrayList<Diagnostic<? extends JavaFileObject>>();
                        errorMessageList.add(createCustomDiagnosticMessage(
                                "No compilable Java source file found in the following path:\n" + inDirPath
                        ));
                        if (callBack != null) callBack.onCompilationFinished(false);
                        return;
                    }
                    if ((!outDir.exists() && !outDir.mkdir())) {
                        errorMessageList = new ArrayList<Diagnostic<? extends JavaFileObject>>();
                        errorMessageList.add(createCustomDiagnosticMessage(
                                "Failed to create the build folder in the project directory:\n" + outDirPath
                        ));
                        if (callBack != null) callBack.onCompilationFinished(false);
                        return;
                    }
                    JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();

                    DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<JavaFileObject>();
                    StandardJavaFileManager fileManager = compiler.getStandardFileManager(diagnostics, null, null);
                    Iterable<? extends JavaFileObject> fileObjects = fileManager.getJavaFileObjectsFromFiles(filesNeedToBeCompiled);
                    Iterable<String> options = Arrays.asList("-d", outDirPath);

                    boolean result = compiler.getTask(null, fileManager, diagnostics, options, null, fileObjects).call();
                    errorMessageList = diagnostics.getDiagnostics();
                    try {
                        fileManager.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if (upcomingCompilationTask != null)
                        startCompilationThread();
                    if (callBack != null) callBack.onCompilationFinished(result);
                } catch (Exception e) {
                    e.printStackTrace();
                    errorMessageList = new ArrayList<Diagnostic<? extends JavaFileObject>>();
                    errorMessageList.add(createCustomDiagnosticMessage(
                            "Unexpected internal compiler error."
                    ));
                    if (callBack != null) callBack.onCompilationFinished(false);
                }
            }
        };
        if (compilationThread == null || !compilationThread.isAlive())
            startCompilationThread();

    }

    private static void startCompilationThread() {
        Runnable runnable = upcomingCompilationTask;
        upcomingCompilationTask = null;
        compilationThread = new Thread(runnable);
        compilationThread.start();
    }

    public static void compileProject(String srcDirPath, String binDirPath, final AsyncCompilationCallBack callBack) {
        compileProject(getAllJavaFilesInDir(srcDirPath), srcDirPath, binDirPath, callBack);
    }

    private static ArrayList<File> getAllJavaFilesInDir(String dirPath) {
        ArrayList<File> list = new ArrayList<File>();
        try {
            File dir = new File(dirPath);
            if (dir.isDirectory()) {
                for (File f : dir.listFiles()) {
                    for (File result : getAllJavaFilesInDir(f.getAbsolutePath()))
                        list.add(result);
                }
            } else if (dir.isFile() && dir.getAbsolutePath().toLowerCase().endsWith(".java"))
                list.add(dir);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public static boolean setJDKLocation(String dir) {
        System.setProperty("java.home", new File(dir).getAbsolutePath() + "/jre");
        return isCompilerReady();
    }

    public static boolean isCompilerReady() {
        if (ToolProvider.getSystemJavaCompiler() == null) {
            errorMessageList = null;
            return false;
        } else
            return true;
    }

    public static void startGameExecutable(Component component, String gameBinDirectory, Font font, Dimension windowSize) {
        try {
            Process process = new ProcessBuilder("java", "-cp", gameBinDirectory, "Executable.Game").start();

            final LogWindow processOutputWindow = new LogWindow(component, "java -cp " + gameBinDirectory + " Executable.Game", font, windowSize);
            processOutputWindow.addWindowListener(new WindowAdapter() {
                public void windowClosing(WindowEvent e) {
                    if (process.isAlive()) process.destroyForcibly();
                }
            });

            new Thread() {
                public void run() {
                    BufferedReader err = new BufferedReader(new InputStreamReader(process.getErrorStream()));
                    String line;
                    try {
                        while ((line = err.readLine()) != null)
                            processOutputWindow.pushLog(line);
                        while (process.isAlive()) {
                            sleep(250);
                        }
                        processOutputWindow.pushLog("Process terminated with exit code " + process.exitValue());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }.start();
            new Thread() {
                public void run() {
                    BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream()));
                    String line;
                    try {
                        while (process.isAlive() && (line = in.readLine()) != null)
                            processOutputWindow.pushLog(line);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static List<Diagnostic<? extends JavaFileObject>> getErrorMessageForLastCompilation() {
        return errorMessageList;
    }

    private static Diagnostic<JavaFileObject> createCustomDiagnosticMessage(final String message) {
        return new Diagnostic<JavaFileObject>() {
            @Override
            public Kind getKind() {
                return Kind.ERROR;
            }

            @Override
            public JavaFileObject getSource() {
                return null;
            }

            @Override
            public long getPosition() {
                return 0;
            }

            @Override
            public long getStartPosition() {
                return 0;
            }

            @Override
            public long getEndPosition() {
                return 0;
            }

            @Override
            public long getLineNumber() {
                return 0;
            }

            @Override
            public long getColumnNumber() {
                return 0;
            }

            @Override
            public String getCode() {
                return "";
            }

            @Override
            public String getMessage(Locale locale) {
                return message;
            }

            public String toString() {
                return message;
            }
        };
    }
}