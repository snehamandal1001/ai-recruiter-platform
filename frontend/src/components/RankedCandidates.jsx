import { useEffect, useState } from "react";
import { getRankedCandidates, getInterviewQuestions } from "../api/applications";

function hashHue(str) {
  let hash = 0;
  for (let i = 0; i < str.length; i++) hash = str.charCodeAt(i) + ((hash << 5) - hash);
  return Math.abs(hash) % 360;
}

function initials(name) {
  return name.split(" ").map((p) => p[0]).slice(0, 2).join("").toUpperCase();
}

function Avatar({ name }) {
  const hue = hashHue(name || "?");
  const bg = `linear-gradient(135deg, hsl(${hue},70%,55%), hsl(${(hue + 45) % 360},70%,45%))`;
  return <div className="avatar" style={{ background: bg }}>{initials(name || "?")}</div>;
}

function scoreTier(score) {
  if (score >= 70) return "good";
  if (score >= 40) return "warn";
  return "bad";
}

function ScoreStamp({ score }) {
  return (
    <div className={`stamp ${scoreTier(score)}`}>
      <span className="value">{Math.round(score)}%</span>
    </div>
  );
}

function SkillTag({ skill }) {
  const hue = hashHue(skill);
  const style = {
    background: `hsl(${hue}, 75%, 95%)`,
    borderColor: `hsl(${hue}, 60%, 85%)`,
    color: `hsl(${hue}, 55%, 32%)`,
  };
  return <span className="tag" style={style}>{skill}</span>;
}

function InterviewQuestions({ applicationId }) {
  const [questions, setQuestions] = useState(null);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);
  const [visible, setVisible] = useState(false);

  const handleClick = () => {
    if (questions) {
      setVisible((v) => !v);
      return;
    }
    setLoading(true);
    setError(null);
    getInterviewQuestions(applicationId)
      .then((response) => {
        setQuestions(response.data);
        setVisible(true);
        setLoading(false);
      })
      .catch((err) => {
        console.error(err);
        setError("Failed to generate questions.");
        setLoading(false);
      });
  };

  return (
    <div>
      <button className={`iq-toggle ${visible ? "open" : ""}`} onClick={handleClick} disabled={loading}>
        {loading ? "Generating…" : visible ? "Hide Questions" : "Generate Interview Questions"}
      </button>
      {error && <p className="iq-error">{error}</p>}
      {visible && questions && (
        <ol className="iq-list">
          {questions.map((q, i) => <li key={i}>{q}</li>)}
        </ol>
      )}
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

  if (!jobId) return <p className="empty-state">Select a role to review its candidates.</p>;
  if (loading) return <p className="empty-state">Loading candidates...</p>;
  if (error) return <p className="empty-state" style={{ color: "var(--bad)" }}>{error}</p>;
  if (applications.length === 0) return <p className="empty-state">No applications for this role yet.</p>;

  return (
    <div>
      <h2>Ranked Candidates</h2>
      {applications.map((app) => (
        <div key={app.id} className="dossier">
          <Avatar name={app.candidate.fullName} />
          <div className="dossier-main">
            <p className="name">{app.candidate.fullName}</p>
            <p className="email">{app.candidate.email}</p>
            <div className="tag-row">
              {(app.candidate.extractedSkills || []).map((skill, i) => (
                <SkillTag key={i} skill={skill} />
              ))}
            </div>
            <InterviewQuestions applicationId={app.id} />
          </div>
          <ScoreStamp score={app.atsScore} />
        </div>
      ))}
    </div>
  );
}

export default RankedCandidates;