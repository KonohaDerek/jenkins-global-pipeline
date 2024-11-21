def call() {
    env.buildColor = 'good'
    slackSend(channel: 'deployment-notifications', color: "${buildColor}", message: """${namespace} ${env.project} ${env.GIT_BRANCH} Environment - Code Scan Completed after ${currentBuild.durationString.replace(' and counting', '')} (<${sonarQubeUrl}|Open>)
    :tada: ${namespace} ${env.project} ${env.GIT_BRANCH} branch :tada:
    url: <${sonarQubeUrl}|${sonarQubeUrl}>""")
}