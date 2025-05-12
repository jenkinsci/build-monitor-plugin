import "./tests.scss";

import { Job } from "../../models/job.ts";

export default function Tests({ job }: { job: Job }) {
  // job.realtimeTests = [
  //   {
  //     estimatedRemainingTime: "2m 30s",
  //     completedPercentages: [30, 30],
  //     completedTests: 3,
  //     expectedTests: 6,
  //     style: "success",
  //   },
  // ];

  if (!job.realtimeTests) {
    return null;
  }

  return (
    <div className="realtime-tests">
      {job.realtimeTests.map((realtimeTest, index) => {
        const title = `${realtimeTest.completedTests} / ${realtimeTest.expectedTests}, Remaining Time: ~ ${realtimeTest.estimatedRemainingTime}`;

        return (
          <div className={"bs-progress"} title={title} key={index}>
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
