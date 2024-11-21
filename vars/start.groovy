def call() {
     slackSend(channel: 'deployment-notifications', color: "${env.buildColor}", message: """${env.namespace} ${env.project} ${env.GIT_BRANCH} Environment - BUILD START after ${currentBuild.durationString} (<${env.BUILD_URL}|Open>)
        :hammer_and_wrench: ${namespace} ${env.project} ${env.GIT_BRANCH} branch :hammer_and_wrench:""")
}