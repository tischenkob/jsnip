package org.bodyaq.jsnippet;

import com.squareup.javapoet.JavaFile;
import org.bodyaq.jsnippet.compile.ByteArrayJavaFileObject;
import org.bodyaq.jsnippet.compile.MemoryJavaCompiler;
import org.bodyaq.jsnippet.execute.MemoryClassExecutor;
import org.bodyaq.jsnippet.template.JavaFiles;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Collection;

import static org.bodyaq.jsnippet.parse.ScriptParser.ScriptContent;
import static org.bodyaq.jsnippet.parse.ScriptParser.parse;

public class JSnippet implements Runnable {
    private final File scriptFile;
    private final String[] args;

    private boolean exportJava = false;
    private boolean exportClass = false;
    private boolean exportNative = false;

    public JSnippet(File scriptFile, String... scriptArgs) {
        this.scriptFile = scriptFile;
        this.args = scriptArgs;
    }

    @Override
    public void run() {
        try {
            String script = Files.readString(scriptFile.toPath());
            ScriptContent scriptContent = parse(script);

            String classname = getScriptClassName();
            JavaFile javaFile = JavaFiles.newFromScript(classname, scriptContent);
            if (exportJava) {
                saveJavaFile(javaFile);
                return;
            }

            MemoryJavaCompiler compiler = new MemoryJavaCompiler();
            Collection<ByteArrayJavaFileObject> compiledObjects = compiler.compile(javaFile.toJavaFileObject());
            if (exportClass) {
                saveClassFiles(compiledObjects);
                return;
            }

            if (exportNative) {
                saveNativeExecutable(compiledObjects);
                return;
            }

            MemoryClassExecutor executor = new MemoryClassExecutor();
            executor.execute(compiledObjects, classname, args);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void saveNativeExecutable(Collection<ByteArrayJavaFileObject> compiledObjects) throws IOException {
        saveClassFiles(compiledObjects);
        new ProcessBuilder()
            .command("native-image", getScriptClassName())
            .directory(new File(getCurrentDirectory()))
            .inheritIO()
            .start();
        deleteClassFiles(compiledObjects);
    }

    private void deleteClassFiles(Collection<ByteArrayJavaFileObject> compiledObjects) throws IOException {
        for (ByteArrayJavaFileObject object : compiledObjects) {
            Files.deleteIfExists(
                Path.of(getCurrentDirectory() + object.getName())
            );
        }
    }

    private static String stripExtension(String path) {
        return path.substring(0, path.lastIndexOf('.'));
    }

    private void saveJavaFile(JavaFile javaFile) throws IOException {
        String directoryPath = getCurrentDirectory();
        Path javaFilePath = Path.of(directoryPath + getScriptClassName() + ".java");
        Files.writeString(
            javaFilePath, javaFile.toString(), StandardOpenOption.WRITE, StandardOpenOption.CREATE
        );
    }

    private String getScriptClassName() {
        return stripExtension(scriptFile.getName());
    }

    private String getCurrentDirectory() {
        String path = scriptFile.getAbsolutePath();
        String dirPath = path.substring(0, path.lastIndexOf(File.separatorChar));
        return dirPath + File.separatorChar;
    }

    private void saveClassFiles(Collection<ByteArrayJavaFileObject> objects) throws IOException {
        String directoryPath = getCurrentDirectory();
        for (var cls: objects) {
            Path path = Path.of(directoryPath + cls.getName() + ".class");
            Files.write(path, cls.getBytes(), StandardOpenOption.WRITE, StandardOpenOption.CREATE);
        }
    }

    public void setExportJava(boolean exportJava) {
        this.exportJava = exportJava;
    }

    public void setExportClass(boolean exportClass) {
        this.exportClass = exportClass;
    }

    public void setExportNative(boolean exportNative) {
        this.exportNative = exportNative;
    }
}
