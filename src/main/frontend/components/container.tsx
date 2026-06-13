import { useJobs } from "../context/jobs-provider.tsx";
import { useUserPreferences } from "../context/user-preference-provider.tsx";
import Notice from "./notice.tsx";
import OptionsButton from "./options-button";
import PagedGrid from "./paged-grid.tsx";

function Container() {
  const { jobs, isLoading } = useJobs();
  const { textSize, maximumNumberOfColumns } = useUserPreferences();

  return (
    <>
      {!isLoading && (
        <>
          {jobs.length === 0 && <Notice />}
          {jobs.length > 0 && (
            <PagedGrid
              jobs={jobs}
              textSize={textSize}
              maximumNumberOfColumns={maximumNumberOfColumns}
            />
          )}
        </>
      )}
      <OptionsButton amountOfJobs={jobs.length} />
    </>
  );
}

export default Container;
