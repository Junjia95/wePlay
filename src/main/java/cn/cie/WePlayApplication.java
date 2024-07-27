package cn.cie;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableTransactionManagement
@MapperScan("cn.cie.mapper")
@SpringBootApplication
public class WePlayApplication {

    public static void main(String[] args) {
        SpringApplication.run(WePlayApplication.class, args);
    }

}
