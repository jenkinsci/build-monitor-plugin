package com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel.syntacticsugar;

import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.jenkinsci.plugins.badge.action.BadgeAction;
import java.util.function.Supplier;

/**
 * @author Daniel Beland
 */
public class BadgeBadgePluginRecipe implements Supplier<BadgeAction> {
    private BadgeAction badge;

    public BadgeBadgePluginRecipe() {
        badge = mock(BadgeAction.class);
    }

    public BadgeBadgePluginRecipe withText(String text) {
        when(badge.getIconPath()).thenReturn(null);
        lenient().when(badge.getText()).thenReturn(text);
        return this;
    }

    public BadgeBadgePluginRecipe withIcon(String icon, String text) {
        when(badge.getIconPath()).thenReturn(icon);
        lenient().when(badge.getText()).thenReturn(text);
        return this;
    }

    @Override
    public BadgeAction get() {
        return badge;
    }
}
