const path = require("path");
const MiniCssExtractPlugin = require("mini-css-extract-plugin");

module.exports = {
  entry: "./src/main/webapp/less/index.less",
  module: {
    rules: [
      {
        test: /\.less$/,
        use: [
          MiniCssExtractPlugin.loader,
          "css-loader",
          "less-loader",
        ],
      },
    ],
  },
  plugins: [new MiniCssExtractPlugin({filename: "index.css"})],
  output: {
    path: path.resolve(__dirname, "src/main/webapp/styles"),
  }
};

