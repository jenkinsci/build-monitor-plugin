import { CSSProperties } from "react";
import TextTransition from "react-text-transition";

function Label({ text, style = {} }: { text: string; style?: CSSProperties }) {
  return (
    <TextTransition style={{ position: "relative", ...style }}>{text}</TextTransition>
  );
}

export default Label;
