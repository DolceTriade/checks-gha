load("//tools/bzl:maven_jar.bzl", "maven_jar")

def external_plugin_deps():
  maven_jar(
      name = 'org_kohsuke_github_api',
      artifact = 'org.kohsuke:github-api:1.316',
      sha1 = '90ea530f3aeceb46be27b924ae25b4b7b2388c9d',
  )