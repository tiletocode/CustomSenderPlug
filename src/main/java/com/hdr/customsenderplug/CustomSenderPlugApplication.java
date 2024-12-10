package com.hdr.customsenderplug;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.hdr.customsenderplug.receiver.ReceiverApm;
import com.hdr.customsenderplug.receiver.ReceiverDb;
import com.hdr.customsenderplug.receiver.ReceiverInfra;
import com.hdr.customsenderplug.receiver.ReceiverK8s;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootApplication
@EnableScheduling
public class CustomSenderPlugApplication {
	
	@Autowired
    AutowireCapableBeanFactory beanFactory; 

	Config config = Config.getConfig();
	
	// @Bean
	// public ServletRegistrationBean<ReceiverInfra> ReceiverInfraServletRegistrationBean() {
	// 	log.info("Config file \n" + config.toString());
		
	// 	ServletRegistrationBean srb = new ServletRegistrationBean();
	// 	final ReceiverInfra servlet = new ReceiverInfra();
	// 	beanFactory.autowireBean(servlet); // <--- The most important part
	// 	srb.setServlet(servlet);
	// 	srb.setUrlMappings(Arrays.asList(config.getString("webhook.server.infra.uri", "/webhook_infra")));
	// 	return srb;
	// }
	@Bean
    public ServletRegistrationBean<ReceiverInfra> ReceiverInfraServletRegistrationBean() {
		log.info("Config file \n" + config.toString());

        ServletRegistrationBean<ReceiverInfra> srb = new ServletRegistrationBean<>();
        final ReceiverInfra servlet = new ReceiverInfra();
        beanFactory.autowireBean(servlet);
        srb.setServlet(servlet);
        srb.setUrlMappings(Arrays.asList(config.getString("webhook.server.infra.uri", "/webhook_infra")));
        return srb;
    }
	@Bean
    public ServletRegistrationBean<ReceiverApm> ReceiverApmServletRegistrationBean() {
		log.info("Config file \n" + config.toString());

        ServletRegistrationBean<ReceiverApm> srb = new ServletRegistrationBean<>();
        final ReceiverApm servlet = new ReceiverApm();
        beanFactory.autowireBean(servlet);
        srb.setServlet(servlet);
        srb.setUrlMappings(Arrays.asList(config.getString("webhook.server.apm.uri", "/webhook_apm")));
        return srb;
    }
	@Bean
    public ServletRegistrationBean<ReceiverDb> ReceiverDbServletRegistrationBean() {
		log.info("Config file \n" + config.toString());

        ServletRegistrationBean<ReceiverDb> srb = new ServletRegistrationBean<>();
        final ReceiverDb servlet = new ReceiverDb();
        beanFactory.autowireBean(servlet);
        srb.setServlet(servlet);
        srb.setUrlMappings(Arrays.asList(config.getString("webhook.server.db.uri", "/webhook_db")));
        return srb;
    }
	@Bean
    public ServletRegistrationBean<ReceiverK8s> ReceiverK8sServletRegistrationBean() {
		log.info("Config file \n" + config.toString());

        ServletRegistrationBean<ReceiverK8s> srb = new ServletRegistrationBean<>();
        final ReceiverK8s servlet = new ReceiverK8s();
        beanFactory.autowireBean(servlet);
        srb.setServlet(servlet);
        srb.setUrlMappings(Arrays.asList(config.getString("webhook.server.k8s.uri", "/webhook_k8s")));
        return srb;
    }

	public static void main(String[] args) {
		SpringApplication.run(CustomSenderPlugApplication.class, args);
	}

}
