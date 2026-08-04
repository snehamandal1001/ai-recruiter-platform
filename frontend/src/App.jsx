import { useState } from "react";
import JobList from "./components/JobList";
import RankedCandidates from "./components/RankedCandidates";
import "./App.css";

function App() {
  const [selectedJobId, setSelectedJobId] = useState(null);

  return (
    <div style={{ maxWidth: "1000px", margin: "0 auto", padding: "2rem" }}>
      <h1>AI Recruiter Platform</h1>
      <div style={{ display: "grid", gridTemplateColumns: "1fr 1fr", gap: "2rem" }}>
        <JobList onSelectJob={setSelectedJobId} selectedJobId={selectedJobId} />
        <RankedCandidates jobId={selectedJobId} />
      </div>
    </div>
  );
}

export default App;