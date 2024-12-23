def call(Exception e) {
  stage('Notify Failure') { 
    errorLog = getJenkinsLog()
    if (!errorLog) {
      errorLog = e.message
    }
    currentBuild.result = "FAILED"
    env.buildColor = 'danger'
    def message = """${env.namespace} ${env.project} ${env.GIT_BRANCH} Environment - BUILD ${currentBuild.result} after ${currentBuild.durationString.replace(' and counting', '')} (<${env.BUILD_URL}|Open>)
    :warning: :warning: :warning: ${namespace} admin ${env.GIT_BRANCH} branch :warning: :warning: :warning: """
    def aiResponse = """ ``` ${getAssistantResponse(errorLog)} ``` """
    // get the error trace
    def err = """ ``` ${errorLog}``` """
   
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
        string(credentialsId: 'AZURE_OPENAI_API_KEY', variable: 'API_KEY'),
        string(credentialsId: 'AZURE_OPENAI_ASSISTANT_ID', variable: 'ASSISTANT_ID')]) {
        def dockerCommand = """
       docker run --rm -e NODE_ENV=production -e AZURE_OPENAI_ENDPOINT="https://howdigital-openai-us.openai.azure.com"  \
 -e AZURE_OPENAI_API_KEY="${API_KEY}"  \
 -e AZURE_OPENAI_ASSISTANT_ID="${ASSISTANT_ID}"  \
 -e AZURE_OPENAI_MODEL="gpt-4o-mini" \
 -e AZURE_OPENAI_API_VERSION="2024-07-01-preview" "asia-east1-docker.pkg.dev/howgroup-devops-2021/devops/jenkins-assistant-nest:1.0.3" -e ${err}
       """
        def msg = ""
        msg = sh(script: dockerCommand, returnStdout: true)
        return msg
    }
}
