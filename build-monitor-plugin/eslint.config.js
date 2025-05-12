import simpleImportSort from "eslint-plugin-simple-import-sort";
import neostandard, { plugins, resolveIgnoresFromGitignore } from "neostandard";

export default [
  ...neostandard({
    ignores: resolveIgnoresFromGitignore(),
    noStyle: true,
    ts: true,
  }),
  {
    ...plugins.react.configs.flat.recommended,
    settings: {
      react: {
        version: "detect",
      },
    },
  },
  plugins.react.configs.flat["jsx-runtime"],
  plugins.promise.configs["flat/recommended"],
  {
    plugins: {
      "simple-import-sort": simpleImportSort,
    },
    rules: {
      "simple-import-sort/imports": "error",
      "simple-import-sort/exports": "error",
    },
  },
  {
    rules: {
      "no-restricted-imports": [
        "error",
        {
          paths: [
            {
              name: "react",
              importNames: ["default"],
              message: "Please use named imports instead.",
            },
            {
              name: "react-dom/client",
              importNames: ["default"],
              message: "Please use named imports instead.",
            },
          ],
        },
      ],
    },
  },
];
