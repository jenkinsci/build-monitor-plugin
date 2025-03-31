import React, { useEffect, useState } from "react";
import StageCell from "./stage-cell";
import { Job } from "../models/job";
import { getJobs } from "../apis/api";
import { Statee } from "../models/state";
import OptionsButton from "./options-button";

function Container() {
  const [jobs, setJobs] = useState<Job[]>([]);
  const [state, setState] = React.useState<Statee>(defaultState);

  useEffect(() => {
    getJobs().then((jobs) => setJobs(jobs));

    const intervalID = setInterval(() => {
      getJobs().then((jobs) => setJobs(jobs));
    }, 3000);

    return () => clearInterval(intervalID);
  }, []);

  return (
    <>
      <div
        className="psv-job-grid"
        style={{ fontSize: state.textSize + "rem" }}
      >
        {jobs.map((job) => (
          <StageCell key={job.url} job={job} />
        ))}
      </div>
      <OptionsButton state={state} setState={setState} />
    </>
  );
}

export const defaultState: Statee = {
  colorBlindMode: false,
  maximumNumberOfColumns: 2,
  reduceMotion: false,
  showBadges: false,
  textSize: 1,
};

export default Container;
