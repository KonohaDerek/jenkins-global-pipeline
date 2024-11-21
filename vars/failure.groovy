def call(Exception e) {
  echo "Error: ${e}"
  currentBuild.result = "FAILED"
  env.buildColor = 'danger'
  slackSend(channel: 'deployment-notifications', color: "${env.buildColor}", message: """${env.namespace} ${env.project} ${env.GIT_BRANCH} Environment - BUILD ${currentBuild.result} after ${currentBuild.durationString.replace(' and counting', '')} (<${env.BUILD_URL}|Open>)
  :warning: :warning: :warning: ${namespace} admin ${env.GIT_BRANCH} branch :warning: :warning: :warning:""")
}