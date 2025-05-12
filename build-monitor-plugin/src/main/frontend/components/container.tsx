import React from "react";
import Cell from "./cell.tsx";
import OptionsButton from "./options-button";
import Notice from "./notice.tsx";
import { useUserPreferences } from "../context/user-preference-provider.tsx";
import { useJobs } from "../context/jobs-provider.tsx";

function Container() {
  const { jobs, isLoading } = useJobs();
  const { textSize, maximumNumberOfColumns } = useUserPreferences();

  return (
    <>
      {!isLoading && (
        <>
          {jobs.length === 0 && <Notice />}
          {jobs.length > 0 && (
            <div
              className="bm-grid"
              style={{
                fontSize: textSize + "rem",
                gridTemplateColumns: "1fr ".repeat(
                  Math.min(jobs.length, maximumNumberOfColumns),
                ),
              }}
            >
              {jobs.map((job) => (
                <Cell key={job.url} job={job} />
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
