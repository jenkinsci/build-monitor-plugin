import * as React from "react";
import * as ReactDOMClient from "react-dom/client";
import Container from "./components/container";
import "./app.scss";
import { UserPreferencesProvider } from "./context/user-preference-provider.tsx";
import { UseDialogProvider } from "./context/dialog-provider.tsx";
import PageDescription from "./components/page-description.tsx";
import { JobsProvider } from "./context/jobs-provider.tsx";

const rootElement = document.getElementById("app");
if (!rootElement) throw new Error("Failed to find the 'graph' element");

const root = ReactDOMClient.createRoot(rootElement);

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
