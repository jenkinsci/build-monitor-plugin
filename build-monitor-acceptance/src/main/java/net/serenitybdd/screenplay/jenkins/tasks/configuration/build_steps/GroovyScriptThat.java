package net.serenitybdd.screenplay.jenkins.tasks.configuration.build_steps;

public class GroovyScriptThat {
    public static final GroovyScript Adds_A_Badge = GroovyScript.that("Adds a badge")
            .definedAs(
                    "addShortText(text:'Coverage', color:'black', background:'repeating-linear-gradient(45deg, yellow, yellow 10px, Orange 10px, Orange 20px)', border: 0, borderColor:'white')");

    public static final GroovyScript Pause_In_Middle_Of_Tests = GroovyScript.that("Pause in the middle of tests")
            .separatedBy("")
            .definedAs(
                    "node {",
                    "   parallel firstBranch: {",
                    "       stage('stage1') {",
                    "           realtimeJUnit('a*.xml') {",
                    "               writeFile text: '''<testsuite name='aa' time='4'><testcase name='aa1' time='3'/><testcase name='aa2' time='1'/></testsuite>''', file: 'aa.xml';",
                    "               if (currentBuild.number > 1) {",
                    "                   sleep 300;",
                    "               };",
                    "               writeFile text: '''<testsuite name='ab' time='2'><testcase name='ab1' time='1'/><testcase name='ab2' time='1'/></testsuite>''', file: 'ab.xml';",
                    "           }",
                    "       }",
                    "   }, secondBranch: {",
                    "       stage('stage2') {",
                    "           realtimeJUnit('b*.xml') {",
                    "               writeFile text: '''<testsuite name='ba' time='2'><testcase name='ba1' time='1'/><testcase name='ba2' time='1'><error message='ba2 failed'>ba2 failed</error></testcase></testsuite>''', file: 'ba.xml';",
                    "               if (currentBuild.number > 1) {",
                    "                   sleep 300;",
                    "               };",
                    "               writeFile text: '''<testsuite name='bb' time='4'><testcase name='bb1' time='1'/><testcase name='bb2' time='3'><error message='bb2 failed'>bb2 failed</error></testcase></testsuite>''', file: 'bb.xml';",
                    "           }",
                    "       }",
                    "   },",
                    "   failFast: true;",
                    "   deleteDir();",
                    "}");
}
