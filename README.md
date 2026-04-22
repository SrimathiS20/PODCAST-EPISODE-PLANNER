# PixelCraft — AI Frontend Agent

PixelCraft is a pluggable AI agent that generates **fully functional React web apps** from natural language descriptions. It outputs a self-contained HTML file, auto-serves it on a local port, and opens your browser — so you see your UI instantly.

## Features

- **One function call** — describe a UI, get a running website
- **Self-contained output** — generates a single HTML file with React 18 via CDN (no build step)
- **Live preview** — auto-starts an HTTP server and opens the browser
- **Pluggable** — designed to be called by other agents in a multi-agent system
- **Memory** — logs every generation to `memory.json`

## Project Structure

```
├── app.js                          # CLI entry point (interactive REPL)
├── agents/
│   └── frontend_agent/
│       ├── agent.js                # Core agent — calls EPAM Dial, writes file, serves preview
│       ├── prompt.js               # System prompt & branding
│       └── memory.json             # Generation history
├── tools/
│   ├── code_writer.js              # File writer utility
│   └── preview_server.js           # HTTP server for live preview
├── .env.example                    # Environment template (copy to .env)
└── package.json
```

## Quick Start

### 1. Clone & install

```bash
git clone <your-repo-url>
cd gen-ai-kata
npm install
```

### 2. Configure

```bash
cp .env.example .env
```

Edit `.env` and add your EPAM Dial API key:

```
DIAL_API_KEY=your-api-key-here
```

### 3. Run (CLI mode)

```bash
npm start
```

Type a UI description and PixelCraft will generate it, save to `output/`, and open the preview in your browser.

## Using as a Pluggable Agent

Other agents can call PixelCraft with a single import:

```js
import "dotenv/config";
import { runAgent } from "./agents/frontend_agent/agent.js";

const result = await runAgent("A dashboard with user stats and charts");

console.log(result.previewUrl);   // http://localhost:3111
console.log(result.outputPath);   // output/a_dashboard_with_user_stats_and_char.html
console.log(result.generatedCode); // Full HTML source

// Stop the preview server when done
result.stop();
```

### `runAgent(request, options?)` — API

| Parameter | Type | Default | Description |
|---|---|---|---|
| `request` | `string` | *required* | Natural language description of the UI |
| `options.apiKey` | `string` | `process.env.DIAL_API_KEY` | EPAM Dial API key |
| `options.apiUrl` | `string` | `process.env.DIAL_API_URL` | EPAM Dial base URL |
| `options.model` | `string` | `process.env.DIAL_MODEL` | Model name |
| `options.outputDir` | `string` | `"./output"` | Where to save generated files |
| `options.preview` | `boolean` | `true` | Auto-serve and open browser |

### Return value

```js
{
  agentName: "PixelCraft",
  filename: "a_dashboard_with_user_stats.html",
  outputPath: "output/a_dashboard_with_user_stats.html",
  previewUrl: "http://localhost:3111",   // null if preview=false
  generatedCode: "<!DOCTYPE html>...",
  stop: Function                         // call to shut down preview server
}
```

## Requirements

- Node.js 18+
- EPAM Dial API key (get from [EPAM Dial](https://chat.lab.epam.com))
