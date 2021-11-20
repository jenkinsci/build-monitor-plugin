package com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel.syntacticsugar;

import com.jenkinsci.plugins.badge.action.BadgeAction;

import java.util.function.Supplier;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author Daniel Beland
 */
public class BadgeBadgePluginRecipe implements Supplier<BadgeAction> {
    private BadgeAction badge;

    public BadgeBadgePluginRecipe() {
        badge = mock(BadgeAction.class);
    }

    public BadgeBadgePluginRecipe withText(String text) throws Exception {
        when(badge.getIconPath()).thenReturn(null);
        when(badge.getText()).thenReturn(text);
        return this;
    }

    public BadgeBadgePluginRecipe withIcon(String icon, String text) throws Exception {
        when(badge.getIconPath()).thenReturn(icon);
        when(badge.getText()).thenReturn(text);
        return this;
    }

    @Override
    public BadgeAction get() {
        return badge;
    }
}
