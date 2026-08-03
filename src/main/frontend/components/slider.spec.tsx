import { fireEvent, render, screen } from "@testing-library/react";
import { describe, expect, it, vi } from "vitest";

import Slider from "./slider.tsx";

describe("Slider", () => {
  it("renders label and value", () => {
    render(
      <Slider
        label="Volume"
        min={0}
        max={100}
        step={10}
        value={50}
        setValue={() => {}}
      />,
    );

    expect(screen.getByText("Volume")).toBeInTheDocument();
    expect(screen.getByText("50")).toBeInTheDocument();
  });

  it("sets correct input attributes", () => {
    render(
      <Slider
        label="Brightness"
        min={1}
        max={10}
        step={1}
        value={5}
        setValue={() => {}}
      />,
    );

    const slider = screen.getByRole("slider") as HTMLInputElement;
    expect(slider).toHaveAttribute("min", "1");
    expect(slider).toHaveAttribute("max", "10");
    expect(slider).toHaveAttribute("step", "1");
    expect(slider.value).toBe("5");
  });

  it("calls setValue when changed", () => {
    const handleChange = vi.fn();
    render(
      <Slider
        label="Opacity"
        min={0}
        max={1}
        step={0.1}
        value={0.5}
        setValue={handleChange}
      />,
    );

    const slider = screen.getByRole("slider");
    fireEvent.change(slider, { target: { value: "0.8" } });

    expect(handleChange).toHaveBeenCalledTimes(1);
  });
});
