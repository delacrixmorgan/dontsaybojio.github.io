# Don't Say Bojio

GitHub Pages redirect hub for `dontsaybojio.com`. Each entry in `manifest.json` becomes a path that redirects to its target URL.

## How it works

`generate.js` reads `manifest.json` and writes static HTML redirect pages into `dist/`. GitHub Actions runs this on every push to `main` and deploys `dist/` to GitHub Pages.

## Adding a redirect

Edit `manifest.json` and add a slug to the `redirects` array:

```json
{ "slug": "newproject" }
```

This maps `dontsaybojio.com/newproject` → `https://delacrixmorgan.github.io/newproject.github.io/`.

For a custom target URL:

```json
{ "slug": "blog", "url": "https://medium.com/@delacrixmorgan", "label": "Blog" }
```

Push to `main` — the workflow handles the rest.

## Local development

```sh
node generate.js
```

Inspect the output in `dist/` to verify before pushing.

## One-time setup

1. Repo Settings → Pages → Source: **GitHub Actions**
2. Set custom domain to `dontsaybojio.com` in Pages settings
3. Enable **Enforce HTTPS** once DNS propagates
4. Point `dontsaybojio.com` DNS A records to GitHub Pages IPs:
   - `185.199.108.153`
   - `185.199.109.153`
   - `185.199.110.153`
   - `185.199.111.153`
