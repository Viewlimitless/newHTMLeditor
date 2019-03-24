package ru.platunov;

import javax.swing.filechooser.FileFilter;
import java.io.File;

public class HTMLFileFilter extends FileFilter {
    @Override
    public boolean accept(File f) {
        boolean a = f.isDirectory();
        boolean b = f.getName().toLowerCase().endsWith(".html");
        boolean c = f.getName().toLowerCase().endsWith(".htm");
        return a || b || c;
    }

    @Override
    public String getDescription() {
        return "HTML и HTM файлы";
    }
}
