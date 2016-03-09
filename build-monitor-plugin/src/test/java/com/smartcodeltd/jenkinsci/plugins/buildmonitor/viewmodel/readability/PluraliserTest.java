package com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel.readability;

import com.smartcodeltd.jenkinsci.plugins.buildmonitor.readability.Pluraliser;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class PluraliserTest {
    @Test
    public void should_use_the_plural_format_if_no_other_is_specified() throws Exception {
        assertThat(Pluraliser.pluralise("%s apples", 0), is("0 apples"));
        assertThat(Pluraliser.pluralise("%s apples", 1), is("1 apples")); // eek, said the grammar geek! ;-)
        assertThat(Pluraliser.pluralise("%s apples", 5), is("5 apples"));
    }

    @Test
    public void should_use_a_singular_format_if_the_count_equals_one() throws Exception {
        assertThat(Pluraliser.pluralise("%s apple", "%s apples", 0), is("0 apples"));
        assertThat(Pluraliser.pluralise("%s apple", "%s apples", 1), is("1 apple"));
        assertThat(Pluraliser.pluralise("%s apple", "%s apples", 5), is("5 apples"));
    }

    @Test
    public void should_use_a_correct_format_depending_on_the_count_provided() throws Exception {
        assertThat(Pluraliser.pluralise("no apples :-(", "%s apple", "%s apples", 0), is("no apples :-("));
        assertThat(Pluraliser.pluralise("no apples :-(", "%s apple", "%s apples", 1), is("1 apple"));
        assertThat(Pluraliser.pluralise("no apples :-(", "%s apple", "%s apples", 5), is("5 apples"));
    }
}
