import axios from "axios";

const API = axios.create({
  baseURL: import.meta.env.VITE_API_URL,
});

export const getRankedCandidates = (jobId) =>
  API.get(`/api/applications/job/${jobId}/ranked`);

export const getInterviewQuestions = (applicationId) =>
  API.get(`/api/applications/${applicationId}/interview-questions`);