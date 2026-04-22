import fs from "fs";
import path from "path";

/**
 * Writes code content to a file in the specified output directory.
 * Creates the directory if it doesn't exist.
 */
export function writeCodeToFile(filename, code, outputDir = "./output") {
  if (!filename || !code) {
    throw new Error("filename and code are required");
  }

  const sanitized = path.basename(filename);
  if (!sanitized) {
    throw new Error("Invalid filename");
  }

  fs.mkdirSync(outputDir, { recursive: true });

  const filePath = path.join(outputDir, sanitized);
  fs.writeFileSync(filePath, code, "utf-8");

  console.log(`[code_writer] Wrote file: ${filePath}`);
  return filePath;
}
