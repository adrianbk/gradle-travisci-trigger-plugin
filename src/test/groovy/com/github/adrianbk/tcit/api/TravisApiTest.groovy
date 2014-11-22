package com.github.adrianbk.tcit.api

import com.github.adrianbk.tcit.plugin.EnvVar
import spock.lang.Specification

class TravisApiTest extends Specification {
  final TravisApi travisApi = new TravisApi('', '')

  def "should find an update candidate when names match and value differ"() {
    given:
      def existing = singleTravisVar('TEST_VAR', 'VAL')
      def update = envVar('TEST_VAR', 'VAL2')
    expect:
      def candidates = travisApi.findUpdateCandidateVars(existing, [update])
      candidates[(update)]
  }

  def "should return empty when no candidates"() {
    expect:
      [:] == travisApi.findUpdateCandidateVars([:], [envVar('n', 'v')])
  }

  def "should find an update candidate when 2 existing travis variables"() {
    given:
      def existing = twoEnvVars()
      def update = envVar('name2', 'anyval')
    expect:
      def candidates = travisApi.findUpdateCandidateVars(existing, [update])
      candidates[(update)]
      candidates[(update)][0].id == 'id2'

  }

  def singleTravisVar(String name, String val) {
    [env_vars: [[id: 'tid', name: "$name", 'public': true, repository_id: 'rid', value: "$val"]]]
  }

  def twoEnvVars() {
    [env_vars:
             [
                     [id: 'id1', name: 'name1', 'public': true, repository_id: 3431009, value: 'val1'],
                     [id: 'id2', name: 'name2', 'public': false, repository_id: 3431009, value: 'val2']
             ]
    ]
  }

  def envVar(String name, String val) {
    EnvVar var = new EnvVar(name)
    var.value = val
    var
  }
}
