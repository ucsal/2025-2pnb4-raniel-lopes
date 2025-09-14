package br.com.mariojp.figureeditor;

import java.awt.*;

public class ColoredShape {
    public final Shape shape;
    public final Color color;

    public ColoredShape(Shape shape, Color color) {
        this.shape = shape;
        this.color = color;
    }
}
