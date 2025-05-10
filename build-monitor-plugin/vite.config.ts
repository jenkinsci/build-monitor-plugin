import react from "@vitejs/plugin-react";
import { defineConfig } from "vite";
import cssInjectedByJsPlugin from "vite-plugin-css-injected-by-js";

export default defineConfig({
  base: "./",
  plugins: [
    react(),
    cssInjectedByJsPlugin({
      relativeCSSInjection: true,
    }),
  ],
  build: {
    sourcemap: true,
    cssCodeSplit: true,
    rollupOptions: {
      input: {
        "pipeline-console-view":
          "src/main/frontend/index.tsx",
      },
      output: {
        entryFileNames: "[name]-bundle.js",
        dir: "src/main/webapp/js/bundles",
      },
    },
  },
});
