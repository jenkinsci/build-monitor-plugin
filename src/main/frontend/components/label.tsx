import { CSSProperties } from "react";

function Label({ text, style = {} }: { text: string; style?: CSSProperties }) {
  // The _intention_ is to have this component seamlessly transition between states, hence pulling it out into its own component
  return <span style={{ position: "relative", ...style }}>{text}</span>;
}

export default Label;
