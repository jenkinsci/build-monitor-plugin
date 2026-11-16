import { render, screen } from "@testing-library/react";
import BuildDescription from "./build-description.tsx";

describe("BuildDescription", () => {
  it("renders an HTML link as a working link", () => {
    render(
      <BuildDescription
        description={
          'Download the <a href="https://example.test/report">report</a>'
        }
      />,
    );

    expect(screen.getByText("Download the")).toBeInTheDocument();
    expect(screen.getByRole("link", { name: "report" })).toHaveAttribute(
      "href",
      "https://example.test/report",
    );
  });

  it("preserves plain-text descriptions", () => {
    render(<BuildDescription description="Build completed successfully" />);

    expect(
      screen.getByText("Build completed successfully"),
    ).toBeInTheDocument();
    expect(screen.queryByRole("link")).not.toBeInTheDocument();
  });

  it("removes scripts, event handlers, unsafe URLs, and unsupported tags", () => {
    const { container } = render(
      <BuildDescription
        description={
          '<script>window.xss = true</script><a href="javascript:alert(1)" onclick="window.xss = true">unsafe</a><img src="x" onerror="window.xss = true"><svg onload="window.xss = true"></svg>'
        }
      />,
    );

    expect(container.querySelector("script, img, svg")).not.toBeInTheDocument();
    expect(
      container.querySelector("[onclick], [onerror], [onload]"),
    ).not.toBeInTheDocument();
    expect(screen.getByText("unsafe")).not.toHaveAttribute("href");
    expect(window).not.toHaveProperty("xss");
  });
});
