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

    public JSnippet(File scriptFile, String... scriptArgs) {
        this.scriptFile = scriptFile;
        this.args = scriptArgs;
    }

    @Override
    public void run() {
        try {
            String script = Files.readString(scriptFile.toPath());
            ScriptContent scriptContent = parse(script);

            String classname = stripExtension(scriptFile.getName());
            JavaFile javaFile = JavaFiles.newFromScript(classname, scriptContent);
            if (exportJava) {
                saveJavaFile(javaFile);
            }

            MemoryJavaCompiler compiler = new MemoryJavaCompiler();
            Collection<ByteArrayJavaFileObject> compiledObjects = compiler.compile(javaFile.toJavaFileObject());
            if (exportClass) {
                saveClassFiles(compiledObjects);
            }

            MemoryClassExecutor executor = new MemoryClassExecutor();
            executor.execute(compiledObjects, classname, args);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static String stripExtension(String path) {
        return path.substring(0, path.lastIndexOf('.'));
    }

    private void saveJavaFile(JavaFile javaFile) throws IOException {
        String directoryPath = directoryPathOf(scriptFile.getAbsolutePath()) + File.separatorChar;
        Path javaFilePath = Path.of(directoryPath + stripExtension(scriptFile.getName()) + ".java");
        Files.writeString(
            javaFilePath, javaFile.toString(), StandardOpenOption.WRITE, StandardOpenOption.CREATE
        );
    }

    private void saveClassFiles(Collection<ByteArrayJavaFileObject> objects) {

    }

    private String directoryPathOf(String path) {
        return path.substring(0, path.lastIndexOf(File.separatorChar));
    }

    public void setExportJava(boolean exportJava) {
        this.exportJava = exportJava;
    }

    public void setExportClass(boolean exportClass) {
        this.exportClass = exportClass;
    }
}
