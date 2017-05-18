/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.shaunyl.datareporter.exporter.graph.core;

import it.shaunyl.datareporter.exporter.graph.IChart;
import lombok.*;

/**
 *
 * @author Filippo Testino (filippo.testino@gmail.com)
 */
public class CartesianChart extends IChart {

    @Getter @Setter
    private String xlabel, ylabel;

    public static final String DEFAULT_X_LABEL = "X Axis";
    public static final String DEFAULT_Y_LABEL = "Y Axis";

    public CartesianChart(String title) {
        super(title, false);
    }

    public CartesianChart(String title, boolean legend) {
        this(title, legend, DEFAULT_X_LABEL, DEFAULT_Y_LABEL);
    }

    public CartesianChart(String title, boolean legend, String xlabel, String ylabel) {
        super(title, legend);
        this.xlabel = xlabel;
        this.ylabel = ylabel != null ? ylabel : DEFAULT_Y_LABEL;
    }
}
