package com.processor.tax;

import java.math.RoundingMode;
import java.text.DecimalFormat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.processor.tax.service.Receipt;

@SpringBootApplication
public class SalestaxcalculatorApplication implements CommandLineRunner {
	@Autowired
	private Receipt receipt;

	public static void main(String[] args) {
		SpringApplication.run(SalestaxcalculatorApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		String result = receipt.callLoader();
		System.out.println(result);

	}

}
