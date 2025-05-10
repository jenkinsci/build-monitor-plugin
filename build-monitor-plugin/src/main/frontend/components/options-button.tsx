import React from "react";
import { createPortal } from "react-dom";
import { RESET_SYMBOL, SETTINGS_SYMBOL } from "../utils/symbols";
import Slider from "./slider";
import { Statee } from "../models/state";
import { defaultState } from "./container";
import Checkbox from "./checkbox";
import Dropdown from "./dropdown.tsx";

interface OptionsButtonProps {
  state: Statee;
  setState: (value: ((prevState: Statee) => Statee) | Statee) => void;
}

const OutsideButtonWithDropdown = ({ state, setState }: OptionsButtonProps) => {
  const buttonPortal = document.querySelector(".jenkins-header__actions")!;

  const resetState = () => {
    setState(defaultState);
  };

  return (
    <>
      {createPortal(
        <Dropdown
          items={[
            <Slider
              label={"Text size"}
              min={0.1}
              max={5}
              value={state.textSize}
              setValue={(e) =>
                setState((prevState) => ({
                  ...prevState,
                  textSize: Number(e.target.value),
                }))
              }
              step={0.1}
            />,
            <Slider
              label={"Maximum number of columns"}
              min={1}
              max={20}
              value={state.maximumNumberOfColumns}
              setValue={(e) =>
                setState((prevState) => ({
                  ...prevState,
                  maximumNumberOfColumns: Number(e.target.value),
                }))
              }
              step={1}
            />,
            "separator",
            <div className={"bs-checkboxes"}>
              <Checkbox
                label={"Show badges"}
                id="settings-show-badges"
                value={state.showBadges}
                setValue={(e) =>
                  setState((prevState) => ({
                    ...prevState,
                    showBadges: e,
                  }))
                }
              />
              <Checkbox
                label={"Reduce motion"}
                id="settings-reduce-motion"
                value={state.reduceMotion}
                setValue={(e) =>
                  setState((prevState) => ({
                    ...prevState,
                    reduceMotion: e,
                  }))
                }
              />
              <Checkbox
                label={"Color blind mode"}
                id="settings-color-blind-mode"
                value={state.colorBlindMode}
                setValue={(e) =>
                  setState((prevState) => ({
                    ...prevState,
                    colorBlindMode: e,
                  }))
                }
              />
            </div>,
            "separator",
            {
              icon: SETTINGS_SYMBOL,
              text: "Edit View",
              href: "",
            },
            "separator",
            <button
              className={"jenkins-dropdown__item jenkins-!-warning-color"}
              onClick={resetState}
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
