import { useState } from "react";
import JobList from "./components/JobList";
import RankedCandidates from "./components/RankedCandidates";
import "./App.css";

function App() {
  const [selectedJobId, setSelectedJobId] = useState(null);

  return (
    <div className="app-shell">
      <header className="app-header">
        <span className="mark">AI Recruiter</span>
        <span className="tagline">Screening &amp; ranking, automated</span>
      </header>
      <main className="app-grid">
        <div className="rail">
          <JobList onSelectJob={setSelectedJobId} selectedJobId={selectedJobId} />
        </div>
        <div className="board">
          <RankedCandidates jobId={selectedJobId} />
        </div>
      </main>
    </div>
  );
}

export default App;