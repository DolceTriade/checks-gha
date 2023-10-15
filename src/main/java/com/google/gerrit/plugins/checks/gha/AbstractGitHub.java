package com.google.gerrit.plugins.checks.gha;

import java.io.IOException;
import java.util.ArrayList;

import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GHWorkflowRun;
import org.kohsuke.github.PagedIterable;

public class AbstractGitHub implements GitHub {

    public WorkflowRun[] GetWorkflowRuns(org.kohsuke.github.GitHub githubApi, String repoName, String branch) throws IOException {
        GHRepository repo = githubApi.getRepository(repoName);
            PagedIterable<GHWorkflowRun> runs = repo.queryWorkflowRuns()
                .branch(branch)
                .list();
            ArrayList<WorkflowRun> ret = new ArrayList<>();
            runs.forEach(run -> {
                WorkflowRun wfRun = new WorkflowRun();
                wfRun.name = run.getName();
                wfRun.title = run.getDisplayTitle();
                try {
                    wfRun.statusUrl = run.getHtmlUrl().toString();
                } catch (IOException e) {
                    wfRun.statusUrl = run.getUrl().toString();
                }
                wfRun.url = run.getUrl().toString();
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

    @Override
    public WorkflowRun[] GetWorkflowRuns(String repo, String branch) throws IOException {
        throw new UnsupportedOperationException("Unimplemented method 'GetWorkflowRuns'");
    }


}
