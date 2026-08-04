import JobList from "./components/JobList";
import "./App.css";

function App() {
  return (
    <div style={{ maxWidth: "600px", margin: "0 auto", padding: "2rem" }}>
      <h1>AI Recruiter Platform</h1>
      <JobList />
    </div>
  );
}

export default App;