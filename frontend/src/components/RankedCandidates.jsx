import { useEffect, useState } from "react";
import { getRankedCandidates } from "../api/applications";

function ScoreBar({ score }) {
  const color = score >= 70 ? "#16a34a" : score >= 40 ? "#f59e0b" : "#dc2626";
  return (
    <div style={{ background: "#e5e7eb", borderRadius: "6px", height: "10px", width: "100%" }}>
      <div
        style={{
          width: `${score}%`,
          background: color,
          height: "100%",
          borderRadius: "6px",
          transition: "width 0.3s ease",
        }}
      />
    </div>
  );
}

function RankedCandidates({ jobId }) {
  const [applications, setApplications] = useState([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

  useEffect(() => {
    if (!jobId) return;
    setLoading(true);
    setError(null);
    getRankedCandidates(jobId)
      .then((response) => {
        setApplications(response.data);
        setLoading(false);
      })
      .catch((err) => {
        console.error(err);
        setError("Failed to load candidates for this job.");
        setLoading(false);
      });
  }, [jobId]);

  if (!jobId) return <p style={{ color: "#888" }}>Select a job to see ranked candidates.</p>;
  if (loading) return <p>Loading candidates...</p>;
  if (error) return <p style={{ color: "red" }}>{error}</p>;
  if (applications.length === 0) return <p>No applications for this job yet.</p>;

  return (
    <div>
      <h2>Ranked Candidates</h2>
      {applications.map((app) => (
        <div
          key={app.id}
          style={{
            border: "1px solid #ddd",
            borderRadius: "8px",
            padding: "1rem",
            marginBottom: "0.75rem",
          }}
        >
          <div style={{ display: "flex", justifyContent: "space-between", alignItems: "center" }}>
            <strong>{app.candidate.fullName}</strong>
            <span style={{ fontWeight: "bold" }}>{app.atsScore.toFixed(0)}%</span>
          </div>
          <p style={{ margin: "0.25rem 0", color: "#555", fontSize: "0.9rem" }}>
            {app.candidate.email}
          </p>
          <ScoreBar score={app.atsScore} />
          <p style={{ marginTop: "0.5rem", fontSize: "0.85rem", color: "#666" }}>
            Skills: {app.candidate.extractedSkills?.join(", ") || "—"}
          </p>
        </div>
      ))}
    </div>
  );
}

export default RankedCandidates;