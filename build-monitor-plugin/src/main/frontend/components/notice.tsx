import { BUILD_MONITOR_SYMBOL } from "../utils/symbols.tsx";

export default function Notice() {
  return (
    <div className="bs-grid">
      <div className={"jenkins-notice"}></div>
      <div className={"jenkins-notice"}></div>
      <div className={"jenkins-notice"}></div>
      <div className={"jenkins-notice"}></div>
      <div className={"jenkins-notice"}>
        {BUILD_MONITOR_SYMBOL}It seems a bit empty here...
        <div className="jenkins-notice__description">
          <a href="configure">Add some jobs</a>
        </div>
      </div>
      <div className={"jenkins-notice"}></div>
      <div className={"jenkins-notice"}></div>
      <div className={"jenkins-notice"}></div>
      <div className={"jenkins-notice"}></div>
    </div>
  );
}
