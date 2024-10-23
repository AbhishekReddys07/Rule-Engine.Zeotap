//package com.RuleEngine.Controllers;
//
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RestController;
//
//import com.RuleEngine.models.Node;
//import com.fasterxml.jackson.databind.JsonNode;
//import com.fasterxml.jackson.databind.ObjectMapper;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Stack;
//
//@RestController
//public class RuleController {
//
//    @PostMapping("/createNew")
//    public Node createNewRule(@RequestBody String ruleString) {
//        System.out.println("Received rule string: " + ruleString);
//        Node rootNode = buildAST(ruleString);
//        printNode(rootNode, 0); // Print the created Node structure
//        return rootNode;
//    }
//
//    private Node getASTFromRequest(String ruleAst) throws Exception {
//        ObjectMapper objectMapper = new ObjectMapper();
//        JsonNode jsonNode = objectMapper.readTree(ruleAst);
//        return parseNode(jsonNode);
//    }
//
//    private Node parseNode(JsonNode jsonNode) {
//        Node node = new Node();
//        node.setValue(jsonNode.get("value").asText());
//        node.setOperator(jsonNode.get("operator").asText());
//
//        // Process left child if present
//        if (jsonNode.has("left")) {
//            node.setLeft(parseNode(jsonNode.get("left")));
//        }
//
//        // Process right child if present
//        if (jsonNode.has("right")) {
//            node.setRight(parseNode(jsonNode.get("right")));
//        }
//
//        return node;
//    }
//
////    @PostMapping("/evaluateRule")
////    public ResponseEntity<Boolean> evaluateRule(@RequestBody RuleEvaluationRequest request) {
////        try {
////            Node root = getASTFromRequest(request.getRuleAst());
////            ObjectMapper objectMapper = new ObjectMapper();
////            JsonNode data = objectMapper.readTree(request.getData());
////
////            boolean evaluationResult = RuleEvaluator.evaluateRule(root, data);
////            return ResponseEntity.ok(evaluationResult);
////        } catch (Exception e) {
////            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(false);
////        }
////    }
//
//    public Node buildAST(String ruleString) {
//        List<String> tokens = tokenize(ruleString);
//        Stack<Node> nodes = new Stack<>();
//        Stack<String> operators = new Stack<>();
//
//        for (String token : tokens) {
//            if (isCondition(token)) {
//                String operator = getOperator(token);
//                Node conditionNode = new Node(token.trim(), operator, null, null);
//                nodes.push(conditionNode);
//            } else if (token.equals("AND") || token.equals("OR")) {
//                while (!operators.isEmpty() && precedence(operators.peek()) >= precedence(token)) {
//                    Node right = nodes.pop(); // Right operand
//                    Node left = nodes.pop(); // Left operand
//                    String operator = operators.pop(); // Get the operator
//                    nodes.push(new Node("", operator, left, right)); // Create a new node with the operator and operands
//                }
//                operators.push(token); // Push the current operator onto the stack
//            }
//        }
//
//        while (!operators.isEmpty()) {
//            Node right = nodes.pop(); // Right operand
//            Node left = nodes.pop(); // Left operand
//            String operator = operators.pop(); // Get the operator
//            nodes.push(new Node("", operator, left, right)); // Create a new node with the operator and operands
//        }
//
//        if (nodes.isEmpty()) {
//            throw new IllegalArgumentException("Invalid rule format. No nodes created.");
//        }
//
//        return nodes.pop(); // Return the root of the AST
//    }
//
//    private List<String> tokenize(String ruleString) {
//        String[] tokens = ruleString.split(" (?=AND|OR)|(?<=AND|OR) ");
//        List<String> tokenList = new ArrayList<>();
//
//        for (String token : tokens) {
//            token = token.trim();
//            if (!token.isEmpty()) {
//                tokenList.add(token);
//            }
//        }
//
//        System.out.println("Tokens: " + tokenList);
//        return tokenList;
//    }
//
//    private boolean isCondition(String token) {
//        String[] operators = {"<=", "<", ">=", ">", "==", "!=", "="};
//        for (String operator : operators) {
//            if (token.contains(operator)) {
//                return true;
//            }
//        }
//        return false;
//    }
//
//    private String getOperator(String token) {
//        String[] operators = {"<=", "<", ">=", ">", "==", "!=", "="};
//        for (String operator : operators) {
//            if (token.contains(operator)) {
//                return operator; // Return the operator
//            }
//        }
//        return ""; // If no operator found
//    }
//
//    private int precedence(String operator) {
//        if (operator.equals("AND")) {
//            return 1;
//        } else if (operator.equals("OR")) {
//            return 0;
//        }
//        return -1; // For non-operators
//    }
//
//    private void printNode(Node node, int depth) {
//        if (node != null) {
//            String indent = "  ".repeat(depth);
//            if (!node.getOperator().isEmpty()) {
//                System.out.println(indent + "Node: Operator: " + node.getOperator());
//                printNode(node.getLeft(), depth + 1);
//                printNode(node.getRight(), depth + 1);
//            } else {
//                System.out.println(indent + "Node: " + node.getValue() + " Operator: " + node.getOperator());
//            }
//        }
//    }
//}
