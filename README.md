![CI](https://github.com/snehamandal1001/ai-recruiter-platform/actions/workflows/ci.yml/badge.svg)

# AI Recruiter Platform

AI-powered resume screening, candidate ranking, and interview question generation — built to solve the real problem of recruiters spending hours manually screening resumes.

## ✨ Features

- **Resume upload & parsing** — accepts PDF/DOCX/TXT, extracts text with Apache Tika
- **LLM-powered skill extraction** — automatically identifies technical and soft skills from resume text
- **ATS scoring** — calculates a match score between candidate skills and job requirements
- **Candidate ranking** — recruiters see applicants sorted highest-match first, per job
- **AI-generated interview questions** — tailored questions based on the specific candidate and role
- **Recruiter dashboard** — clickable job list with live-ranked candidates and visual score bars

## 🛠️ Tech Stack

**Backend:** Spring Boot 4, Java 21, PostgreSQL, Apache Tika, Groq (Llama 3) API
**Frontend:** React, Vite
**DevOps:** Docker, Docker Compose, GitHub Actions CI

## 📦 Getting Started

### Option 1: Docker (recommended — one command, no manual installs)

```bash
git clone https://github.com/snehamandal1001/ai-recruiter-platform.git
cd ai-recruiter-platform
cp .env.example .env   # fill in your own DB password + Groq API key (free at console.groq.com)
docker compose up --build
```

Visit `http://localhost:3000`.

### Option 2: Run locally
See [backend setup](#) and [frontend setup](#) — requires Java 21, Node 20, and PostgreSQL installed locally.

## 🧪 Running Tests

```bash
cd backend
./mvnw test
```

## 📖 API Reference

| Endpoint | Method | Description |
|---|---|---|
| `/api/candidates/upload` | POST | Upload a resume file, extract skills via LLM |
| `/api/jobs` | GET/POST | List or create job postings |
| `/api/applications/apply` | POST | Apply a candidate to a job, calculates ATS score |
| `/api/applications/job/{jobId}/ranked` | GET | Get candidates for a job, ranked by score |
| `/api/applications/{applicationId}/interview-questions` | GET | Generate AI interview questions |

## 🗺️ Future Enhancements

- Candidate-facing dashboard (view own applications and scores)
- Recruiter/candidate authentication (JWT-based)
- Semantic (embedding-based) skill matching instead of exact string comparison
- Refactor duplicated LLM HTTP-call logic in `LlmService` into a shared private method
- Integration tests with Testcontainers for real database-backed test coverage

## 📄 License

MIT — see [LICENSE](LICENSE)
## 🚀 Live Demo

**Frontend:** https://ai-recruiter-platform-navy.vercel.app
**Backend API:** https://ai-recruiter-platform.onrender.com/api/jobs

> Note: the backend is on Render's free tier, which spins down after inactivity — the first request may take 30-50 seconds to wake up.
