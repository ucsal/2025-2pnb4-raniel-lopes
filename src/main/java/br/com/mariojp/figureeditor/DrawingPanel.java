package br.com.mariojp.figureeditor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;

class DrawingPanel extends JPanel {

    private static final long serialVersionUID = 1L;
    private final List<ColoredShape> shapes = new ArrayList<>();
    private Point startDrag = null;
    private Point endDrag = null;
    private Color currentColor  = new Color(30,144,255);

    DrawingPanel() {
        setBackground(Color.WHITE);
        setOpaque(true);
        setDoubleBuffered(true);

        JButton colorButton = new JButton("Cor...");
        colorButton.addActionListener(e -> {
            Color chosen = JColorChooser.showDialog(this, "Escolha a cor", currentColor);
            if (chosen != null) {
                currentColor = chosen;
            }
        });
        this.add(colorButton);

        var mouse = new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                startDrag = e.getPoint();
                endDrag = startDrag;
                repaint();
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                endDrag = e.getPoint();
                repaint();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (startDrag != null && endDrag != null) {
                    int x = Math.min(startDrag.x, endDrag.x);
                    int y = Math.min(startDrag.y, endDrag.y);
                    int w = Math.abs(startDrag.x - endDrag.x);
                    int h = Math.abs(startDrag.y - endDrag.y);
                    if (w > 10 && h > 10) {
                        Shape s = new Ellipse2D.Double(x, y, w, h);
                        shapes.add(new ColoredShape(s, currentColor));
                    }
                }
                startDrag = null;
                endDrag = null;
                repaint();
            }
        };
        addMouseListener(mouse);
        addMouseMotionListener(mouse);
    }

    void clear() {
        shapes.clear();
        repaint();
    }

    @Override protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        for (ColoredShape cs : shapes) {
            g2.setColor(cs.color);
            g2.fill(cs.shape);
            g2.setColor(new Color(0,0,0,70));
            g2.setStroke(new BasicStroke(1.2f));
            g2.draw(cs.shape);
        }

        // Desenha pré-visualização tracejada
        if (startDrag != null && endDrag != null) {
            int x = Math.min(startDrag.x, endDrag.x);
            int y = Math.min(startDrag.y, endDrag.y);
            int w = Math.abs(startDrag.x - endDrag.x);
            int h = Math.abs(startDrag.y - endDrag.y);
            if (w > 10 && h > 10) {
                Stroke dashed = new BasicStroke(2, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{9}, 0);
                g2.setColor(currentColor);
                g2.setStroke(dashed);
                g2.draw(new Ellipse2D.Double(x, y, w, h));
            }
        }

        g2.dispose();
    }
}
