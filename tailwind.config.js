/** @type {import('tailwindcss').Config} */
module.exports = {
  content: [
    './src/main/resources/templates/**/*.html',
  ],
  safelist: [
    {
      pattern: /.*/,
    },
  ],
  theme: {
    extend: {},
  },
  plugins: [],
}
