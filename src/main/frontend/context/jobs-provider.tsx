import {
  createContext,
  ReactNode,
  useContext,
  useEffect,
  useState,
} from "react";

import { getJobs } from "../apis/api.ts";
import { Job } from "../models/job.ts";
import { useDialog } from "./dialog-provider.tsx";

interface JobsContextType {
  jobs: Job[];
  isLoading: boolean;
}

const JobsContext = createContext<JobsContextType | undefined>(undefined);

export const JobsProvider = ({ children }: { children: ReactNode }) => {
  const [jobs, setJobs] = useState<Job[]>([]);
  const [isLoading, setIsLoading] = useState(true);
  const { createDialog } = useDialog();

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
  }, [createDialog]);

  return (
    <JobsContext.Provider value={{ jobs, isLoading }}>
      {children}
    </JobsContext.Provider>
  );
};

export const useJobs = (): JobsContextType => {
  const context = useContext(JobsContext);
  if (!context) {
    throw new Error("useJobs must be used within a JobsProvider");
  }
  return context;
};
