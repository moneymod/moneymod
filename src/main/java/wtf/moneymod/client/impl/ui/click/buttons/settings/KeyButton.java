package wtf.moneymod.client.impl.ui.click.buttons.settings;

import net.minecraft.client.gui.Gui;
import org.lwjgl.input.Keyboard;
import wtf.moneymod.client.Main;
import wtf.moneymod.client.impl.module.global.ClickGui;
import wtf.moneymod.client.impl.ui.click.Component;
import wtf.moneymod.client.impl.ui.click.Screen;
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
        if ( isHovered( mouseX, mouseY ) && this.button.open ) {
            if(button == 0) {
                binding = !binding;
            } if(button == 1) {
                this.button.module.setHold(!this.button.module.isHold());
            }
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
        Screen.abstractTheme.drawKeyButton(this, button.panel.getX(), button.panel.getY() + offset, button.panel.getWidth(), 12, isHovered(mouseX,mouseY));
    }

    public boolean isHovered( final double x, final double y ) {
        return x > this.x && x < this.x + 110 && y > this.y && y < this.y + 12;
    }

    public boolean isBinding() {
        return binding;
    }

}
