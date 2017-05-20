package it.shaunyl.datareporter.utils.swing;

import it.shaunyl.presentationmodel.datamodel.swing.PresentationModelComboBoxModel;
import it.shaunyl.presentationmodel.datamodel.swing.PresentationModelTableModel;
import it.tidalwave.role.ui.PresentationModel;
import it.tidalwave.role.ui.UserAction;
import java.awt.event.ActionEvent;
import javax.annotation.Nonnull;
import javax.swing.AbstractAction;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.ListCellRenderer;
import javax.swing.table.TableCellRenderer;
import org.springframework.stereotype.Service;

/**
 *
 * @author Filippo Testino
 */
@Service
public class SwingBinder {

    public void bind(final JTable table, final PresentationModel pm) {
        table.setModel(new PresentationModelTableModel(pm));
    }

    public void bind(final JTable table, final PresentationModel pm, final TableCellRenderer renderer) {
        table.setDefaultRenderer(PresentationModel.class, renderer);
        this.bind(table, pm);
    }

    public void bind(final JComboBox combo, final PresentationModel pm) {
        combo.setModel(new PresentationModelComboBoxModel(pm));
    }

    public void bind(final JComboBox combo, final PresentationModel pm, final ListCellRenderer renderer) {
        combo.setRenderer(renderer);
        this.bind(combo, pm);
    }

    public void bind(final @Nonnull JButton button, final @Nonnull UserAction action) {
        String text = button.getText();
        Icon icon = button.getIcon();
        button.setAction(new AbstractAction(text == null ? "" : text, icon) {
            @Override
            public void actionPerformed(ActionEvent e) {
                action.actionPerformed();
            }
        });
    }
    
    public <T> void bind(final @Nonnull JComboBox<T> combo, final @Nonnull UserAction action) {
        combo.setAction(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                action.actionPerformed();
            }
        });
    }
}

// questa classe è un binder al presentation model, dipende dalla tecnologia ed è univoco.
// una volta creatolo, non lo cambi più..
// in questo modo l'interfaccia non sa nulla del controllo in cui scrive.. c'è una JTable?
// bene, la bindo e al resto ci pensa il presentation model table model.. nessuna logica più nel
// presentation.
