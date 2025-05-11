import React from "react";
import Label from "./label";
import time from "../utils/time";
import { Job } from "../models/job";
import { buildStatusToClass } from "../utils/utils.ts";
import {useUserPreferences} from "../providers/user-preference-provider.tsx";

function Cell({ job }: { job: Job }) {
  const { colorBlindMode, showBadges } = useUserPreferences();

  const highlightColor =
    "color-mix(in srgb, var(--psv-cell-color) 75%, var(--text-color))";

  let badges = <></>;
  if (job.badges && showBadges) {
    badges = (
      <>
        {job.badges.map((badge) => (
          <p className={"bm-badge"}>{badge.text}</p>
        ))}
      </>
    );
  }

  return (
    <a
      href={job.url}
      className={
        "psv-cell psv-cell" +
        buildStatusToClass(job.status) +
        " " +
        (colorBlindMode ? "psv-cell--color-blind-mode" : "")
      }
    >
      <div
        className={"durationboi durationboi--animate"}
        style={{ width: job.progress + "%" }}
      ></div>

      <Label
        text={job.name}
        style={{
          fontWeight: "550",
          color: "color-mix(in srgb, var(--psv-cell-color), var(--text-color))",
        }}
      ></Label>
      <Label
        text={job.headline}
        style={{ fontSize: "0.75em", color: highlightColor }}
      ></Label>
      {badges}

      <div className={"psv-cell__details"}>
        <Label
          text={job.lastCompletedBuild.name}
          style={{ fontSize: "0.75em", color: highlightColor }}
        />
        <Label
          text={time(job.lastCompletedBuild.timeElapsedSince)}
          style={{
            fontSize: "0.75em",
            textAlign: "right",
            color: highlightColor,
          }}
        />
      </div>
    </a>
  );
}

export default Cell;
