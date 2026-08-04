import { useEffect, useState } from "react";
import { getJobs } from "../api/jobs";

function JobList() {
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
      <ul>
        {jobs.map((job) => (
          <li key={job.id} style={{ marginBottom: "1rem" }}>
            <strong>{job.title}</strong>
            <p>{job.description}</p>
            <small>Posted by: {job.recruiterEmail}</small>
          </li>
        ))}
      </ul>
    </div>
  );
}

export default JobList;