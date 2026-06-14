import React, { useEffect, useState } from "react";
import api from "../api/client";

export default function Incidents() {
  const [incidents, setIncidents] = useState([]);
  const [services, setServices] = useState([]);
  const [error, setError] = useState("");
  const [form, setForm] = useState({ serviceId: "", severity: "P3", description: "" });

  const loadData = () => {
    api.get("/incidents").then((res) => setIncidents(res.data)).catch(() => setError("Failed to load incidents"));
    api.get("/services").then((res) => setServices(res.data)).catch(() => {});
  };

  useEffect(() => {
    loadData();
  }, []);

  const handleCreate = async (e) => {
    e.preventDefault();
    try {
      await api.post("/incidents", {
        serviceId: Number(form.serviceId),
        severity: form.severity,
        description: form.description,
      });
      setForm({ serviceId: "", severity: "P3", description: "" });
      loadData();
    } catch {
      setError("Failed to create incident");
    }
  };

  const handleResolve = async (id) => {
    await api.patch(`/incidents/${id}/resolve`);
    loadData();
  };

  return (
    <div style={{ fontFamily: "sans-serif" }}>
      <h2>Incidents</h2>
      {error && <p style={{ color: "red" }}>{error}</p>}

      <form onSubmit={handleCreate} style={{ marginBottom: 20, padding: 10, border: "1px solid #ddd" }}>
        <h4>Report New Incident</h4>
        <select
          value={form.serviceId}
          onChange={(e) => setForm({ ...form, serviceId: e.target.value })}
          required
          style={{ marginRight: 8 }}
        >
          <option value="">Select Service</option>
          {services.map((s) => (
            <option key={s.id} value={s.id}>{s.name}</option>
          ))}
        </select>
        <select
          value={form.severity}
          onChange={(e) => setForm({ ...form, severity: e.target.value })}
          style={{ marginRight: 8 }}
        >
          <option value="P1">P1</option>
          <option value="P2">P2</option>
          <option value="P3">P3</option>
          <option value="P4">P4</option>
        </select>
        <input
          placeholder="Description"
          value={form.description}
          onChange={(e) => setForm({ ...form, description: e.target.value })}
          required
          style={{ marginRight: 8, width: 300 }}
        />
        <button type="submit">Create</button>
      </form>

      <table style={{ width: "100%", borderCollapse: "collapse" }}>
        <thead>
          <tr style={{ textAlign: "left", borderBottom: "2px solid #ddd" }}>
            <th style={{ padding: 8 }}>Service</th>
            <th style={{ padding: 8 }}>Severity</th>
            <th style={{ padding: 8 }}>Status</th>
            <th style={{ padding: 8 }}>Description</th>
            <th style={{ padding: 8 }}>Reported By</th>
            <th style={{ padding: 8 }}>Action</th>
          </tr>
        </thead>
        <tbody>
          {incidents.map((i) => (
            <tr key={i.id} style={{ borderBottom: "1px solid #eee" }}>
              <td style={{ padding: 8 }}>{i.service?.name}</td>
              <td style={{ padding: 8 }}>{i.severity}</td>
              <td style={{ padding: 8 }}>{i.status}</td>
              <td style={{ padding: 8 }}>{i.description}</td>
              <td style={{ padding: 8 }}>{i.reportedBy?.username}</td>
              <td style={{ padding: 8 }}>
                {i.status !== "RESOLVED" && (
                  <button onClick={() => handleResolve(i.id)}>Resolve</button>
                )}
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
}
