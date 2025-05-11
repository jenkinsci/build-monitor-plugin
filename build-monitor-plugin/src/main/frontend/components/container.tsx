import React, { useEffect, useState } from "react";
import Cell from "./cell.tsx";
import { Job } from "../models/job";
import { getJobs } from "../apis/api";
import OptionsButton from "./options-button";
import Notice from "./notice.tsx";
import { useUserPreferences } from "../providers/user-preference-provider.tsx";
import { useDialog } from "../providers/dialog-provider.tsx";

function Container() {
  const [jobs, setJobs] = useState<Job[]>([]);
  const [isLoading, setIsLoading] = useState(true);

  const { createDialog } = useDialog();
  const { textSize, maximumNumberOfColumns } = useUserPreferences();

  useEffect(() => {
    const fetchJobs = async () => {
      try {
        const jobs = await getJobs();

        setJobs(jobs);
      } catch (error) {
        createDialog(error);
      } finally {
        setIsLoading(false);
      }
    };

    fetchJobs();

    const intervalID = setInterval(fetchJobs, 3000);

    return () => clearInterval(intervalID);
  }, []);

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
