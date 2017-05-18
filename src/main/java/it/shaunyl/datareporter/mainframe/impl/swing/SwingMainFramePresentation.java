package it.shaunyl.datareporter.mainframe.impl.swing;

import it.shaunyl.datareporter.utils.swing.SwingBinder;
import it.shaunyl.datareporter.mainframe.ui.MainFramePresentation;
import it.shaunyl.datareporter.renderer.JButtonMetro;
import it.shaunyl.datareporter.renderer.JComboBoxMetro;
import it.shaunyl.presentationmodel.renderer.swing.PresentationModelComboBoxCellRenderer;
import it.shaunyl.presentationmodel.renderer.swing.PresentationModelTableCellRenderer;
import com.ansaldosts.experimental.table.filter.TableRowFilterSupport;
import it.tidalwave.role.ui.PresentationModel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;
import javax.inject.Inject;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 *
 * @author Filippo Testino
 */
@Service @Slf4j
public class SwingMainFramePresentation implements MainFramePresentation {

    @Inject
    private SwingBinder binder;

    private JButtonMetro btnQuickQuery, btnExportExcel, btnExportXml, btnExportHtml, btnViewPlot, btnGo;

    private JFrame frmMain;

    private JComboBoxMetro<String> comboCategory, comboQuery;
    
    private JTable table, supportTable = null;

    private JPanel pnlTop, pnlMiddle, pnlBottom, pnlBottomFlowPanel, pnlStatus;

    private JLabel lblStatus;

    private static final Font DEFAULT_FRAME_FONT = new Font("Tahoma", Font.BOLD, 16);
    
    private static final String PROTOTYPE_COMBOS = "Query: SELECT * FROM plants WHERE description = 'No Description'";

    private Action quickQueryAction, selectQueryCategoryAction, exportGraphAction, exportXmlAction, exportExcelAction, exportHtmlAction, goAction;

    @Override
    public void bind(Action quickQueryAction, Action selectQueryCategoryAction, Action exportGraphAction, Action exportXmlAction, Action exportExcelAction, Action exportHtmlAction, Action goAction) {
        this.quickQueryAction = quickQueryAction;
        this.selectQueryCategoryAction = selectQueryCategoryAction;
        this.exportGraphAction = exportGraphAction;
        this.exportXmlAction = exportXmlAction;
        this.exportExcelAction = exportExcelAction;
        this.exportHtmlAction = exportHtmlAction;
        this.goAction = goAction;
    }

    @Override
    public void showUp() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                createView();
                btnGo.setAction(goAction);
                btnQuickQuery.setAction(quickQueryAction);
                comboCategory.setAction(selectQueryCategoryAction);
                btnViewPlot.setAction(exportGraphAction);
                btnExportXml.setAction(exportXmlAction);
                btnExportExcel.setAction(exportExcelAction);
                btnExportHtml.setAction(exportHtmlAction);
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

