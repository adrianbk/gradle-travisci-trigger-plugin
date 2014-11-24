package com.github.adrianbk.tcitrigger.api

import groovy.json.JsonBuilder
import groovy.json.JsonSlurper
import groovyx.net.http.RESTClient
import spock.lang.Specification

class TravisApiTest extends Specification {

  public static final String GITHUB_API_KEY = "github_api_key"
  public static final String TRAVIS_ACCESS_TOKEN = 'travis_access_token'
  public static final String GITHUB_REPO = 'my/git-repo'
  public static final String GITHUB_REPO_ID = 'repo_id'

  final TravisApi api = new TravisApi('userAgent', '')

  def "Should fetch a github api token"() {
    given:
      RESTClient restClient = Mock {
        1 * post(
                [
                        contentType: 'application/json',
                        path       : "auth/github",
                        headers    : api.httpHeaders(),
                        body       : githubApiTokenRequestJson()

                ]
        ) >> [status: 200, data: [access_token: TRAVIS_ACCESS_TOKEN]]
      }

      api.restClient = restClient
    expect:
      api.fetchApiToken(GITHUB_API_KEY) == TRAVIS_ACCESS_TOKEN
  }

  def "should find repo id"() {
    given:
      RESTClient restClient = Mock {
        1 * get(
                [
                        path   : "repos/${GITHUB_REPO}",
                        headers: headersWithAuth(),

                ]
        ) >> [status: 200, data: [repo: [id: GITHUB_REPO_ID]]]
      }
      api.restClient = restClient
    expect:
      api.findRepositoryId(GITHUB_REPO, TRAVIS_ACCESS_TOKEN) == GITHUB_REPO_ID
  }

  def "should get existing environment variables"() {
    given:
      RESTClient restClient = Mock {
        1 * get(
                [
                        path   : "/settings/env_vars",
                        query  : [repository_id: GITHUB_REPO],
                        headers: headersWithAuth(),

                ]
        ) >> [status: 200, data: [travisEnvironmentVariable()]]
      }
      api.restClient = restClient
    expect:
      api.getEnvVars(GITHUB_REPO, TRAVIS_ACCESS_TOKEN)[0].id == 'id'
  }


  private Map<String, String> headersWithAuth() {
    api.httpHeaders() + ["Authorization": "token ${TRAVIS_ACCESS_TOKEN}"]
  }

  def travisEnvironmentVariable() {
    String jsonStr = '''{"id":"id","name":"vanme","value":"vvalue","public":true,"repository_id":123}'''
    new JsonSlurper().parseText(jsonStr)
  }

  private String githubApiTokenRequestJson() {
    JsonBuilder jb = new JsonBuilder()
    jb {
      github_token "$GITHUB_API_KEY"
    }
    jb.toString()
  }
}
