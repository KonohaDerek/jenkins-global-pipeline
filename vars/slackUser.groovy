def call(Boolean isDevOps = false) {
   def username = env.gitlabUserUsername || env.BUILD_USER_ID || 'derek'
   if (isDevOps) {
       username = 'derek.chen'
   }
   def userMap = [
        // other
        'phantom': '<@U01D2CKSPCK>',
        'songo': '<@U01CVJQTQT1>',
        // DevOps
        'derek': '<@U06MHPF6VCN>', // jenkins
        'derek.chen': '<@U06MHPF6VCN>', // gitlab
        // RD
        'mandy': '<@U01DESVR19P>', // jenkins
        'rocbird.hsieh', '<@U06N32QAKK3>', // gitlab
        'naremloa': '<@U02B2N35Y2J>', // jenkins
        'jeeying': '<@U01QG8K4K1Q>',// jenkins
        'jeeying.chen': '<@U01QG8K4K1Q>', // gitlab
        'gene.ke': '<@U04GGLDFPQQ>', // gitlab
        'ray':'<@U02NFKVMVAP>', // jenkins
        'ray.tsai': '<@U02NFKVMVAP>', // gitlab
        'bill.cheng': '<@U04US7Y3K44>', // gitlab
        'aura.huang': '<@U062FUSQKUN>', // gitlab
        'ellen': '<@U03U59J1SRF>', // gitlab
    ]

    // 檢查 username 是否存在於 Map 中
    if (userMap.containsKey(username)) {
        def value = userMap[username]
        println "Value for ${username}: ${value}"
        return value
    } else {
         println "Username ${username} not found in the map."
        // 如果沒有找到，則回傳預設值
        return userMap[ 'derek']
    }
}