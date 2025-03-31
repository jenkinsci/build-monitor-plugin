import React, { useEffect, useRef, useState } from "react";
import { createPortal } from "react-dom";
import { RESET_SYMBOL, SETTINGS_SYMBOL } from "../utils/symbols";
import Slider from "./slider";

const OutsideButtonWithDropdown = () => {
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
                defaultValue={1}
                step={0.1}
              />
              <Slider
                label={"Maximum number of columns"}
                min={1}
                max={20}
                defaultValue={2}
                step={1}
              />
              <div className={"jenkins-dropdown__separator"} />
              <div className={"bs-checkboxes"}>
                <div className="jenkins-checkbox">
                  <input
                    ng-model="settings.colourBlind"
                    ng-false-value="'0'"
                    ng-true-value="'1'"
                    id="settings-colour-blind"
                    type="checkbox"
                  />
                  <label
                    htmlFor="settings-colour-blind"
                    title="Applies a colour blind-friendly colour scheme"
                  >
                    Color blind mode
                  </label>
                </div>
                <div className="jenkins-checkbox">
                  <input
                    ng-model="settings.reduceMotion"
                    ng-false-value="'0'"
                    ng-true-value="'1'"
                    id="settings-reduce-motion"
                    type="checkbox"
                  />
                  <label
                    htmlFor="settings-reduce-motion"
                    title="Reduces the amount of animation Build Monitor uses"
                  >
                    Reduce motion
                  </label>
                </div>
                <div className="jenkins-checkbox">
                  <input
                    ng-model="settings.showBadges"
                    ng-false-value="'0'"
                    ng-true-value="'1'"
                    id="settings-show-badges"
                    type="checkbox"
                  />
                  <label
                    htmlFor="settings-show-badges"
                    title="Show the last build badges"
                  >
                    Show badges
                  </label>
                </div>
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
