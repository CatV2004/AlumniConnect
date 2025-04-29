import React from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import LoginPage from '../pages/LoginPage';
import HomePage from '../pages/Home';
import RegisterPage from '../pages/RegisterPage';
import Profile from '../pages/Profile';

const RoutesConfig = () => (
  <Router>
    <Routes>
      <Route path="/login" element={<LoginPage />} />
      <Route path="/home" element={<HomePage />} />
      <Route path="/register" element={<RegisterPage />} />
      <Route path="/profile/:id" element={<Profile />} />

    </Routes> 
  </Router>
);

export default RoutesConfig;
