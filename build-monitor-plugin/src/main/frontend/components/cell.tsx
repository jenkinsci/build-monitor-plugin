import React from "react";
import Label from "./label";
import time from "../utils/time";
import { Job } from "../models/job";
import { buildStatusToClass } from "../utils/utils.ts";
import { useUserPreferences } from "../providers/user-preference-provider.tsx";
import Claim from "./snippets/claims.tsx";
import Badges from "./snippets/badges.tsx";
import Problems from "./snippets/problems.tsx";
import Tests from "./snippets/tests.tsx";

function Cell({ job }: { job: Job }) {
  const { colorBlindMode } = useUserPreferences();

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
        className={"bm-cell-duration bm-cell-duration--animate"}
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

      <Claim job={job} />
      <Problems job={job} />
      <Tests job={job} />
      <Badges job={job} />

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
