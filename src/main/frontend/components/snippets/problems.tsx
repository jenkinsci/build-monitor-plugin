import { Job } from "../../models/job.ts";

export default function Problems({ job }: { job: Job }) {
  job.problems = ["hello world", "testing"];

  if (!job.problems || job.problems.length === 0) {
    return;
  }

  return (
    <>
      <p>{job.problems.length} problems identified</p>
      <ul className="identified-failures">
        {job.problems.map((problem) => (
          <li key={problem}>{problem}</li>
        ))}
      </ul>
    </>
  );
}
