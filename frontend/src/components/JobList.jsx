import { useEffect, useState } from "react";
import { getJobs } from "../api/jobs";

function hashHue(str) {
  let hash = 0;
  for (let i = 0; i < str.length; i++) hash = str.charCodeAt(i) + ((hash << 5) - hash);
  return Math.abs(hash) % 360;
}

function JobIcon({ title }) {
  const hue = hashHue(title || "?");
  const bg = `linear-gradient(135deg, hsl(${hue},70%,55%), hsl(${(hue + 45) % 360},70%,45%))`;
  return <div className="job-icon" style={{ background: bg }}>{(title || "?")[0]}</div>;
}

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

  if (loading) return <p className="empty-state">Loading jobs...</p>;
  if (error) return <p className="empty-state" style={{ color: "var(--bad)" }}>{error}</p>;

  return (
    <div>
      <h2>Open Roles</h2>
      {jobs.length === 0 && <p className="empty-state">No jobs posted yet.</p>}
      {jobs.map((job) => (
        <button
          key={job.id}
          className={`job-tab ${selectedJobId === job.id ? "active" : ""}`}
          onClick={() => onSelectJob(job.id)}
        >
          <div className="job-tab-head">
            <JobIcon title={job.title} />
            <span className="title">{job.title}</span>
          </div>
          <span className="desc">{job.description}</span>
          <span className="skills">{job.requiredSkills?.join(" · ") || "—"}</span>
        </button>
      ))}
    </div>
  );
}

export default JobList;