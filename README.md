# Don't Say Bojio

GitHub Pages redirect hub for `dontsaybojio.com`. Each entry in `manifest.json` becomes a path that redirects to its target URL.

## How it works

`generate.js` reads `manifest.json` and writes static HTML redirect pages into `dist/`. GitHub Actions runs this on every push to `main` and deploys `dist/` to GitHub Pages.

## Adding a redirect

Edit `manifest.json` and add an entry to the `redirects` array. Each entry specifies a
`slug`, a `label`, and the target `url`:

```json
{ "slug": "blog", "label": "Blog", "url": "https://medium.com/@delacrixmorgan" }
```

This maps `dontsaybojio.com/blog` → the given `url`. Keeping `url` explicit on every entry
means the target is always visible and doesn't depend on repo naming.

If `url` is omitted, `generate.js` falls back to
`https://delacrixmorgan.github.io/{slug}.github.io/` — which only resolves when the GitHub
repo is named exactly `{slug}.github.io`. Prefer an explicit `url` rather than relying on
this fallback.

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
