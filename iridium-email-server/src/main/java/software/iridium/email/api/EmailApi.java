/*
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package software.iridium.email.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import freemarker.cache.ClassTemplateLoader;
import freemarker.cache.TemplateLoader;
import freemarker.template.Configuration;
import java.util.Date;
import java.util.TimeZone;
import javax.annotation.PostConstruct;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

@PropertySource(
    ignoreResourceNotFound = false,
    value = {"classpath:application.properties"})
@ComponentScan(basePackages = {"software.iridium"})
@EnableJpaRepositories(basePackages = {"software.iridium.email.api.repository"})
@EnableScheduling
// @SpringBootApplication(exclude = { SecurityAutoConfiguration.class,
// DataSourceAutoConfiguration.class })
@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
public class EmailApi implements WebMvcConfigurer {

  @Bean
  public WebMvcConfigurer corsConfigurer() {
    return new WebMvcConfigurer() {
      @Override
      public void addCorsMappings(final CorsRegistry registry) {
        registry.addMapping("/**").allowedOrigins("*");
      }
    };
  }

  @Bean
  public PasswordEncoder encoder() {
    return new BCryptPasswordEncoder();
  }

  @PostConstruct
  public void init() {
    TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
    System.out.println("Email application running in UTC timezone :" + new Date());
  }

  public static void main(String... args) throws JsonProcessingException {
    SpringApplication.run(EmailApi.class);
  }

  @Bean
  public FreeMarkerConfigurer freemarkerClassLoaderConfig() {
    Configuration configuration = new Configuration(Configuration.VERSION_2_3_31);
    TemplateLoader templateLoader = new ClassTemplateLoader(this.getClass(), "/templates");
    configuration.setTemplateLoader(templateLoader);
    FreeMarkerConfigurer freeMarkerConfigurer = new FreeMarkerConfigurer();
    freeMarkerConfigurer.setConfiguration(configuration);
    return freeMarkerConfigurer;
  }
}