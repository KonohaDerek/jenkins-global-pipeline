def call() {
    currentBuild.result = "SUCCESS"
    env.buildColor = 'good'
    slackSend(channel: 'deployment-notifications', color: "${env.buildColor}", message: """${env.namespace} ${env.project} ${env.GIT_BRANCH} Environment - BUILD ${currentBuild.result} after ${currentBuild.durationString.replace(' and counting', '')} (<${env.BUILD_URL}|Open>)
    :tada: ${namespace} admin ${env.GIT_BRANCH} branch :tada:
    url: <${targetUrl}|${targetUrl}>""")
}