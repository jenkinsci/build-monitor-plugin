pipeline { 
    agent any  
    stages { 
        stage('Build HPI') { 
            steps { 
               git branch: 'feature/show-displayname-plus-hide-description', url: 'https://github.com/hqplus-software/build-monitor-plugin'
               powershell 'mvn clean package -DskipTests'
               archiveArtifacts artifacts: 'target/*.hpi', fingerprint: true
            }
        }
    }
}
