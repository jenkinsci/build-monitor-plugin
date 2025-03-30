import React, {useEffect, useState} from "react";
import Label from "./Label";
import BuildHeader from "./BuildHeader";
import StageCell from "./StageCell";
import {Build} from "../models/Build";
import {Column} from "../models/Column";
import time from "../utils/time";

function Stageview() {
    const [columns, setColumns] = useState<Column[]>([])
    const [builds, setBuilds] = useState<Build[]>([]);

    function generateColumns() {
        let firstNonInProgressJob = builds
            .find(build => build.status !== 'IN_PROGRESS');

        if (!firstNonInProgressJob) {
            firstNonInProgressJob = builds[0];
        }

        if (!firstNonInProgressJob) {
            return [];
        }

        setColumns(firstNonInProgressJob.stages.map(stage => (
            {name: stage.name,
                averageDuration: averageDuration(stage.id),
                durationMillis: stage.durationMillis}
        )))
    }

    function averageDuration(id: string, includeInProgress: boolean = true) {
        const durations = builds
            .flatMap(build => build.stages)
            .filter(e => e.id === id)
            .filter(e => e.status !== 'IN_PROGRESS')
            .map(s => s.durationMillis);

        if (!durations || durations.length === 0) {
            return null;
        }

        let duration = durations.reduce((p, c) => p + c) / durations.length;

        if (includeInProgress) {
            const inProgressDurations = builds
                .flatMap(build => build.stages)
                .filter(e => e.id === id)
                .filter(e => e.status === 'IN_PROGRESS')
                .map(s => s.durationMillis);

            if (!inProgressDurations || inProgressDurations.length === 0) {
                return duration;
            }

            const inProgressDuration = inProgressDurations.reduce((p, c) => p + c) / inProgressDurations.length;

            if (inProgressDuration > duration) {
                duration = (duration + inProgressDuration) / 2;
            }
        }

        return duration;
    }

    useEffect(() => {
        generateColumns();
    }, [builds]);

    useEffect(() => {
        const url = "";
        
        fetch('http://localhost:8080/jenkins/job/coming%20back/wfapi/runs?since=%235&fullStages=true&_=1690120329481')
            .then(response => {
                return response.json();
            })
            .then(data => setBuilds(data));

        const intervalID = setInterval(() =>  {
            fetch('http://localhost:8080/jenkins/job/coming%20back/wfapi/runs?since=%235&fullStages=true&_=1690120329481')
                .then(response => {
                    return response.json();
                })
                .then(data => setBuilds(data));
        }, 3000);

        return () => clearInterval(intervalID);
    }, [])

    return (
        <table className={"psv-stageview"}>
            <thead className={"psv-stageview__heading"}>
                <tr>
                    <th>
                        {/*Average time: 10 minutes 48 seconds*/}
                    </th>
                    {columns.map((column: Column) => {
                        return (
                            <th>
                                <p>{column.name}</p>
                                <p>
                                    <Label text={time(column.averageDuration ?? 0, 2)}></Label>
                                </p>
                            </th>
                        )
                    })}
                </tr>
            </thead>
            <tbody className={"psv-stageview__body"}>
                {builds.map((build: any) => {
                    return (
                        <tr className={"psv-stageview__build"}>
                            <td width={"180px"}>
                                <BuildHeader build={build} />
                            </td>
                            {columns.map((column: Column) => {
                                const stage = build.stages[columns.indexOf(column)];

                                return (
                                    <td>
                                        <StageCell stage={stage} column={column} />
                                    </td>
                                )
                            })}
                        </tr>
                    )
                })}
            </tbody>
        </table>
    )
}

export default Stageview
