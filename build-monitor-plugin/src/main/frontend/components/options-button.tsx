import React from "react";
import { createPortal } from "react-dom";

export default function OptionsButton() {
  return createPortal(
    <button
      className={"jenkins-button jenkins-button--tertiary"}
      onClick={() => alert("Clicked from React!")}
    >
      Click Me (React Button)
    </button>,
    document.querySelector(".jenkins-header__actions")!
  );
}
