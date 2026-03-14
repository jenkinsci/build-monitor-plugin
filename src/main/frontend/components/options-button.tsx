import { useEffect, useState } from "react";
import { createPortal } from "react-dom";

import { useConfirmation } from "../context/confirmation-provider.tsx";
import { useUserPreferences } from "../context/user-preference-provider.tsx";
import { DELETE_SYMBOL, RESET_SYMBOL, SETTINGS_SYMBOL } from "../utils/symbols";
import Checkbox from "./checkbox";
import Dropdown from "./dropdown.tsx";
import Slider from "./slider";

interface OptionsButtonProps {
  amountOfJobs: number;
}

const OutsideButtonWithDropdown = ({ amountOfJobs }: OptionsButtonProps) => {
  const {
    textSize,
    setTextSize,
    maximumNumberOfColumns,
    setMaximumNumberOfColumns,
    colorBlindMode,
    setColorBlindMode,
    showBadges,
    setShowBadges,
    reset,
    isResettable,
  } = useUserPreferences();
  const [ready, setReady] = useState(false);
  const buttonPortal = document.querySelector(".jenkins-header__actions")!;
  const { createConfirmation } = useConfirmation();
  const controlsEnabled = amountOfJobs === 0;

  useEffect(() => {
    if (buttonPortal) {
      buttonPortal.innerHTML = "";
      setReady(true); // Signal that the portal can now be created
    }
  }, []);

  return (
    <>
      {ready &&
        createPortal(
          <>
            <span className={"bm-credit"}>
              Brought to you by{" "}
              <a href="https://www.linkedin.com/in/janmolak/" target="_alt">
                Jan Molak
              </a>{" "}
              and{" "}
              <a href="https://janfaracik.github.io" target="_alt">
                Jan Faracik
              </a>
            </span>
            <Dropdown
              items={[
                <Slider
                  key={"text-size"}
                  label={"Text size"}
                  min={0.1}
                  max={5}
                  value={textSize}
                  setValue={(e) => setTextSize(Number(e.target.value))}
                  step={0.1}
                  disabled={controlsEnabled}
                />,
                <Slider
                  key={"maximum-number-of-columns"}
                  label={"Maximum number of columns"}
                  min={1}
                  max={Math.min(amountOfJobs, 20)}
                  value={Math.min(amountOfJobs, maximumNumberOfColumns)}
                  setValue={(e) =>
                    setMaximumNumberOfColumns(Number(e.target.value))
                  }
                  step={1}
                  disabled={controlsEnabled || amountOfJobs === 1}
                />,
                "separator",
                <div key={"show-badges"} className={"bm-checkboxes"}>
                  <Checkbox
                    label={"Show badges"}
                    id="settings-show-badges"
                    value={showBadges}
                    setValue={(e) => setShowBadges(e)}
                    disabled={controlsEnabled}
                  />
                  <Checkbox
                    label={"Color blind mode"}
                    id="settings-color-blind-mode"
                    value={colorBlindMode}
                    setValue={(e) => setColorBlindMode(e)}
                    disabled={controlsEnabled}
                  />
                </div>,
                "separator",
                {
                  icon: SETTINGS_SYMBOL,
                  text: "Edit View",
                  href: "configure",
                },
                <button
                  key={"reset"}
                  className={"jenkins-dropdown__item jenkins-!-warning-color"}
                  onClick={reset}
                  disabled={isResettable}
                >
                  <div className={"jenkins-dropdown__item__icon"}>
                    {RESET_SYMBOL}
                  </div>
                  Reset to default
                </button>,
                "separator",
                <button
                  key={"delete"}
                  className={
                    "jenkins-dropdown__item jenkins-!-destructive-color"
                  }
                  onClick={() =>
                    createConfirmation(
                      "Are you sure you want to delete this view?",
                      "This won't delete the jobs inside of it.",
                      "doDelete",
                    )
                  }
                >
                  <div className={"jenkins-dropdown__item__icon"}>
                    {DELETE_SYMBOL}
                  </div>
                  Delete View
                </button>,
              ]}
            />
          </>,
          buttonPortal,
        )}
    </>
  );
};

export default OutsideButtonWithDropdown;
