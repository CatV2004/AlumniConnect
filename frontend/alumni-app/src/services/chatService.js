import { db } from "../app/firebaseConfig";
import {
  collection,
  doc,
  setDoc,
  getDoc,
  addDoc,
  query,
  orderBy,
  onSnapshot,
  serverTimestamp,
  where,
  writeBatch,
  updateDoc,
  getDocs,
} from "firebase/firestore";

export const ChatService = {
  /**
   * Lấy danh sách các cuộc trò chuyện của người dùng
   * @param {string} userId - ID của người dùng hiện tại
   * @param {function} callback - Hàm callback khi có dữ liệu mới
   * @returns {function} Hàm unsubscribe
   */
  subscribeToUserChats: (userId, callback) => {
    const chatsRef = collection(db, "chats");
    const q = query(
      chatsRef,
      where("participants", "array-contains", userId),
      orderBy("lastMessageTime", "desc")
    );

    return onSnapshot(q, (snapshot) => {
      const chats = snapshot.docs.map((doc) => ({
        id: doc.id,
        ...doc.data(),
        lastMessageTime: doc.data().lastMessageTime?.toDate()?.getTime() || 0,
      }));
      callback(chats);
    });
  },

  /**
   * Lắng nghe tin nhắn trong một cuộc trò chuyện cụ thể
   * @param {string} chatId - ID của cuộc trò chuyện
   * @param {function} callback - Hàm callback khi có tin nhắn mới
   * @returns {function} Hàm unsubscribe
   */
  subscribeToMessages: (chatId, callback) => {
    const messagesRef = collection(db, "chats", chatId, "messages");
    const q = query(messagesRef, orderBy("timestamp", "asc"));

    return onSnapshot(q, (snapshot) => {
      const messages = snapshot.docs.map((doc) => ({
        id: doc.id,
        ...doc.data(),
        timestamp: doc.data().timestamp?.toDate()?.getTime() || Date.now(),
      }));
      callback(messages);
    });
  },

  /**
   * Gửi tin nhắn mới
   * @param {string} chatId - ID của cuộc trò chuyện
   * @param {object} message - Đối tượng tin nhắn
   * @param {string} message.senderId - ID người gửi
   * @param {string} message.text - Nội dung tin nhắn
   * @param {string} [message.avatar] - Avatar người gửi
   * @returns {Promise<object>} Kết quả gửi tin nhắn
   */
  sendMessage: async (chatId, message) => {
    try {
      const messagesRef = collection(db, "chats", chatId, "messages");
      const chatRef = doc(db, "chats", chatId);

      const newMessage = {
        ...message,
        timestamp: serverTimestamp(),
        status: "sent",
        seen: false,
      };

      // Thêm tin nhắn mới
      const docRef = await addDoc(messagesRef, newMessage);
      const participants = (await getDoc(chatRef)).data().participants;
      const receiverId = participants.find((id) => id !== message.senderId);
      // Cập nhật thông tin cuộc trò chuyện
      await updateDoc(chatRef, {
        lastMessage: message.text || "[Media]",
        lastMessageTime: serverTimestamp(),
        lastSenderId: message.senderId,
        [`participantInfo.${receiverId}.hasUnread`]: true,
      });

      return {
        success: true,
        messageId: docRef.id,
      };
    } catch (error) {
      console.error("Error sending message:", error);
      return {
        success: false,
        error: error.message,
      };
    }
  },

  /**
   * Tạo hoặc lấy thông tin cuộc trò chuyện giữa 2 người dùng
   * @param {string} currentUserId - ID người dùng hiện tại
   * @param {string} otherUserId - ID người dùng còn lại
   * @returns {Promise<object>} Kết quả tạo/lấy cuộc trò chuyện
   */
  getOrCreateChat: async (currentUserId, otherUserId) => {
    try {
      const chatId = [currentUserId, otherUserId].sort().join("_");
      const chatRef = doc(db, "chats", chatId);

      const chatDoc = await getDoc(chatRef);

      if (!chatDoc.exists()) {
        await setDoc(chatRef, {
          participants: [currentUserId, otherUserId],
          createdAt: serverTimestamp(),
          lastMessage: null,
          lastMessageTime: null,
          lastSenderId: null,
          participantInfo: {
            [currentUserId]: { hasUnread: true },
            [otherUserId]: { hasUnread: true },
          },
        });
      }

      return {
        success: true,
        chatId,
      };
    } catch (error) {
      console.error("Error getting or creating chat:", error);
      return {
        success: false,
        error: error.message,
      };
    }
  },

  /**
   * Đánh dấu tin nhắn đã đọc
   * @param {string} chatId - ID cuộc trò chuyện
   * @param {string} userId - ID người dùng hiện tại
   * @returns {Promise<object>} Kết quả cập nhật
   */
  markMessagesAsRead: async (chatId, userId) => {
    try {
      const messagesRef = collection(db, "chats", chatId, "messages");
      const q = query(
        messagesRef,
        where("senderId", "!=", userId),
        where("seen", "==", false)
      );

      const snapshot = await getDocs(q);
      const batch = writeBatch(db);

      snapshot.forEach((doc) => {
        batch.update(doc.ref, {
          seen: true,
          seenAt: serverTimestamp(),
        });
      });

      // Cập nhật trạng thái unread trong chat
      const chatRef = doc(db, "chats", chatId);
      batch.update(chatRef, {
        [`participantInfo.${userId}.hasUnread`]: false,
      });

      await batch.commit();
      return { success: true };
    } catch (error) {
      console.error("Error marking messages as read:", error);
      return { success: false };
    }
  },

  /**
   * Lấy số tin nhắn chưa đọc cho tất cả cuộc trò chuyện
   * @param {string} userId - ID người dùng hiện tại
   * @returns {Promise<object>} Kết quả với số lượng tin nhắn chưa đọc
   */
  getTotalUnreadCount: async (userId) => {
    try {
      const chatsRef = collection(db, "chats");
      const q = query(
        chatsRef,
        where("participants", "array-contains", userId)
      );

      const snapshot = await getDocs(q);
      let totalUnread = 0;

      snapshot.forEach((doc) => {
        const chatData = doc.data();
        if (chatData.participantInfo?.[userId]?.hasUnread) {
          totalUnread++;
        }
      });

      return { success: true, count: totalUnread };
    } catch (error) {
      console.error("Error getting total unread count:", error);
      return { success: false, error: error.message };
    }
  },
};
