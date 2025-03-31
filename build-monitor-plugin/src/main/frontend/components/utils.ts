import { BuildStatus } from "../models/Build";

export function buildStatusToClass(status: string) {
  const stat = status.split(" ")[0] as BuildStatus;

  const map: { [key in BuildStatus]: string } = {
    NOT_EXECUTED: "--null",
    aborted: "--null",
    successful: "--successful",
    IN_PROGRESS: "--in-progress",
    failing: "--failing",
    UNSTABLE: "--warning",
  };

  return map[stat];
}

export function isRunning(status: string) {
  return status.includes("running");
}
