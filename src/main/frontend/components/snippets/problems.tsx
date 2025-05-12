import { Job } from "../../models/job.ts";

export default function Problems({ job }: { job: Job }) {
  if (!job.problems || job.problems.length === 0) {
    return;
  }

  return (
    <>
      <div>{job.problems.length} {job.problems.length === 1 ? 'problem identified' : 'problems identified'}</div>
      <ul>
        {job.problems.map((problem) => (
          <li key={problem}>{problem}</li>
        ))}
      </ul>
    </>
  );
}
