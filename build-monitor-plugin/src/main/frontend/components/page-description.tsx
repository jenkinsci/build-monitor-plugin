import React from "react";
import { createPortal } from "react-dom";

const PageDescription = ({ description }: { description?: string }) => {
  if (!description) {
    return;
  }

  const descriptionPortal = document.querySelector(
    ".jenkins-breadcrumbs__list-item",
  )!;
  return createPortal(
    <p
      className="jenkins-!-margin-left-1"
      style={{ color: "var(--text-color-secondary)" }}
    >
      {description}
    </p>,
    descriptionPortal,
  );
};

export default PageDescription;
