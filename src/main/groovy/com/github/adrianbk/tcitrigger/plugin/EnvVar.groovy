package com.github.adrianbk.tcitrigger.plugin

import groovy.json.JsonBuilder
import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

@EqualsAndHashCode
@ToString
class EnvVar {
  String name
  String value
  boolean visible

  public EnvVar(String name) {
    this.name = name
  }

  public String toJson() {
    def jsonBuilder = new JsonBuilder()
    jsonBuilder {
      env_var {
        'name' "${name}"
        'value' "${value}"
        'public' visible
      }
    }
    jsonBuilder.toString()
  }
}
