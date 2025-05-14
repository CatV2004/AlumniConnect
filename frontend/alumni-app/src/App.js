import React from "react";
import RoutesConfig from "./app/routes";
import "react-toastify/dist/ReactToastify.css";
// import { useDispatch, useSelector } from 'react-redux';
// import { fetchCurrentUser } from './features/auth/authSlice';
import AuthProvider from "./features/auth/AuthProvider";
import { ToastContainer } from "react-toastify";

const App = () => {
  // const dispatch = useDispatch();
  // const token = useSelector((state) => state.auth.token);

  // useEffect(() => {
  //   if (token) {
  //     dispatch(fetchCurrentUser());
  //   }
  // }, [token, dispatch]);
  return (
    <>
      <ToastContainer position="top-right" autoClose={3000} />
      <AuthProvider>
        <RoutesConfig />
      </AuthProvider>
    </>
  );
};

export default App;
