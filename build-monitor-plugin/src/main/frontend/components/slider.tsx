import React, { ChangeEvent } from "react";
import Label from "./label";

export default function Slider({
  label,
  min,
  max,
  step,
  value,
  setValue,
}: {
  label: string;
  min: number;
  max: number;
  step: number;
  value: number;
  setValue: (e: ChangeEvent<HTMLInputElement>) => void;
}) {
  return (
    <div className={"bs-slider__container"}>
      <p className={"jenkins-form-label"}>
        {label}
        <Label
          text={value.toString()}
          style={{
            marginLeft: "0.75rem",
            color: "var(--text-color-secondary)",
            opacity: 0.65,
          }}
        />
      </p>
      <input
        className={"bs-slider"}
        type="range"
        min={min}
        max={max}
        value={value}
        step={step}
        onChange={setValue}
      />
    </div>
  );
}
