import { useEffect, useState } from "react";
import { createPortal } from "react-dom";

import { useUserPreferences } from "../context/user-preference-provider.tsx";
import { RESET_SYMBOL, SETTINGS_SYMBOL } from "../utils/symbols";
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
          <Dropdown
            disabled={amountOfJobs === 0}
            items={[
              <Slider
                key={"text-size"}
                label={"Text size"}
                min={0.1}
                max={5}
                value={textSize}
                setValue={(e) => setTextSize(Number(e.target.value))}
                step={0.1}
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
              />,
              "separator",
              <div key={"show-badges"} className={"bm-checkboxes"}>
                <Checkbox
                  label={"Show badges"}
                  id="settings-show-badges"
                  value={showBadges}
                  setValue={(e) => setShowBadges(e)}
                />
                <Checkbox
                  label={"Color blind mode"}
                  id="settings-color-blind-mode"
                  value={colorBlindMode}
                  setValue={(e) => setColorBlindMode(e)}
                />
              </div>,
              "separator",
              {
                icon: SETTINGS_SYMBOL,
                text: "Edit View",
                href: "configure",
              },
              "separator",
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
            ]}
          />,
          buttonPortal,
        )}
    </>
  );
};

export default OutsideButtonWithDropdown;
