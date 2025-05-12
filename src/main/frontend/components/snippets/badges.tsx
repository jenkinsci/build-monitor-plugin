import { useUserPreferences } from "../../context/user-preference-provider.tsx";
import { Job } from "../../models/job.ts";
import './badges.scss';

export default function Badges({ job }: { job: Job }) {
  const { showBadges } = useUserPreferences();

  if (!job.badges || !showBadges) {
    return;
  }

  return (
    <>
      {job.badges.map((badge) => (
        <p className={"bm-badge"} key={badge.text}>
          {badge.text}
        </p>
      ))}
    </>
  );
}
