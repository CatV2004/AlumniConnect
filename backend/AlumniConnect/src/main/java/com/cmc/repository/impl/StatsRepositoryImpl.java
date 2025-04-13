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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author PHAT
 */
@Repository
@Transactional
public class StatsRepositoryImpl implements StatsRepository {

    @Autowired
    private LocalSessionFactoryBean factory;

    @Override
    public List<Object[]> statsUser(Map<String, String> pagram) {
        Session s = factory.getObject().getCurrentSession();
        CriteriaBuilder builder = s.getCriteriaBuilder();
        CriteriaQuery<Object[]> q = builder.createQuery(Object[].class);
        Root<User> rootUser = q.from(User.class);

        Expression<Integer> yearEx = builder.function("YEAR", Integer.class, rootUser.get("createdDate"));
        Expression<Integer> monthEx = builder.function("MONTH", Integer.class, rootUser.get("createdDate"));
        Expression<Integer> quarterEx = builder.function("QUARTER", Integer.class, rootUser.get("createdDate"));

        q.multiselect(yearEx, monthEx, quarterEx, builder.count(rootUser));

        List<Predicate> predicates = new ArrayList<>();

        if (pagram != null) {
            String year = pagram.get("year");
            if (year == null || year.isEmpty()) {
                year = String.valueOf(Year.now().getValue());
            }
            predicates.add(builder.equal(yearEx, Integer.parseInt(year)));

            String month = pagram.get("month");
            if (month != null && !month.isEmpty()) {
                predicates.add(builder.equal(monthEx, Integer.parseInt(month)));
            }

            String quarter = pagram.get("quarter");
            if (quarter != null && !quarter.isEmpty()) {
                predicates.add(builder.equal(quarterEx, Integer.parseInt(quarter)));
            }
        }

        if (!predicates.isEmpty()) {
            q.where(predicates.toArray(Predicate[]::new));
        }

        q.groupBy(yearEx, monthEx, quarterEx);
        q.orderBy(builder.asc(yearEx), builder.asc(quarterEx), builder.asc(monthEx));

        return s.createQuery(q).getResultList();
    }

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

    @Override
    public List<Object[]> statsPost(Map<String, String> pagram) {
        Session s = factory.getObject().getCurrentSession();
        CriteriaBuilder builder = s.getCriteriaBuilder();
        CriteriaQuery<Object[]> q = builder.createQuery(Object[].class);
        Root<Post> rootPost = q.from(Post.class);

        Expression<Integer> yearEx = builder.function("YEAR", Integer.class, rootPost.get("createdDate"));
        Expression<Integer> monthEx = builder.function("MONTH", Integer.class, rootPost.get("createdDate"));
        Expression<Integer> quarterEx = builder.function("QUARTER", Integer.class, rootPost.get("createdDate"));

        q.multiselect(yearEx, monthEx, quarterEx, builder.count(rootPost));

        List<Predicate> predicates = new ArrayList<>();

        if (pagram != null) {
            String year = pagram.get("year");
            if (year == null || year.isEmpty()) {
                year = String.valueOf(Year.now().getValue());
            }
            predicates.add(builder.equal(yearEx, Integer.parseInt(year)));

            String month = pagram.get("month");
            if (month != null && !month.isEmpty()) {
                predicates.add(builder.equal(monthEx, Integer.parseInt(month)));
            }

            String quarter = pagram.get("quarter");
            if (quarter != null && !quarter.isEmpty()) {
                predicates.add(builder.equal(quarterEx, Integer.parseInt(quarter)));
            }
        }

        if (!predicates.isEmpty()) {
            q.where(predicates.toArray(Predicate[]::new));
        }

        q.groupBy(yearEx, monthEx, quarterEx);
        q.orderBy(builder.asc(yearEx), builder.asc(quarterEx), builder.asc(monthEx));

        return s.createQuery(q).getResultList();
    }

}