    @Override
    public void notifyDataReturned(final String message) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                lblStatus.setText(message);
            }
        });
    }

    @Override
    public void notifyExporterStatus(final String message) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                lblStatus.setText(message);
            }
        });
    }

    @Override
    public void populateGrid(final PresentationModel pm) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                binder.bind(table, pm, new PresentationModelTableCellRenderer());
            }
        });
    }

    @Override
    public void populateCategoryCombo(final PresentationModel pm) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override @SuppressWarnings("unchecked")
            public void run() {
                binder.bind(comboCategory, pm, new PresentationModelComboBoxCellRenderer());
            }
        });
    }

    @Override
    public void populateQueryCombo(final PresentationModel pm) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override @SuppressWarnings("unchecked")
            public void run() {
                binder.bind(comboQuery, pm, new PresentationModelComboBoxCellRenderer());
            }
        });
    }

    @Override
    public PresentationModel getQueryCategoryCurrentlySelected() {
        return (PresentationModel) comboCategory.getSelectedItem();
    }

    @Override
    public PresentationModel getQueryCurrentlySelected() {
        return (PresentationModel) comboQuery.getSelectedItem();
    }

    private void createView() {
        // Main Panel
        JPanel pnlMain = new JPanel();
        pnlMain.setLayout(new BoxLayout(pnlMain, BoxLayout.Y_AXIS));
        pnlMain.setBackground(Color.WHITE);
        pnlMain.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Top Subpanel
        pnlTop = new JPanel(new GridBagLayout());
        pnlTop.setPreferredSize(new Dimension(1000, 170));
        pnlTop.setMaximumSize(new Dimension(Integer.MAX_VALUE, 170));
        pnlTop.setBorder(BorderFactory.createLineBorder(new Color(241, 241, 241)));
        pnlTop.setBackground(Color.WHITE);

        this.createTopPanelComponents();

        // Middle Subpanel
        pnlMiddle = new JPanel(new BorderLayout(10, 10));
        pnlMiddle.setBackground(Color.WHITE);

        this.createMiddlePanelComponents();

        // Bottom Subpanel
        pnlBottom = new JPanel(new BorderLayout());
        pnlBottom.setPreferredSize(new Dimension(700, 50));
        pnlBottom.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));
        pnlBottomFlowPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        pnlBottomFlowPanel.setBackground(Color.WHITE);
        pnlBottom.setBackground(Color.WHITE);

        this.createBottomPanelComponents();

        pnlStatus = new JPanel();
        pnlStatus.setBackground(new Color(248, 248, 248));
        pnlStatus.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        pnlStatus.setLayout(new BoxLayout(pnlStatus, BoxLayout.X_AXIS));
        pnlStatus.setPreferredSize(new Dimension(pnlMain.getWidth(), 32));

        this.createStatusPanelComponents();

        pnlMain.add(pnlTop);
        pnlMain.add(Box.createRigidArea(new Dimension(1, 10)));
        pnlMain.add(pnlMiddle);
        pnlMain.add(pnlBottom);

        // Main Frame
        frmMain = new JFrame("Data Reporter");
        frmMain.setIconImage(new ImageIcon(getClass().getResource("data-reporter.png")).getImage());
        frmMain.setLayout(new BorderLayout());
        frmMain.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frmMain.getContentPane().add(pnlMain);
        frmMain.add(pnlStatus, BorderLayout.SOUTH);
        frmMain.setResizable(true);
        frmMain.pack();
        frmMain.setLocationRelativeTo(null);
        frmMain.setMinimumSize(new Dimension(800, 600));
        frmMain.setVisible(true);
        frmMain.setExtendedState(frmMain.getExtendedState() | JFrame.MAXIMIZED_BOTH);
    }

    private void createStatusPanelComponents() {
        lblStatus = new JLabel("");
        lblStatus.setFont(new Font("Tahoma", Font.PLAIN, 16));
        lblStatus.setForeground(new Color(0, 102, 204));
        lblStatus.setBorder(new EmptyBorder(0, 10, 0, 0));
        lblStatus.setHorizontalAlignment(SwingConstants.LEFT);
        pnlStatus.add(lblStatus);
    }

    private void createMiddlePanelComponents() {
        table = buildTable();
        final TableCellRenderer hr = table.getTableHeader().getDefaultRenderer();
        final JTableHeader header = table.getTableHeader();

        final Icon unsortedIcon = new ImageIcon(getClass().getResource("HeaderArrowUnsorted.png"));
        final Icon ascIcon = new ImageIcon(getClass().getResource("HeaderArrowAsc.png"));
        final Icon descIcon = new ImageIcon(getClass().getResource("HeaderArrowDesc.png"));

        header.setDefaultRenderer(new TableCellRenderer() {
            private JLabel lbl;

            @Override
            public Component getTableCellRendererComponent(
                    final JTable table, Object value, boolean isSelected, boolean hasFocus,
                    int row, final int column) {

                lbl = (JLabel) hr.getTableCellRendererComponent(table, value,
                        isSelected, hasFocus, row, column);
                lbl.setBorder(BorderFactory.createEmptyBorder(8, 9, 8, 9));

                lbl.setIcon(unsortedIcon);
                java.util.List<? extends RowSorter.SortKey> sortKeys = table.getRowSorter().getSortKeys();

                if (sortKeys.size() > 1) {
                    final RowSorter.SortKey get = sortKeys.get(0);
                    java.util.List<RowSorter.SortKey> sorts = new ArrayList<>();
                    sorts.add(get);
                    table.getRowSorter().setSortKeys(sorts);

                }
                for (RowSorter.SortKey sortKey : sortKeys) {
                    if (sortKey.getColumn() == table.convertColumnIndexToModel(column)) {
                        SortOrder o = sortKey.getSortOrder();
                        lbl.setIcon(o == SortOrder.ASCENDING ? ascIcon : descIcon);

                        break;
                    }
                }

                lbl.setText((String) value + " ");
                lbl.setBackground(new Color(45, 137, 239));
                lbl.setForeground(Color.WHITE);
                lbl.setFont(new Font("Tahoma", Font.PLAIN, 16));

                return lbl;
            }
        });

        pnlMiddle.add(new JScrollPane(table));
    }

    private JTable buildTable() {
        supportTable = new JTable() {
            private static final long serialVersionUID = 1L;

            @Override
            public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
                Component c = super.prepareRenderer(renderer, row, column); //To change body of generated methods, choose Tools | Templates.
                Color alternateColor = new Color(249, 249, 249);
                Color whiteColor = Color.WHITE;
                if (!c.getBackground().equals(getSelectionBackground())) {
                    Color bg = (row % 2 == 0 ? alternateColor : whiteColor);
                    c.setBackground(bg);
                    bg = null;
                }
                return c;
            }
        };

        table = TableRowFilterSupport.forTable(supportTable).actions(true).searchable(true).useTableRenderers(true).apply();
        table.setFont(new Font("Tahoma", Font.PLAIN, 15));
        table.setRowHeight(28);
        table.setForeground(new Color(61, 61, 61));
        table.setGridColor(new Color(221, 221, 221));
        table.setSelectionBackground(new Color(232, 234, 239));
        table.setSelectionForeground(new Color(31, 31, 31));
        table.setBackground(new Color(249, 249, 249));

        return table;
    }

    private void createTopPanelComponents() {
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.gridwidth = GridBagConstraints.RELATIVE;
        constraints.fill = GridBagConstraints.NONE;
        constraints.anchor = GridBagConstraints.WEST;
        constraints.weighty = 1;

        // Query Category Section
        JLabel lblQueryCategory = new JLabel("Category:");
        lblQueryCategory.setFont(new Font("Tahoma", Font.PLAIN, 16));
        constraints.insets = new Insets(0, 10, 0, 0);
        pnlTop.add(lblQueryCategory, constraints);

        comboCategory = new JComboBoxMetro<>();
        comboCategory.setPrototypeDisplayValue(this.PROTOTYPE_COMBOS);
        constraints.gridy++;
        comboCategory.setFont(new Font("Tahoma", Font.PLAIN, 16));
        constraints.insets = new Insets(-15, 10, 0, 0);

        pnlTop.add(comboCategory, constraints);

        // Query Section
        JLabel lblQuery = new JLabel("Query:");
        constraints.gridy++;
        constraints.insets = new Insets(-5, 10, 0, 0);
        lblQuery.setFont(new Font("Tahoma", Font.PLAIN, 16));
        pnlTop.add(lblQuery, constraints);

        comboQuery = new JComboBoxMetro<>();
        comboQuery.setPrototypeDisplayValue(SwingMainFramePresentation.PROTOTYPE_COMBOS);
        constraints.gridy++;
        constraints.gridwidth = 2;
        comboQuery.setFont(new Font("Tahoma", Font.PLAIN, 16));
        constraints.insets = new Insets(-15, 10, 0, 0);
        pnlTop.add(comboQuery, constraints);

        constraints.weightx = 1;

        btnGo = new JButtonMetro("GO");
        btnGo.setFont(new Font("Tahoma", Font.BOLD, 15));
        btnGo.setForeground(Color.WHITE);
        constraints.gridx += 2;
        constraints.insets = new Insets(-15, 12, 0, 0);
        constraints.gridwidth = GridBagConstraints.REMAINDER;
        pnlTop.add(btnGo, constraints);
    }

    private void createBottomPanelComponents() {
        // Button: QUICK QUERY
        btnQuickQuery = new JButtonMetro("Quick Query");
        btnQuickQuery.setFont(DEFAULT_FRAME_FONT);

        pnlBottomFlowPanel.add(btnQuickQuery);
        pnlBottomFlowPanel.add(Box.createRigidArea(new Dimension(60, 0)));

        // Button: PLOT
        btnViewPlot = new JButtonMetro("Graph");
        btnViewPlot.setFont(DEFAULT_FRAME_FONT);
        pnlBottomFlowPanel.add(btnViewPlot);

        pnlBottomFlowPanel.add(Box.createRigidArea(new Dimension(10, 0)));

        // Button: EXCEL
        btnExportExcel = new JButtonMetro("Excel");
        btnExportExcel.setFont(DEFAULT_FRAME_FONT);
        pnlBottomFlowPanel.add(btnExportExcel);

        pnlBottomFlowPanel.add(Box.createRigidArea(new Dimension(10, 0)));

        // Button: XML
        btnExportXml = new JButtonMetro("XML");
        btnExportXml.setFont(DEFAULT_FRAME_FONT);
        pnlBottomFlowPanel.add(btnExportXml);

        pnlBottomFlowPanel.add(Box.createRigidArea(new Dimension(10, 0)));

        // Button: HTML
        btnExportHtml = new JButtonMetro("HTML");
        btnExportHtml.setFont(DEFAULT_FRAME_FONT);
        pnlBottomFlowPanel.add(btnExportHtml, BorderLayout.EAST);

        pnlBottom.add(pnlBottomFlowPanel, BorderLayout.SOUTH);
    }
}
