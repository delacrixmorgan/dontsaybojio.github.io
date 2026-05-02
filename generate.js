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
  const items = redirects.map(({ slug, label, targetUrl }) =>
    `    <li><a href="/${slug}/">${label}</a><span class="target"> → ${targetUrl}</span></li>`
  ).join('\n');

  return `<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>${config.site_title}</title>
  <meta name="description" content="${config.site_description}">
  <style>
    body { font-family: system-ui, sans-serif; max-width: 600px; margin: 4rem auto;
           padding: 0 1.5rem; background: #0d1117; color: #e6edf3; }
    h1 { font-size: 1.5rem; margin-bottom: 0.25rem; }
    p  { color: #8b949e; margin-top: 0; }
    ul { list-style: none; padding: 0; }
    li { margin: 0.75rem 0; }
    a  { color: #58a6ff; text-decoration: none; font-size: 1.1rem; }
    a:hover { text-decoration: underline; }
    .target { color: #8b949e; font-size: 0.85rem; margin-left: 0.5rem; }
  </style>
</head>
<body>
  <h1>${config.site_title}</h1>
  <p>${config.site_description}</p>
  <ul>
${items}
  </ul>
</body>
</html>
`;
}
