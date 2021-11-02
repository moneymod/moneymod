package wtf.moneymod.client.impl.utility.impl.render;

import java.awt.*;

public class JColor {

    private Color color;
    private boolean rainbow;

    public JColor(Color color, boolean rainbow) {
        this.color = color;
        this.rainbow = rainbow;
    }

    public JColor(int r, int g, int b, boolean rainbow) {
        this.color = new Color(r,g,b);
        this.rainbow = rainbow;
    }

    public JColor(int r, int g, int b, int a, boolean rainbow) {
        this.color = new Color(r,g,b, a);
        this.rainbow = rainbow;
    }

    public Color getColor() {
        return color;
    }

    public boolean isRainbow() {
        return rainbow;
    }

    public void setRainbow(boolean rainbow) {
        this.rainbow = rainbow;
    }

    public void setColor(Color color) {
        this.color = color;
    }

}
