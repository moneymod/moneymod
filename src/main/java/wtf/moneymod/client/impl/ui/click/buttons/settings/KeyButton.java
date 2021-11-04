package wtf.moneymod.client.impl.ui.click.buttons.settings;

import net.minecraft.client.gui.Gui;
import org.lwjgl.input.Keyboard;
import wtf.moneymod.client.Main;
import wtf.moneymod.client.impl.module.global.ClickGui;
import wtf.moneymod.client.impl.ui.click.Component;
import wtf.moneymod.client.impl.ui.click.buttons.ModuleButton;

import java.awt.*;

public class KeyButton extends Component {

    private boolean binding;
    public final ModuleButton button;
    private boolean isHovered;
    private int offset;
    private int x;
    private int y;

    public KeyButton(final ModuleButton button, final int offset ) {
        this.button = button;
        this.x = button.panel.getX( ) + button.panel.getWidth( );
        this.y = button.panel.getY( ) + button.offset;
        this.offset = offset;
    }

    @Override public void setOffset( final int offset ) {
        this.offset = offset;
    }

    @Override public void updateComponent( final double mouseX, final double mouseY ) {
        isHovered = isHovered( mouseX, mouseY );
        y = button.panel.getY( ) + this.offset;
        x = button.panel.getX( );
    }

    @Override public void mouseClicked( final double mouseX, final double mouseY, final int button ) {
        if ( isHovered( mouseX, mouseY ) && button == 0 && this.button.open ) {
            binding = !binding;
        }
    }

    @Override public void keyTyped( final int key ) {
        if ( this.binding ) {
            if ( key == Keyboard.KEY_DELETE ) button.module.setKey( Keyboard.KEY_NONE );
            else button.module.setKey( key );
            this.binding = false;
        }
    }

    @Override public void render( int mouseX, int mouseY ) {
        Gui.drawRect( button.panel.getX( ), button.panel.getY( ) + offset, button.panel.getX( ) + button.panel.getWidth( ), button.panel.getY( ) + offset + 12, isHovered ? new Color( 0, 0, 0, 160 ).getRGB( ) : new Color( 0, 0, 0, 140 ).getRGB( ) );
        mc.fontRenderer.drawStringWithShadow( "Key", button.panel.getX( ) + 5, button.panel.getY( ) + offset + 2, -1 );
        if ( binding ) {
            mc.fontRenderer.drawStringWithShadow( "...", button.panel.getX( ) + button.panel.getWidth( ) - 5 - mc.fontRenderer.getStringWidth( "..." ), button.panel.getY( ) + offset + ( ( ( ClickGui ) Main.getMain().getModuleManager( ).get( ClickGui.class ) ).bounding ? ( isHovered ? 1 : 2 ) : 2 ), -1 );
        }
        else {
            String key;
            switch ( button.module.getKey( ) ) {
                case 345:
                    key = "RCtrl";
                    break;
                case 341:
                    key = "Ctrl";
                    break;
                case 346:
                    key = "RAlt";
                    break;
                case -1:
                    key = "NONE";
                    break;
                default:
                    key = Keyboard.getKeyName( button.module.getKey( ) );
            }

            mc.fontRenderer.drawStringWithShadow( key, button.panel.getX( ) + button.panel.getWidth( ) - 5 - mc.fontRenderer.getStringWidth( key ), button.panel.getY( ) + offset + ( ( ( ClickGui ) Main.getMain().getModuleManager( ).get( ClickGui.class ) ).bounding ? ( isHovered ? 1 : 2 ) : 2 ), -1 );
        }
    }

    public boolean isHovered( final double x, final double y ) {
        return x > this.x && x < this.x + 110 && y > this.y && y < this.y + 12;
    }

    public boolean isBinding() {
        return binding;
    }

}
