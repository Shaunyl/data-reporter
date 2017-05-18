/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.shaunyl.datareporter.exporter.graph;

import lombok.*;

/**
 *
 * @author Filippo Testino (filippo.testino@gmail.com)
 */
public abstract class IChart {

    @Getter @Setter
    protected String title;
    @Getter @Setter
    protected boolean legend;

    public IChart(String title) {
        this(title, false);
    }

    public IChart(String title, boolean legend) {
        this.title = title;
        this.legend = legend;
    }
}
