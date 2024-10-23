package com.RuleEngine.Zeptap;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication(scanBasePackages = { "com.RuleEngine.Controllers", "com.RuleEngine.models",
		"com.RuleEngine.services", "com.RuleEngine.Repositories" })
@EntityScan(basePackages = "com.RuleEngine.models") // Specify the package containing your entities
public class ZeptapApplication {
	public static void main(String[] args) {
		SpringApplication.run(ZeptapApplication.class, args);
	}
}
