import React, { useEffect, useState } from "react";
import api from "../api/client";

const statusColors = {
  HEALTHY: "#2e7d32",
  DEGRADED: "#f9a825",
  DOWN: "#c62828",
};

export default function Services() {
  const [services, setServices] = useState([]);
  const [error, setError] = useState("");

  useEffect(() => {
    api
      .get("/services")
      .then((res) => setServices(res.data))
      .catch(() => setError("Failed to load services"));
  }, []);

  return (
    <div style={{ fontFamily: "sans-serif" }}>
      <h2>Service Catalog</h2>
      {error && <p style={{ color: "red" }}>{error}</p>}
      <table style={{ width: "100%", borderCollapse: "collapse" }}>
        <thead>
          <tr style={{ textAlign: "left", borderBottom: "2px solid #ddd" }}>
            <th style={{ padding: 8 }}>Name</th>
            <th style={{ padding: 8 }}>Owner Team</th>
            <th style={{ padding: 8 }}>Criticality</th>
            <th style={{ padding: 8 }}>Status</th>
          </tr>
        </thead>
        <tbody>
          {services.map((s) => (
            <tr key={s.id} style={{ borderBottom: "1px solid #eee" }}>
              <td style={{ padding: 8 }}>{s.name}</td>
              <td style={{ padding: 8 }}>{s.ownerTeam}</td>
              <td style={{ padding: 8 }}>{s.criticalityTier}</td>
              <td style={{ padding: 8, color: statusColors[s.status] || "#000", fontWeight: "bold" }}>
                {s.status}
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
}
