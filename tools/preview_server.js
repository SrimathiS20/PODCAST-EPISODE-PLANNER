import http from "http";
import fs from "fs";
import path from "path";
import { exec } from "child_process";

let server = null;

/**
 * Serves a directory on a local port and opens the browser.
 * Returns { url, stop } — call stop() to shut down.
 */
export function serve(filePath, port = 3111) {
  return new Promise((resolve, reject) => {
    // Stop any previous server
    if (server) {
      server.close();
      server = null;
    }

    const absPath = path.resolve(filePath);
    if (!fs.existsSync(absPath)) {
      return reject(new Error(`File not found: ${absPath}`));
    }

    const html = fs.readFileSync(absPath, "utf-8");

    server = http.createServer((req, res) => {
      res.writeHead(200, { "Content-Type": "text/html; charset=utf-8" });
      res.end(html);
    });

    server.listen(port, () => {
      const url = `http://localhost:${port}`;
      console.log(`[preview] Serving at ${url}`);

      // Open browser
      const cmd =
        process.platform === "win32"
          ? `start ${url}`
          : process.platform === "darwin"
          ? `open ${url}`
          : `xdg-open ${url}`;
      exec(cmd);

      resolve({
        url,
        stop: () => {
          server.close();
          server = null;
        },
      });
    });

    server.on("error", (err) => {
      if (err.code === "EADDRINUSE") {
        // Try next port
        resolve(serve(filePath, port + 1));
      } else {
        reject(err);
      }
    });
  });
}
