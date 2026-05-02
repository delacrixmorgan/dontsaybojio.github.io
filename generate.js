import { readFileSync, mkdirSync, writeFileSync } from 'fs';
import { join, dirname } from 'path';
import { fileURLToPath } from 'url';

const __dirname = dirname(fileURLToPath(import.meta.url));
const manifest = JSON.parse(readFileSync(join(__dirname, 'manifest.json'), 'utf8'));
const { config, redirects } = manifest;

const cssDir = join(__dirname, 'css');
const lightCss   = readFileSync(join(cssDir, 'light.css'), 'utf8');
const lightMcCss = readFileSync(join(cssDir, 'light-mc.css'), 'utf8');
const lightHcCss = readFileSync(join(cssDir, 'light-hc.css'), 'utf8');
const darkCss    = readFileSync(join(cssDir, 'dark.css'), 'utf8');
const darkMcCss  = readFileSync(join(cssDir, 'dark-mc.css'), 'utf8');
const darkHcCss  = readFileSync(join(cssDir, 'dark-hc.css'), 'utf8');

const themeTokens = `
${lightCss}
${darkCss}
@media (prefers-contrast: more) {
${lightMcCss}
${darkMcCss}
}
@media (prefers-contrast: forced) {
${lightHcCss}
${darkHcCss}
}`;
const outDir = join(__dirname, 'dist');

mkdirSync(outDir, { recursive: true });
writeFileSync(join(outDir, 'CNAME'), 'dontsaybojio.com');
writeFileSync(join(outDir, '.nojekyll'), '');

if (config.bluesky_did) {
  mkdirSync(join(outDir, '.well-known'), { recursive: true });
  writeFileSync(join(outDir, '.well-known', 'atproto-did'), config.bluesky_did);
}

const resolved = redirects.map(entry => ({
  slug: entry.slug,
  label: entry.label ?? entry.slug,
  targetUrl: entry.url ?? `https://${config.github_username}.github.io/${entry.slug}.github.io/`,
}));

for (const { slug, label, targetUrl } of resolved) {
  mkdirSync(join(outDir, slug), { recursive: true });
  writeFileSync(join(outDir, slug, 'index.html'), redirectPage(label, targetUrl));
  console.log(`  /${slug}/ → ${targetUrl}`);
}

writeFileSync(join(outDir, 'index.html'), rootPage(config, resolved));
console.log(`Built ${resolved.length} redirect(s) into dist/`);

function redirectPage(label, targetUrl) {
  return `<!DOCTYPE html>
<html lang="en" class="light">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Redirecting to ${label}…</title>
  <link rel="canonical" href="${targetUrl}">
  <meta http-equiv="refresh" content="0; url=${targetUrl}">
  <script>window.location.replace("${targetUrl}");</script>
  <script>document.documentElement.classList.replace('light', matchMedia('(prefers-color-scheme:dark)').matches ? 'dark' : 'light');</script>
  <style>
    ${themeTokens}
    body { font-family: system-ui, sans-serif; display: flex; align-items: center;
           justify-content: center; min-height: 100vh; margin: 0;
           background: var(--md-sys-color-background); color: var(--md-sys-color-on-background); }
    a { color: var(--md-sys-color-primary); }
  </style>
</head>
<body>
  <p>Redirecting to <a href="${targetUrl}">${label}</a>…</p>
</body>
</html>
`;
}

