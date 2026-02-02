# Tailwind CSS Setup

## Current Status

The application is currently using Tailwind CSS via CDN (included in the layout templates). This works for development but should be replaced with a compiled CSS file for production.

## Setting Up Tailwind CSS Compilation

### Prerequisites
- Node.js and npm installed
- Tailwind CSS CLI

### Steps

1. **Install dependencies:**
   ```bash
   npm install
   ```

2. **Compile Tailwind CSS:**
   ```bash
   npm run build-css
   ```

   This will compile `src/main/resources/static/css/application.css` with all Tailwind utilities.

3. **For development with auto-rebuild:**
   ```bash
   npm run watch-css
   ```

4. **Update templates:**
   Once compiled, update `layouts/admin.html` and `layouts/application.html` to use:
   ```html
   <link rel="stylesheet" th:href="@{/css/application.css}">
   ```
   Instead of the CDN script tag.

## Files Created

- `tailwind.config.js` - Tailwind configuration
- `package.json` - npm scripts for building CSS
- `src/main/resources/static/css/application.css` - Tailwind source file (with @tailwind directives)

## Production Build

For production, ensure Tailwind CSS is compiled before building the JAR:
```bash
npm run build-css
./gradlew build
```
