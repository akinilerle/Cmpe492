const { override, useEslintRc, useBabelRc } = require('customize-cra');

module.exports = {
  webpack: override(useEslintRc(), useBabelRc())
};
