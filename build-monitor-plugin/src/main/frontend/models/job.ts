import {BuildStatus} from "./Build";

export interface Job {
  name: string;
  url: string;
  // successful running
  status: string;
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
  currentBuilds: any[]; // If you know the shape of builds, replace `any` with a proper type
}