package akki;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableAutoConfiguration(exclude = { SecurityAutoConfiguration.class })
@EnableCaching
@ComponentScan(basePackages = { "akki" })
public class Application {
    static {
        // IPv4 を優先する
        // https://docs.oracle.com/javase/jp/7/api/java/net/doc-files/net-properties.html
        System.setProperty("java.net.preferIPv4Stack", "true");
    }

    /**
     * Main method to run the spring application
     *
     * @param args
     */
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
