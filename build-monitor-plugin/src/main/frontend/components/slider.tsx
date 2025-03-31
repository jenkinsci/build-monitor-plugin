import React, { useState } from "react";
import Label from "./Label";

export default function Slider({
  label,
  min,
  max,
  defaultValue,
  step,
}: {
  label: string;
  min: number;
  max: number;
  defaultValue: number;
  step: number;
}) {
  const [value, setValue] = useState<number>(defaultValue);

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
        defaultValue={value}
        step={step}
        onChange={(e) => setValue(Number(e.target.value))}
      />
    </div>
  );
}
