package com.smartcodeltd.jenkinsci.plugins.buildmonitor_acceptance.recipes;

/*
 * The MIT License
 *
 * Copyright (c) 2004-2009, Sun Microsystems, Inc., Kohsuke Kawaguchi
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

import org.apache.commons.io.FileUtils;
import org.jvnet.hudson.test.HudsonTestCase;
import org.jvnet.hudson.test.JenkinsRecipe;
import org.jvnet.hudson.test.JenkinsRule;
import org.jvnet.hudson.test.recipes.Recipe;

import java.io.File;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.net.URL;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Installs specified plugins before launching Jenkins
 */
@Documented
@Recipe(With.RunnerImpl.class)
@JenkinsRecipe(With.RuleRunnerImpl.class)
@Target(METHOD)
@Retention(RUNTIME)
public @interface With {
    String[] plugins();

    class RunnerImpl extends Recipe.Runner<With> {
        private With a;

        @Override
        public void setup(HudsonTestCase testCase, With recipe) throws Exception {
            a = recipe;
            testCase.useLocalPluginManager = true;
        }

        @Override
        public void decorateHome(HudsonTestCase testCase, File home) throws Exception {
            for(String plugin : a.plugins()) {
                URL res = getClass().getClassLoader().getResource("plugins/" + plugin);
                FileUtils.copyURLToFile(res, new File(home,"plugins/" + plugin));
            }
        }
    }

    class RuleRunnerImpl extends JenkinsRecipe.Runner<With> {
        private With a;

        @Override
        public void setup(JenkinsRule jenkinsRule, With recipe) throws Exception {
            a = recipe;
            jenkinsRule.useLocalPluginManager = true;
        }

        @Override
        public void decorateHome(JenkinsRule jenkinsRule, File home) throws Exception {
            for(String plugin : a.plugins()) {
                URL res = getClass().getClassLoader().getResource("plugins/" + plugin);
                FileUtils.copyURLToFile(res, new File(home,"plugins/" + plugin));
            }
        }
    }
}