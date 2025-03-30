import React from "react";
import Label from "./Label";
import {buildStatusToClass, isRunning} from "./utils";
import {Stage} from "../models/Stage";
import time from "../utils/time";
import {Column} from "../models/Column";
import {Job} from "../models/job";

function StageCell({job}: {job: Job}) {
    if (!job) {
        return (
            <button className={'psv-cell psv-cell--null'}></button>
        )
    }

    // let percentage = 100;

    // if (stage.status === 'IN_PROGRESS') {
    //     const duration = stage.durationMillis - stage.pauseDurationMillis;
    //     const expectedDuration = column.averageDuration ?? 0;
    //
    //     percentage = (duration / expectedDuration) * 100;
    //
    //     if (percentage > 100) {
    //         percentage = 95;
    //     }
    // }

    // function clicky() {
    //     const div = document.createElement("pre")
    //
    //     // fetch(stage.stageFlowNodes[0]._links.log.href)
    //     //     .then(response => {
    //     //         return response.json();
    //     //     })
    //     //     .then(data => {
    //     //         div.textContent = JSON.stringify(data);
    //     //         // @ts-ignore
    //     //         dialog.modal(div, {
    //     //             maxWidth: "1000px",
    //     //             title: 'hello world',
    //     //         });
    //     //     });
    // }

    return (
        <a href={job.url} className={"psv-cell psv-cell" + buildStatusToClass(job.status) + " psv-stage-cell"}>

          {isRunning(job.status) && <div className={"durationboi durationboi--animate"} style={{width: job.progress + "%"}}></div>}

          <Label text={job.name}></Label>
          <Label text={job.headline}></Label>

          <div className={"psv-cell__details"}>
            <Label text={job.lastCompletedBuild.name} />
            <Label text={time(job.lastCompletedBuild.timeElapsedSince)} />
          </div>
        </a>
    )
}

export default StageCell;
