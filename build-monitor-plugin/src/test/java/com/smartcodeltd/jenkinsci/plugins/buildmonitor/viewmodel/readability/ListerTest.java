package com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel.readability;

import com.smartcodeltd.jenkinsci.plugins.buildmonitor.readability.Lister;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class ListerTest {
    private static final String APPLE = "apple";
    private static final String ORANGE = "orange";
    private static final String FRUITS_FORMAT = "Fruits: %s";

    @Test
    public void should_return_an_empty_string_when_given_an_empty_list() throws Exception {
        assertThat(Lister.asString(listOf()), is(""));
    }

    @Test
    public void should_return_a_string_representation_of_a_given_object() throws Exception {
        assertThat(Lister.asString(listOf(APPLE)), is(APPLE));
    }

    @Test
    public void should_use_and_instead_of_a_comma_before_the_last_item() throws Exception {
        assertThat(Lister.asString(listOf(APPLE, ORANGE)), is("apple and orange"));
    }

    @Test
    public void should_use_comma_to_delimit_all_the_elements_except_the_last_one() throws Exception {
        assertThat(Lister.asString(listOf(APPLE, ORANGE, "banana")), is("apple, orange and banana"));
    }

    @Test
    public void should_use_an_optional_template_to_describe_a_list_with_multiple_items() throws Exception {
        assertThat(Lister.describe(FRUITS_FORMAT, listOf(APPLE, ORANGE)), is("Fruits: apple and orange"));
    }

    @Test
    public void should_say_when_the_list_has_no_items() throws Exception {
        assertThat(Lister.describe("No fruit for you today :-(", FRUITS_FORMAT, listOf()), is("No fruit for you today :-("));
    }

    @Test
    public void should_allow_to_use_a_different_template_to_describe_a_list_with_one_item() throws Exception {
        assertThat(Lister.describe("Empty", "%s fruit", FRUITS_FORMAT, listOf(APPLE)), is("apple fruit"));
    }

    private <T> List<T> listOf(T... items) {
        return Arrays.asList(items);
    }
}