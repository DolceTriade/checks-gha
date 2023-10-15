// Copyright (C) 2022 The Android Open Source Project
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.google.gerrit.plugins.checks.gha;

import static com.google.gerrit.server.change.RevisionResource.REVISION_KIND;
import static com.google.gerrit.server.project.ProjectResource.PROJECT_KIND;

import com.google.common.flogger.FluentLogger;
import com.google.gerrit.extensions.annotations.PluginName;
import com.google.gerrit.extensions.restapi.RestApiModule;
import com.google.gerrit.server.config.PluginConfig;
import com.google.gerrit.server.config.PluginConfigFactory;
import com.google.inject.Inject;
import com.google.inject.TypeLiteral;
import com.googlesource.gerrit.plugins.github.oauth.GitHubLogin;
import com.googlesource.gerrit.plugins.github.oauth.IdentifiedUserGitHubLoginProvider;
import com.googlesource.gerrit.plugins.github.oauth.UserScopedProvider;

public class ApiModule extends RestApiModule {
  private static final FluentLogger logger = FluentLogger.forEnclosingClass();
  private final PluginConfigFactory config;
  private final String pluginName;

  @Inject
  public ApiModule(PluginConfigFactory config, @PluginName String pluginName) {
    this.config = config;
    this.pluginName = pluginName;
  }

  @Override
  protected void configure() {
    PluginConfig pluginConfig = this.config.getFromGerritConfig(this.pluginName);
    String user = pluginConfig.getString(GlobalConfigGitHub.CONFIG_GITHUB_USERNAME);
    String pat = pluginConfig.getString(GlobalConfigGitHub.CONFIG_GITHUB_PAT);
    if (user == null || pat == null || user.isEmpty() || pat.isEmpty()) {
      logger.atFine().log("Using per-user GitHub credentials from the GitHub plugin");
      bind(new TypeLiteral<UserScopedProvider<GitHubLogin>>() {
      })
          .to(IdentifiedUserGitHubLoginProvider.class);
      bind(GitHub.class).to(UserCredsGitHub.class);
    } else {
      logger.atFine().log("Using Globally configured GitHub credentials");
      bind(GitHub.class).to(GlobalConfigGitHub.class);
    }

    get(REVISION_KIND, "checks").to(GetChecks.class);
  }
}
