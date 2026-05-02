import { readFileSync, mkdirSync, writeFileSync } from 'fs';
import { join, dirname } from 'path';
import { fileURLToPath } from 'url';

const __dirname = dirname(fileURLToPath(import.meta.url));
const manifest = JSON.parse(readFileSync(join(__dirname, 'manifest.json'), 'utf8'));
const { config, redirects } = manifest;
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
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Redirecting to ${label}…</title>
  <link rel="canonical" href="${targetUrl}">
  <meta http-equiv="refresh" content="0; url=${targetUrl}">
  <script>window.location.replace("${targetUrl}");</script>
  <style>
    body { font-family: system-ui, sans-serif; display: flex; align-items: center;
           justify-content: center; min-height: 100vh; margin: 0;
           background: #0d1117; color: #e6edf3; }
    a { color: #58a6ff; }
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
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>${config.site_title}</title>
  <meta name="description" content="${config.site_description}">
  <style>
    *, *::before, *::after { box-sizing: border-box; margin: 0; padding: 0; }

    body {
      font-family: 'Inter', system-ui, -apple-system, sans-serif;
      background: #0d1117;
      color: #e6edf3;
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
      color: #a78bfa;
      margin-bottom: 1.25rem;
    }

    h1 {
      font-size: clamp(2.5rem, 8vw, 5rem);
      font-weight: 800;
      letter-spacing: -0.02em;
      line-height: 1.05;
      color: #e6edf3;
      margin-bottom: 1.25rem;
    }

    h1 span { color: #a78bfa; }

    .tagline {
      font-size: clamp(1rem, 2.5vw, 1.2rem);
      color: #8b949e;
      line-height: 1.6;
      max-width: 46ch;
      margin-bottom: 2.25rem;
    }

    .social-row { display: flex; flex-wrap: wrap; gap: 0.75rem; }

    .social {
      display: inline-flex;
      align-items: center;
      padding: 0.45rem 1rem;
      border: 1.5px solid #a78bfa;
      border-radius: 999px;
      color: #a78bfa;
      font-size: 0.85rem;
      font-weight: 600;
      text-decoration: none;
      letter-spacing: 0.01em;
      transition: background 150ms ease, color 150ms ease;
    }

    .social:hover { background: #a78bfa; color: #0d1117; }

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
      color: #8b949e;
      margin-bottom: 1.25rem;
      padding-bottom: 0.75rem;
      border-bottom: 1px solid #21262d;
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
      background: #161b22;
      border: 1px solid #30363d;
      border-radius: 12px;
      text-decoration: none;
      color: #e6edf3;
      transition: transform 150ms ease, box-shadow 150ms ease, border-color 150ms ease;
    }

    .card:hover {
      transform: translateY(-3px);
      box-shadow: 0 8px 32px rgba(167, 139, 250, 0.12);
      border-color: #a78bfa;
    }

    .card-label { font-size: 1.05rem; font-weight: 600; letter-spacing: -0.01em; }

    .card-arrow {
      font-size: 1.1rem;
      color: #a78bfa;
      transition: transform 150ms ease;
    }

    .card:hover .card-arrow { transform: translateX(3px); }

    footer {
      padding: 1.5rem;
      text-align: center;
      font-size: 0.75rem;
      color: #484f58;
      border-top: 1px solid #21262d;
    }

    @media (max-width: 480px) {
      header { padding: 3.5rem 1.25rem 3rem; }
      main   { padding: 0 1.25rem 4rem; }
      .cards { grid-template-columns: 1fr; }
    }
  </style>
</head>
<body>
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
