import { useCallback, useEffect } from "react";
import {
  connectWebSocket,
  disconnectWebSocket,
} from "../services/websocketService";
import { useDispatch, useSelector } from "react-redux";
import {
  addNotification,
  markAsRead,
} from "../features/notifications/notificationSlice";

export const useNotifications = () => {
  const dispatch = useDispatch();
  const currentUser = useSelector((state) => state.auth.user);
  const notifications = useSelector((state) => state.notifications.list);

  const handleNewNotification = useCallback(
    (notification) => {
      dispatch(addNotification(notification));
      if (!notification.read) {
        const audio = new Audio("/notification-sound.wav");
        audio.play().catch((e) => console.log("Audio play error:", e));
      }
    },
    [dispatch]
  );

  useEffect(() => {
    if (!currentUser?.id) return;

    connectWebSocket(currentUser.id, handleNewNotification);

    return () => {
      disconnectWebSocket(handleNewNotification);
    };
  }, [currentUser?.id, handleNewNotification]);


  const handleMarkAsRead = (notificationId) => {
    dispatch(markAsRead(notificationId));
  };

  return {
    notifications,
    handleMarkAsRead,
  };
};
