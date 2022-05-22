package yutong.tools;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Insets;
import java.awt.Rectangle;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.Scrollable;

public class ScrollablePanel extends JPanel implements Scrollable //public class ScrollablePanel extends JPanel
{

    @Override
    public Dimension getPreferredScrollableViewportSize() {
        return getPreferredSize();
    }

    @Override
    public int getScrollableUnitIncrement(
            Rectangle visibleRect, int orientation, int direction) {
        return 20;
    }

    @Override
    public int getScrollableBlockIncrement(
            Rectangle visibleRect, int orientation, int direction) {
        return 60;
    }

    @Override
    public boolean getScrollableTracksViewportWidth() {
        return true;
    }

    @Override
    public boolean getScrollableTracksViewportHeight() {
        return false;
    }

    /**
     * This LayoutManager alters java.awt.FlowLayout to solve a problem with
     * FlowLayout when FlowLayout needs to wrap the components. Specifically,
     * the preferred height of a FlowLayout is always the height of 1 row of
     * components, not the total height of all the rows, which sometimes results
     * in only the first row of components being visible.
     */
    public static class BetterFlowLayout extends FlowLayout {

        public BetterFlowLayout() {
            super();
        }

        public BetterFlowLayout(int align) {
            super(align);
        }

        public BetterFlowLayout(int align, int hgap, int vgap) {
            super(align, hgap, vgap);
        }

        @Override
        public Dimension preferredLayoutSize(Container target) {
            return betterPreferredSize(target);
        }

        @Override
        public Dimension minimumLayoutSize(Container target) {
            return betterPreferredSize(target);
        }

        public Dimension betterPreferredSize(Container target) {
            synchronized (target.getTreeLock()) {
                Insets insets = target.getInsets();
                int maxwidth = target.getWidth() - (insets.left + insets.right + getHgap() * 2);
                int nmembers = target.getComponentCount();
                int x = 0, y = insets.top + getVgap();
                int rowh = 0;

                for (int i = 0; i < nmembers; i++) {
                    Component m = target.getComponent(i);
                    if (m.isVisible()) {
                        Dimension d = m.getPreferredSize();
                        m.setSize(d.width, d.height);

                        if ((x == 0) || ((x + d.width) <= maxwidth)) {
                            if (x > 0) {
                                x += getHgap();
                            }
                            x += d.width;
                            rowh = Math.max(rowh, d.height);
                        } else {
                            x = d.width;
                            y += getVgap() + rowh;
                            rowh = d.height;
                        }
                    }
                }
                return new Dimension(maxwidth, y + rowh + getVgap());
            }
        }
    }
}
