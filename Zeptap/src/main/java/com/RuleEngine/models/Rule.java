package com.RuleEngine.models;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;

import java.time.LocalDateTime;

@Entity
public class Rule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String operator; // AND, OR, etc.
    private String value; // "age < 30", "salary < 30000", etc.

    @Column    
    private String ruleString;

    @ManyToOne
    @JoinColumn(name = "parent_id")
    private Rule parent; // Parent node reference

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "left_child_id", referencedColumnName = "id")
    private Rule leftChild; // Left child reference

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "right_child_id", referencedColumnName = "id")
    private Rule rightChild; // Right child reference

    @Column(name = "created_at")
    private LocalDateTime createdAt; // Timestamp for rule creation

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Rule getParent() {
        return parent;
    }

    public void setParent(Rule parent) {
        this.parent = parent;
    }

    public Rule getLeftChild() {
        return leftChild;
    }

    public void setLeftChild(Rule leftChild) {
        this.leftChild = leftChild;
    }

    public Rule getRightChild() {
        return rightChild;
    }

    public void setRightChild(Rule rightChild) {
        this.rightChild = rightChild;
    }

    public String getRuleString() {
        return ruleString;
    }

    public void setRuleString(String ruleString) {
        this.ruleString = ruleString;
    }

    @PrePersist
	protected void onCreate() {
		createdAt = LocalDateTime.now();
	}
}
