import React, { useEffect, useState } from "react";
import StageCell from "./stage-cell";
import { Job } from "../models/job";
import { getJobs } from "../apis/api";
import OptionsButton from "./options-button";
import Notice from "./notice.tsx";
import { useUserPreferences } from "../providers/user-preference-provider.tsx";

function Container() {
  const [jobs, setJobs] = useState<Job[]>([]);
  const [isLoading, setIsLoading] = useState(true);
  const { textSize, maximumNumberOfColumns, colorBlindMode } =
    useUserPreferences();

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
                fontSize: textSize + "rem",
                gridTemplateColumns: "1fr ".repeat(
                  Math.min(jobs.length, maximumNumberOfColumns),
                ),
              }}
            >
              {jobs.map((job) => (
                <StageCell
                  key={job.url}
                  job={job}
                  colorBlindMode={colorBlindMode}
                />
              ))}
            </div>
          )}
        </>
      )}
      <OptionsButton amountOfJobs={jobs.length} />
    </>
  );
}

export default Container;