function rootPage(config, redirects) {
  const cards = redirects.map(({ slug, label }) => `
    <a class="card" href="/${slug}/">
      <span class="card-label">${label}</span>
      <span class="card-arrow">→</span>
    </a>`
  ).join('\n');

  const githubUrl = `https://github.com/${config.github_username}`;
  const blueskyUrl = config.bluesky_handle
    ? `https://bsky.app/profile/${config.bluesky_handle}`
    : null;

  const socialLinks = [
    `<a class="social" href="${githubUrl}" target="_blank" rel="noopener noreferrer">GitHub</a>`,
    blueskyUrl
      ? `<a class="social" href="${blueskyUrl}" target="_blank" rel="noopener noreferrer">Bluesky</a>`
      : '',
  ].filter(Boolean).join('\n    ');

  return `<!DOCTYPE html>
<html lang="en" class="light">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>${config.site_title}</title>
  <meta name="description" content="${config.site_description}">
  <script>(function(){var s=localStorage.getItem('theme')||(matchMedia('(prefers-color-scheme:dark)').matches?'dark':'light');document.documentElement.classList.replace('light',s);})();</script>
  <style>
    ${themeTokens}

    *, *::before, *::after { box-sizing: border-box; margin: 0; padding: 0; }

    body {
      font-family: 'Inter', system-ui, -apple-system, sans-serif;
      background: var(--md-sys-color-background);
      color: var(--md-sys-color-on-background);
      min-height: 100vh;
      display: flex;
      flex-direction: column;
    }

    header {
      padding: 5rem 1.5rem 4rem;
      max-width: 720px;
      width: 100%;
      margin: 0 auto;
    }

    .eyebrow {
      font-size: 0.75rem;
      font-weight: 600;
      letter-spacing: 0.12em;
      text-transform: uppercase;
      color: var(--md-sys-color-primary);
      margin-bottom: 1.25rem;
    }

    h1 {
      font-size: clamp(2.5rem, 8vw, 5rem);
      font-weight: 800;
      letter-spacing: -0.02em;
      line-height: 1.05;
      color: var(--md-sys-color-on-background);
      margin-bottom: 1.25rem;
    }

    h1 span { color: var(--md-sys-color-primary); }

    .tagline {
      font-size: clamp(1rem, 2.5vw, 1.2rem);
      color: var(--md-sys-color-on-surface-variant);
      line-height: 1.6;
      max-width: 46ch;
      margin-bottom: 2.25rem;
    }

    .social-row { display: flex; flex-wrap: wrap; gap: 0.75rem; }

    .social {
      display: inline-flex;
      align-items: center;
      padding: 0.45rem 1rem;
      border: 1.5px solid var(--md-sys-color-primary);
      border-radius: 999px;
      color: var(--md-sys-color-primary);
      font-size: 0.85rem;
      font-weight: 600;
      text-decoration: none;
      letter-spacing: 0.01em;
      transition: background 150ms ease, color 150ms ease;
    }

    .social:hover {
      background: var(--md-sys-color-primary-container);
      color: var(--md-sys-color-on-primary-container);
    }

    main {
      flex: 1;
      padding: 0 1.5rem 5rem;
      max-width: 720px;
      width: 100%;
      margin: 0 auto;
    }

    .section-title {
      font-size: 0.75rem;
      font-weight: 600;
      letter-spacing: 0.12em;
      text-transform: uppercase;
      color: var(--md-sys-color-on-surface-variant);
      margin-bottom: 1.25rem;
      padding-bottom: 0.75rem;
      border-bottom: 1px solid var(--md-sys-color-outline-variant);
    }

    .cards {
      display: grid;
      grid-template-columns: repeat(auto-fill, minmax(260px, 1fr));
      gap: 1rem;
    }

    .card {
      display: flex;
      align-items: center;
      justify-content: space-between;
      padding: 1.25rem 1.5rem;
      background: var(--md-sys-color-surface-container-low);
      border: 1px solid var(--md-sys-color-outline-variant);
      border-radius: 12px;
      text-decoration: none;
      color: var(--md-sys-color-on-surface);
      transition: transform 150ms ease, box-shadow 150ms ease, border-color 150ms ease;
    }

    .card:hover {
      transform: translateY(-3px);
      box-shadow: 0 8px 32px color-mix(in srgb, var(--md-sys-color-primary) 12%, transparent);
      border-color: var(--md-sys-color-primary);
    }

    .card-label { font-size: 1.05rem; font-weight: 600; letter-spacing: -0.01em; }

    .card-arrow {
      font-size: 1.1rem;
      color: var(--md-sys-color-primary);
      transition: transform 150ms ease;
    }

    .card:hover .card-arrow { transform: translateX(3px); }

    footer {
      padding: 1.5rem;
      text-align: center;
      font-size: 0.75rem;
      color: var(--md-sys-color-on-surface-variant);
      border-top: 1px solid var(--md-sys-color-outline-variant);
    }

    .theme-toggle {
      position: fixed;
      top: 1.25rem;
      right: 1.25rem;
      display: flex;
      align-items: center;
      justify-content: center;
      width: 2.25rem;
      height: 2.25rem;
      border: 1.5px solid var(--md-sys-color-outline-variant);
      border-radius: 999px;
      background: var(--md-sys-color-surface-container-low);
      color: var(--md-sys-color-on-surface-variant);
      cursor: pointer;
      transition: border-color 150ms ease, background 150ms ease, color 150ms ease;
      z-index: 100;
    }

    .theme-toggle:hover {
      border-color: var(--md-sys-color-primary);
      color: var(--md-sys-color-primary);
    }

    .theme-toggle svg { width: 1rem; height: 1rem; fill: currentColor; }

    .theme-toggle .icon-sun  { display: none; }
    .theme-toggle .icon-moon { display: block; }
    .dark .theme-toggle .icon-sun  { display: block; }
    .dark .theme-toggle .icon-moon { display: none; }

    @media (max-width: 480px) {
      header { padding: 3.5rem 1.25rem 3rem; }
      main   { padding: 0 1.25rem 4rem; }
      .cards { grid-template-columns: 1fr; }
    }
  </style>
</head>
<body>
  <button class="theme-toggle" aria-label="Toggle dark/light mode" onclick="(function(){var h=document.documentElement,n=h.classList.contains('dark')?'light':'dark';h.classList.replace(h.classList.contains('dark')?'dark':'light',n);localStorage.setItem('theme',n);})()">
    <svg class="icon-sun" viewBox="0 0 24 24" aria-hidden="true"><path d="M12 17a5 5 0 1 0 0-10 5 5 0 0 0 0 10zm0-13a1 1 0 0 0 1-1V2a1 1 0 1 0-2 0v1a1 1 0 0 0 1 1zm0 16a1 1 0 0 0-1 1v1a1 1 0 1 0 2 0v-1a1 1 0 0 0-1-1zM4.22 5.64a1 1 0 0 0 1.42-1.42L4.93 3.51a1 1 0 0 0-1.42 1.42l.71.71zm14.14 12.72a1 1 0 0 0-1.42 1.42l.71.71a1 1 0 0 0 1.42-1.42l-.71-.71zM3 12a1 1 0 0 0-1-1H1a1 1 0 1 0 0 2h1a1 1 0 0 0 1-1zm20 0a1 1 0 0 0-1-1h-1a1 1 0 1 0 0 2h1a1 1 0 0 0 1-1zM4.93 20.49l.71-.71a1 1 0 0 0-1.42-1.42l-.71.71a1 1 0 0 0 1.42 1.42zm14.14-16.98a1 1 0 0 0 1.42-1.42l-.71-.71a1 1 0 0 0-1.42 1.42l.71.71z"/></svg>
    <svg class="icon-moon" viewBox="0 0 24 24" aria-hidden="true"><path d="M21 12.79A9 9 0 0 1 11.21 3a7 7 0 1 0 9.79 9.79z"/></svg>
  </button>
  <header>
    <p class="eyebrow">Portfolio</p>
    <h1>${config.site_title.replace("Don't", "<span>Don't</span>")}</h1>
    <p class="tagline">${config.tagline ?? config.site_description}</p>
    <nav class="social-row" aria-label="Social links">
    ${socialLinks}
    </nav>
  </header>

  <main>
    <p class="section-title">Projects</p>
    <div class="cards">
${cards}
    </div>
  </main>

  <footer>
    <p>&copy; ${new Date().getFullYear()} ${config.github_username}</p>
  </footer>
</body>
</html>
`;
}
