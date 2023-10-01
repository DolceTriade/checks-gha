load("//tools/bzl:plugin.bzl", "gerrit_plugin")

package_group(
    name = "visibility",
    packages = ["//plugins/checks-gha/..."],
)

package(default_visibility = [":visibility"])

gerrit_plugin(
    name = "checks-gha",
    srcs = glob(["src/main/java/com/google/gerrit/plugins/checks/gha/**/*.java"]),
    manifest_entries = [
        "Gerrit-PluginName: checks-gha",
        "Gerrit-Module: com.google.gerrit.plugins.checks.gha.ApiModule",
        "Gerrit-HttpModule: com.google.gerrit.plugins.checks.gha.HttpModule",
    ],
    resource_jars = ["//plugins/checks-gha/web:checks-gha"],
    resources = glob(["src/main/resources/**/*"]),
)
