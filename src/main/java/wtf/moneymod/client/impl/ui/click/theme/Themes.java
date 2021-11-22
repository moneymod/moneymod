package wtf.moneymod.client.impl.ui.click.theme;

import wtf.moneymod.client.impl.ui.click.theme.impl.DarkTheme;
import wtf.moneymod.client.impl.ui.click.theme.impl.NodusTheme;
import wtf.moneymod.client.impl.ui.click.theme.impl.WurstTheme;

public enum Themes {

    NODUS(NodusTheme.getInstance()),
    WURST(WurstTheme.getInstance()),
    DARKTHEME(DarkTheme.getInstance());

    private AbstractTheme abstractTheme;

    Themes(AbstractTheme theme) {
        this.abstractTheme = theme;
    }

    public AbstractTheme getTheme() {
        return abstractTheme;
    }
}
