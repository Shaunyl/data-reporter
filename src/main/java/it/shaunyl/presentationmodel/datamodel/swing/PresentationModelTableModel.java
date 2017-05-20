package it.shaunyl.presentationmodel.datamodel.swing;

import it.shaunyl.presentationmodel.role.Rowable;
import it.shaunyl.presentationmodel.role.GridHeaderProvider;
import it.tidalwave.role.Composite;
import it.tidalwave.role.spi.DefaultDisplayable;
import it.tidalwave.role.ui.PresentationModel;
import it.tidalwave.role.ui.spi.DefaultPresentationModel;
import it.tidalwave.util.Finder;
import java.util.ArrayList;
import java.util.List;
import javax.swing.SwingWorker;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author Filippo Testino
 */
//adapter di swing per paginazione tabella...
//la logica sta tutta qui (non più nella view), dipende da Swing, ma tale componente è riutilizzabile dovunque..
//in qualunque applicazione Swing abbia una jtable da paginare, posso usare tale adapter
public class PresentationModelTableModel extends AbstractTableModel {

    private static final long serialVersionUID = 1L;

    public static final DefaultPresentationModel EMPTY_PRESENTATION_MODEL = new DefaultPresentationModel(null, new DefaultDisplayable("..."));

    private volatile boolean metadataReadStarted = false, metadataRead = false, readingCurrentBlock = false;

    private int rowCount = 0;

    private static final int DEFAULT_PAGE_SIZE = 25;

    private List<String> columnNames = new ArrayList<>();

    private PresentationModel[][] presentationModelGrid;

    private final PresentationModel presentationModel;

    public PresentationModelTableModel(final PresentationModel presentationModel) {
        this.presentationModel = presentationModel;
    }

    private void readMetadata() {
        metadataReadStarted = true;

        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception {
                @SuppressWarnings("unchecked")
                Finder<PresentationModel> finder = (Finder<PresentationModel>) presentationModel.as(Composite.class).findChildren();
                
                rowCount = finder.count();
                List<? extends PresentationModel> rowPms = finder.from(0).max(DEFAULT_PAGE_SIZE).results();
                columnNames = presentationModel.as(GridHeaderProvider.class).getHeaderLabels();

                presentationModelGrid = new PresentationModel[rowCount][columnNames.size()];
                
                for (int i = 0; i < rowPms.size(); i++) {
                    PresentationModel[] columns = rowPms.get(i).as(Rowable.class).getColumns();
                    System.arraycopy(columns, 0, presentationModelGrid[i], 0, presentationModelGrid[i].length);
                }
                metadataRead = true;
                return null;
            }

            @Override
            protected void done() {
                fireTableStructureChanged();
            }
        };
        worker.execute();
    }

    private void readDataBlockOnDemand(final int rowIndex) {
        readingCurrentBlock = true;
        // Uso lo Swing Worker per evitare di usare un'altro thread executor e poi chiamare l'InvokeLater..
        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception {
                @SuppressWarnings("unchecked")
                Finder<PresentationModel> finder = (Finder<PresentationModel>) presentationModel.as(Composite.class).findChildren();
                final int base = (rowIndex / DEFAULT_PAGE_SIZE) * DEFAULT_PAGE_SIZE;
                List<? extends PresentationModel> rowPms = finder.from(base).max(DEFAULT_PAGE_SIZE).results();

                for (int i = 0; i < rowPms.size(); i++) {
                    PresentationModel[] columns = rowPms.get(i).as(Rowable.class).getColumns();
                    System.arraycopy(columns, 0, presentationModelGrid[base + i], 0, presentationModelGrid[base + i].length);
                }
                return null;
            }

            @Override
            protected void done() {
                fireTableDataChanged();
                readingCurrentBlock = false;
            }
        };
        worker.execute();
    }

    private void attemptReadingMetadata() {
        if (!metadataReadStarted) {
            readMetadata();
        }
    }

    @Override
    public int getRowCount() {
        attemptReadingMetadata();
        return rowCount;
    }

    @Override
    public int getColumnCount() {
        attemptReadingMetadata();
        return columnNames.size();
    }

    @Override
    public String getColumnName(int columnIndex) {
        attemptReadingMetadata();
        return columnNames.get(columnIndex);
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        attemptReadingMetadata();

        if (!metadataRead) {
            return EMPTY_PRESENTATION_MODEL;
        }

        PresentationModel cellAt = presentationModelGrid[rowIndex][columnIndex];
        if (cellAt == null) {
            if (!readingCurrentBlock) {
                readDataBlockOnDemand(rowIndex);
            }
            return EMPTY_PRESENTATION_MODEL;
        }

        return cellAt;
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return PresentationModel.class;
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
    }
}
//Sta classe non la devo testare, perché praticamente non fa un cavolo :).
//inoltre non dovrei manco testare la Presentation, perché poi grazie al PM e DCI non conterrà più logica..
