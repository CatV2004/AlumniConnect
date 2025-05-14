/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cmc.repository.impl;

import com.cmc.pojo.Post;
import com.cmc.pojo.PostImage;
import com.cmc.pojo.SurveyPost;
import com.cmc.repository.PostRepository;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.CriteriaUpdate;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author FPTSHOP
 */
@Repository
@Transactional
public class PostRepositoryImpl implements PostRepository {

    @Autowired
    private LocalSessionFactoryBean Factory;

    private Session getSession() {
        return Factory.getObject().getCurrentSession();
    }

    @Override
    public List<Post> getPostsByUserId(Map<String, Object> params) {
        Session session = getSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<Post> cq = cb.createQuery(Post.class);
        Root<Post> root = cq.from(Post.class);

        List<Predicate> predicates = new ArrayList<>();

        if (params.get("userId") != null) {
            Long userId = (Long) params.get("userId");
            predicates.add(cb.equal(root.get("userId").get("id"), userId));
        }

        predicates.add(cb.isNull(root.get("deletedDate")));

        predicates.add(cb.isTrue(root.get("active")));

        cq.select(root)
                .where(cb.and(predicates.toArray(new Predicate[0])))
                .orderBy(cb.desc(root.get("createdDate")));

        Query<Post> query = session.createQuery(cq);

        int page = params.get("page") != null ? (int) params.get("page") : 0;
        int size = params.get("size") != null ? (int) params.get("size") : 10;

        query.setFirstResult(page * size);
        query.setMaxResults(size);

        return query.getResultList();
    }

    @Override
    public List<Post> getPostByKeywords(String kw, Pageable pageable) {
        String hql = "FROM Post p WHERE p.content LIKE CONCAT('%', :kw, '%') ORDER BY p.id DESC";
        Query query = getSession().createQuery(hql, Post.class);
        query.setParameter("kw", kw);

        query.setFirstResult((int) pageable.getOffset());
        query.setMaxResults(pageable.getPageSize());

        return query.getResultList();
    }

    @Override
    public Post addPost(Post post) {
        Session s = this.getSession();
        try {
            if (post.getId() == null) {
                s.persist(post);
            } else {
                s.merge(post);
            }
        } catch (Exception ex) {
            return null;
        }
        s.refresh(post);
        return post;
    }

    @Override
    public boolean deletePost(Long id) {
        Session session = this.getSession();
        Post post = session.get(Post.class, id);
        if (post != null && Boolean.TRUE.equals(post.getActive())) {
            post.setActive(false);
            post.setDeletedDate(LocalDateTime.now());
            post.setUpdatedDate(LocalDateTime.now());
            return true;
        }
        return false;
    }

    @Override
    public boolean restorePost(Long id) {
        Session session = this.getSession();
        Post post = session.get(Post.class, id);
        if (post != null && Boolean.FALSE.equals(post.getActive())) {
            post.setActive(true);
            post.setDeletedDate(null);
            post.setUpdatedDate(LocalDateTime.now());
            return true;
        }
        return false;
    }

    @Override
    public boolean deletePostPermanently(Long postId) {
        Session session = getSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();

        CriteriaQuery<Post> cq = cb.createQuery(Post.class);
        Root<Post> root = cq.from(Post.class);
        cq.select(root).where(
                cb.and(
                        cb.equal(root.get("id"), postId),
                        cb.isFalse(root.get("active")),
                        cb.isNotNull(root.get("deletedDate"))
                )
        );

        Post post = session.createQuery(cq).uniqueResult();

        if (post != null) {
            session.remove(post);
            return true;
        }

        return false;
    }

    @Override
    public int updateContent(Long id, String content) {
        Query q = getSession().createQuery("UPDATE Post p SET p.content = :content WHERE p.id = :id");
        q.setParameter("id", id);
        q.setParameter("content", content);
        return q.executeUpdate();
    }

    @Override
    public Post getPostId(Long id) {
        String sql = "FROM Post p WHERE p.active = true AND p.id = :id";
        Query q = getSession().createQuery(sql, Post.class);
        q.setParameter("id", id);
        List<Post> results = q.getResultList();
        if (results.isEmpty()) {
            return null;
        } else {
            return results.get(0);
        }
    }

    @Override
    public long countPosts(Map<String, Object> params) {
        CriteriaBuilder cb = getSession().getCriteriaBuilder();
        CriteriaQuery<Long> cq = cb.createQuery(Long.class);
        Root<Post> root = cq.from(Post.class);

        List<Predicate> predicates = new ArrayList<>();
        predicates.add(cb.isNull(root.get("deletedDate")));

        if (params.containsKey("kw")) {
            String kw = params.get("kw").toString();
            if (!kw.isBlank()) {
                predicates.add(cb.like(root.get("content"), "%" + kw.trim() + "%"));
            }
        }

        if (Boolean.TRUE.equals(params.get("hasSurvey"))) {
            predicates.add(cb.isNotNull(root.get("surveyPost")));
        }

        if (Boolean.TRUE.equals(params.get("hasImage"))) {
            predicates.add(cb.isNotEmpty(root.get("postImageSet")));
        }

        if (Boolean.TRUE.equals(params.get("hasInvitation"))) {
            predicates.add(cb.isNotNull(root.get("invitationPost")));
        }

        cq.select(cb.countDistinct(root)).where(cb.and(predicates.toArray(new Predicate[0])));
        return getSession().createQuery(cq).getSingleResult();
    }

