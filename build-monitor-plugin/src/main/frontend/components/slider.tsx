import React from "react";

export default function Slider({ label }: { label: string }) {
  return (
    <div className={"bs-slider__container"}>
      <p className={"jenkins-form-label"}>{label}</p>
      <input className={"bs-slider"} type="range" min="1" max="100" defaultValue="50" />
    </div>
  )
}
