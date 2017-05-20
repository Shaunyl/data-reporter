package it.shaunyl.datareporter.mainframe.ui;

import it.tidalwave.role.ui.PresentationModel;
import javax.swing.Action;

/**
 *
 * @author Filippo Testino
 */
public interface MainFramePresentation {

    void bind(Action quickQueryAction, Action selectQueryCategoryAction, Action exportGraphAction, Action exportXmlAction, Action exportExcelAction, Action exportHtmlAction, Action goAction);

    void showUp();

    void populateCategoryCombo(final PresentationModel pm);
    
    void populateQueryCombo(final PresentationModel pm);
        
    PresentationModel getQueryCategoryCurrentlySelected();
    
    PresentationModel getQueryCurrentlySelected();
    
    void populateGrid(final PresentationModel pm);

    void notifyFailedQueryExecution(final String message);

    void notifyDataReturned(final String message);

    void notifyExporterStatus(final String message);
}
