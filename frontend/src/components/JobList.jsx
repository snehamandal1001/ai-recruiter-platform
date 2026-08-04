import { useEffect, useState } from "react";
import { getJobs } from "../api/jobs";

function JobList({ onSelectJob, selectedJobId }) {
  const [jobs, setJobs] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    getJobs()
      .then((response) => {
        setJobs(response.data);
        setLoading(false);
      })
      .catch((err) => {
        console.error(err);
        setError("Failed to load jobs. Is the backend running?");
        setLoading(false);
      });
  }, []);

  if (loading) return <p>Loading jobs...</p>;
  if (error) return <p style={{ color: "red" }}>{error}</p>;

  return (
    <div>
      <h2>Open Positions</h2>
      {jobs.length === 0 && <p>No jobs posted yet.</p>}
      <ul style={{ listStyle: "none", padding: 0 }}>
        {jobs.map((job) => (
          <li
            key={job.id}
            onClick={() => onSelectJob(job.id)}
            style={{
              marginBottom: "0.75rem",
              padding: "1rem",
              border: selectedJobId === job.id ? "2px solid #4f46e5" : "1px solid #ddd",
              borderRadius: "8px",
              cursor: "pointer",
              backgroundColor: selectedJobId === job.id ? "#eef2ff" : "white",
            }}
          >
            <strong>{job.title}</strong>
            <p style={{ margin: "0.25rem 0", color: "#555" }}>{job.description}</p>
            <small>Required: {job.requiredSkills?.join(", ") || "—"}</small>
          </li>
        ))}
      </ul>
    </div>
  );
}

export default JobList;