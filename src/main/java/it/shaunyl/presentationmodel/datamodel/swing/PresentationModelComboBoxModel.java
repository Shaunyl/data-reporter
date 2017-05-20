package it.shaunyl.presentationmodel.datamodel.swing;

import it.tidalwave.role.ui.PresentationModel;
import java.util.List;
import javax.swing.SwingWorker;
import lombok.RequiredArgsConstructor;

/**
 *
 * @author Filippo Testino
 */
@RequiredArgsConstructor
public class PresentationModelComboBoxModel extends PresentationModelComboBoxModelAdapter {

    private static final long serialVersionUID = 1L;

    private final PresentationModel presentationModel;

    private List<? extends PresentationModel> presentationModels;

    private Object current = null;

    private volatile boolean metadataReadStarted, metadataReadFinished = false;

    private void readMetadata() {
        this.metadataReadStarted = true;

        new SwingWorker<Void, Void>() {
            @Override @SuppressWarnings("unchecked")
            protected Void doInBackground() throws Exception {
                presentationModels = presentationModel.as(List.class);
                setSelectedItem(presentationModels.get(0));
                return null;
            }

            @Override
            protected void done() {
                metadataReadFinished = true;
                fireContentsChanged(this, 0, getSize() - 1);
            }
        }.execute();
    }

    private void attemptReadingMetadata() {
        if (!metadataReadStarted) {
            this.readMetadata();
        }
    }

    @Override @SuppressWarnings("unchecked")
    public int getSize() {
        this.attemptReadingMetadata();
        return metadataReadFinished ? this.presentationModels.size() : 0;
    }

    @Override @SuppressWarnings("unchecked")
    public Object getElementAt(int index) {
        this.attemptReadingMetadata();
        return this.presentationModels.get(index);
    }

    @Override
    public void setSelectedItem(Object anItem) {
        this.current = anItem;
    }

    @Override
    public Object getSelectedItem() {
        this.attemptReadingMetadata();
        return this.current;
    }
}
