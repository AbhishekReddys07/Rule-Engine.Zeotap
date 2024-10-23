package com.RuleEngine.Repositories;

import com.RuleEngine.models.Rule;
import java.util.List;

public interface RuleRepository {
    void saveRule(com.RuleEngine.models.Rule rootRule);
    
    List<Rule> findByRuleStrings(List<String> ruleStrings); // New method
}
