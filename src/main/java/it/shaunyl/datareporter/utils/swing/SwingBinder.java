package it.shaunyl.datareporter.utils.swing;

import it.shaunyl.presentationmodel.datamodel.swing.PresentationModelComboBoxModel;
import it.shaunyl.presentationmodel.datamodel.swing.PresentationModelTableModel;
import it.tidalwave.role.ui.PresentationModel;
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
}

// questa classe è un binder al presentation model, dipende dalla tecnologia ed è univoco.
// una volta creatolo, non lo cambi più..
// in questo modo l'interfaccia non sa nulla del controllo in cui scrive.. c'è una JTable?
// bene, la bindo e al resto ci pensa il presentation model table model.. nessuna logica più nel
// presentation.