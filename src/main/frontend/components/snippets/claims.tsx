import { Job } from "../../models/job.ts";

export default function Claim({ job }: { job: Job }) {
  if (!job.claim || !job.claim.active) {
    return;
  }

  return (
    <p>
      Claimed by {job.claim.author}: {job.claim.reason}
    </p>
  );
}
