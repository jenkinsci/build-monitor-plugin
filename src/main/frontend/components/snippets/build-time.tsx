import { Job } from "../../models/job.ts";
import time from "../../utils/time.ts";
import Label from "../label.tsx";

export default function BuildTime({ job }: { job: Job }) {
  const hasCurrentBuilds = job.estimatedDuration && job.progress > 0;
  const hasLastCompletedBuild =
    job.lastCompletedBuild?.timeElapsedSince && job.progress === 0;

  return (
    <div>
      {hasCurrentBuilds &&
        job.currentBuilds?.map((build, index) => (
          <div key={index} className="build-time">
            <Label text={build.duration} />
            {" | "}
            <Label text={job.estimatedDuration} />
          </div>
        ))}

      {hasLastCompletedBuild && (
        <div className="build-time">
          <Label text={time(job.lastCompletedBuild.timeElapsedSince)} />
        </div>
      )}
    </div>
  );
}
