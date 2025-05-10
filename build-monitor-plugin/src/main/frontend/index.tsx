import * as React from "react";
import * as ReactDOMClient from "react-dom/client";
import Container from "./components/container";
import "./app.scss";
import { UserPreferencesProvider } from "./providers/user-preference-provider.tsx";

const rootElement = document.getElementById("app");
if (!rootElement) throw new Error("Failed to find the 'graph' element");

const root = ReactDOMClient.createRoot(rootElement);

root.render(
  <UserPreferencesProvider monitorId="123">
    <Container />
  </UserPreferencesProvider>,
);
