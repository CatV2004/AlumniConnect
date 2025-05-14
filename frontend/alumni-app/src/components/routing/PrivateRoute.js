import { Navigate } from "react-router-dom";
import { useSelector } from "react-redux";

const PrivateRoute = ({ element, roles }) => {
  const { role } = useSelector((state) => state.auth);

  if (!role || (roles && !roles.includes(role))) {
    return <Navigate to="/login" replace />;
  }

  return element;
};

export default PrivateRoute;
