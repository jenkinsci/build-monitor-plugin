import { BUILD_MONITOR_SYMBOL } from "../utils/symbols.tsx";

export default function Notice() {
  return (
    <div
      className={"jenkins-notice"}
      style={{ marginInline: "var(--section-padding)" }}
    >
      {BUILD_MONITOR_SYMBOL}It seems a bit empty here...
      <div className="jenkins-notice__description">
        <a href="configure">Add some jobs</a>
      </div>
    </div>
  );
}
