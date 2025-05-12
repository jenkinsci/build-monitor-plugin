import { Job } from "../../models/job.ts";
import './claim.scss';

export default function Claim({ job }: { job: Job }) {
  if (!job.claim || !job.claim.active) {
    return;
  }

  let avatar = <></>;
  if (job.claim.avatar) {
    avatar = <img className={"jenkins-avatar"} width={10} height={10} src={job.claim.avatar} alt={job.claim.author + " avatar"} aria-hidden="true" />
  }

  return (
    <div className="bm-claim">
      Claimed by {avatar} {job.claim.author}: {job.claim.reason}
    </div>
  );
}
