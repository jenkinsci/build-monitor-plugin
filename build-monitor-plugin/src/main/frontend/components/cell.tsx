import React from "react";
import Label from "./label";
import time from "../utils/time";
import { Job } from "../models/job";
import { buildStatusToClass } from "../utils/utils.ts";
import { useUserPreferences } from "../providers/user-preference-provider.tsx";

function Cell({ job }: { job: Job }) {
  const { colorBlindMode, showBadges } = useUserPreferences();

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
        "bm-cell bm-cell" +
        buildStatusToClass(job.status) +
        " " +
        (colorBlindMode ? "bm-cell--color-blind-mode" : "")
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
          color: "color-mix(in srgb, var(--bm-cell-color), var(--text-color))",
        }}
      ></Label>
      <Label text={job.headline} style={{ fontSize: "0.75em" }}></Label>
      {badges}

      <div className={"bm-cell__details"}>
        <Label
          text={job.lastCompletedBuild.name}
          style={{ fontSize: "0.75em" }}
        />
        <Label
          text={time(job.lastCompletedBuild.timeElapsedSince)}
          style={{
            fontSize: "0.75em",
            textAlign: "right",
          }}
        />
      </div>
    </a>
  );
}

export default Cell;
