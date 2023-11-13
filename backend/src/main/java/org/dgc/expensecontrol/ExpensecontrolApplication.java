package org.dgc.expensecontrol;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ExpensecontrolApplication {

	public static void main(String[] args) {
		SpringApplication.run(ExpensecontrolApplication.class, args);
	}

}
