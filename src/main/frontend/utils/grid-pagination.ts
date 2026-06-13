const BASE_CELL_HEIGHT_PX = 208;
const ROW_GAP_ALLOWANCE_PX = 16;
const MAXIMUM_ROWS_PER_PAGE = 4;
const MINIMUM_CELL_HEIGHT_PX = 160;

interface RowsPerPageOptions {
  viewportHeight: number;
  textSize: number;
}

export function getColumnCount(
  jobCount: number,
  maximumNumberOfColumns: number,
) {
  if (jobCount <= 0) {
    return 0;
  }

  const safeMaximumColumns = Number.isFinite(maximumNumberOfColumns)
    ? Math.max(1, Math.floor(maximumNumberOfColumns))
    : 1;

  return Math.min(jobCount, safeMaximumColumns);
}

export function getRowsPerPage({
  viewportHeight,
  textSize,
}: RowsPerPageOptions) {
  if (viewportHeight <= 0) {
    return 1;
  }

  const safeTextSize = Number.isFinite(textSize) ? Math.max(0.1, textSize) : 1;
  const minimumCellHeight = Math.max(
    MINIMUM_CELL_HEIGHT_PX,
    Math.round(BASE_CELL_HEIGHT_PX * safeTextSize),
  );

  return Math.max(
    1,
    Math.min(
      MAXIMUM_ROWS_PER_PAGE,
      Math.floor(
        (viewportHeight + ROW_GAP_ALLOWANCE_PX) /
          (minimumCellHeight + ROW_GAP_ALLOWANCE_PX),
      ),
    ),
  );
}

export function paginateItems<T>(
  items: T[],
  columns: number,
  rowsPerPage: number,
) {
  const pageSize = Math.max(1, columns * rowsPerPage);
  const pages: T[][] = [];

  for (let index = 0; index < items.length; index += pageSize) {
    pages.push(items.slice(index, index + pageSize));
  }

  return pages;
}
