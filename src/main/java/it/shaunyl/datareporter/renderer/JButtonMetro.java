/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.shaunyl.datareporter.renderer;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Polygon;
import java.awt.RenderingHints;
import javax.swing.AbstractButton;
import javax.swing.ButtonModel;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.plaf.ButtonUI;
import javax.swing.plaf.basic.BasicButtonListener;

/**
 *
 * @author Filippo
 */
public class JButtonMetro extends JButton {

    private Icon icon;

    public JButtonMetro() {
        this(null);
    }

    public JButtonMetro(String title) {
        this(title, null);
    }

    public JButtonMetro(String title, Icon icon) {
        super(title, icon);
        this.icon = icon;
        this.setUI(new MetroButtonUI());
    }

    static class MetroButtonUI extends ButtonUI {

        private final Insets DEFAULT_INSETS = new Insets(6, 13, 6, 13);
        private static final Color bgPressedColor = new Color(9, 91, 153);
//        private static final Color bgPressedColor = new Color(102, 179, 255);
        private static final Color bgDefaultColor = new Color(45, 137, 239);
        private static final Color bgRolloverColor = new Color(27, 127, 204);
//        private static final Color bgRolloverColor = new Color(61, 121, 222);

        public void installUI(JComponent c) {
            AbstractButton b = (AbstractButton) c;
            BasicButtonListener listener = new BasicButtonListener(b);
            b.addMouseListener(listener);
        }

        public void uninstallUI(JComponent c) {
            AbstractButton b = (AbstractButton) c;
            BasicButtonListener listener = new BasicButtonListener(b);
            b.removeMouseListener(listener);
        }

        public Insets getDefaultMargin(AbstractButton ab) {
            return DEFAULT_INSETS;
        }

        public Insets getInsets(JComponent c) {
            return DEFAULT_INSETS;
        }

        public Dimension getMaximumSize(JComponent c) {
            return this.getPreferredSize(c);
        }

        public Dimension getMinimumSize(JComponent c) {
            return this.getPreferredSize(c);
        }

        public Dimension getPreferredSize(JComponent c) {
            Graphics g = c.getGraphics();
            FontMetrics fm = g.getFontMetrics();

            Dimension d = new Dimension();
            d.width = fm.stringWidth(((JButton) c).getText()) + DEFAULT_INSETS.left + DEFAULT_INSETS.right;
            d.height = fm.getHeight() + DEFAULT_INSETS.top + DEFAULT_INSETS.bottom;
            return d;
        }

        public void paint(Graphics g, JComponent c) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            AbstractButton b = (AbstractButton) c;
            ButtonModel bm = b.getModel();

            FontMetrics fm = g2.getFontMetrics();

            // Define some colors for drawing..
            Color backgroundColor;
//            Color bevelColor;
            Color lineColor;

            if (bm.isPressed()) {
                backgroundColor = bgPressedColor;
//                bevelColor = bgPressedColor;
                lineColor = new Color(191, 191, 191);
            } else if (bm.isRollover()) {
                backgroundColor = bgRolloverColor;
//                bevelColor = bgRolloverColor;
                lineColor = new Color(201, 201, 201);
            } else if (!bm.isEnabled()) {
                backgroundColor = new Color(248, 248, 248);
//                bevelColor = Color.LIGHT_GRAY;
                lineColor = new Color(191, 191, 191);
            } else {
                backgroundColor = bgDefaultColor;
//                bevelColor = Color.PINK;
                lineColor = new Color(221, 221, 221);
            }

            // Define some polygons for drawing..

            Dimension d = c.getPreferredSize();
            int x = d.width - 1;    // x-coordinate of right edge
            int y = d.height - 1;   // y-coordinate of bottom edge
            final int BW = 1;       // bevel width

            int[] outerX = {0, 0, x, x};
            int[] outerY = {0, y, y, 0};
            //int[] outerY = { 0, y, 0, 0 };

//            int[] innerX = {BW, BW, x - BW, x - BW};
//            int[] innerY = {BW, y - BW, y - BW, BW};
//
//            int[] topBevelX = {0, BW, x - BW, x};
//            int[] topBevelY = {0, BW, BW, 0};
//
//            int[] leftBevelX = {0, 0, BW, BW};
//            int[] leftBevelY = {0, y, y - BW, BW};
//
//            int[] bottomBevelX = {0, x, x - BW, BW};
//            int[] bottomBevelY = {y, y, y - BW, y - BW};
//
//            int[] rightBevelX = {x, x - BW, x - BW, x};
//            int[] rightBevelY = {0, BW, y - BW, y};

            Polygon outer = new Polygon(outerX, outerY, outerX.length);
//            Polygon inner = new Polygon(innerX, innerY, innerX.length);
//            Polygon topBevel = new Polygon(topBevelX, topBevelY, topBevelX.length);
//            Polygon leftBevel = new Polygon(leftBevelX, leftBevelY, leftBevelX.length);
//            Polygon bottomBevel = new Polygon(bottomBevelX, bottomBevelY, bottomBevelX.length);
//            Polygon rightBevel = new Polygon(rightBevelX, rightBevelY, rightBevelX.length);

            g2.setColor(backgroundColor);
            g2.fillPolygon(outer);

//            g2.setColor(bevelColor);
//            g2.fillPolygon(topBevel);
//            g2.fillPolygon(rightBevel);
//            g2.fillPolygon(leftBevel);
//            g2.fillPolygon(bottomBevel);

            g2.setColor(lineColor);
            g2.drawPolygon(outer);
//            g2.drawPolygon(inner);
//            g2.drawPolygon(topBevel);
//            g2.drawPolygon(leftBevel);
//            g2.drawPolygon(bottomBevel);
//            g2.drawPolygon(rightBevel);
//            g2.drawPolygon(outer);

            // paint foreground

            if (bm.isEnabled()) {
                g2.setColor(Color.WHITE);
            } else {
                g2.setColor(new Color(51, 51, 51));
            }
            
            String s = ((JButton) c).getText();
            x = DEFAULT_INSETS.left;
            y = DEFAULT_INSETS.top + fm.getAscent();

//            g2.(new ImageIcon(icon), AffineTransform.TYPE_UNIFORM_SCALE, );
            g2.drawString(s, x, y);
        }
    }
}
