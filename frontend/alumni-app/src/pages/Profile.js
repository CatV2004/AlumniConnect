import React, { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import { getUserProfile, getUserPosts } from "../services/userService";
import ProfileHeader from "../components/Profile/ProfileHeader";
import ProfileTabs from "../components/Profile/ProfileTabs";
import ProfileTimeline from "../components/Profile/ProfileTimeline";
import ProfileAbout from "../components/Profile/ProfileAbout";
import ProfilePhotos from "../components/Profile/ProfilePhotos";
import { useSelector } from "react-redux";
import CreatePostBar from "../components/PostForm/CreatePostBar";

const Profile = () => {
  const { id } = useParams();
  const { token, user: currentUser } = useSelector((state) => state.auth);
  const [profileUser, setProfileUser] = useState(null);
  const [posts, setPosts] = useState([]);
  const [page, setPage] = useState(0);
  const [hasMore, setHasMore] = useState(true);
  const [activeTab, setActiveTab] = useState("timeline");
  const [loading, setLoading] = useState(false);
  const [initialLoad, setInitialLoad] = useState(true);

  const loadPosts = async (pageNumber, reset = false) => {
    try {
      setLoading(true);
      const response = await getUserPosts(id, token, pageNumber, 5); 
      setPosts((prev) =>
        reset ? response.content : [...prev, ...response.content]
      );
      setHasMore(!response.last);
      setInitialLoad(false);
    } catch (error) {
      console.error("Error loading posts:", error);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    if (token) {
      const fetchProfile = async () => {
        try {
          const userData = await getUserProfile(id, token);
          setProfileUser(userData);
        } catch (error) {
          console.error("Error fetching profile:", error);
        }
      };
      
      fetchProfile();
      setPage(0);
      loadPosts(0, true);
    }
  }, [id, token]);

  const handleLoadMore = async () => {
    const nextPage = page + 1;
    setPage(nextPage);
    await loadPosts(nextPage);
  };

  if (!profileUser && initialLoad) {
    return (
      <div className="max-w-4xl mx-auto mt-6 space-y-4">
        <div className="h-48 bg-gray-200 animate-pulse rounded-xl" />
        <div className="h-12 bg-gray-200 animate-pulse rounded-xl" />
      </div>
    );
  }

  const isCurrentUserProfile = currentUser?.id === profileUser?.id;
  return (
    <div className="max-w-4xl mx-auto">
      {profileUser && <ProfileHeader user={profileUser} />}
      <ProfileTabs activeTab={activeTab} setActiveTab={setActiveTab} />

      {activeTab === "timeline" && (
        <>
          {isCurrentUserProfile && (
            <div className="max-w-xl mx-auto mb-4">
              <CreatePostBar user={currentUser} />
            </div>
          )}
          <ProfileTimeline
            posts={posts}
            loadMore={handleLoadMore}
            hasMore={hasMore}
            loading={loading}
          />
        </>
      )}

      {activeTab === "about" && (
        <div className="max-w-xl mx-auto">
          <ProfileAbout user={profileUser} />
        </div>
      )}

      {activeTab === "photos" && (
        <div className="max-w-xl mx-auto">
          <ProfilePhotos posts={posts} />
        </div>
      )}
    </div>
  );
};

export default Profile;