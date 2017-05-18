package it.shaunyl.presentationmodel.renderer.swing;

import it.shaunyl.presentationmodel.role.Describable;
import it.tidalwave.role.ui.PresentationModel;
import java.awt.Component;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;

/**
 *
 * @author Filippo Testino
 */
public class PresentationModelComboBoxCellRenderer extends DefaultListCellRenderer {
    private static final long serialVersionUID = 1L;

    @Override @SuppressWarnings("AssignmentToMethodParameter")
    public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        
        if (value != null && value instanceof PresentationModel) {
            PresentationModel pm = (PresentationModel) value;
            value = pm.as(Describable.class).getDescription();
        }
        
        return super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus); //To change body of generated methods, choose Tools | Templates.
    }
}