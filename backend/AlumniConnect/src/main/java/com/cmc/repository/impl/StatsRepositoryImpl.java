/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cmc.repository.impl;

import com.cmc.pojo.Post;
import com.cmc.pojo.User;
import com.cmc.repository.StatsRepository;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.time.Year;
import java.util.List;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;

/**
 *
 * @author PHAT
 */
public class StatsRepositoryImpl implements StatsRepository {

    @Autowired
    private LocalSessionFactoryBean factory;

    @Override
    public List<Object[]> countUsersByYear(Integer year) {

        Session s = factory.getObject().getCurrentSession();
        CriteriaBuilder builder = s.getCriteriaBuilder();
        CriteriaQuery<Object[]> q = builder.createQuery(Object[].class);
        Root<User> root = q.from(User.class);

        Expression<Integer> yearEx = builder.function("YEAR", Integer.class, root.get("createdDate"));
        q.multiselect(yearEx, builder.count(root));

        if (year != 0 || year != null) {
            Predicate yearPredicate = builder.equal(yearEx, year);
            q.multiselect(yearEx, builder.count(root)).where(yearPredicate);
        }

        q.groupBy(yearEx);
        q.orderBy(builder.asc(yearEx));

        return s.createQuery(q).getResultList();
    }

    @Override
    public List<Object[]> countUsersByMonth(Integer year, Integer month) {
        if (year == 0 || year == null) {
            year = Year.now().getValue();
        }
        Session s = this.factory.getObject().getCurrentSession();
        CriteriaBuilder builder = s.getCriteriaBuilder();
        CriteriaQuery q = builder.createQuery(Object.class);
        Root<User> root = q.from(User.class);

        Expression<Integer> yearEx = builder.function("YEAR", Integer.class, root.get("CreatedDate"));
        Expression<Integer> monthEx = builder.function("MONTH", Integer.class, root.get("createdDate"));

        Predicate yearPredicate = builder.equal(yearEx, year);
        Predicate monthPredicate = builder.equal(monthEx, month);

        q.multiselect(yearEx, monthEx, builder.count(root)).where(builder.and(yearPredicate, monthPredicate));
        q.groupBy(yearEx, monthEx);
        q.orderBy(builder.asc(monthEx));

        return s.createQuery(q).getResultList();
    }

    @Override
    public List<Object[]> countUsersByQuarter(Integer year, Integer quarter) {
        if (year == 0 || year == null) {
            year = Year.now().getValue();
        }
        Session s = this.factory.getObject().getCurrentSession();
        CriteriaBuilder builder = s.getCriteriaBuilder();
        CriteriaQuery q = builder.createQuery(Object.class);
        Root<User> root = q.from(User.class);

        Expression<Integer> yearExp = builder.function("YEAR", Integer.class, root.get("createdAt"));
        Expression<Integer> monthExp = builder.function("MONTH", Integer.class, root.get("createdAt"));
        Expression<Number> quarterExp = builder.quot(builder.sum(monthExp, 2), 3);
        Predicate yearPredicate = builder.equal(yearExp, year);
        q.multiselect(yearExp, quarterExp, builder.count(root));
        
        if (quarter != 0 || quarter != null) {
            int startMonth = (quarter - 1) * 3 + 1;
            int endMonth = startMonth + 2;
            Predicate quarterPredicate = builder.between(monthExp, startMonth, endMonth);
            q.multiselect(yearExp, quarterExp, builder.count(root)).where(builder.and(yearPredicate, quarterPredicate));
        }
        q.groupBy(yearExp, quarterExp);
        q.orderBy(builder.asc(yearExp), builder.asc(quarterExp));
        return s.createQuery(q).getResultList();
    }

    @Override
    public List<Object[]> countPostsByYear(Integer year) {
        Session s = factory.getObject().getCurrentSession();
        CriteriaBuilder builder = s.getCriteriaBuilder();
        CriteriaQuery<Object[]> q = builder.createQuery(Object[].class);
        Root<Post> root = q.from(Post.class);

        Expression<Integer> yearEx = builder.function("YEAR", Integer.class, root.get("createdDate"));
        q.multiselect(yearEx, builder.count(root));

        if (year != 0 || year != null) {
            Predicate yearPredicate = builder.equal(yearEx, year);
            q.multiselect(yearEx, builder.count(root)).where(yearPredicate);
        }

        q.groupBy(yearEx);
        q.orderBy(builder.asc(yearEx));

        return s.createQuery(q).getResultList();
    }

    @Override
    public List<Object[]> countPostsByMonth(Integer year, Integer month) {
        if (year == 0 || year == null) {
            year = Year.now().getValue();
        }
        Session s = this.factory.getObject().getCurrentSession();
        CriteriaBuilder builder = s.getCriteriaBuilder();
        CriteriaQuery q = builder.createQuery(Object.class);
        Root<Post> root = q.from(Post.class);

        Expression<Integer> yearEx = builder.function("YEAR", Integer.class, root.get("CreatedDate"));
        Expression<Integer> monthEx = builder.function("MONTH", Integer.class, root.get("createdDate"));

        Predicate yearPredicate = builder.equal(yearEx, year);
        Predicate monthPredicate = builder.equal(monthEx, month);

        q.multiselect(yearEx, monthEx, builder.count(root)).where(builder.and(yearPredicate, monthPredicate));
        q.groupBy(yearEx, monthEx);
        q.orderBy(builder.asc(monthEx));

        return s.createQuery(q).getResultList();
    }

    @Override
    public List<Object[]> countPostsByQuarter(Integer year, Integer quarter) {
        if (year == 0 || year == null) {
            year = Year.now().getValue();
        }
        Session s = this.factory.getObject().getCurrentSession();
        CriteriaBuilder builder = s.getCriteriaBuilder();
        CriteriaQuery q = builder.createQuery(Object.class);
        Root<Post> root = q.from(Post.class);

        Expression<Integer> yearExp = builder.function("YEAR", Integer.class, root.get("createdAt"));
        Expression<Integer> monthExp = builder.function("MONTH", Integer.class, root.get("createdAt"));
        Expression<Number> quarterExp = builder.quot(builder.sum(monthExp, 2), 3);
        Predicate yearPredicate = builder.equal(yearExp, year);
        q.multiselect(yearExp, quarterExp, builder.count(root));
        
        if (quarter != 0 || quarter != null) {
            int startMonth = (quarter - 1) * 3 + 1;
            int endMonth = startMonth + 2;
            Predicate quarterPredicate = builder.between(monthExp, startMonth, endMonth);
            q.multiselect(yearExp, quarterExp, builder.count(root)).where(builder.and(yearPredicate, quarterPredicate));
        }
        q.groupBy(yearExp, quarterExp);
        q.orderBy(builder.asc(yearExp), builder.asc(quarterExp));
        return s.createQuery(q).getResultList();
    }

}
