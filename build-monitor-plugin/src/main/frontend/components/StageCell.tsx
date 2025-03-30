import React from "react";
import Label from "./Label";
import {buildStatusToClass} from "./utils";
import {Stage} from "../models/Stage";
import time from "../utils/time";
import {Column} from "../models/Column";

function StageCell({stage, column}: {stage: Stage, column: Column}) {
    if (!stage) {
        return (
            <button className={'psv-cell psv-cell--null'}></button>
        )
    }

    let percentage = 100;

    if (stage.status === 'IN_PROGRESS') {
        const duration = stage.durationMillis - stage.pauseDurationMillis;
        const expectedDuration = column.averageDuration ?? 0;

        percentage = (duration / expectedDuration) * 100;

        if (percentage > 100) {
            percentage = 95;
        }
    }

    function clicky() {
        const div = document.createElement("pre")

        // fetch(stage.stageFlowNodes[0]._links.log.href)
        //     .then(response => {
        //         return response.json();
        //     })
        //     .then(data => {
        //         div.textContent = JSON.stringify(data);
        //         // @ts-ignore
        //         dialog.modal(div, {
        //             maxWidth: "1000px",
        //             title: 'hello world',
        //         });
        //     });
    }

    return (
        <button onClick={() => clicky()} className={"psv-cell psv-cell" + buildStatusToClass(stage.status) + " psv-stage-cell"}>
            <Label text={time(stage.durationMillis, 2)}></Label>
            <div className={"durationboi " + (percentage !== 100 ? 'durationboi--animate' : '')} style={{width: percentage + "%"}}></div>
        </button>
    )
}

export default StageCell;
