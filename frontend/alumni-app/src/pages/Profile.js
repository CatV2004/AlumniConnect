import React, { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import { getUserProfile, getUserPosts } from "../services/userService";
import ProfileHeader from "../components/Profile/ProfileHeader";
import ProfileTabs from "../components/Profile/ProfileTabs";
import ProfileTimeline from "../components/Profile/ProfileTimeline";
import ProfileAbout from "../components/Profile/ProfileAbout";
import ProfilePhotos from "../components/Profile/ProfilePhotos";
import { useSelector } from "react-redux";
import Navbar from "../components/layout/Navbar";
import { ChatService } from "../services/chatService";
import ChatContainer from "../components/Message/ChatContainer";

const Profile = () => {
  const { id } = useParams();
  const { token, user } = useSelector((state) => state.auth);
  console.log("user info: ", user)
  // const [user, setUser] = useState(null);

  const [posts, setPosts] = useState([]);
  const [page, setPage] = useState(0);
  const [hasMore, setHasMore] = useState(true);
  const [activeTab, setActiveTab] = useState("timeline");
  const [loading, setLoading] = useState(false);

  const loadPosts = async (pageNumber, reset = false) => {
    try {
      const response = await getUserPosts(id, token, pageNumber);
      setPosts((prev) =>
        reset ? response.content : [...prev, ...response.content]
      );
      setHasMore(!response.last);
    } catch (error) {
      console.error("Error loading posts:", error);
    }
  };

  useEffect(() => {
    if (token) {
      // getUserProfile(id, token).then(setUser);
      setPage(0);
      loadPosts(0, true);
      console.log("Current posts:", posts);
    }
  }, [id, token]);

  const handleLoadMore = () => {
    const nextPage = page + 1;
    setPage(nextPage);
    return loadPosts(nextPage);
  };

  if (!user)
    return (
      <div className="max-w-4xl mx-auto mt-6 space-y-4">
        <div className="h-48 bg-gray-200 animate-pulse rounded-xl" />
        <div className="h-12 bg-gray-200 animate-pulse rounded-xl" />
      </div>
    );

  return (
    <div className="max-w-4xl mx-auto">
      <ProfileHeader user={user} />
      <ProfileTabs activeTab={activeTab} setActiveTab={setActiveTab} />

      {activeTab === "timeline" && (
        <ProfileTimeline
          posts={posts}
          loadMore={handleLoadMore}
          hasMore={hasMore}
        />
      )}

      {activeTab === "about" && <ProfileAbout user={user} />}

      {activeTab === "photos" && <ProfilePhotos posts={posts} />}
    </div>
  );
};

export default Profile;
