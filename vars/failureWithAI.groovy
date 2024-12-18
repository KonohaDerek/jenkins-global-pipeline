def call(Exception e) {
  stage('Notify Failure') {
    currentBuild.result = "FAILED"
    env.buildColor = 'danger'
    def message = """${env.namespace} ${env.project} ${env.GIT_BRANCH} Environment - BUILD ${currentBuild.result} after ${currentBuild.durationString.replace(' and counting', '')} (<${env.BUILD_URL}|Open>)
    :warning: :warning: :warning: ${namespace} admin ${env.GIT_BRANCH} branch :warning: :warning: :warning: """
    def aiResponse = """ ``` ${getAssistantResponse(e.toString())} ``` """
    // get the error trace
    def err = """ ``` ${e.toString()}``` """

    // send the message to slack
    def isDevOps = aiResponse.contains("DevOps 處理")
    def user = slackUser(isDevOps)
    def attachments = [
      [
        text: message + '\n' + user + '\n' + aiResponse + err, 
        fallback: 'Fallback',
        color: "${env.buildColor}"
      ]
    ]
    slackSend(channel: 'deployment-notifications', color: "${env.buildColor}", attachments: attachments)
  }
}

def getAssistantResponse(err) {
      withCredentials([
        string(credentialsId: 'AZURE_OPENAI_API_KEY', variable: 'AZURE_OPENAI_API_KEY'),
        string(credentialsId: 'AZURE_OPENAI_ASSISTANT_ID', variable: 'AZURE_OPENAI_ASSISTANT_ID')]) {
        def dockerCommand = """
       docker run --rm -e NODE_ENV=production -e AZURE_OPENAI_ENDPOINT="https://howdigital-openai-us.openai.azure.com"  \
 -e AZURE_OPENAI_API_KEY="${AZURE_OPENAI_API_KEY}"  \
 -e AZURE_OPENAI_ASSISTANT_ID="${AZURE_OPENAI_ASSISTANT_ID}"  \
 -e AZURE_OPENAI_MODEL="gpt-4o-mini" "asia-east1-docker.pkg.dev/howgroup-devops-2021/devops/jenkins-assistant-nest:1.0.2" -e ${err}
       """
        def msg = ""
        msg = sh(script: dockerCommand, returnStdout: true)
        return msg
    }
}