import { Job } from "../../models/job.ts";
import './tests.scss';

export default function Tests({ job }: { job: Job }) {
  job.realtimeTests = [
    {
      estimatedRemainingTime: "2m 30s",
      completedPercentages: [10, 30, 50],
      completedTests: 3,
      expectedTests: 6,
      style: "success",
    },
  ];

  if (!job.realtimeTests) {
    return null;
  }

  return (
    <li className="realtime-tests">
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
    </li>
  );
}
