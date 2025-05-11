import { Job } from "../../models/job.ts";
import React from "react";
import { useUserPreferences } from "../../providers/user-preference-provider.tsx";

export default function Badges({ job }: { job: Job }) {
  const { showBadges } = useUserPreferences();

  if (!job.badges || !showBadges) {
    return;
  }

  return (
    <>
      {job.badges.map((badge) => (
        <p className={"bm-badge"}>{badge.text}</p>
      ))}
    </>
  );
}