    @Override
    public long countTotalPosts(String kw) {
        String hql = "SELECT COUNT(p) FROM Post p WHERE p.content LIKE :kw AND p.deletedDate IS NULL";
        Query query = getSession().createQuery(hql, Long.class);
        query.setParameter("kw", "%" + kw + "%");
        return (long) query.getSingleResult();
    }

    @Override
    public List<Post> getPosts(Pageable pageable) {
        String hql = "FROM Post p ORDER BY p.id DESC";
        Query query = getSession().createQuery(hql, Post.class);

        query.setFirstResult((int) pageable.getOffset());
        query.setMaxResults(pageable.getPageSize());

        return query.getResultList();
    }

    @Override
    public int lockComment(Long id) {
        CriteriaBuilder builder = getSession().getCriteriaBuilder();
        CriteriaUpdate<Post> update = builder.createCriteriaUpdate(Post.class);
        Root<Post> root = update.from(Post.class);

        CriteriaQuery<Boolean> selectQuery = builder.createQuery(Boolean.class);
        Root<Post> selectRoot = selectQuery.from(Post.class);
        selectQuery.select(selectRoot.get("lockComment")).where(builder.equal(selectRoot.get("id"), id));

        Boolean currentStatus = getSession().createQuery(selectQuery).uniqueResult();
        boolean newStatus = (currentStatus != null) ? !currentStatus : true;

        update.set(root.get("lockComment"), newStatus);
        update.where(builder.equal(root.get("id"), id));

        return getSession().createQuery(update).executeUpdate();
    }

    @Override
    public long countTotalPosts() {
        String hql = "SELECT COUNT(p) FROM Post p WHERE p.deletedDate IS NULL";
        Query query = getSession().createQuery(hql, Long.class);
        return (long) query.getSingleResult();
    }

    @Override
    public long countTotalPostsByUser(Map<String, Object> params) {
        CriteriaBuilder cb = getSession().getCriteriaBuilder();
        CriteriaQuery<Long> cq = cb.createQuery(Long.class);
        Root<Post> root = cq.from(Post.class);

        List<Predicate> predicates = new ArrayList<>();

        if (params.get("userId") != null) {
            Long userId = (Long) params.get("userId");
            predicates.add(cb.equal(root.get("userId").get("id"), userId));
        }

        predicates.add(cb.isNull(root.get("deletedDate")));

        if (params.get("active") == null || Boolean.TRUE.equals(params.get("active"))) {
            predicates.add(cb.isTrue(root.get("active")));
        }

        cq.select(cb.count(root)).where(cb.and(predicates.toArray(new Predicate[0])));
        return getSession().createQuery(cq).getSingleResult();
    }

    @Override
    public PostImage addPostImage(Long postId, String imageUrl) {
        Session s = getSession();

        Post post = s.find(Post.class, postId);
        if (post == null) {
            return null;
        }

        PostImage img = new PostImage();
        img.setImage(imageUrl);
        img.setPostId(post);

        s.persist(img);

        post.getPostImageSet().add(img);
        s.merge(post);

        return img;
    }

    @Override
    public List<PostImage> getImagesByPostId(Long postId) {
        return this.getSession().createQuery("SELECT pi FROM PostImage pi WHERE pi.postId.id = :postId", PostImage.class)
                .setParameter("postId", postId)
                .getResultList();
    }

    @Override
    public PostImage getPostImageById(Long id) {
        Query q = this.getSession().createNamedQuery("PostImage.findById", PostImage.class);
        q.setParameter("id", id);
        return (PostImage) q.uniqueResult();
    }

    @Override
    public void saveOrUpdate(Post post) {
        post.setUpdatedDate(LocalDateTime.now());
        if (post.getId() == null) {
            post.setActive(Boolean.TRUE);
            post.setCreatedDate(LocalDateTime.now());
            this.getSession().persist(post);
        }
        this.getSession().merge(post);
        getSession().refresh(post);
    }

