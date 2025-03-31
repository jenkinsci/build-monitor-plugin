import * as React from "react";
import * as ReactDOMClient from "react-dom/client";
import Container from "./components/container";
import "./app.scss";

const rootElement = document.getElementById("graph");
if (!rootElement) throw new Error("Failed to find the 'graph' element");

const root = ReactDOMClient.createRoot(rootElement);

root.render(<Container />);
