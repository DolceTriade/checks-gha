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

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import com.google.gerrit.entities.Change;
import com.google.gerrit.extensions.annotations.PluginName;
import com.google.gerrit.extensions.restapi.BadRequestException;
import com.google.gerrit.extensions.restapi.Response;
import com.google.gerrit.extensions.restapi.RestReadView;
import com.google.gerrit.plugins.checks.gha.GitHub.WorkflowRun;
import com.google.gerrit.server.change.RevisionResource;
import com.google.gerrit.server.permissions.PermissionBackendException;
import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
class GetChecks implements RestReadView<RevisionResource> {
  private final GitHub github;

  @Inject
  GetChecks(
      @PluginName String pluginName, GitHub github) {
    this.github = github;
  }

  @Override
  public Response<List<GitHub.WorkflowRun>> apply(RevisionResource rev) throws BadRequestException, PermissionBackendException, IOException {
    final Change change = rev.getChange();

    String changeId = change.getKey().toString();
    String commit = rev.getPatchSet().commitId().getName();
    WorkflowRun[] runs = this.github.GetWorkflowRuns(rev.getProject().toString(), String.format("ci/%s/%s", changeId, commit));
    return Response.ok(Arrays.asList(runs));
  }
}
