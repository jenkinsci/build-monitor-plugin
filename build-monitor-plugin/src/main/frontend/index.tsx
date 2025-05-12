import "./app.scss";

import { createRoot } from "react-dom/client";

import Container from "./components/container";
import PageDescription from "./components/page-description.tsx";
import { UseDialogProvider } from "./context/dialog-provider.tsx";
import { JobsProvider } from "./context/jobs-provider.tsx";
import { UserPreferencesProvider } from "./context/user-preference-provider.tsx";

const rootElement = document.getElementById("app");
if (!rootElement) throw new Error("Failed to find the 'graph' element");

const root = createRoot(rootElement);

root.render(
  <UseDialogProvider>
    <UserPreferencesProvider monitorId={rootElement.dataset.buildMonitorId!}>
      <JobsProvider>
        <Container />
        <PageDescription
          description={rootElement.dataset.buildMonitorDescription}
        />
      </JobsProvider>
    </UserPreferencesProvider>
  </UseDialogProvider>,
);
