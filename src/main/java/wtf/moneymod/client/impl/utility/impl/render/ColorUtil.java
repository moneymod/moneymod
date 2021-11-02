package wtf.moneymod.client.impl.utility.impl.render;

import org.lwjgl.opengl.GL11;

import java.awt.*;

public class ColorUtil {

    public static Color injectAlpha(Color color, int alpha ) {
        return new Color( color.getRed( ), color.getGreen( ), color.getBlue( ), alpha );
    }

    public static Color injectBrightness( Color color, float brightness ) {
        float[] hsb = Color.RGBtoHSB( color.getRed( ), color.getGreen( ), color.getBlue( ), null );
        return Color.getHSBColor( hsb[ 0 ], hsb[ 1 ], brightness );
    }

    public static Color rainbowColor( int delay, float s, float b ) {
        return Color.getHSBColor( ( ( System.currentTimeMillis( ) + delay ) % ( 360 * 32 ) ) / ( 360f * 32 ), s, b );
    }

    public static void glColor(Color color) {
        GL11.glColor4f((float)color.getRed() / 255.0F, (float)color.getGreen() / 255.0F, (float)color.getBlue() / 255.0F, (float)color.getAlpha() / 255.0F);
    }


    public static Color releasedDynamicRainbow( final int delay, int alpha ) {
        double rainbowState = Math.ceil( ( System.currentTimeMillis( ) + delay ) / 20.0 );
        rainbowState %= 360.0;

        Color c = Color.getHSBColor( ( float ) ( rainbowState / 360.0 ), 1f, 1f );
        return new Color( c.getRed( ), c.getGreen( ), c.getBlue( ), alpha );
    }

}
