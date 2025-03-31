import * as React from "react";
import * as ReactDOMClient from "react-dom/client";
import Stageview from "./components/Stageview";
import "./app.scss";
import OptionsButton from "./components/options-button";

const rootElement = document.getElementById("graph");
if (!rootElement) throw new Error("Failed to find the 'graph' element");

const root = ReactDOMClient.createRoot(rootElement);

root.render(
  <>
    <Stageview />
    <OptionsButton />
  </>
);
