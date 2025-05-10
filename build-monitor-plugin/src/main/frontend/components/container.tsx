import React, { useEffect, useState } from "react";
import StageCell from "./stage-cell";
import { Job } from "../models/job";
import { getJobs } from "../apis/api";
import { UserPreferences } from "../models/user-preferences.ts";
import OptionsButton from "./options-button";
import Notice from "./notice.tsx";

function Container() {
  const [jobs, setJobs] = useState<Job[]>([]);
  const [isLoading, setIsLoading] = useState(true);
  const [state, setState] = React.useState<UserPreferences>(defaultState);

  useEffect(() => {
    getJobs().then((jobs) => {
      setIsLoading(false);
      setJobs(jobs);
    });

    const intervalID = setInterval(() => {
      getJobs().then((jobs) => setJobs(jobs));
    }, 3000);

    return () => clearInterval(intervalID);
  }, []);

  return (
    <>
      {!isLoading && (
        <>
          {jobs.length === 0 && <Notice />}
          {jobs.length > 0 && (
            <div
              className="bs-grid"
              style={{
                fontSize: state.textSize + "rem",
                gridTemplateColumns: "1fr ".repeat(
                  Math.min(jobs.length, state.maximumNumberOfColumns),
                ),
              }}
            >
              {jobs.map((job) => (
                <StageCell
                  key={job.url}
                  job={job}
                  colorBlindMode={state.colorBlindMode}
                />
              ))}
            </div>
          )}
        </>
      )}
      <OptionsButton
        state={state}
        setState={setState}
        disabled={jobs.length === 0}
      />
    </>
  );
}

export const defaultState: UserPreferences = {
  colorBlindMode: false,
  maximumNumberOfColumns: 3,
  showBadges: false,
  textSize: 1,
};

export default Container;
