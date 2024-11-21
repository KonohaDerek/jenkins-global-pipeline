def call() {
    env.buildColor = 'good'
    slackSend(channel: 'deployment-notifications', color: "${env.buildColor}", message: """[即將發佈] \n 名稱: ${env.namespace} ${env.project} \n 版本: ${env.VERSION_TAG} \n 環境: ${env.IMAGE_TAG} """)

}