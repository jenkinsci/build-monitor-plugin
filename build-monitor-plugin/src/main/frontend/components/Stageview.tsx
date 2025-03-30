import React, {useEffect, useState} from "react";
import StageCell from "./StageCell";
import {Job} from "../models/job";
import {getJobs} from "../apis/api";

function Stageview() {
    const [jobs, setJobs] = useState<Job[]>([]);

    useEffect(() => {
        getJobs().then(jobs => setJobs(jobs));

        const intervalID = setInterval(() =>  {
            getJobs().then(jobs => setJobs(jobs));
        }, 3000);

        return () => clearInterval(intervalID);
    }, [])

    return (
      <div className="psv-job-grid">
          {jobs.map(job => (
            <StageCell key={job.url} job={job} />
          ))}
      </div>
    )
}

export default Stageview
