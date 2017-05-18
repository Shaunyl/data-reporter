package it.shaunyl.datareporter.quickquery.ui;

import javax.swing.Action;

/**
 *
 * @author Filippo Testino
 */
public interface QuickQueryPresentation {

    void bind(Action executeQueryAction, Action highlightQueryAction);

    void showUp();

    String getSQL();

    void dismiss();

    void notifyFailedQueryExecution(String message);

    void updateQueryColor(int start, int i);

    void clearQueryColors();
}
