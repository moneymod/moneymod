package wtf.moneymod.client.impl.ui.click.buttons.settings;

import wtf.moneymod.client.api.setting.Option;
import wtf.moneymod.client.impl.ui.click.Component;
import wtf.moneymod.client.impl.ui.click.Screen;
import wtf.moneymod.client.impl.ui.click.buttons.ModuleButton;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class SliderButton extends Component {

    private final Option<Number> setting;
    private final ModuleButton button;
    private boolean isHovered;
    private int offset;
    private int x;
    private int y;
    private boolean dragging;
    private float renderWidth;

    public SliderButton(Option<Number> setting, ModuleButton button, int offset) {
        this.setting = setting;
        this.button = button;
        this.x = button.panel.getX() + button.panel.getWidth();
        this.y = button.panel.getY() + button.offset;
        this.offset = offset;
    }

    private static double round(final double value, final int places) {
        if (places < 0) {
            throw new IllegalArgumentException();
        }

        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    @Override public void setOffset(final int offset) {
        this.offset = offset;
    }

    @Override public void mouseClicked(final double mouseX, final double mouseY, final int button) {
        if (isHovered(mouseX, mouseY) && button == 0 && this.button.open) {
            dragging = true;
        }
    }

    @Override public void mouseReleased(final double mouseX, final double mouseY, final int mouseButton) {
        dragging = false;
    }

    @Override public void updateComponent(final double mouseX, final double mouseY) {
        isHovered = isHovered(mouseX, mouseY);
        y = button.panel.getY() + offset;
        x = button.panel.getX();
        final double diff = Math.min(106, Math.max(0, mouseX - x));
        final double min = setting.getMin();
        final double max = setting.getMax();
        double inc = setting.getValue().getClass().getSimpleName().equalsIgnoreCase("Integer") ? 1 : 0.1;
        renderWidth = ( float ) (104 * ((inc == 1 ? setting.getValue().intValue() : setting.getValue().floatValue()) - min) / (max - min));
        if (dragging) {
            if (diff == 0){
                if (inc == 1) setting.setValue(setting.getMin());
                else setting.setValue(setting.getMin());
            }
            else {
                final float newValue = ( float ) round(diff / 104 * (max - min) + min, 1);
                float precision = ( float ) (1.0D / inc);
                if(inc == 0.1) setting.setValue(Math.round(Math.max(min, Math.min(max, newValue)) * precision) / precision);
                else setting.setValue((int) Math.max(min, Math.min(max, newValue)));
            }
        }
    }

    @Override public void render(int mouseX, int mouseY) {
        Screen.abstractTheme.drawSliderButton(setting, button.panel.getX(), button.panel.getY() + offset, button.panel.getWidth(),12, renderWidth, isHovered);
    }

    public boolean isHovered(final double x, final double y) {
        return x > this.x && x < this.x + 110 && y > this.y && y < this.y + 12;
    }

}
