import express from "express";
import helmet from "helmet";
import rateLimit from "express-rate-limit";
import { GoogleGenAI } from "@google/genai";

const app = express();
app.disable("x-powered-by");
app.use(helmet());
app.use(express.json({ limit: "32kb" }));

const port = Number(process.env.PORT || 3000);
const apiKey = process.env.GEMINI_API_KEY;
const accessToken = process.env.USHORT_APP_TOKEN;
const model = process.env.GEMINI_MODEL || "gemini-3.8-flash";

if (!apiKey) console.error("Missing GEMINI_API_KEY environment variable.");
if (!accessToken) console.error("Missing USHORT_APP_TOKEN environment variable.");

const ai = apiKey ? new GoogleGenAI({ apiKey }) : null;
const limiter = rateLimit({ windowMs: 60_000, limit: 20, standardHeaders: "draft-8", legacyHeaders: false });

app.get("/health", (_req, res) => {
  res.status(200).json({ ok: true, service: "u-short-gemini", model });
});

app.post("/api/chat", limiter, async (req, res) => {
  try {
    if (!apiKey || !accessToken || !ai) {
      return res.status(503).json({ error: "Server is not configured. Add GEMINI_API_KEY and USHORT_APP_TOKEN." });
    }

    const supplied = (req.get("authorization") || "").replace(/^Bearer\\s+/i, "");
    if (!supplied || supplied !== accessToken) {
      return res.status(401).json({ error: "Invalid app access token." });
    }

    const message = typeof req.body?.message === "string" ? req.body.message.trim() : "";
    if (!message) return res.status(400).json({ error: "message is required." });
    if (message.length > 8000) return res.status(413).json({ error: "Message is too long (max 8000 characters)." });

    const prompt = [
      "You are U SHORT AI, a helpful professional assistant for short-form video creators.",
      "Help with scripts, storyboards, hooks, captions, titles, editing plans, color grading advice, audio, and creator strategy.",
      "Reply in the same language as the user. If they write Egyptian Arabic, reply naturally in Egyptian Arabic.",
      "Be practical and concise. Never claim to have edited, rendered, uploaded, or published a video unless a connected tool actually did it.",
      "Respect copyright and platform rules. Do not promise virality.",
      "",
      "User request:",
      message
    ].join("\\n");

    const interaction = await ai.interactions.create({
      model,
      input: prompt
    });

    const reply = (interaction.output_text || "").trim();
    if (!reply) return res.status(502).json({ error: "Gemini returned an empty response. Please retry." });

    return res.status(200).json({ reply, model });
  } catch (error) {
    console.error("Gemini request failed:", error);
    return res.status(502).json({ error: "Gemini request failed. Check server logs and model availability." });
  }
});

app.listen(port, () => console.log(`U SHORT Gemini API listening on port ${port}`));
