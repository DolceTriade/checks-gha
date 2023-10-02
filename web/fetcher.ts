/**
 * @license
 * Copyright (C) 2022 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
import {
  ChangeData,
  CheckRun,
  ChecksProvider,
  ResponseCode,
  RunStatus,
  CheckResult,
  Category
} from '@gerritcodereview/typescript-api/checks';
import { PluginApi } from '@gerritcodereview/typescript-api/plugin';

export declare interface WorkflowRun {
  name: string;
  title: string;
  url: string;
  conclusion: string;
  status: string;
  run_attempt: number;
  runNumber: number;
}

export class ChecksFetcher implements ChecksProvider {
  private plugin: PluginApi;

  runs: WorkflowRun[] | null;

  constructor(pluginApi: PluginApi) {
    this.plugin = pluginApi;
    this.runs = null;
  }

  async fetch(data: ChangeData) {
    const checkRuns: CheckRun[] = [];
    if (this.runs === null) {
      await this.plugin.restApi().get<WorkflowRun[]>(
        `/changes/${data.changeInfo.id}/revisions/${data.patchsetSha}/checks`
      ).then(result => {
        this.runs = result;
      }).catch(reason => {
        throw reason;
      });
    }
    if (this.runs !== null) {
      this.runs.forEach(run => {
        checkRuns.push({
          attempt: run.run_attempt,
          checkName: run.name,
          checkDescription: run.title,
          checkLink: run.url,
          status: this.convertStatus(run.status),
          results: this.convertResult(run.conclusion),
        });
      });
    }
    return {
      responseCode: ResponseCode.OK,
      runs: checkRuns,
    };
  }

  convertStatus(s: string): RunStatus {
    let v = {
      'QUEUED': RunStatus.SCHEDULED,
      'IN_PROGRESS': RunStatus.RUNNING,
      'COMPLETED': RunStatus.COMPLETED
    }[s];
    return v === undefined ? RunStatus.RUNNABLE : v;
  }

  convertResult(conclusion: string): CheckResult[] {
    return [{
      category: conclusion === 'SUCCESS' ? Category.SUCCESS : Category.ERROR,
      summary: conclusion,
    }];

  }
}
