package Chreator;

import com.sun.jndi.toolkit.url.Uri;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import javax.tools.Diagnostic;
import javax.tools.DiagnosticCollector;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.SimpleJavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;

public abstract class ProjectCompiler {
    public interface AsyncCompilationCallBack {
        public void onCompilationFinished(boolean result);
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

    public static void startGameExecutable(String gameBinDirectory) {
        try {
            Process process = new ProcessBuilder("java", "-cp", gameBinDirectory, "Executable.Game").start();
            new Thread() {
                public void run() {
                    BufferedReader err = new BufferedReader(new InputStreamReader(process.getErrorStream()));
                    String line;
                    try {
                        while (process.isAlive() && (line = err.readLine()) != null)
                            System.out.println("exe err| " + line);
                    } catch (Exception e) {
                    }
                }
            }.start();
            new Thread() {
                public void run() {
                    BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream()));
                    String line;
                    try {
                        while (process.isAlive() && (line = in.readLine()) != null)
                            System.out.println("exe out| " + line);
                    } catch (Exception e) {
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