    @Override
    public List<Post> findPosts(Map<String, Object> params) {
        CriteriaBuilder cb = getSession().getCriteriaBuilder();
        CriteriaQuery<Post> cq = cb.createQuery(Post.class);
        Root<Post> root = cq.from(Post.class);

        root.fetch("surveyPost", JoinType.LEFT);
        root.fetch("postImageSet", JoinType.LEFT);
        root.fetch("invitationPost", JoinType.LEFT);

        List<Predicate> predicates = new ArrayList<>();

        predicates.add(cb.isNull(root.get("deletedDate")));

        if (params.containsKey("kw")) {
            String kw = params.get("kw").toString();
            if (!kw.isBlank()) {
                predicates.add(cb.like(root.get("content"), "%" + kw.trim() + "%"));
            }
        }

        if (Boolean.TRUE.equals(params.get("hasSurvey"))) {
            predicates.add(cb.isNotNull(root.get("surveyPost")));
        }

        if (Boolean.TRUE.equals(params.get("hasImage"))) {
            predicates.add(cb.isNotEmpty(root.get("postImageSet")));
        }

        if (Boolean.TRUE.equals(params.get("hasInvitation"))) {
            predicates.add(cb.isNotNull(root.get("invitationPost")));
        }

        cq.select(root).distinct(true)
                .where(cb.and(predicates.toArray(new Predicate[0])))
                .orderBy(cb.desc(root.get("createdDate")));

        Query<Post> query = getSession().createQuery(cq);

        int page = params.get("page") != null ? (int) params.get("page") : 1;
        int size = params.get("size") != null ? (int) params.get("size") : 10;

        query.setFirstResult((page - 1) * size);
        query.setMaxResults(size);

        return query.getResultList();
    }

    @Override
    public Post getPostIdOfDL(Long id) {
        Session session = getSession();

        String hql = "FROM Post p WHERE p.id = :id AND p.active = false AND p.deletedDate IS NOT NULL";
        return session.createQuery(hql, Post.class)
                .setParameter("id", id)
                .uniqueResult();
    }

    @Override
    public List<Post> getDeletedPostsByUser(Map<String, Object> params) {
        Session session = getSession();
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<Post> query = builder.createQuery(Post.class);
        Root<Post> root = query.from(Post.class);

        List<Predicate> predicates = new ArrayList<>();

        if (params.get("userId") != null) {
            Long userId = (Long) params.get("userId");
            predicates.add(builder.equal(root.get("userId").get("id"), userId));
        }

        predicates.add(builder.equal(root.get("active"), false));

        query.select(root)
                .where(builder.and(predicates.toArray(new Predicate[0])))
                .orderBy(builder.desc(root.get("deletedDate")));

        Query<Post> q = session.createQuery(query);

        // Pagination
        int page = params.get("page") != null ? (int) params.get("page") : 1;
        int size = params.get("size") != null ? (int) params.get("size") : 10;
        int start = (page - 1) * size;

        q.setFirstResult(start);
        q.setMaxResults(size);

        return q.getResultList();
    }

    @Override
    public long countDeletedPostsByUser(Map<String, Object> params) {
        Session session = getSession();
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<Long> query = builder.createQuery(Long.class);
        Root<Post> root = query.from(Post.class);

        List<Predicate> predicates = new ArrayList<>();

        if (params.get("userId") != null) {
            Long userId = (Long) params.get("userId");
            predicates.add(builder.equal(root.get("userId").get("id"), userId));
        }

        predicates.add(builder.equal(root.get("active"), false));

        query.select(builder.count(root))
                .where(builder.and(predicates.toArray(new Predicate[0])));

        return session.createQuery(query).getSingleResult();
    }

    @Override
    public void createImagePost(Long postId, String url) {
        Post post = getSession().get(Post.class, postId);
        if (post == null) {
            throw new IllegalArgumentException("Post with ID " + postId + " not found.");
        }
        PostImage postImage = new PostImage();
        postImage.setImage(url);
        postImage.setPostId(post);

        getSession().persist(postImage);
    }

    @Override
    public void deleteImagesByPost(Post post) {
        Session session = getSession();

        String hql = "DELETE FROM PostImage WHERE postId = :post";
        session.createMutationQuery(hql)
                .setParameter("post", post)
                .executeUpdate();

        post.getPostImageSet().clear();
    }

    @Override
    public void deleteImagesNotInList(Post post, List<Long> keepImageIds) {
        if (keepImageIds == null || keepImageIds.isEmpty()) {
            System.out.println("xóa tất cảaaaaaaaaaaaaaaaaaaaaaaaaa");
            this.deleteImagesByPost(post);
            return;
        }

        keepImageIds.forEach(id -> System.out.println("keepImageIdsId: " + id));

        Session session = this.getSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<PostImage> cq = cb.createQuery(PostImage.class);
        Root<PostImage> root = cq.from(PostImage.class);

        Predicate postPredicate = cb.equal(root.get("postId"), post);
        Predicate notInKeepIds = root.get("id").in(keepImageIds).not();
        cq.select(root).where(cb.and(postPredicate, notInKeepIds));

        List<PostImage> imagesToDelete = session.createQuery(cq).getResultList();

        for (PostImage image : imagesToDelete) {
            post.getPostImageSet().remove(image); 
            session.remove(image);              

            // TODO: Xóa file khỏi Cloudinary nếu cần
            System.out.println("Đã xóa ảnh có ID: " + image.getId());
        }
    }

}
