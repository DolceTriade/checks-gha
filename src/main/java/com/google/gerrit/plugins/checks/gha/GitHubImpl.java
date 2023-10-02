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
import com.google.gerrit.extensions.restapi.Response;
import com.google.gerrit.extensions.restapi.RestReadView;
import com.google.gerrit.server.config.PluginConfig;
import com.google.gerrit.server.config.PluginConfigFactory;
import com.google.gerrit.server.project.NoSuchProjectException;
import com.google.gerrit.server.project.ProjectResource;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

import org.eclipse.jgit.errors.ConfigInvalidException;
import org.eclipse.jgit.lib.Config;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GHWorkflowRun;
import org.kohsuke.github.GitHubBuilder;
import org.kohsuke.github.PagedIterable;

@Singleton
class GitHubImpl implements GitHub {
    private static final FluentLogger logger = FluentLogger.forEnclosingClass();
    private static final String CONFIG_GITHUB_USERNAME = "username";
    private static final String CONFIG_GITHUB_PAT = "pat";

    private final org.kohsuke.github.GitHub githubApi;

    @Inject
    GitHubImpl(PluginConfigFactory config, @PluginName String pluginName) throws ConfigInvalidException, IOException {
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
        logger.atInfo().log("GETTING RUNS FOR %s -- %s", repoName, branch);
        GHRepository repo = this.githubApi.getRepository(repoName);
            PagedIterable<GHWorkflowRun> runs = repo.queryWorkflowRuns()
                .branch(branch)
                .list();
            ArrayList<WorkflowRun> ret = new ArrayList<>();
            runs.forEach(run -> {
                WorkflowRun wfRun = new WorkflowRun();
                wfRun.name = run.getName();
                wfRun.title = run.getDisplayTitle();
                try {
                    wfRun.url = run.getHtmlUrl().toString();
                } catch (IOException e) {
                    wfRun.url = run.getUrl().toString();
                }
                if (run.getConclusion() != null) {
                    wfRun.conclusion = run.getConclusion().name();
                }
                if (run.getStatus() != null) {
                    wfRun.status = run.getStatus().name();
                }
                wfRun.runAttempt = run.getRunAttempt();
                wfRun.runNumber = run.getRunNumber();
                ret.add(wfRun);
            });
            WorkflowRun[] arr = new WorkflowRun[ret.size()];
            return ret.toArray(arr);
    }


}