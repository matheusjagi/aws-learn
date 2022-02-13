package br.com.jagi.aws_learn;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories("br.com.jagi.aws_learn.repository")
public class AwsLearnApplication {

	public static void main(String[] args) {
		SpringApplication.run(AwsLearnApplication.class, args);
	}

}
