package it.shaunyl.presentationmodel.datamodel.swing;

import javax.swing.AbstractListModel;
import javax.swing.MutableComboBoxModel;

/**
 *
 * @author Filippo
 * @param <T>
 */
public abstract class PresentationModelComboBoxModelAdapter<T> extends AbstractListModel<T> implements MutableComboBoxModel<T> {
    private static final long serialVersionUID = 1L;

    @Override
    public int getSize() { return 0; };

    @Override
    public T getElementAt(int index) {
        return null;
    }

    @Override
    public void addElement(T item) {
    }

    @Override
    public void removeElement(Object obj) {
    }

    @Override
    public void insertElementAt(T item, int index) {
    }

    @Override
    public void removeElementAt(int index) {
    }

    @Override
    public void setSelectedItem(Object anItem) {
    }

    @Override
    public Object getSelectedItem() {
        return null;
    }
}
