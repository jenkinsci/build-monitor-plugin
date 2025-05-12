import { JobStatus } from "../models/job.ts";

export function buildStatusToClass(status: JobStatus) {
  const map: { [key in JobStatus]: string } = {
    successful: "--successful",
    failing: "--failing",
    unstable: "--warning",
    unknown: "--null",
    running: "--running",
    aborted: "--null",
  };

  return map[status];
}
