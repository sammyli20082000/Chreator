package Chreator;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.tools.Diagnostic;
import javax.tools.DiagnosticCollector;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;

public abstract class ProjectCompiler {
    private static List<Diagnostic<? extends JavaFileObject>> errorMessageList;

    public static boolean
    compileProject(ArrayList<File> filesNeedToBeCompiled, String outDirPath) {
        //setJDKLocation("/cdrom/android/jdk1.8.0_65");
        File outDir = new File(outDirPath);
        if (!isCompilerReady() || (!outDir.exists() && !outDir.mkdir())) {
            errorMessageList = null;
            return false;
        }
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        /*String[] programs = {
                "class Test1{" + "   public static void main (String [] args){"
                        + "      System.out.println (\"Hello, World\");" + "   }" + "}",
                "class Test2{" + "   public static void main (String [] args){"
                        + "      System.out.println (\"Good bye, World\");" + "   }" + "}"
        };*/

        DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<JavaFileObject>();
        StandardJavaFileManager fileManager = compiler.getStandardFileManager(diagnostics, null, null);
        Iterable<? extends JavaFileObject> fileObjects = fileManager.getJavaFileObjectsFromFiles(filesNeedToBeCompiled);
        Iterable<String> options = Arrays.asList("-d", outDirPath);
        //Arrays.asList("-d", "classes", "-sourcepath", "src");

        //getJavaSourceFromString(programs);

        boolean result = compiler.getTask(null, fileManager, diagnostics, options, null, fileObjects).call();
        errorMessageList = diagnostics.getDiagnostics();
        try {
            fileManager.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static boolean compileProject(String srcDirPath, String binDirPath) {
        return compileProject(getAllJavaFilesInDir(srcDirPath), binDirPath);
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
        System.setProperty("java.home", dir + "/jre");
        return isCompilerReady();
    }

    public static boolean isCompilerReady() {
        if (ToolProvider.getSystemJavaCompiler() == null) {
            errorMessageList = null;
            return false;
        } else
            return true;
    }

    public static boolean startGameExecutable(String gameBinDirectory) {
        try {
            new ProcessBuilder("java", "-cp", gameBinDirectory, "Executable.Game").start();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static List<Diagnostic<? extends JavaFileObject>> getErrorMessageForLastCompilation() {
        return errorMessageList;
    }
}
/*
    static Iterable<JavaSourceFromString> getJavaSourceFromString(String[] code) {
        return new Iterable<JavaSourceFromString>() {
            public Iterator<JavaSourceFromString> iterator() {
                return new Iterator<JavaSourceFromString>() {
                    int index = 0;

                    public boolean hasNext() {
                        return index < code.length;
                    }

                    public JavaSourceFromString next() {
                        if (!hasNext())
                            throw new NoSuchElementException();
                        index++;
                        return new JavaSourceFromString("code", code[index - 1]);
                    }

                    public void remove() {
                        throw new UnsupportedOperationException();
                    }
                };
            }
        };
    }*/

/*
class JavaSourceFromString extends SimpleJavaFileObject {
    final String code;

    JavaSourceFromString(String name, String code) {
        super(URI.create("string:///" + name.replace('.', '/') + Kind.SOURCE.extension), Kind.SOURCE);
        this.code = code;
    }

    public CharSequence getCharContent(boolean ignoreEncodingErrors) {
        return code;
    }
}
*/