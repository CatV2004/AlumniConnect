import { useEffect } from 'react';
import { useSelector, useDispatch } from 'react-redux';
import { 
  setNotifications, 
  markAsRead as markAsReadAction,
  markAllAsRead as markAllAsReadAction
} from '../features/notifications/notificationSlice';
import { 
  connectNotificationSocket, 
  disconnectNotificationSocket 
} from '../services/notificationSocket';
import NotificationService from '../services/notificationService';

const useNotifications = () => {
  const dispatch = useDispatch();
  const { user, token } = useSelector(state => state.auth);
  const notifications = useSelector(state => state.notifications);

  useEffect(() => {
    if (user) {
      // Load initial notifications
      NotificationService.getUserNotifications()
        .then(data => dispatch(setNotifications(data)));
      
      // Connect to WebSocket
      connectNotificationSocket(dispatch, token);
      
      return () => {
        disconnectNotificationSocket();
      };
    }
  }, [user, dispatch]);

  const handleMarkAsRead = (id) => {
    NotificationService.markAsRead(id)
      .then(() => dispatch(markAsReadAction(id)));
  };

  const handleMarkAllAsRead = () => {
    NotificationService.markAllAsRead()
      .then(() => dispatch(markAllAsReadAction()));
  };

  return {
    notifications,
    handleMarkAsRead,
    handleMarkAllAsRead,
  };
};

export default useNotifications;