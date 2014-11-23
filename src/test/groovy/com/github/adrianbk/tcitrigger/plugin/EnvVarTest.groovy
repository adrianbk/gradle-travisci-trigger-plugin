package com.github.adrianbk.tcitrigger.plugin

import spock.lang.Specification
import spock.lang.Unroll

class EnvVarTest extends Specification {

  def "should convert to json"() {
    def var = new EnvVar('aName')
    var.setValue('aValue')
    var.setVisible(true)

    expect:
      var.toString()
      var.toJson() == '{"env_var":{"name":"aName","value":"aValue","public":true}}'
  }

  @Unroll
  def "should equal"() {
    def envVarA = new EnvVar(aname)
    envVarA.setValue(avalue)
    envVarA.setVisible(avisible)

    def envVarB = new EnvVar(bname)
    envVarB.setValue(bvalue)
    envVarB.setVisible(bvisible)

    expect:
      action.call(envVarA, envVarB)

    where:
      aname   | avalue   | avisible | bname   | bvalue   | bvisible | action
      'aname' | 'avalue' | true     | 'aname' | 'avalue' | true     | { EnvVar a, EnvVar b -> a == b && a.hashCode() == b.hashCode() }
      null    | null     | true     | null    | null     | true     | { EnvVar a, EnvVar b -> a == b && a.hashCode() == b.hashCode() }
      'aname' | 'avalue' | true     | 'bname' | 'bvalue' | true     | { EnvVar a, EnvVar b -> a != b && a.hashCode() != b.hashCode() }
      'aname' | 'avalue' | true     | null    | null     | true     | { EnvVar a, EnvVar b -> a != b && a.hashCode() != b.hashCode() }
      'aname' | 'avalue' | true     | null    | null     | false    | { EnvVar a, EnvVar b -> a != b && a.hashCode() != b.hashCode() }
  }
}
