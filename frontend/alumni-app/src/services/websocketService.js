import SockJS from "sockjs-client";
import { Stomp } from "@stomp/stompjs";

let stompClient = null;
let subscriptions = new Map();
let currentUserId = null;

export const connectWebSocket = (userId, onNotificationReceived) => {
  if (stompClient && stompClient.connected && userId === currentUserId) {
    if (!subscriptions.has(onNotificationReceived)) {
      const sub = stompClient.subscribe(
        `/user/${userId}/queue/notifications`,
        (message) => onNotificationReceived(JSON.parse(message.body))
      );
      subscriptions.set(onNotificationReceived, sub);
    }
    return;
  }

  if (stompClient) {
    disconnectWebSocket();
  }

  currentUserId = userId;
  const socketFactory = () =>
    new SockJS("http://localhost:8080/AlumniConnect/ws");
  stompClient = Stomp.over(socketFactory);

  stompClient.reconnectDelay = 5000;
  stompClient.heartbeatIncoming = 4000;
  stompClient.heartbeatOutgoing = 4000;

  stompClient.connect({}, () => {
    console.log("WebSocket Connected");
    console.log(`Subscribed to /user/${userId}/queue/notifications`);

    const sub = stompClient.subscribe(
      `/user/${userId}/queue/notifications`,
      (message) => {
        try {
          console.log("Full message object:", message);
          console.log("Headers:", message.headers);
          console.log("Body:", message.body);
          const notification = JSON.parse(message.body);

          if (!notification.id || !notification.message) {
            console.error("Invalid notification format:", notification);
            return;
          }

          console.log("Processed notification:", notification);
          subscriptions.forEach((_, callback) => callback(notification));
        } catch (error) {
          console.error("Error processing notification:", error);
        }
      }
    );

    subscriptions.set(onNotificationReceived, sub);
  });

  stompClient.onStompError = (error) => {
    console.error("WebSocket Error:", error);
  };
};

export const disconnectWebSocket = (callback) => {
  if (!stompClient) return;

  if (callback) {
    const sub = subscriptions.get(callback);
    if (sub) {
      sub.unsubscribe();
      subscriptions.delete(callback);
    }

    if (subscriptions.size > 0) return;
  } else {
    subscriptions.forEach((sub) => sub.unsubscribe());
    subscriptions.clear();
  }

  stompClient.disconnect();
  console.log("WebSocket Disconnected");
  stompClient = null;
  currentUserId = null;
};
