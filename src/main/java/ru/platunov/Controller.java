package ru.platunov;


import org.springframework.context.support.ClassPathXmlApplicationContext;

import javax.swing.*;

import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;
import java.io.*;

public class Controller {
    private View view;
    private HTMLDocument document;
    private File currentFile;
    private static ClassPathXmlApplicationContext context;

    public static ClassPathXmlApplicationContext getContext() {
        return context;
    }

    public static void setContext(ClassPathXmlApplicationContext context) {
        Controller.context = context;
    }

    public static void main(String[] args) {

        context = new ClassPathXmlApplicationContext("applicationContext.xml");
        try{
            setContext(context);
            View view = context.getBean("viewBean", View.class);
            Controller controller = context.getBean("controllerBean", Controller.class);
            view.setController(controller);
            view.init();
            controller.init();

        }catch (Exception e){ExceptionHandler.log(e);}
    }

    public Controller(View view) {
        this.view = view;
    }

    public void init() {
        createNewDocument();
    }

    public void exit() {
        context.close();
        System.exit(0);
    }

    public HTMLDocument getDocument() {
        return document;
    }

    public void resetDocument() {
        if (document != null)
            document.removeUndoableEditListener(view.getUndoListener());
        document = (HTMLDocument) (new HTMLEditorKit().createDefaultDocument());
        document.addUndoableEditListener(view.getUndoListener());
        view.update();
    }

    public void setPlainText(String text) {
        try (StringReader reader = new StringReader(text)) {
            resetDocument();
            new HTMLEditorKit().read(reader, document, 0);
        } catch (Exception e) {
            ExceptionHandler.log(e);
        }
    }

    public String getPlainText() {
        String s = "";
        StringWriter writer = new StringWriter();
        try {

            new HTMLEditorKit().write(writer, document, 0, document.getLength());

            writer.flush();
            return writer.toString();
        } catch (Exception e) {
            ExceptionHandler.log(e);
        }
        return writer.toString();
    }


    public void createNewDocument() {
        view.selectHtmlTab();
        resetDocument();
        view.setTitle("HTML Редактор");
        view.resetUndo();
        currentFile = null;
    }

    public void openDocument() {
        try {
            view.selectHtmlTab();
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileFilter(context.getBean("htmlFileFilter", HTMLFileFilter.class));
            int result = fileChooser.showOpenDialog(view);
            if (result == fileChooser.APPROVE_OPTION) {
                currentFile = fileChooser.getSelectedFile();
                resetDocument();
                view.setTitle(currentFile.getName());
                FileReader reader = new FileReader(currentFile);
                new HTMLEditorKit().read(reader, document, 0);
                view.resetUndo();
            }
        } catch (Exception e) {
            ExceptionHandler.log(e);
        }
    }

    public void saveDocument() {
        if (currentFile == null) {
            saveDocumentAs();
        } else {
            try {
                view.selectHtmlTab();
                view.setTitle(currentFile.getName());
                FileWriter writer = new FileWriter(currentFile);
                new HTMLEditorKit().write(writer, document, 0, document.getLength());
            } catch (Exception e) {
                ExceptionHandler.log(e);
            }
        }
    }

    public void saveDocumentAs() {
        try {
            view.selectHtmlTab();
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileFilter(context.getBean("htmlFileFilter", HTMLFileFilter.class));
            int result = fileChooser.showSaveDialog(view);
            if (result == fileChooser.APPROVE_OPTION) {
                currentFile = fileChooser.getSelectedFile();
                view.setTitle(currentFile.getName());
                FileWriter writer = new FileWriter(currentFile);
                new HTMLEditorKit().write(writer, document, 0, document.getLength());
            }
        } catch (Exception e) {
            ExceptionHandler.log(e);
        }
    }
}
