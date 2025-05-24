import React from "react";
import {
  BrowserRouter as Router,
  Routes,
  Route,
  Navigate,
} from "react-router-dom";
import LoginPage from "../pages/LoginPage";
import HomePage from "../pages/Home";
import RegisterPage from "../pages/RegisterPage";
import Profile from "../pages/Profile";
import Layout from "../components/layout/Layout";
import DeletedPostsPage from "../pages/DeletedPostsPage";
import SurveyPage from "../pages/SurveyPage";
import SurveyDetailPage from "../pages/SurveyDetailPage";
import SurveyStatsPage from "../pages/SurveyStatsPage";
import PrivateRoute from "../components/routing/PrivateRoute";
import ChangePasswordPage from "../pages/ChangePasswordPage";

const RoutesConfig = () => (
  <Router>
    <Routes>
      <Route path="/" element={<Layout />}>
        <Route path="/" element={<Navigate to="/home" replace />} />
        <Route index element={<HomePage />} />
        <Route path="/home" element={<HomePage />} />
        <Route path="/survey" element={<SurveyPage />} />
        <Route path="/survey/:postId" element={<SurveyDetailPage />} />
        <Route path="/profile/:id" element={<Profile />} />
        <Route path="/deleted-posts" element={<DeletedPostsPage />} />
        <Route path="/change-password" element={<ChangePasswordPage />} />
        <Route
          path="/surveys/stats/:surveyPostId"
          element={
            <PrivateRoute roles={["ADMIN"]} element={<SurveyStatsPage />} />
          }
        />
      </Route>

      <Route path="/login" element={<LoginPage />} />
      <Route path="/register" element={<RegisterPage />} />
    </Routes>
  </Router>
);

export default RoutesConfig;
