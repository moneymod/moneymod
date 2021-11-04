package wtf.moneymod.client.impl.ui.click.theme;

import wtf.moneymod.client.impl.ui.click.theme.impl.NodusTheme;
import wtf.moneymod.client.impl.ui.click.theme.impl.TestTheme;

public enum Themes {

    NODUS(NodusTheme.getInstance()),
    TESTTHEME(TestTheme.getInstance());

    private AbstractTheme abstractTheme;

    Themes(AbstractTheme theme) {
        this.abstractTheme = theme;
    }

    public AbstractTheme getTheme() {
        return abstractTheme;
    }
}
