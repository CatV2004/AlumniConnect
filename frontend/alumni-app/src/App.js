// import React, { useEffect } from "react";
// import RoutesConfig from "./app/routes";
// import "react-toastify/dist/ReactToastify.css";
// import AuthProvider from "./features/auth/AuthProvider";
// import { ToastContainer } from "react-toastify";
// import {
//   connectWebSocket,
//   disconnectWebSocket,
// } from "./services/websocketService";
// import { useDispatch, useSelector } from "react-redux";
// import { addNotification } from "./features/notifications/notificationSlice";

// const App = () => {
//   const dispatch = useDispatch();
//   const currentUser = useSelector((state) => state.auth.user);

//   useEffect(() => {
//     if (!currentUser?.id) return;

//     const handleNotification = (notification) => {
//       console.log("Nhận thông báo mới:", notification);
//       dispatch(addNotification(notification));
//     };

//     connectWebSocket(currentUser.id, handleNotification);

//     return () => {
//       disconnectWebSocket(handleNotification);
//     };
//   }, [currentUser?.id, dispatch]);
//   return (
//     <>
//       <ToastContainer position="top-right" autoClose={3000} />
//       <AuthProvider>
//         <RoutesConfig />
//       </AuthProvider>
//     </>
//   );
// };

// export default App;

import RoutesConfig from "./app/routes";
import "react-toastify/dist/ReactToastify.css";
import AuthProvider from "./features/auth/AuthProvider";
import { ToastContainer } from "react-toastify";

const App = () => {
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
