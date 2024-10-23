package com.RuleEngine.Repositories;

import com.RuleEngine.models.Rule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;

import java.util.List;

@Repository
public class RuleRepositoryImpl implements RuleRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    @Override
    public void saveRule(Rule rule) {
        entityManager.persist(rule); // Save the root rule
        // Handle cascading saves if necessary
        if (rule.getLeftChild() != null) {
            entityManager.persist(rule.getLeftChild());
        }
        if (rule.getRightChild() != null) {
            entityManager.persist(rule.getRightChild());
        }
    }

    @Override
    @Transactional
    public List<Rule> findByRuleStrings(List<String> ruleStrings) {
        // Correct JPQL query
        String jpql = "SELECT r FROM Rule r WHERE r.ruleString IN :ruleStrings"; // 'r' is the alias for Rule

        // Create the typed query
        TypedQuery<Rule> query = entityManager.createQuery(jpql, Rule.class);

        // Set the parameter correctly
        query.setParameter("ruleStrings", ruleStrings);

        // Execute the query and return the result
        return query.getResultList();
    }

}
