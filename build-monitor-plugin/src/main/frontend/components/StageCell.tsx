import React from "react";
import Label from "./Label";
import {buildStatusToClass, isRunning} from "./utils";
import time from "../utils/time";
import {Job} from "../models/job";

function StageCell({job}: {job: Job}) {
  const highlightColor = "oklch(from var(--psv-cell-color) calc(l + 0.2) c h)"

    return (
        <a href={job.url} className={"psv-cell psv-cell" + buildStatusToClass(job.status)}>

          {isRunning(job.status) && <div className={"durationboi durationboi--animate"} style={{width: job.progress + "%"}}></div>}

          <Label text={job.name} style={{ fontWeight: "550", color: "oklch(from var(--psv-cell-color) calc(l + 100) c h)" }}></Label>
          <Label text={job.headline} style={{ fontSize: "0.75em", color: highlightColor }}></Label>

          <div className={"psv-cell__details"}>
            <Label text={job.lastCompletedBuild.name} style={{ fontSize: "0.75em", color: highlightColor }} />
            <Label text={time(job.lastCompletedBuild.timeElapsedSince)} style={{ fontSize: "0.75em", textAlign: "right", color: highlightColor }} />
          </div>
        </a>
    )
}

export default StageCell;
