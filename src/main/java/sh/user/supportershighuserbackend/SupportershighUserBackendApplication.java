package sh.user.supportershighuserbackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class SupportershighUserBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(SupportershighUserBackendApplication.class, args);
        System.out.println("======================= [Application Active] =======================");
    }

}
