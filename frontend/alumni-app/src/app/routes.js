import React from "react";
import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import LoginPage from "../pages/LoginPage";
import HomePage from "../pages/Home";
import RegisterPage from "../pages/RegisterPage";
import Profile from "../pages/Profile";
import Layout from "../components/layout/Layout";

const RoutesConfig = () => (
  <Router>
    <Routes>
      
      <Route path="/" element={<Layout />}>
        <Route index element={<HomePage />} /> 
        <Route path="home" element={<HomePage />} />
        <Route path="profile/:id" element={<Profile />} />
      </Route>

      <Route path="/login" element={<LoginPage />} />
      <Route path="/register" element={<RegisterPage />} />
    
    </Routes>
  </Router>
);

export default RoutesConfig;
