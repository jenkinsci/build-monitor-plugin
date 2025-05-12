import { createPortal } from "react-dom";

/**
 * Optional description of the page which appears next to the page's breadcrumb in the header
 */
const PageDescription = ({ description }: { description?: string }) => {
  if (!description) {
    return;
  }

  const breadcrumbs = document.querySelectorAll(
    ".jenkins-breadcrumbs__list-item",
  );
  const descriptionPortal = breadcrumbs[breadcrumbs.length - 1];

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
