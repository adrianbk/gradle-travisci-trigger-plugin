package com.github.adrianbk.tcitrigger.api

import com.github.adrianbk.tcitrigger.plugin.EnvVar
import groovy.json.JsonBuilder
import groovy.util.logging.Slf4j
import groovyx.net.http.RESTClient

@Slf4j
class TravisApi {
  private final String baseUri
  private String userAgent
  RESTClient restClient

  def TravisApi(String baseUri, String userAgent) {
    this.userAgent = userAgent
    this.baseUri = baseUri
  }

  def restClient() {
    restClient ?: initializeClient()
  }

  def initializeClient() {
    def client = new RESTClient(baseUri)
    client.ignoreSSLIssues()
    client
  }

  def httpHeaders = {
    [
            "Content-Type": "application/json",
            "User-Agent"  : "${userAgent}",
            "Host"        : "api.travis-ci.org",
            "Accept"      : "application/vnd.travis-ci.2+json"
    ]
  }

  String fetchApiToken(String gitHubApiKey) {
    assert gitHubApiKey?.trim(): "gitHubApiKey must not be null or empty"
    def travisClient = restClient()
    def tokBuilder = new JsonBuilder()
    tokBuilder {
      github_token "$gitHubApiKey"
    }

    def accessTokenResp = travisClient.post(
            contentType: 'application/json',
            path: 'auth/github',
            headers: httpHeaders(),
            body: tokBuilder.toString()
    )
    accessTokenResp.data.access_token
  }

  int saveOrUpdate(ArrayList<EnvVar> envVars, String repoId, String token) {
    def existing = getEnvVars(repoId, token)
    def updateCandidates = findUpdateCandidateVars(existing, envVars)
    int count = 0;
    envVars.each { EnvVar envVar ->
      if (updateCandidates.containsKey(envVar)) {
        def matches = updateCandidates.get(envVar)
        matches.each {
          String existingId = it.id
          updateEnvironmentVariable(envVar, existingId, repoId, token)
        }
      } else {
        createEnvironmentVariable(envVar, repoId, token)
      }
      count++
    }
    log.info("Processed [$count] environment variables")
    count
  }

  def createEnvironmentVariable = { EnvVar envVar, String repoId, String token ->
    def travisClient = restClient()
    def envResp = travisClient.post(
            contentType: 'application/json',
            path: 'settings/env_vars',
            query: [repository_id: repoId],
            headers: httpHeaders() + ["Authorization": "token ${token}"],
            body: envVar.toJson()
    )
    def data = envResp.data
    log.info("Created environment variable:[${data}]")
    data
  }

  def updateEnvironmentVariable = { EnvVar envVar, String travisVarId, String repoId, String token ->
    def travisClient = restClient()
    def envResp = travisClient.patch(
            contentType: 'application/json',
            path: "settings/env_vars/$travisVarId",
            query: [repository_id: repoId],
            headers: httpHeaders() + ["Authorization": "token ${token}"],
            body: envVar.toJson()
    )
    def data = envResp.data
    log.info("Updated environment variable:[${data}]")
    data
  }

  def findUpdateCandidateVars = { def existing, ArrayList<EnvVar> envVars ->
    def result = [:]
    if (!existing?.env_vars) {
      return result
    } else {
      envVars.each { EnvVar envVar ->
        def matches = existing.env_vars.findAll { it.name == envVar.name }
        if (matches) {
          result << [(envVar): matches]
        }
      }
    }
    log.info("Found update candidates: $result")
    result
  }

  def getEnvVars = { String repoId, String token ->
    def travisClient = restClient()
    def response = travisClient.get(
            path: "/settings/env_vars",
            query: [repository_id: repoId],
            headers: httpHeaders() + ["Authorization": "token ${token}"],
    )
    def data = response.data
    log.debug("Existing environment variables: $data")
    data
  }

  String findRepositoryId(String githubRepo, String token) {
    def travisClient = restClient()
    def response = travisClient.get(
            path: "repos/${githubRepo}",
            headers: httpHeaders() + ["Authorization": "token ${token}"],
    )

    def repoId = response.data.repo.id
    log.info("Found repository id:[$repoId]")
    repoId
  }

  def restartLastBuild(String repoId, String token) {
    def travisClient = restClient()
    def buildData = fetchBuilds(repoId, token)
    if (buildData?.builds) {
      def builds = buildData.builds
      log.info("Found ${builds.size()} existing builds")
      log.debug("Found ${builds[0]} existing builds")
      def buildId = builds[0].id
      log.info("Restarting buildId:[${buildId}]")
      def response = travisClient.post(
              contentType: 'application/json',
              path: "builds/${buildId}/restart",
              headers: httpHeaders() + ["Authorization": "token ${token}"]
      )
      return response.data
    } else {
      throw new IllegalStateException("Could not trigger a build - there must be at least one existing travisci build")
    }
  }

  def fetchBuilds = { String repoId, String token ->
    def travisClient = restClient()
    def response = travisClient.get(
            path: '/builds',
            query: [repository_id: repoId],
            headers: httpHeaders() + ["Authorization": "token ${token}"],
    )
    def data = response.data
    log.info("Data:${data}")
    data
  }

  def deleteEnvVar = { String envVarId, String token ->
    def travisClient = restClient()
    def response = travisClient.get(
            path: "settings/env_vars/$envVarId",
            headers: httpHeaders() + ["Authorization": "token ${token}"],
    )
    response.data
  }
}
