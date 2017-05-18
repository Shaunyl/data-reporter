package it.shaunyl.datareporter.renderer;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.geom.Area;
import java.awt.geom.RoundRectangle2D;
import javax.swing.BorderFactory;
import javax.swing.CellRendererPane;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.UIManager;
import javax.swing.border.AbstractBorder;
import javax.swing.plaf.basic.BasicComboBoxUI;
import javax.swing.plaf.basic.BasicComboPopup;
import javax.swing.plaf.basic.ComboPopup;

/**
 *
 * @author Filippo Testino
 */
public class JComboBoxMetro<T> extends JComboBox<T> {

    public static final Color DEFAULT_BACKGROUND_COLOR = new Color(45, 137, 239);

    static {
        UIManager.put("ComboBox.foreground", Color.WHITE);
        UIManager.put("ComboBox.background", DEFAULT_BACKGROUND_COLOR);
        UIManager.put("ComboBox.selectionForeground", Color.WHITE);
        UIManager.put("ComboBox.selectionBackground", new Color(45, 137, 239)/*new Color(102, 179, 255)*/);
        UIManager.put("ComboBox.buttonDarkShadow", Color.WHITE);
        UIManager.put("ComboBox.border", new RoundedCornerBorder());
    }

    public JComboBoxMetro() {
        render();
    }

    public JComboBoxMetro(ComboBoxModel dataModel) {
        super(dataModel);
        render();
    }

    public JComboBoxMetro(T[] items) {
        super(items);
        render();
    }

    public void render() {
        this.setUI(new BasicComboBoxUI() {
            @Override
            protected JButton createArrowButton() {
                JButton b = super.createArrowButton();
                b.setContentAreaFilled(false);
                b.setBackground(new Color(45, 137, 239));
                b.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 0, new Color(/*9, 91, 153*/11, 103, 205)));
                return b;
            }

            @Override
            public void paintCurrentValue(Graphics g, Rectangle bounds, boolean hasFocus) {
                Graphics2D g2 = (Graphics2D)g;
                g2.setColor(new Color(/*9, 91, 153*/11, 103, 205));
                  
                int topX = bounds.width - 1;
                int topY = 6;
                int bottomX = topX;
                int bottomY = bounds.height + 2;
                g2.setStroke(new BasicStroke(2));
                g2.drawLine(topX, topY, bottomX, bottomY);

                // -15 serve per limitare la lunghezza del testo selezionato, altrimenti fa sopra la linea che disegno prima
                super.paintCurrentValue(g, new Rectangle(bounds.x, bounds.y, bounds.width - 15, bounds.height), hasFocus);
            }
 
            @Override
            protected ComboPopup createPopup() {
                BasicComboPopup bcp = (BasicComboPopup) super.createPopup();

                bcp.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(221, 221, 221), 1), BorderFactory.createLineBorder(Color.WHITE, 3)));
                bcp.setBackground(Color.WHITE);

                JList list = bcp.getList();
                list.setBackground(Color.WHITE);
                list.setForeground(new Color(81, 81, 81));

                return bcp;
            }
        });
        this.setRenderer(new ComboBoxRenderer());
    }

    static class RoundedCornerBorder extends AbstractBorder {

        private static final long serialVersionUID = 1L;

        @Override
        public void paintBorder(
                Component c, Graphics g, int x, int y, int width, int height) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            int r = 11;
            Area round = new Area(new RoundRectangle2D.Float(x, y, width - 1, height - 1, r, r));
            Rectangle b = round.getBounds();
            b.setBounds(b.x, b.y, b.width, b.height/* + r, b.width, b.height - r*/);
            round.add(new Area(b));

//            Container parent = c.getParent();
//            if (parent != null) {
//                g2.setColor(Color.RED/*parent.getBackground()*/);
//                Area corner = new Area(new Rectangle2D.Float(x, y, width, height));
//                corner.subtract(round);
//                g2.fill(corner);
//            }
//            g2.setColor(new Color(45, 137, 239));
//            g2
//            JPanel panel = (JPanel) parent;

            g2.setColor(new Color(221, 221, 221));
            g2.draw(round);

//            g2.dispose();
        }

        @Override
        public Insets getBorderInsets(final Component c) {
            return new Insets(4, 8, 4, 8);
        }

        @Override
        public Insets getBorderInsets(final Component c, final Insets insets) {
            insets.left = insets.right = 8;
            insets.top = insets.bottom = 4;
            return insets;
        }
    }

    static class ComboBoxRenderer extends DefaultListCellRenderer {

        ComboBoxRenderer() {
            setOpaque(true);
        }

        public Component getListCellRendererComponent(
                JList list,
                Object value,
                int index,
                boolean isSelected,
                boolean cellHasFocus) {

            JLabel c = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            if (isSelected) {
                c.setBackground(new Color(102, 179, 255));
            } else {
                c.setBackground(Color.WHITE);
            }

            return this;
        }
    }
}