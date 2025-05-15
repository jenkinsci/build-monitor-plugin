import { useUserPreferences } from "../context/user-preference-provider.tsx";
import { Job } from "../models/job";
import { buildStatusToClass } from "../utils/utils.ts";
import Label from "./label";
import Badges from "./snippets/badges.tsx";
import BuildNumber from "./snippets/build-number.tsx";
import BuildTime from "./snippets/build-time.tsx";
import Claim from "./snippets/claim.tsx";
import Problems from "./snippets/problems.tsx";
import Tests from "./snippets/tests.tsx";

function Cell({ job }: { job: Job }) {
  const { colorBlindMode } = useUserPreferences();

  return (
    <a
      href={job.url}
      className={
        "bm-cell bm-cell" +
        buildStatusToClass(job) +
        " " +
        (colorBlindMode ? "bm-cell--color-blind-mode" : "")
      }
    >
      <div
        className={"bm-cell-duration bm-cell-duration--animate"}
        style={{ width: job.progress + "%" }}
      />

      <h2>
        <Label
          text={job.name}
          style={{
            fontSize: "1.25em",
            fontWeight: "550",
            color:
              "var(--bm-contrast, color-mix(in srgb, var(--bm-cell-color), var(--text-color)))",
          }}
        />
      </h2>

      {job.headline && !job.claim?.active && <Label text={job.headline} />}

      <Claim job={job} />
      <Problems job={job} />
      <Tests job={job} />
      <Badges job={job} />

      <div className={"bm-cell__details"}>
        <BuildNumber job={job} />

        <BuildTime job={job} />
      </div>
    </a>
  );
}

export default Cell;
