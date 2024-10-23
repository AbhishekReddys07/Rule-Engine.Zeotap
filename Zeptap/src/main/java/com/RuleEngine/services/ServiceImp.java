package com.RuleEngine.services;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.RuleEngine.Repositories.RuleRepository;
import com.RuleEngine.models.Rule;

@Service
public class ServiceImp implements com.RuleEngine.services.Service {
	  @Autowired
	    private RuleRepository ruleRepo;

	    @Transactional
	    @Override
	    public void Save(String str,Map<String, Object> ruleData) {
	        System.err.println("Saving rule data: " + ruleData);

	        // Here we convert the map to an entity (Rule) and save it
	        Rule rootRule = mapToEntity(ruleData, str); // root rule with no parent
	        ruleRepo.saveRule(rootRule);
	    }	

	    // Recursive method to convert map to Rule entity
	    private Rule mapToEntity(Map<String, Object> ruleMap, String parentRuleString) {
	        Rule rule = new Rule();

	        // Set the operator and value from the map
	        rule.setOperator((String) ruleMap.get("operator"));
	        rule.setValue((String) ruleMap.get("value"));

	        // Use the parent's rule string to set the current rule's string representation
	        String currentRuleString = (parentRuleString != null ? parentRuleString + " " : "") 
	                                   + rule.getValue() + " " + rule.getOperator();
	        rule.setRuleString(currentRuleString); // Set the string representation of the current rule

	        // Recursively save left and right child nodes if they exist
	        if (ruleMap.get("left") != null) {
	            @SuppressWarnings("unchecked")
	            Rule leftRule = mapToEntity((Map<String, Object>) ruleMap.get("left"), currentRuleString);
	            rule.setLeftChild(leftRule);
	        }
	        if (ruleMap.get("right") != null) {
	            @SuppressWarnings("unchecked")
	            Rule rightRule = mapToEntity((Map<String, Object>) ruleMap.get("right"), currentRuleString);
	            rule.setRightChild(rightRule);
	        }

	        return rule;
	    }


}
