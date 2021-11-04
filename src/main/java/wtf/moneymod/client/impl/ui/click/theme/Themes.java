package wtf.moneymod.client.impl.ui.click.theme;

import wtf.moneymod.client.impl.ui.click.theme.impl.NodusTheme;

public enum Themes {

    NODUS(NodusTheme.getInstance());

    private AbstractTheme abstractTheme;

    Themes(AbstractTheme theme) {
        this.abstractTheme = theme;
    }

    public AbstractTheme getTheme() {
        return abstractTheme;
    }
}
