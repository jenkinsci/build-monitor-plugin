import "./claim.scss";

import { Job } from "../../models/job.ts";

export default function Claim({ job }: { job: Job }) {
  if (!job.claim || !job.claim.active) {
    return;
  }

  let avatar = <></>;
  if (job.claim.avatar) {
    avatar = (
      <img
        className={"jenkins-avatar"}
        width={10}
        height={10}
        src={job.claim.avatar}
        alt={job.claim.author + " avatar"}
        aria-hidden="true"
      />
    );
  }

  return (
    <div className="bm-claim">
      <span className={"bm-claim__bubble"}>{job.claim.reason}</span>
      <span className={"bm-claim__person"}>{avatar} <span>{job.claim.author}</span></span>
    </div>
  );
}
