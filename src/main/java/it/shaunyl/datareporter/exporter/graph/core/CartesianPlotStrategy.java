/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.shaunyl.datareporter.exporter.graph.core;

import it.shaunyl.datareporter.exception.UnexpectedException;
import it.shaunyl.datareporter.exporter.graph.IChart;
import it.shaunyl.datareporter.exporter.graph.IPlotStrategy;
import java.awt.*;
import java.io.*;
import java.text.NumberFormat;
import java.util.*;
import java.util.regex.*;
import org.jfree.chart.*;
import org.jfree.chart.axis.*;
import org.jfree.chart.labels.*;
import org.jfree.chart.plot.*;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.general.Dataset;
import org.jfree.data.xy.DefaultXYDataset;
import org.jfree.ui.RectangleInsets;
import org.jfree.ui.TextAnchor;

/**
 *
 * @author Filippo Testino (filippo.testino@gmail.com)
 */
public class CartesianPlotStrategy implements IPlotStrategy {

    protected Dataset dataset;
    protected String parameterFile = "config/chartCartesianFormatting.properties";
    private Font titleFont, labelFont;
    protected JFreeChart jfreechart;
    protected XYPlot plot;
    protected TickUnitSource ticks;

    public CartesianPlotStrategy(final Dataset dataset) {
        this.dataset = dataset;
    }

    protected JFreeChart createSkeleton(IChart chart) {
        CartesianChart xychart = (CartesianChart) chart;
        return ChartFactory.createXYLineChart(
                chart.getTitle().toUpperCase(),
                xychart.getXlabel(),
                xychart.getYlabel(),
                (DefaultXYDataset)this.dataset, // initial series
                PlotOrientation.VERTICAL, // orientation
                chart.isLegend(),
                false, // tooltips?
                false // URLs?
                );
    }

    protected void customizeTitle(Font font, Color color, RectangleInsets padding) {
        jfreechart.getTitle().setFont(font);
        jfreechart.getTitle().setPaint(color);
        jfreechart.getTitle().setPadding(padding);
    }

    protected void setDomainAxis(Font font, RectangleInsets insets) {
        NumberAxis domain = (NumberAxis) plot.getDomainAxis();
        domain.setLabelFont(font);
        domain.setLabelInsets(insets);
        domain.setStandardTickUnits(ticks);
        domain.setTickLabelFont(new Font("Miramonte", Font.BOLD, 14));
    }

    protected void setRangeAxis(Font font, RectangleInsets insets) {
        NumberAxis range = (NumberAxis) plot.getRangeAxis();
        range.setLabelFont(font);
        range.setLabelInsets(insets);
        range.setStandardTickUnits(ticks);
        range.setTickLabelFont(new Font("Miramonte", Font.BOLD, 14));
    }

    public JFreeChart plot(IChart chart) {
        jfreechart = this.createSkeleton(chart);

        // set chart background
        jfreechart.setBackgroundPaint(Color.WHITE);

        this.setFonts();

        this.customizeTitle(titleFont, new Color(61, 61, 61), new RectangleInsets(20, 0, 10, 0));
        jfreechart.getLegend().setPadding(0, 0, 20, 0);
        jfreechart.getLegend().setBorder(0, 0, 0, 0);

        // set a few custom plot features
        plot = (XYPlot) jfreechart.getPlot();

        plot.setBackgroundPaint(new Color(251, 251, 251));
        plot.setDomainGridlinesVisible(true);
        plot.setDomainGridlinePaint(new Color(221, 221, 221));
        plot.setRangeGridlinePaint(new Color(61, 61, 61));

        // set the plot's axes to display integers
        ticks = NumberAxis.createIntegerTickUnits();

        this.setDomainAxis(labelFont, new RectangleInsets(10, 0, 10, 0));
        this.setRangeAxis(labelFont, new RectangleInsets(0, 10, 0, 5));

        // render shapes and lines
        XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer(true, true);
        plot.setRenderer(renderer);        
        renderer.setBaseItemLabelPaint(new Color(61, 61, 61));
        renderer.setBaseItemLabelFont(new Font("Miramonte", Font.BOLD, 14));
//        renderer.setBasePositiveItemLabelPosition(
//            new ItemLabelPosition(ItemLabelAnchor.OUTSIDE12, TextAnchor.BASELINE_LEFT));
//        renderer.setBaseNegativeItemLabelPosition(new ItemLabelPosition(ItemLabelAnchor.OUTSIDE12, TextAnchor.BASELINE_LEFT));
        renderer.setBaseShapesVisible(true);
        renderer.setBaseShapesFilled(true);
//        renderer.setShapesVisible(true);
        renderer.setSeriesPaint(0, new Color(45, 137, 239));
        renderer.setSeriesStroke(0, new BasicStroke(
                2.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND,
                1.0f, new float[]{6.0f, 6.0f}, 0.0f));

        // set the renderer's stroke
        Stroke stroke = new BasicStroke(4f, BasicStroke.JOIN_ROUND, BasicStroke.JOIN_BEVEL);
        renderer.setBaseOutlineStroke(stroke);

        // label the points
        NumberFormat numberFormat = NumberFormat.getNumberInstance();
        numberFormat.setMaximumFractionDigits(3);
        XYItemLabelGenerator generator = new StandardXYItemLabelGenerator(
                StandardXYItemLabelGenerator.DEFAULT_ITEM_LABEL_FORMAT,
                numberFormat, numberFormat);
        renderer.setBaseItemLabelGenerator(generator);
        renderer.setBaseItemLabelsVisible(true);

        return jfreechart;
    }

    private void setFonts() {
        titleFont = new Font("Calibri", Font.BOLD, 36);
        labelFont = new Font("Calibri", Font.BOLD, 28);

        File propertiesFile = new File(this.parameterFile);
        if (propertiesFile.exists()) {
            try {
                this.parseCartesianFile(new FileInputStream(propertiesFile));

            } catch (FileNotFoundException e) {
                throw new UnexpectedException(e.getMessage());
            }
        }
    }

    private void parseCartesianFile(InputStream propertiesInputStream) {
        Properties props = new Properties();
        try {
            props.load(propertiesInputStream);
        } catch (IOException e) {
            throw new UnexpectedException(e.getMessage());
        }

        Pattern regexHead = Pattern.compile("font.(title|label).*(.*|height|bold)");

        for (Map.Entry entry : props.entrySet()) {
            String value = entry.getValue().toString().trim();
            String key = (String) entry.getKey();
            Matcher matcher = regexHead.matcher(key);

            if (matcher.find()) {
                String scope = matcher.group(1);
                if (key.equals(String.format("font.%s.height", scope))) {
                    if (scope.equalsIgnoreCase("title")) {
                        titleFont = titleFont.deriveFont(Float.parseFloat(value));
                    } else {
                        labelFont = labelFont.deriveFont(Float.parseFloat(value));
                    }
                } else if (key.equals(String.format("font.%s.bold", scope))) {
                    boolean bold = Boolean.parseBoolean(value);
                    if (scope.equalsIgnoreCase("title")) {
                        titleFont = titleFont.deriveFont(bold ? Font.BOLD : Font.PLAIN);
                    } else {
                        labelFont = labelFont.deriveFont(bold ? Font.BOLD : Font.PLAIN);
                    }
                } else if (key.equals(String.format("font.%s", scope))) {
                    if (scope.equalsIgnoreCase("title")) {
                        titleFont = new Font(value, titleFont.getStyle(), titleFont.getSize());
                    } else {
                        labelFont = new Font(value, labelFont.getStyle(), labelFont.getSize());
                    }
                }
            }
        }
    }
}
