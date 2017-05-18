package it.shaunyl.datareporter.quickquery.impl.swing;

import it.shaunyl.datareporter.quickquery.ui.QuickQueryPresentation;
import it.shaunyl.datareporter.renderer.JButtonMetro;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.text.*;
import org.springframework.stereotype.Service;

/**
 *
 * @author Filippo Testino
 */
@Service
public class SwingQuickQueryPresentation implements QuickQueryPresentation {

    private JButtonMetro btnExecuteQuery;

    private JDialog mainDialog;

    private JPanel pnlMiddle, pnlBottom;

    private JLabel lblStatus;

    private Action executeQueryAction, highlightQueryAction;

    public static final Color DEFAULT_KEYWORD_COLOR = new Color(45, 137, 239);

    public static final Color DEFAULT_TEXTPANE_COLOR = new Color(120, 120, 120);

    private StyledDocument styledDocument;

    private JTextPane txtPaneQuery;

    @Override
    public void bind(Action executeQueryAction, Action highlightQueryAction) {
        this.executeQueryAction = executeQueryAction;
        this.highlightQueryAction = highlightQueryAction;
    }

    @Override
    public void showUp() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                createView();
            }
        });
    }

    @Override
    public String getSQL() {
        return txtPaneQuery.getText();
    }

    @Override
    public void dismiss() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                mainDialog.dispose();
                btnExecuteQuery.setAction(null);
            }
        });
    }

    @Override
    public void notifyFailedQueryExecution(final String message) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                lblStatus.setText(message);
            }
        });
    }

    private void createView() {
        // Main Panel
        JPanel pnlMain = new JPanel();
        pnlMain.setLayout(new BoxLayout(pnlMain, BoxLayout.Y_AXIS));
        pnlMain.setBackground(Color.WHITE);
        pnlMain.setBorder(BorderFactory.createEmptyBorder(20, 10, 10, 10));

        // Middle Subpanel
        pnlMiddle = new JPanel(new BorderLayout(10, 10));
        TitledBorder brdMiddlePanel = BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED), "Query Editor", TitledBorder.RIGHT, TitledBorder.DEFAULT_POSITION, new Font("Tahoma", Font.BOLD, 28), Color.DARK_GRAY);
        pnlMiddle.setBorder(brdMiddlePanel);
        pnlMiddle.setPreferredSize(new Dimension(700, 400));
        pnlMiddle.setBackground(Color.WHITE);

        txtPaneQuery = new JTextPane();
        txtPaneQuery.setForeground(DEFAULT_TEXTPANE_COLOR);
        txtPaneQuery.setFont(new Font("Arial", Font.PLAIN, 18));
        styledDocument = txtPaneQuery.getStyledDocument();
        txtPaneQuery.getDocument().putProperty(DefaultEditorKit.EndOfLineStringProperty, "\n");
        txtPaneQuery.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                highlightQueryAction.actionPerformed(new ActionEvent(e.getSource(), -1, null));
            }
        });

        JScrollPane scrollPane = new JScrollPane(txtPaneQuery);
        scrollPane.setBorder(BorderFactory.createEmptyBorder( 0, 0, 0, 0 ));
        
        pnlMiddle.add(scrollPane);

        // Bottom Subpanel
        pnlBottom = new JPanel(new BorderLayout());
        pnlBottom.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));

        btnExecuteQuery = new JButtonMetro("Execute");
        btnExecuteQuery.setFont(new Font("Tahoma", Font.BOLD, 16));
        btnExecuteQuery.setAction(executeQueryAction);

        lblStatus = new JLabel("");
        lblStatus.setFont(new Font("Tahoma", Font.PLAIN, 16));
        lblStatus.setAlignmentX(Component.LEFT_ALIGNMENT);
        lblStatus.setHorizontalAlignment(JLabel.LEFT);

        JPanel pnlBottomFlowPanelRight = new JPanel(new BorderLayout());
        pnlBottomFlowPanelRight.setBorder(BorderFactory.createEmptyBorder(8, 4, 0, 4));
        pnlBottomFlowPanelRight.setBackground(Color.WHITE);
        pnlBottom.add(pnlBottomFlowPanelRight, BorderLayout.SOUTH);
        pnlBottomFlowPanelRight.add(lblStatus, BorderLayout.WEST);
        pnlBottomFlowPanelRight.add(btnExecuteQuery, BorderLayout.EAST);

        pnlMain.add(pnlMiddle);
        pnlMain.add(pnlBottom);

        // Main Frame
        mainDialog = new JDialog(new JFrame(), "Quick Query");
        mainDialog.setIconImage(new ImageIcon(getClass().getResource("Query.png")).getImage());
        mainDialog.getContentPane().add(pnlMain);
        mainDialog.setModal(true);
        mainDialog.setLayout(new BorderLayout());
        mainDialog.getContentPane().add(pnlMain);
        mainDialog.setResizable(true);
        mainDialog.pack();
        mainDialog.setLocationRelativeTo(null);
        mainDialog.setVisible(true);
    }
//

    public void updateTextColor(int offset, int length, Color c) {
        StyleContext sc = StyleContext.getDefaultStyleContext();
        AttributeSet aset = sc.addAttribute(SimpleAttributeSet.EMPTY, StyleConstants.Foreground, c);
        styledDocument.setCharacterAttributes(offset, length, aset, true);
    }

    @Override
    public void clearQueryColors() {
        updateTextColor(0, txtPaneQuery.getText().length(), DEFAULT_TEXTPANE_COLOR);
    }

    @Override
    public void updateQueryColor(int offset, int length) {
        updateTextColor(offset, length, DEFAULT_KEYWORD_COLOR);
    }
}