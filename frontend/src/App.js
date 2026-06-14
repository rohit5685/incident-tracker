import React from "react";
import { BrowserRouter, Routes, Route, Link, Navigate, useNavigate } from "react-router-dom";
import Login from "./pages/Login";
import Services from "./pages/Services";
import Incidents from "./pages/Incidents";

function NavBar() {
  const navigate = useNavigate();
  const username = localStorage.getItem("username");

  const handleLogout = () => {
    localStorage.clear();
    navigate("/login");
  };

  return (
    <nav style={{ padding: 10, borderBottom: "1px solid #ddd", marginBottom: 20, fontFamily: "sans-serif" }}>
      <Link to="/services" style={{ marginRight: 15 }}>Services</Link>
      <Link to="/incidents" style={{ marginRight: 15 }}>Incidents</Link>
      {username && (
        <span style={{ float: "right" }}>
          Logged in as <b>{username}</b>{" "}
          <button onClick={handleLogout}>Logout</button>
        </span>
      )}
    </nav>
  );
}

function PrivateRoute({ children }) {
  const token = localStorage.getItem("token");
  return token ? children : <Navigate to="/login" />;
}

export default function App() {
  return (
    <BrowserRouter>
      <NavBar />
      <div style={{ padding: "0 20px" }}>
        <Routes>
          <Route path="/login" element={<Login />} />
          <Route path="/services" element={<PrivateRoute><Services /></PrivateRoute>} />
          <Route path="/incidents" element={<PrivateRoute><Incidents /></PrivateRoute>} />
          <Route path="/" element={<Navigate to="/services" />} />
        </Routes>
      </div>
    </BrowserRouter>
  );
}
