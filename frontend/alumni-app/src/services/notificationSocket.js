import { Client } from "@stomp/stompjs";
import { addNotification } from "../features/notifications/notificationSlice";
import SockJS from "sockjs-client";

let stompClient = null;

export const connectNotificationSocket = (dispatch, token) => {
  stompClient = new Client({
    webSocketFactory: () =>
      new SockJS("http://localhost:8080/AlumniConnect/ws"),
    reconnectDelay: 5000,
    heartbeatIncoming: 0,
    heartbeatOutgoing: 0,
    connectHeaders: {
      Authorization: token,
    },
    onConnect: () => {
      console.log("✅ WebSocket connected!");
      const notificationSound = new Audio("/sounds/notification.wav");
      stompClient.subscribe(`/user/queue/notifications`, (message) => {
        const notification = JSON.parse(message.body);
        console.log("📩 Notification received:", notification);
        
        notificationSound
          .play()
          .catch((err) => console.error("⚠️ Không thể phát âm thanh:", err));

        dispatch(addNotification(notification));
      });
    },
    onWebSocketError: (error) => {
      console.error("Error with websocket", error);
    },
    onStompError: (frame) => {
      console.error("Broker reported error: " + frame.headers["message"]);
      console.error("Additional details: " + frame.body);
    },
    debug: function (str) {
      console.log("STOMP DEBUG:", str);
    },
    onWebSocketClose: (event) => {
      console.log("WebSocket closed:", event);
    },
  });

  stompClient.activate();
};

export const disconnectNotificationSocket = () => {
  if (stompClient) {
    stompClient.deactivate();
  }
};
