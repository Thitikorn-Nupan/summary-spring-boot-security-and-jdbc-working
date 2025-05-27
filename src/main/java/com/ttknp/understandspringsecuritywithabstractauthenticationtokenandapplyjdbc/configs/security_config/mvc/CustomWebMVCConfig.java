package com.ttknp.understandspringsecuritywithabstractauthenticationtokenandapplyjdbc.configs.security_config.mvc;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.converter.xml.MarshallingHttpMessageConverter;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
     Spring WebMvcConfigurer enables developers to customize the default MVC setups in Spring applications
     Request Mapping: Spring MVC maps requests to controllers using logical default patterns. Controller methods are annotated with @RequestMapping, and by default, their method names are used to match URLs.
     DispatcherServlet: This is the main component of Spring MVC. By accepting all incoming requests and forwarding them to the relevant controllers, it serves as a front controller.
     Model Binding: To make processing form submissions easier, Spring MVC automatically ties request parameters to method parameters.
     View Resolution: Spring MVC's default view resolution is intended to be adaptable. It finds the relevant view templates by combining configuration and convention.
*/
@Configuration
public class CustomWebMVCConfig implements WebMvcConfigurer {

    private static final Logger log = LoggerFactory.getLogger(CustomWebMVCConfig.class);

    // it's same @CrossOrigin()
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // WebMvcConfigurer.super.addCorsMappings(registry);
        log.debug("Configuring addCorsMappings");
        registry.addMapping("/api/**")
                .allowedOrigins("http://localhost:4200","http://thitikorn-nupan.com")
                .allowedMethods(RequestMethod.GET.toString(), RequestMethod.POST.toString(), RequestMethod.DELETE.toString(), RequestMethod.PUT.toString(), RequestMethod.OPTIONS.toString());
    }



    /**
     You should have a @Configuration class that implements WebMvcConfigurer. There you can overrides addViewControllers
     If you add this
     @Override
     public void addViewControllers(ViewControllerRegistry theRegistry) {
         theRegistry.addRedirectViewController("/", "swagger-ui.html");
     }
    */


    // Custom Converters Configuration
    /**
     Internally Spring MVC uses a component called a HttpMessageConverter to convert the Http request to an object representation and back
     Now, if there is a need to customize the message converters in some way, Spring Boot makes it simple. As an example consider
     if the POST method in the sample above needs to be little more flexible and should ignore properties which are not present in
     the Hotel entity - typically this can be done by configuring the Jackson ObjectMapper, all that needs to be done with
     Spring Boot is to create a new HttpMessageConverter bean and that would end up overriding all the default message converters,
     this way:
     !!!!
     So you will get error if you use @RequestBody you have to specify @JsonProperty each on Model class
    */
    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        converters.add(mappingJackson2HttpMessageConverter());
    }

    @Bean
    public MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter() {
        MappingJackson2HttpMessageConverter jsonConverter = new MappingJackson2HttpMessageConverter();
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        jsonConverter.setObjectMapper(objectMapper);
        return jsonConverter;
    }


    /**
      By default, Spring MVC uses the Accept header in the HTTP request to determine the client’s media type preference.
      If the client doesn’t specify a preference, the server may use a default media type, such as JSON or XML,
      depending on the configuration.
     */
    @Override
    public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
        configurer.defaultContentType(MediaType.APPLICATION_JSON);
        configurer.mediaType("xml", MediaType.APPLICATION_XML);
        configurer.mediaType("json", MediaType.APPLICATION_JSON);
    }

}
