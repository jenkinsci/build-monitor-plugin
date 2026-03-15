import "./badges.scss";

import { CSSProperties } from "react";

import { useUserPreferences } from "../../context/user-preference-provider.tsx";
import { Job } from "../../models/job.ts";

function styleStringToObject(style?: string): CSSProperties | undefined {
  if (!style) {
    return;
  }

  const json = `{"${style
    .replace(/; /g, '", "')
    .replace(/: /g, '": "')
    .replace(";", "")}"}`;

  const obj = JSON.parse(json);

  const keyValues = Object.keys(obj).map((key) => {
    const camelCased = key.replace(/-[a-z]/g, (g) => g[1].toUpperCase());
    return { [camelCased]: obj[key] };
  });
  return Object.assign({}, ...keyValues);
}

export default function Badges({ job }: { job: Job }) {
  const { showBadges } = useUserPreferences();

  if (!job.badges || !showBadges) {
    return;
  }

  return (
    <>
      {job.badges.map((badge) => (
        <p
          style={styleStringToObject(badge.style)}
          className={"bm-badge"}
          key={badge.text}
        >
          {badge.text}
        </p>
      ))}
    </>
  );
}
