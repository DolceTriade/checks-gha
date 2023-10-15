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
    provided_deps = [
        "@com_fasterxml_jackson_core_jackson_annotations//jar",
        "@com_fasterxml_jackson_core_jackson_databind//jar",
        "@com_fasterxml_jackson_core_jackson_core//jar",
        "@commons_io_commons_io//jar",
        "@org_apache_commons_commons_lang3//jar",
        "@org_kohsuke_github_api//jar",
        "@com_infradna_tool_bridge_method_annotation//jar",
    ],
    resource_jars = ["//plugins/checks-gha/web:checks-gha"],
    resources = glob(["src/main/resources/**/*"]),
    deps = [
        "//plugins/checks-gha/third_party:external_jars",
    ],
)
