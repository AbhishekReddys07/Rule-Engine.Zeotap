package com.RuleEngine.models;

import java.util.ArrayList;
import java.util.List;

public class Node {
    private String value;  // Operand value, like a condition (e.g., "age > 30")
    private String operator;  // Operator, like "AND" or "OR"
    private Node left;  // Left child node (for operator nodes)
    private Node right;  // Right child node (for operator nodes)
    private List<Node> children;  // Additional child nodes for n-ary operations (like multiple conditions under an "AND")

    // Constructor for operand (leaf) nodes
    public Node(String value) {
        this.value = value;
        this.operator = "";  // No operator for operand nodes
        this.left = null;
        this.right = null;
        this.children = new ArrayList<>();  // Initialize as empty list
    }

    // Constructor for operator nodes (with left and right children)
    public Node(String value, String operator, Node left, Node right) {
        this.value = value;
        this.operator = operator;
        this.left = left;
        this.right = right;
        this.children = new ArrayList<>();  // Initialize as empty list
    }
    
    public Node(String value, Node left, Node right) {
        this.value = value;
        this.left = left;
        this.right = right;
    }

    public Node() {
        this.children = new ArrayList<>();
    }

    // Method to add child nodes for n-ary operator nodes
    public void addChild(Node child) {
        this.children.add(child);
    }

    // Getter and setter methods
    public String getValue() {
        return value;
    }

    public String getOperator() {
        return operator;
    }

    public Node getLeft() {
        return left;
    }

    public Node getRight() {
        return right;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public void setLeft(Node left) {
        this.left = left;
    }

    public void setRight(Node right) {
        this.right = right;
    }

    // Getter for children
    public List<Node> getChildren() {
        return children;
    }

    // Overriding toString() method to represent the Node and its children
    @Override
    public String toString() {
        if (children.isEmpty()) {
            return value; // Return the value if there are no children
        }
        
        StringBuilder sb = new StringBuilder();
        sb.append("(").append(value); // Start with the operator

        for (Node child : children) {
            sb.append(" ").append(child.toString()); // Append child node's string representation
        }
        
        sb.append(")"); // Close the parenthesis
        return sb.toString();
    }
    
    
}
