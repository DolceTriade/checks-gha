load("//tools/bzl:maven_jar.bzl", "maven_jar")

def external_plugin_deps():
    maven_jar(
        name = "org_apache_commons_commons_lang3",
        artifact = "org.apache.commons:commons-lang3:3.9",
    )
    maven_jar(
        name = "commons_io_commons_io",
        artifact = "commons-io:commons-io:2.8.0",
    )
    maven_jar(
        name = "com_fasterxml_jackson_core_jackson_annotations",
        artifact = "com.fasterxml.jackson.core:jackson-annotations:2.15.2",
    )
    maven_jar(
        name = "com_fasterxml_jackson_core_jackson_databind",
        artifact = "com.fasterxml.jackson.core:jackson-databind:2.15.2",
    )
    maven_jar(
        name = "com_fasterxml_jackson_core_jackson_core",
        artifact = "com.fasterxml.jackson.core:jackson-core:2.15.2",
        sha1 = "a6fe1836469a69b3ff66037c324d75fc66ef137c",
    )
    maven_jar(
        name = "com_infradna_tool_bridge_method_annotation",
        artifact = "com.infradna.tool:bridge-method-annotation:1.23",
    )
    maven_jar(
        name = "com_infradna_tool_bridge_method_annotation",
        artifact = "com.infradna.tool:bridge-method-annotation:1.23",
    )

    maven_jar(
        name = "org_kohsuke_github_api",
        artifact = "org.kohsuke:github-api:1.316",
        sha1 = "90ea530f3aeceb46be27b924ae25b4b7b2388c9d",
    )
