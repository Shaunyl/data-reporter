package it.shaunyl.presentationmodel.renderer.swing;

import it.tidalwave.role.Displayable;
import it.tidalwave.role.ui.PresentationModel;
import java.awt.Component;
import javax.swing.BorderFactory;
import javax.swing.JTable;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableCellRenderer;

/**
 *
 * @author Filippo Testino
 */
public class PresentationModelTableCellRenderer extends DefaultTableCellRenderer {

    private static final long serialVersionUID = 1L;

    @Override @SuppressWarnings("AssignmentToMethodParameter")
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        
        if (value != null) {
            PresentationModel pm = (PresentationModel) value;
            value = pm.as(Displayable.class).getDisplayName();
        }

        final Component component = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        Border padding = BorderFactory.createEmptyBorder(0, 10, 0, 10);
        setBorder(BorderFactory.createCompoundBorder(noFocusBorder, padding));  

        return component;
    }
}
