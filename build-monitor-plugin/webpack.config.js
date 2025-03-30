const path = require('path');

const wrapWithDefaultModule = (config) => {
  return {
    devtool: 'source-map',
    module: {
      rules: [
        {
          test: /\.tsx?$/,
          exclude: /node_modules/,
          use: {
            loader: 'ts-loader',
          },
        },
        {
          test: /\.scss$/i,
          use: ['style-loader', 'css-loader', 'sass-loader'],
        },
      ]
    },
    resolve: {
      extensions: ['.ts', '.tsx'],
    },
    ...config
  }
}

module.exports = [
  wrapWithDefaultModule({
    entry: './src/main/frontend/',
    output: {
      path: path.resolve(__dirname, 'src/main/webapp/js/bundles'),
      filename: 'app.js',
    }
  }),
];
