export interface Job {
  name: string;
  url: string;
  status: JobStatus;
  disabled: boolean;
  hashCode: number;
  progress: number;
  estimatedDuration: string;
  config: {
    displayBadges: string;
  };
  headline: string;
  lastCompletedBuild: {
    name: string;
    url: string;
    duration: string;
    description: string;
    timeElapsedSince: number;
  };
  currentBuilds: {
    name: string;
    url: string;
    result: any; // todo
    isRunning: boolean;
    elapsedTime: string; // todo
    timeElapsedSince: string; // todo
    duration: string;
    estimatedDuration: string;
    progress: number;
    description: string;
    isPipeline: boolean;
    pipelineStages: string[];
    hasPreviousBuild: boolean;
    previousBuild: any; // todo
    culprits: string[];
    committers: string[];
    // detailsOf // todo
    // allDetailsOf // todo
  };
  badges: {
    background: string | null;
    color: string | null;
    text: "Build Started";
    borderColor: string | null;
    border: string | null;
  }[];
}

// Aligns with build-monitor-plugin/src/main/java/com/smartcodeltd/jenkinsci/plugins/buildmonitor/viewmodel/CssStatus.java
export type JobStatus =
  | "successful"
  | "unstable"
  | "failing"
  | "unknown"
  | "aborted"
  | "running";
