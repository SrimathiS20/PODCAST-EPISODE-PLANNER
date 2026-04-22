import "dotenv/config";
import readline from "readline";
import { runAgent } from "./agents/frontend_agent/agent.js";

const rl = readline.createInterface({
  input: process.stdin,
  output: process.stdout,
});

function ask(question) {
  return new Promise((resolve) => rl.question(question, resolve));
}

async function main() {
  console.log("\n  \u2728  PixelCraft — AI Frontend Agent  \u2728");
  console.log("  Describe any UI and I'll build a full React app you can view instantly.\n");
  console.log('  Type "exit" to quit.\n');

  let stopPreview = null;

  while (true) {
    const userRequest = await ask("You: ");

    if (userRequest.toLowerCase() === "exit") {
      if (stopPreview) stopPreview();
      console.log("Goodbye!");
      rl.close();
      break;
    }

    try {
      if (stopPreview) {
        stopPreview();
        stopPreview = null;
      }

      const result = await runAgent(userRequest);
      stopPreview = result.stop;

      console.log(`\n  File:    ${result.outputPath}`);
      console.log(`  Preview: ${result.previewUrl}\n`);
    } catch (err) {
      console.error(`\nError: ${err.message}\n`);
    }
  }
}

main();
