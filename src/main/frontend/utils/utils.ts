import {Job, JobStatus} from "../models/job.ts";

export function buildStatusToClass(job: Job) {
  if (job.disabled) {
    return "--disabled"
  }

  const map: { [key in JobStatus]: string } = {
    successful: "--successful",
    failing: "--failing",
    unstable: "--warning",
    unknown: "--null",
    running: "--running",
    aborted: "--null",
  };

  return map[job.status];
}
