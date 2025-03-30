import React from "react";
import {Build} from "../models/Build";
import {buildStatusToClass} from "./utils";

function BuildHeader({build}: {build: Build}) {
    function toDate(milli: number) {
        return new Date(milli).toLocaleTimeString('en-gb', { month: "short", day: "numeric" })
    }

    return (
        <a href={build.id} className={"psv-cell psv-cell" + buildStatusToClass(build.status)}>
            {build.name}
            <div className={'psv-cell__time'}>
                {toDate(build.startTimeMillis)}
            </div>
        </a>
    )
}

export default BuildHeader;
