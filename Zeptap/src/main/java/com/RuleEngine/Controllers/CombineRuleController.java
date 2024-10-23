package com.RuleEngine.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.RuleEngine.Repositories.RuleRepository;
import com.RuleEngine.models.Node;
import com.RuleEngine.models.Rule;
import com.RuleEngine.services.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

@RestController
public class CombineRuleController {

    @Autowired
    private Service service;
    
    @Autowired
    private RuleRepository rulerepo;

    private static final ObjectMapper objectMapper = new ObjectMapper();
    // create_rule API - Converts a rule string into a Map representing the AST
    @PostMapping("/createNew")
    public Map<String, Object> createNewRule(@RequestBody String ruleString) {
        System.err.println("Received rule string: " + ruleString);

        // Build the AST (Abstract Syntax Tree)
        Node rootNode = buildAST(ruleString);
        printNode(rootNode, 0); // Print the created Node structure

        Map<String, Object> ruleMap = convertNodeToMap(rootNode);

        // Calling service layer to save the rule as a map
        service.Save(ruleString, ruleMap);

        return ruleMap;  // Return the map for confirmation or further use
    }

    // Convert Node to Map
    private Map<String, Object> convertNodeToMap(Node node) {
        if (node == null) {
            return null; // Handle null nodes
        }

        Map<String, Object> nodeMap = new HashMap<>();

        // Store the node value and operator in the map
        nodeMap.put("value", node.getValue());
        nodeMap.put("operator", node.getOperator());

        // If left and right nodes exist, add them recursively as child maps
        if (node.getLeft() != null) {
            nodeMap.put("left", convertNodeToMap(node.getLeft()));
        }
        if (node.getRight() != null) {
            nodeMap.put("right", convertNodeToMap(node.getRight()));
        }

        return nodeMap;
    }

    // combine_rules API - Combines multiple rule ASTs into a single AST
    @PostMapping("/combineRules")
    public Rule combineRules(@RequestBody List<String> ruleStrings) {
        System.out.println("inside combineRules endpoint");

        // Step 1: Fetch existing rules from the database based on the provided rule strings
        List<Rule> existingRules = rulerepo.findByRuleStrings(ruleStrings);
        
        System.err.println(existingRules);

        // Step 2: Combine the rules into a single AST
        Rule combinedRule = combineIntoAST(ruleStrings);
        rulerepo.saveRule(combinedRule);

        // Return the combined rule
        return combinedRule;
    }

    private Rule combineIntoAST(List<String> ruleStrings) {
        // Step 1: Create a root node with a logical operator (e.g., "AND")
        Node rootNode = new Node("AND");

        // Step 2: Traverse the list of rule strings and build the tree structure (AST)
        for (String ruleString : ruleStrings) {
            // Ensure the rule string is not empty or null before creating a node
            if (ruleString != null && !ruleString.trim().isEmpty()) {
                Node ruleNode = new Node(ruleString); // Create a node for each valid rule string
                rootNode.addChild(ruleNode); // Add the rule node as a child of the root node
            }
        }

        // Step 3: Create a new Rule object to represent the combined rule
        Rule combinedRule = new Rule();

        // Set the combined rule's ruleString to the string representation of the rootNode
        combinedRule.setRuleString(rootNode.toString()); 
        return combinedRule;
    }

    // Build an AST from a single rule string
    private Node buildAST(String ruleString) {
        Stack<Node> operandStack = new Stack<>();
        Stack<String> operatorStack = new Stack<>();

        List<String> tokens = tokenize(ruleString);
        for (String token : tokens) {
            if (token.equals("AND") || token.equals("OR")) {
                while (!operatorStack.isEmpty()) {
                    String topOperator = operatorStack.peek();
                    if (precedence(topOperator) >= precedence(token)) {
                        Node rightOperand = operandStack.pop();
                        Node leftOperand = operandStack.pop();
                        operandStack.push(new Node("", operatorStack.pop(), leftOperand, rightOperand));
                    } else {
                        break;
                    }
                }
                operatorStack.push(token);
            } else {
                operandStack.push(new Node(token)); // Operand node (leaf)
            }
        }

        while (!operatorStack.isEmpty()) {
            Node rightOperand = operandStack.pop();
            Node leftOperand = operandStack.pop();
            operandStack.push(new Node("", operatorStack.pop(), leftOperand, rightOperand));
        }

        return operandStack.pop(); // The root of the AST
    }

    // Tokenize method to break down rule strings into individual tokens
    private List<String> tokenize(String ruleString) {
        String[] tokens = ruleString.split(" (?=AND|OR)|(?<=AND|OR) ");
        List<String> tokenList = new ArrayList<>();
        for (String token : tokens) {
            token = token.trim();
            if (!token.isEmpty()) {
                tokenList.add(token);
            }
        }
        return tokenList;
    }

