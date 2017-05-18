package it.shaunyl.presentationmodel.role;

import it.tidalwave.role.ui.PresentationModel;
import javax.annotation.Nonnull;

/**
 *
 * @author Filippo Testino
 */
public interface Rowable {
    @Nonnull
    public PresentationModel[] getColumns();
}
