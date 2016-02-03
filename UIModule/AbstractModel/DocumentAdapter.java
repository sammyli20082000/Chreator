package Chreator.UIModule.AbstractModel;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/**
 * Created by him on 2015/12/29.
 */
public abstract class DocumentAdapter implements DocumentListener {
    @Override
    public void insertUpdate(DocumentEvent e) {
        editedUpdate(e);
    }

    @Override
    public void removeUpdate(DocumentEvent e) {
        editedUpdate(e);
    }

    @Override
    public void changedUpdate(DocumentEvent e) {
        editedUpdate(e);
    }

    public void editedUpdate(DocumentEvent e){

    }
}