    // Helper method to determine precedence of operators
    private int precedence(String operator) {
        if (operator.equals("AND")) {
            return 2;
        } else if (operator.equals("OR")) {
            return 1;
        } else {
            return 0;
        }
    }

    // Print the structure of the AST for debugging
    private void printNode(Node node, int depth) {
        if (node != null) {
            String indent = "  ".repeat(depth);
            if (!node.getOperator().isEmpty()) {
                System.out.println(indent + "Node: Operator: " + node.getOperator());
                printNode(node.getLeft(), depth + 1);
                printNode(node.getRight(), depth + 1);
            } else {
                System.out.println(indent + "Node: " + node.getValue());
            }
        }
    }
    
    @PostMapping("/evaluateRule")
    public ResponseEntity<Map<String, Object>> evaluateRule(@RequestBody Map<String, Object> requestData) {
    	System.out.println("inside evaluate function ");
        // Extract rule and attributes from the request data
        Object ruleObj = requestData.get("rule");
        @SuppressWarnings("unchecked")
        Map<String, Object> attributes = (Map<String, Object>) requestData.get("data");

        // Validate the inputs
        if (ruleObj == null || attributes == null) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", "Rule or attributes are missing."));
        }

        try {
            // Assume the rule is passed as a string (adjust as necessary based on your input)
            String ruleString = ruleObj.toString();

            // Parse the rule string to create the AST (Abstract Syntax Tree)
            Node ast = parseRuleStringToAST(ruleString);

            if (ast == null) {
                // If AST couldn't be built, return false
                return ResponseEntity.ok(Map.of("success", false, "message", "Failed to build AST."));
            }

            // At this point, the AST is successfully built
            // Return the success status and the built structure
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("ast", ast);  // You may need to adjust how you return the AST if it has complex nested data

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            // Handle any parsing or rule-building exceptions
            System.err.println("Error while building AST: " + e.getMessage());
            return ResponseEntity.ok(Map.of("success", false, "message", "Error building AST."));
        }
    }


    private Node parseRuleStringToAST(String ruleString) {
    // Remove unnecessary whitespaces from the rule string
    ruleString = ruleString.trim();

    // Base case: if the ruleString is an atomic condition like "age > 30", return a leaf node
    if (isAtomicCondition(ruleString)) {
        return new Node(ruleString);
    }

    // Parse the rule string recursively
    int openBrackets = 0;
    int splitIndex = -1;
    String operator = null;

    for (int i = 0; i < ruleString.length(); i++) {
        char ch = ruleString.charAt(i);

        if (ch == '(') {
            openBrackets++;
        } else if (ch == ')') {
            openBrackets--;
        }

        // Find top-level AND/OR (not nested inside parentheses)
        if (openBrackets == 0) {
            if (ruleString.startsWith(" AND ", i)) {
                splitIndex = i;
                operator = "AND";
                break;
            } else if (ruleString.startsWith(" OR ", i)) {
                splitIndex = i;
                operator = "OR";
                break;
            }
        }
    }

    if (splitIndex == -1) {
        // If we didn't find an AND/OR, remove outer parentheses and recurse
        if (ruleString.startsWith("(") && ruleString.endsWith(")")) {
            return parseRuleStringToAST(ruleString.substring(1, ruleString.length() - 1));
        }
        // Otherwise, return an atomic condition as a node
        return new Node(ruleString);
    }

    // Recursive case: split the rule at the top-level AND/OR
    String leftPart = ruleString.substring(0, splitIndex).trim();
    String rightPart = ruleString.substring(splitIndex + operator.length()).trim();

    Node leftNode = parseRuleStringToAST(leftPart);
    Node rightNode = parseRuleStringToAST(rightPart);

    // Return a new node with the operator and two child nodes
    return new Node(operator, leftNode, rightNode);
}

// Helper function to identify atomic conditions like "age > 30"
private boolean isAtomicCondition(String ruleString) {
    return ruleString.matches("\\w+\\s*(>|<|=)\\s*\\w+");
}

	// Helper method to convert a Map to a JSON string
    public static String convertRuleToJson(Object ruleObj) {
        try {
            if (ruleObj instanceof String) {
                // If the rule is already a string, return it as JSON
                return objectMapper.writeValueAsString(ruleObj);
            } else if (ruleObj instanceof Map) {
                // If the rule is a map, convert it directly to JSON
                return objectMapper.writeValueAsString(ruleObj);
            } else {
                throw new IllegalArgumentException("Invalid rule format");
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error converting rule to JSON", e);
        }
    }

}
