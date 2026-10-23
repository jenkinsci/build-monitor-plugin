import DOMPurify from "dompurify";

type BuildDescriptionProps = {
  description: string;
};

const BuildDescription = ({ description }: BuildDescriptionProps) => (
  <div
    className="bm-cell__description"
    dangerouslySetInnerHTML={{
      __html: DOMPurify.sanitize(description, {
        ALLOWED_TAGS: [
          "a",
          "abbr",
          "b",
          "blockquote",
          "br",
          "code",
          "dd",
          "del",
          "div",
          "dl",
          "dt",
          "em",
          "h1",
          "h2",
          "h3",
          "h4",
          "h5",
          "h6",
          "hr",
          "i",
          "ins",
          "li",
          "ol",
          "p",
          "pre",
          "s",
          "small",
          "span",
          "strong",
          "sub",
          "sup",
          "u",
          "ul",
        ],
        ALLOWED_ATTR: ["href", "title"],
      }),
    }}
  />
);

export default BuildDescription;
