package it.shaunyl.datareporter.mainframe;

import it.shaunyl.presentationmodel.role.Rowable;
import it.tidalwave.role.ui.PresentationModel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

/**
 *
 * @author Filippo Testino
 */
@RequiredArgsConstructor @ToString
public class Row implements Rowable {
    @Getter
    private final PresentationModel[] columns;
}
