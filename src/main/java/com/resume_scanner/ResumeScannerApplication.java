package com.resume_scanner;


import com.resume_scanner.Data.KeyWords;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.FileNotFoundException;

@SpringBootApplication
public class ResumeScannerApplication {

	public static void main(String[] args) throws FileNotFoundException {
		SpringApplication.run(ResumeScannerApplication.class, args);


	}

}
