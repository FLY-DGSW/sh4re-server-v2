package sh4re_v2.sh4re_v2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class Sh4reV2Application {

	public static void main(String[] args) {
		SpringApplication.run(Sh4reV2Application.class, args);
	}

}
