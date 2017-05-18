/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.shaunyl.datareporter.exporter.graph.core;

import it.shaunyl.datareporter.exporter.graph.IChart;
import java.awt.Font;
import java.text.SimpleDateFormat;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.general.Dataset;
import org.jfree.data.xy.XYDataset;
import org.jfree.ui.RectangleInsets;

/**
 *
 * @author Filippo Testino
 */
public class TimeSeriesPlotStrategy extends CartesianPlotStrategy {

    private String parameterFile = "config/chartTimeSeriesFormatting.properties";

    public TimeSeriesPlotStrategy(final Dataset dataset) {
        super(dataset);
    }

    protected JFreeChart createSkeleton(IChart chart) {
        CartesianChart xychart = (CartesianChart) chart;
        return ChartFactory.createTimeSeriesChart(
                chart.getTitle().toUpperCase(),
                xychart.getXlabel(),
                xychart.getYlabel(),
                (XYDataset) this.dataset, // initial series
                chart.isLegend(),
                false, // tooltips?
                false // URLs?
                );
    }

    @Override
    public JFreeChart plot(final IChart chart) {
        return super.plot(chart);
    }

    @Override
    public void setDomainAxis(final Font font, final RectangleInsets insets) {
        // Mi da casino a sta riga, come faccio a vedere l'eccezione? Sono in un thread diverso penso..
        DateAxis domain = (DateAxis) plot.getDomainAxis();
        domain.setLabelFont(font);
        domain.setLabelInsets(insets);
//        domain.setStandardTickUnits(ticks);
        domain.setDateFormatOverride(new SimpleDateFormat("dd-MMM-yyyy HH:mm"));
        domain.setTickLabelFont(new Font("Miramonte", Font.BOLD, 14));
    }
}
