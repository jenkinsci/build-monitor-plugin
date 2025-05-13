import "./tests.scss";

import { Job } from "../../models/job.ts";

export default function Tests({ job }: { job: Job }) {
  if (!job.realtimeTests) {
    return null;
  }

  return (
    <div className="realtime-tests">
      {job.realtimeTests.map((realtimeTest, index) => {
        const title = `${realtimeTest.completedTests} / ${realtimeTest.expectedTests}, Remaining Time: ~ ${realtimeTest.estimatedRemainingTime}`;

        return (
          <div
            className={
              "bs-progress " +
              (realtimeTest.style === "red" ? "bs-progress--red" : "")
            }
            title={title}
            key={index}
          >
            {realtimeTest.completedPercentages[1] > 0 && (
              <div
                className="bar bar-progress2"
                style={{ width: `${realtimeTest.completedPercentages[1]}%` }}
              />
            )}
            <div
              className="bar"
              style={{ width: `${realtimeTest.completedPercentages[0]}%` }}
            />
          </div>
        );
      })}
    </div>
  );
}
