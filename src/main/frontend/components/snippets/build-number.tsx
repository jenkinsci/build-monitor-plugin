import { Job } from "../../models/job.ts";
import Label from "../label.tsx";

export default function BuildNumber({ job }: { job: Job }) {
  const hasCurrentBuilds = job.estimatedDuration && job.progress > 0;
  const hasLastCompletedBuild =
    job.lastCompletedBuild?.timeElapsedSince && job.progress === 0;

  return (
    <div>
      {hasCurrentBuilds &&
        job.currentBuilds?.map((build, index) => (
          <div key={index}>
            {build.name && (
              <a
                className="build-name"
                title={`Details of ${job.name}, build ${build.name}`}
                href={build.url}
              >
                <Label text={build.name} />
              </a>
            )}
            {build.pipelineStages && <Label text={build.pipelineStages} />}
          </div>
        ))}

      {hasLastCompletedBuild && job.lastCompletedBuild?.name && (
        <a
          className="build-name"
          title={`Details of ${job.name}, build ${job.lastCompletedBuild.name}`}
          href={job.lastCompletedBuild.url}
        >
          <Label text={job.lastCompletedBuild.name} />
        </a>
      )}
    </div>
  );
}
