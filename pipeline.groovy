def version = 'unknown'

stage 'Build'
node('standard') {

    git url: 'git@github.com:jan-molak/jenkins-build-monitor-plugin.git', branch: 'acceptance'

    use_jdk    '1.7.latest'
    use_nodejs '0.10.26'

    mvn "release-candidate:updateVersion"
    mvn "clean package --projects build-monitor-plugin"

    version = read_property('version', 'build-monitor-plugin/target/classes/build-monitor.properties');

    assign_build_name version

    archive_junit_results '**/target/surefire-reports/TEST-*.xml,**/target/javascript/test-results.xml'

    stash name: 'sources', includes: '**,**/target/*.hpi', excludes: '**/target/**/*'
}

stage 'Verify'
node('standard') {

    unstash 'sources'

    use_jdk '1.7.latest'

    with_browser_stack 'linux-x64', {
        mvn "verify --projects build-monitor-acceptance"
    }

    archive_artifacts 'target/*.hpi,pom.xml'
}

stage 'Publish to GitHub'
node('standard') {

    unstash 'sources'

    sh "git status"

//    push_release_branch_for version
}

// --

def assign_build_name (new_name) {
    currentBuild.setDisplayName(new_name)
}

def read_property(property_name, properties_file) {
    def matcher = readFile(properties_file) =~ "${property_name}=(.+)"

    if (! matcher) {
        throw "Couldn't find '${property_name}' in '${properties_file}" as Throwable
    }

    return matcher[0][1]
}

def archive_artifacts(artifacts) {
    step([$class: 'ArtifactArchiver', artifacts: artifacts])
}

def archive_junit_results(results) {
    step([$class: 'JUnitResultArchiver', testResults: results])
}

def with_browser_stack(version, actions) {

    echo "> Using BrowserStackLocal for '${version}'"

    if (! env.BROWSERSTACK_AUTOMATION_KEY) {
        throw "Looks like you haven't specified the BROWSERSTACK_AUTOMATION_KEY env variable." as Throwable
    }

    def auth_key = env.BROWSERSTACK_AUTOMATION_KEY

    if (!fileExists("/var/tmp/BrowserStackLocal")) {
        exec "curl -sS https://www.browserstack.com/browserstack-local/BrowserStackLocal-${version}.zip > /var/tmp/BrowserStackLocal.zip", 'Downloading BrowserStackLocal'
        exec "unzip -o /var/tmp/BrowserStackLocal.zip -d /var/tmp", 'Unpacking BrowserStackLocal'
        exec "chmod +x /var/tmp/BrowserStackLocal", 'Making the BrowserStackLocal binary executable'
    }

    spawn "browserstack-${version}", "/var/tmp/BrowserStackLocal ${auth_key} -onlyAutomate -forcelocal"

    try {
        actions()
    } catch(e) {
        echo "ERROR: ${e}"
        throw e
    } finally {
        stop "browserstack-${version}"
    }
}

def spawn (name, executable) {
    exec "BUILD_ID=dontKillMe nohup ${executable} > /var/tmp/${name}.log 2>&1 & echo \$! > /var/tmp/${name}.pid", "Spawning ${name}"
    def pid = exec "cat /var/tmp/${name}.pid"

    echo "> Started ${name} with Process ID: ${pid}"
}

def stop (name) {
    def pid = exec "cat /var/tmp/${name}.pid"

    exec "kill ${pid}", "Stopping ${name} [${pid}]"
    exec "rm /var/tmp/${name}.pid", "Removing the PID file for ${name}"
}

def use_jdk (version) {
    echo "> Using JDK ${version}"

    env.JAVA_HOME="/opt/jdk/jdk${version}"

    env.PATH="${env.JAVA_HOME}/bin:${env.PATH}"
}

def use_nodejs (version) {
    exec 'wget --quiet https://repository-cloudbees.forge.cloudbees.com/distributions/ci-addons/node/use-node -O use-node', 'Downloading Node.js installer'
    exec "NODE_VERSION=${version} . ./use-node", "Installing Node.js ${version}"

    def node_home = exec 'dirname `which node`'
    env.PATH="${node_home}:${env.PATH}"

    echo "> Using node.js at: ${node_home}"
}

def mvn (command) {
    def maven_version = '3.2.5'
    env.M2_HOME  = "/opt/maven/apache-maven-${maven_version}"
    def mvn_home = tool "Maven (${maven_version})"

    // `sh` instead of `exec` as we actually do care about the output
    sh "${mvn_home}/bin/mvn -B -e -q ${command}"
}

def push_release_branch_for (version) {
    sh "git checkout -b release-${version}"
    sh "git commit -a -m \"Release candidate v${version}\""
    sh "git push origin release-${version}"
}

def exec (command, description='') {
    if (description) {
        echo "> '${description}'"
    } else {
        echo "> '${command}'"
    }

    try {
        sh "#!/usr/bin/env bash\n ${command} | tail -n 1 > /tmp/pipeline"

        return readFile('/tmp/pipeline').split("\r?\n")[0]
    } catch(e) {
        echo "Executing '${description}': ${command} failed: ${e}"
        throw e
    }
}