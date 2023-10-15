package com.google.gerrit.plugins.checks.gha;

import java.io.IOException;

import com.google.common.flogger.FluentLogger;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.googlesource.gerrit.plugins.github.oauth.GitHubLogin;
import com.googlesource.gerrit.plugins.github.oauth.UserScopedProvider;

@Singleton
public class UserCredsGitHub extends AbstractGitHub {
    private static final FluentLogger logger = FluentLogger.forEnclosingClass();
    private final UserScopedProvider<GitHubLogin> ghLoginProvider;

    @Inject
    public UserCredsGitHub(final UserScopedProvider<GitHubLogin> ghLoginProvider) {
        this.ghLoginProvider = ghLoginProvider;
    }

    @Override
    public WorkflowRun[] GetWorkflowRuns(String repoName, String branch) throws IOException {
        logger.atFine().log("Getting runs for %s -- %s", repoName, branch);
        GitHubLogin login = this.ghLoginProvider.get();
        if (login == null || !login.isLoggedIn()) {
            WorkflowRun[] empty = new WorkflowRun[0];
            return empty;
        }
        return this.GetWorkflowRuns(login.getHub(), repoName, branch);
    }
}
