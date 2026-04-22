import https from "https";
import fs from "fs";
import path from "path";
import { fileURLToPath } from "url";
import { buildPrompt, AGENT_NAME } from "./prompt.js";
import { writeCodeToFile } from "../../tools/code_writer.js";
import { serve } from "../../tools/preview_server.js";

const __dirname = path.dirname(fileURLToPath(import.meta.url));
const MEMORY_PATH = path.join(__dirname, "memory.json");

function loadMemory() {
  try {
    const data = fs.readFileSync(MEMORY_PATH, "utf-8");
    return data ? JSON.parse(data) : [];
  } catch {
    return [];
  }
}

function saveMemory(memory) {
  fs.writeFileSync(MEMORY_PATH, JSON.stringify(memory, null, 2), "utf-8");
}

function stripCodeFences(text) {
  return text
    .replace(/^```[\w]*\n?/, "")
    .replace(/\n?```$/, "")
    .trim();
}

function callDial(model, messages, apiKey, baseURL) {
  return new Promise((resolve, reject) => {
    const url = new URL(
      `${baseURL}/${model}/chat/completions?api-version=2024-02-01`
    );

    const postData = JSON.stringify({ messages, temperature: 0.2 });

    const options = {
      hostname: url.hostname,
      port: 443,
      path: url.pathname + url.search,
      method: "POST",
      headers: {
        "Content-Type": "application/json",
        "api-key": apiKey,
        "Content-Length": Buffer.byteLength(postData),
      },
      rejectUnauthorized: false,
    };

    const req = https.request(options, (res) => {
      let data = "";
      res.on("data", (chunk) => (data += chunk));
      res.on("end", () => {
        if (res.statusCode >= 400) {
          reject(new Error(`EPAM Dial API error ${res.statusCode}: ${data}`));
        } else {
          try {
            resolve(JSON.parse(data));
          } catch (e) {
            reject(new Error(`Failed to parse response: ${data}`));
          }
        }
      });
    });

    req.on("error", (e) => reject(new Error(`Request failed: ${e.message}`)));
    req.write(postData);
    req.end();
  });
}

/**
 * PixelCraft Agent — pluggable API for multi-agent systems.
 *
 * @param {string} userRequest - Description of the UI to generate.
 * @param {object} [options] - Optional overrides.
 * @param {string} [options.apiKey]    - EPAM Dial API key (defaults to env).
 * @param {string} [options.apiUrl]    - EPAM Dial base URL (defaults to env).
 * @param {string} [options.model]     - Model name (defaults to env).
 * @param {string} [options.outputDir] - Where to write files (defaults to ./output).
 * @param {boolean} [options.preview]  - Auto-serve and open browser (default true).
 *
 * @returns {Promise<{ agentName, filename, outputPath, previewUrl, generatedCode, stop }>}
 */
export async function runAgent(userRequest, options = {}) {
  if (!userRequest || !userRequest.trim()) {
    throw new Error("A user request is required");
  }

  const apiKey = options.apiKey || process.env.DIAL_API_KEY;
  const dialBase =
    options.apiUrl ||
    process.env.DIAL_API_URL ||
    "https://ai-proxy.lab.epam.com/openai/deployments";
  const model = options.model || process.env.DIAL_MODEL || "gpt-4o";
  const outputDir = options.outputDir || "./output";
  const shouldPreview = options.preview !== undefined ? options.preview : true;

  if (!apiKey) {
    throw new Error("DIAL_API_KEY is required (pass via options or env)");
  }

  const prompt = buildPrompt(userRequest);

  console.log(`[${AGENT_NAME}] Generating UI for: "${userRequest}"`);
  console.log(`[${AGENT_NAME}] Using model: ${model}`);

  const response = await callDial(
    model,
    [{ role: "user", content: prompt }],
    apiKey,
    dialBase
  );

  const generatedCode = stripCodeFences(
    response.choices[0].message.content ?? ""
  );

  // Use .html extension for self-contained files
  const filename =
    userRequest
      .toLowerCase()
      .replace(/[^a-z0-9]+/g, "_")
      .replace(/^_|_$/g, "")
      .slice(0, 40) + ".html";

  // Save to memory
  const memory = loadMemory();
  memory.push({
    timestamp: new Date().toISOString(),
    request: userRequest,
    filename,
    model,
  });
  saveMemory(memory);

  // Write file
  const outputPath = writeCodeToFile(filename, generatedCode, outputDir);

  console.log(`[${AGENT_NAME}] Wrote: ${outputPath}`);

  // Auto-serve and open browser
  let previewUrl = null;
  let stop = () => {};

  if (shouldPreview) {
    const preview = await serve(outputPath);
    previewUrl = preview.url;
    stop = preview.stop;
    console.log(`[${AGENT_NAME}] Preview: ${previewUrl}`);
  }

  console.log(`[${AGENT_NAME}] Done.`);

  return {
    agentName: AGENT_NAME,
    filename,
    outputPath,
    previewUrl,
    generatedCode,
    stop,
  };
}