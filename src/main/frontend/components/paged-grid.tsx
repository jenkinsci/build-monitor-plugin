import { useEffect, useMemo, useRef, useState } from "react";

import { Job } from "../models/job.ts";
import {
  getColumnCount,
  getRowsPerPage,
  paginateItems,
} from "../utils/grid-pagination.ts";
import Cell from "./cell.tsx";

const getInitialViewportHeight = () =>
  typeof window === "undefined" ? 0 : window.innerHeight;
const getInitialViewportWidth = () =>
  typeof window === "undefined" ? 0 : window.innerWidth;

interface PagedGridProps {
  jobs: Job[];
  textSize: number;
  maximumNumberOfColumns: number;
}

function PagedGrid({ jobs, textSize, maximumNumberOfColumns }: PagedGridProps) {
  const viewportRef = useRef<HTMLDivElement>(null);
  const [viewportHeight, setViewportHeight] = useState(
    getInitialViewportHeight,
  );
  const [viewportWidth, setViewportWidth] = useState(getInitialViewportWidth);
  const [currentPage, setCurrentPage] = useState(0);

  const columnCount = getColumnCount(jobs.length, maximumNumberOfColumns);
  const rowsPerPage = getRowsPerPage({ viewportHeight, textSize });
  const pages = useMemo(
    () => paginateItems(jobs, columnCount, rowsPerPage),
    [jobs, columnCount, rowsPerPage],
  );

  const scrollToPage = (pageIndex: number, behavior: ScrollBehavior = "smooth") => {
    const viewport = viewportRef.current;
    if (!viewport) {
      return;
    }

    viewport.scrollTo({
      left: pageIndex * viewport.clientWidth,
      behavior,
    });
  };

  useEffect(() => {
    const viewport = viewportRef.current;
    if (!viewport) {
      return;
    }

    const updateViewportSize = () => {
      setViewportHeight(viewport.clientHeight);
      setViewportWidth(viewport.clientWidth);
    };

    updateViewportSize();

    if (typeof ResizeObserver === "undefined") {
      window.addEventListener("resize", updateViewportSize);
      return () => window.removeEventListener("resize", updateViewportSize);
    }

    const observer = new ResizeObserver(updateViewportSize);
    observer.observe(viewport);

    return () => observer.disconnect();
  }, []);

  useEffect(() => {
    setCurrentPage((page) => Math.min(page, Math.max(pages.length - 1, 0)));
  }, [pages.length]);

  useEffect(() => {
    if (viewportWidth <= 0) {
      return;
    }

    scrollToPage(currentPage, "auto");
  }, [viewportWidth]);

  useEffect(() => {
    const lastPageIndex = Math.max(pages.length - 1, 0);
    if (currentPage <= lastPageIndex) {
      return;
    }

    setCurrentPage(lastPageIndex);
    scrollToPage(lastPageIndex, "auto");
  }, [currentPage, pages.length]);

  const handleScroll = () => {
    const viewport = viewportRef.current;
    if (!viewport) {
      return;
    }

    const nextPage = Math.max(
      0,
      Math.min(
        pages.length - 1,
        Math.round(viewport.scrollLeft / Math.max(viewport.clientWidth, 1)),
      ),
    );

    if (nextPage !== currentPage) {
      setCurrentPage(nextPage);
    }
  };

  return (
    <div className="bm-grid-shell">
      <div
        ref={viewportRef}
        className="bm-grid-viewport"
        onScroll={handleScroll}
      >
        <div className="bm-grid-track">
          {pages.map((pageJobs, pageIndex) => (
            <section
              key={`page-${pageIndex}`}
              className="bm-grid-page"
              aria-label={`Page ${pageIndex + 1} of ${pages.length}`}
            >
              <div
                className="bm-grid"
                style={{
                  fontSize: textSize + "rem",
                  gridTemplateColumns: "1fr ".repeat(columnCount),
                  gridTemplateRows: "minmax(0, 1fr) ".repeat(rowsPerPage),
                }}
              >
                {pageJobs.map((job) => (
                  <Cell key={job.url} job={job} />
                ))}
              </div>
            </section>
          ))}
        </div>
      </div>

      {pages.length > 1 && (
        <div className="bm-grid-pagination" aria-label="Build monitor pages">
          {pages.map((_, pageIndex) => (
            <button
              key={`page-dot-${pageIndex}`}
              type="button"
              className={
                "bm-grid-pagination__dot" +
                (currentPage === pageIndex ? " bm-grid-pagination__dot--active" : "")
              }
              aria-label={`Go to page ${pageIndex + 1}`}
              aria-current={
                currentPage === pageIndex ? ("page" as const) : undefined
              }
              onClick={() => {
                setCurrentPage(pageIndex);
                scrollToPage(pageIndex);
              }}
            />
          ))}
        </div>
      )}
    </div>
  );
}

export default PagedGrid;
