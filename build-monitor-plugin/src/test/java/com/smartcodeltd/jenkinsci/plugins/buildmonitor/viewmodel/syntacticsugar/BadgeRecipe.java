package com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel.syntacticsugar;

import com.google.common.base.Supplier;

import com.jenkinsci.plugins.badge.action.BadgeAction;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author Daniel Beland
 */
public class BadgeRecipe implements Supplier<BadgeAction> {
    private BadgeAction badge;

    public BadgeRecipe() {
    	badge = mock(BadgeAction.class);
    }

    public BadgeRecipe withText(String text) throws Exception {
    	when(badge.getIconPath()).thenReturn(null);
    	when(badge.getText()).thenReturn(text);
        return this;
    }

    public BadgeRecipe withIcon(String icon, String text) throws Exception {
    	when(badge.getIconPath()).thenReturn(icon);
    	when(badge.getText()).thenReturn(text);
        return this;
    }

    @Override
    public BadgeAction get() {
        return badge;
    }
}
