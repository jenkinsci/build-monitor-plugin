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
        "app":
          "src/main/frontend/index.tsx",
      },
      output: {
        entryFileNames: "[name].js",
        dir: "src/main/webapp/js/bundles",
      },
    },
  },
});
