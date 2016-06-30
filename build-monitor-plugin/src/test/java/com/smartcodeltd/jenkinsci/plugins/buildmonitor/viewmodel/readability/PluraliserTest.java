package com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel.readability;

import com.smartcodeltd.jenkinsci.plugins.buildmonitor.readability.Pluraliser;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class PluraliserTest {
    private static final String APPLES_FORMAT="%s apples";
    private static final String APPLE_FORMAT="%s apple";
    private static final String NO_APPLES="no apples :-(";

    @Test
    public void should_use_the_plural_format_if_no_other_is_specified() throws Exception {
        assertThat(Pluraliser.pluralise(APPLES_FORMAT, 0), is("0 apples"));
        assertThat(Pluraliser.pluralise(APPLES_FORMAT, 1), is("1 apples")); // eek, said the grammar geek! ;-)
        assertThat(Pluraliser.pluralise(APPLES_FORMAT, 5), is("5 apples"));
    }

    @Test
    public void should_use_a_singular_format_if_the_count_equals_one() throws Exception {
        assertThat(Pluraliser.pluralise(APPLE_FORMAT, APPLES_FORMAT, 0), is("0 apples"));
        assertThat(Pluraliser.pluralise(APPLE_FORMAT, APPLES_FORMAT, 1), is("1 apple"));
        assertThat(Pluraliser.pluralise(APPLE_FORMAT, APPLES_FORMAT, 5), is("5 apples"));
    }

    @Test
    public void should_use_a_correct_format_depending_on_the_count_provided() throws Exception {
        assertThat(Pluraliser.pluralise(NO_APPLES, APPLE_FORMAT, APPLES_FORMAT, 0), is(NO_APPLES));
        assertThat(Pluraliser.pluralise(NO_APPLES, APPLE_FORMAT, APPLES_FORMAT, 1), is("1 apple"));
        assertThat(Pluraliser.pluralise(NO_APPLES, APPLE_FORMAT, APPLES_FORMAT, 5), is("5 apples"));
    }
}
