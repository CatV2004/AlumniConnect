import React, {  } from 'react';
import RoutesConfig from './app/routes';
// import { useDispatch, useSelector } from 'react-redux';
// import { fetchCurrentUser } from './features/auth/authSlice';
import AuthProvider from './features/auth/AuthProvider';

const App = () => {
  // const dispatch = useDispatch();
  // const token = useSelector((state) => state.auth.token);

  // useEffect(() => {
  //   if (token) {
  //     dispatch(fetchCurrentUser());
  //   }
  // }, [token, dispatch]);
  return (
    <AuthProvider>
      <RoutesConfig />
    </AuthProvider>
  );
};

export default App;
