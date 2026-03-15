import "./app.scss";

import { createRoot } from "react-dom/client";

import Container from "./components/container";
import PageDescription from "./components/page-description.tsx";
import { UseConfirmationProvider } from "./context/confirmation-provider.tsx";
import { JobsProvider } from "./context/jobs-provider.tsx";
import { UseNotificationProvider } from "./context/notification-provider.tsx";
import { UserPreferencesProvider } from "./context/user-preference-provider.tsx";

const rootElement = document.getElementById("app");
if (!rootElement) throw new Error("Failed to find the 'app' element");

const root = createRoot(rootElement);

root.render(
  <UseNotificationProvider>
    <UseConfirmationProvider>
      <UserPreferencesProvider
        monitorId={rootElement.dataset.buildMonitorId!}
        defaults={{
          textSize: Number(rootElement.dataset.appearanceTextSize),
          maximumNumberOfColumns: Number(
            rootElement.dataset.appearanceMaximumNumberOfColumns,
          ),
          colorBlindMode:
            rootElement.dataset.appearanceColorBlindMode === "true",
          showBadges: rootElement.dataset.appearanceShowBadges === "true",
        }}
      >
        <JobsProvider>
          <Container />
          <PageDescription
            description={rootElement.dataset.buildMonitorDescription}
          />
        </JobsProvider>
      </UserPreferencesProvider>
    </UseConfirmationProvider>
    ,
  </UseNotificationProvider>,
);
