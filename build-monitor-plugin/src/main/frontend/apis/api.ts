import {Job} from "../models/job";

export function getJobs(): Promise<Job[]> {
  return new Promise((resolve, reject) => {
    // @ts-ignore
    buildMonitorBind.fetchJobViews((e: any) => {
      if (e && e.responseJSON && e.responseJSON.data) {
        console.log(e.responseJSON.data);
        resolve(e.responseJSON.data as Job[]);
      } else {
        reject(new Error("Failed to fetch job data"));
      }
    });
  });
}
