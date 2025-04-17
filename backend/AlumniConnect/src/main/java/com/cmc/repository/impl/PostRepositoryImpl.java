/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cmc.repository.impl;

import com.cmc.pojo.Post;
import com.cmc.pojo.PostImage;
import com.cmc.repository.PostRepository;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.CriteriaUpdate;
import jakarta.persistence.criteria.Root;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
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
    public List<Post> getPostByUserId(Long id) {
        Query q = getSession().createQuery("""
            SELECT p FROM Post p 
            JOIN p.userId u  
            LEFT JOIN p.postImageSet pi  
            WHERE p.userId = :userId 
            ORDER BY p.id DESC
            """);
        q.setParameter("userId", id);
        return q.getResultList();
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
    public int deletePost(Long id) {
        Query q = getSession().createQuery("UPDATE Post p SET p.active = FALSE WHERE p.id = :id");
        q.setParameter("id", id);
        return q.executeUpdate();
    }

    @Override
    public int restorePost(Long id) {
        Query q = getSession().createQuery("UPDATE Post p SET p.active = TRUE WHERE p.id = :id");
        q.setParameter("id", id);
        return q.executeUpdate();
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
    public long countTotalPosts(String kw) {
        String hql = "SELECT COUNT(p) FROM Post p WHERE p.content LIKE :kw";
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
        String hql = "SELECT COUNT(*) FROM Post";
        Query query = getSession().createQuery(hql, Long.class);
        return (long) query.getSingleResult();
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
    public int deletePostImage(Long imageId) {
    Session s = getSession();
    PostImage img = s.find(PostImage.class, imageId);
    
    if (img != null) {
        Post post = img.getPostId();
        if (post != null) {
            post.getPostImageSet().remove(img);
        }
        s.remove(img);
        return 1;
    }
    return 0;
}

    @Override
    public List<PostImage> getImagesByPostId(Long postId) {
        return this.getSession().createQuery("SELECT pi FROM PostImage pi WHERE pi.postId.id = :postId", PostImage.class)
                .setParameter("postId", postId)
                .getResultList();
    }
    
    
    @Override
    public PostImage getPostImageById(Long id){
        Query q = this.getSession().createNamedQuery("PostImage.findById", PostImage.class);
        q.setParameter("id", id);
        return (PostImage) q.uniqueResult();
    }

    @Override
    public Post saveOrUpdate(Post post) {
        post.setUpdatedDate(LocalDateTime.now());
        if (post.getId() == null) {
            post.setActive(Boolean.TRUE);
            post.setCreatedDate(LocalDateTime.now());
            this.getSession().persist(post);
        }
        this.getSession().merge(post);
        getSession().refresh(post);
        return post;
    }

}
