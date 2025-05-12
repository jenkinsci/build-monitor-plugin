import { Job } from "../../models/job.ts";
import './problems.scss';

export default function Problems({ job }: { job: Job }) {
  if (!job.problems || job.problems.length === 0) {
    return;
  }

  return (
    <div className="bm-problems">
      {job.problems.length}{" "}
      {job.problems.length === 1 ? "problem identified" : "problems identified"}
      <ul>
        {job.problems.map((problem) => (
          <li key={problem}>{problem}</li>
        ))}
      </ul>
    </div>
  );
}
