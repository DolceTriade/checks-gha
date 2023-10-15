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

import com.google.common.flogger.FluentLogger;
import com.google.gerrit.extensions.annotations.PluginName;
import com.google.gerrit.server.config.PluginConfig;
import com.google.gerrit.server.config.PluginConfigFactory;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import java.io.IOException;
import org.eclipse.jgit.errors.ConfigInvalidException;
import org.kohsuke.github.GitHubBuilder;

@Singleton
class GlobalConfigGitHub extends AbstractGitHub {
    private static final FluentLogger logger = FluentLogger.forEnclosingClass();
    public static final String CONFIG_GITHUB_USERNAME = "username";
    public static final String CONFIG_GITHUB_PAT = "pat";

    private final org.kohsuke.github.GitHub githubApi;

    @Inject
    GlobalConfigGitHub(PluginConfigFactory config, @PluginName String pluginName) throws ConfigInvalidException, IOException {
        this.githubApi = this.getAuth(pluginName, config.getFromGerritConfig(pluginName));
    }

    private org.kohsuke.github.GitHub getAuth(String pluginName, PluginConfig config) throws ConfigInvalidException, IOException {
        String user = config.getString(CONFIG_GITHUB_USERNAME);
        String pat = config.getString(CONFIG_GITHUB_PAT);
        if (user == null || pat == null || user.isEmpty() || pat.isEmpty()) {
            throw new ConfigInvalidException("Must specify a \"username\" and a \"pat\".");
        }
        return new GitHubBuilder()
            .withOAuthToken(pat, user)
            .build();
    }

    @Override
    public WorkflowRun[] GetWorkflowRuns(String repoName, String branch) throws IOException {
        logger.atFine().log("Getting runs for %s -- %s", repoName, branch);
        return this.GetWorkflowRuns(this.githubApi, repoName, branch);
   }
}