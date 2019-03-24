package ru.platunov.listeners;

import ru.platunov.View;

import javax.swing.*;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;
import java.awt.*;

public class TextEditMenuListener implements MenuListener {
    private View view;
    public TextEditMenuListener(View view){
        this.view = view;
    }

    @Override
    public void menuSelected(MenuEvent e) {
        for(Component c:((JMenu)e.getSource()).getMenuComponents()){
            c.setEnabled(view.isHtmlTabSelected());
        }
    }

    @Override
    public void menuDeselected(MenuEvent e) {

    }

    @Override
    public void menuCanceled(MenuEvent e) {

    }
}
