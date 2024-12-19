def call(){
  stage('Get Jenkins Log') {
     withCredentials([
            usernamePassword(credentialsId: 'jenkins-console-pat', usernameVariable: 'JENKINS_CONSOLE_USER', passwordVariable: 'JENKINS_CONSOLE_TOKEN')
        ]) {
            def replaceMap = [
                '[BUILD]': '',
                '[BUILD_ERROR]': '',
                '[Pipeline] isUnix': '',
                '[Pipeline] sh': '',
                '[Pipeline] withEnv': '',
                '[Pipeline] echo': '',
                '[Pipeline] stage': '',
                '[Pipeline] }': '',
                '\n': ',',
                '\r': '',
            ]
            sleep 5
            def errorLog = ""
            // 读取控制台日志到变量中
            def consoleLog = sh(
                script: "curl -s --user ${JENKINS_CONSOLE_USER}:${JENKINS_CONSOLE_TOKEN} ${env.BUILD_URL}consoleText",
                returnStdout: true
            ).trim()
            
            if (consoleLog.contains("[BUILD_ERROR]")) {
                consoleLog = consoleLog.substring(consoleLog.indexOf("[BUILD]")+7, consoleLog.indexOf("[BUILD_ERROR]"))
                errorLog = consoleLog.substring(consoleLog.indexOf("docker build"), consoleLog.indexOf("[Pipeline] }")).replaceAll(replaceMap)
            }

            println "errorLog : ${errorLog}"
           return errorLog
    }
  }
}