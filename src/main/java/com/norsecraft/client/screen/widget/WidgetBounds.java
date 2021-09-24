package com.norsecraft.client.screen.widget;

public class WidgetBounds {

    public static final WidgetBounds NULL = new WidgetBounds(0, 0, 0, 0);

    public int x;
    public int y;
    public int width;
    public int height;

    public WidgetBounds(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public WidgetBounds(WidgetBounds other) {
        this.x = other.x;
        this.y = other.y;
        this.width = other.width;
        this.height = other.height;
    }

}
