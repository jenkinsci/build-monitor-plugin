import React, { useEffect, useRef, useState } from "react";
import { createPortal } from "react-dom";
import { RESET_SYMBOL, SETTINGS_SYMBOL } from "../utils/symbols";
import Slider from "./slider";
import { Statee } from "../models/state";
import { defaultState } from "./container";
import Checkbox from "./checkbox";

interface OptionsButtonProps {
  state: Statee;
  setState: (value: ((prevState: Statee) => Statee) | Statee) => void;
}

const OutsideButtonWithDropdown = ({ state, setState }: OptionsButtonProps) => {
  const [open, setOpen] = useState(false);
  const buttonRef = useRef<HTMLButtonElement | null>(null);
  const dropdownRef = useRef<HTMLDivElement | null>(null);

  const toggleOpen = () => setOpen((prev) => !prev);

  // Handle outside click
  useEffect(() => {
    const handleClickOutside = (e: MouseEvent) => {
      if (
        !dropdownRef.current?.contains(e.target as Node) &&
        !buttonRef.current?.contains(e.target as Node)
      ) {
        setOpen(false);
      }
    };

    document.addEventListener("mousedown", handleClickOutside);

    return () => {
      document.removeEventListener("mousedown", handleClickOutside);
    };
  }, []);

  const buttonPortal = document.querySelector(".jenkins-header__actions")!;
  const dropdownPortal = document.body;

  const resetState = () => {
    setState(defaultState);
  };

  return (
    <>
      {createPortal(
        <button
          ref={buttonRef}
          onClick={toggleOpen}
          className={
            "jenkins-button " + (open ? "" : "jenkins-button--tertiary")
          }
        >
          {SETTINGS_SYMBOL}
        </button>,
        buttonPortal
      )}

      {open &&
        createPortal(
          <div
            ref={dropdownRef}
            className="tippy-box"
            data-theme="dropdown"
            style={{
              position: "fixed",
              top: "60px",
              right: "var(--section-padding)",
              zIndex: 1000,
            }}
          >
            <div className="jenkins-dropdown">
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
              />
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
              />
              <div className={"jenkins-dropdown__separator"} />
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
              </div>
              <div className={"jenkins-dropdown__separator"} />
              <a href="configure" className={"jenkins-dropdown__item"}>
                <div className={"jenkins-dropdown__item__icon"}>
                  {SETTINGS_SYMBOL}
                </div>
                Edit View
              </a>
              <div className={"jenkins-dropdown__separator"} />
              <button
                className={"jenkins-dropdown__item jenkins-!-warning-color"}
                onClick={resetState}
              >
                <div className={"jenkins-dropdown__item__icon"}>
                  {RESET_SYMBOL}
                </div>
                Reset to default
              </button>
            </div>
          </div>,
          dropdownPortal
        )}
    </>
  );
};

export default OutsideButtonWithDropdown;
