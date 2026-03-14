import { Job } from "../models/job";

export async function getJobs(): Promise<Job[]> {
  const response = await fetch("fetchJobViews");

  if (!response.ok) {
    throw new Error(`Failed to fetch jobs: ${response.statusText}`);
  }

  return (await response.json())["data"] as Job[];
}
