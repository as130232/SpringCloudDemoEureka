//package com.cimforce.apigateway.config;
//
//import java.io.File;
//
//import javax.servlet.MultipartConfigElement;
//
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.boot.web.servlet.MultipartConfigFactory;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//@Configuration
//public class FileUploadConfig {
//
//	/**
//	 * Spring 加載文件上傳配置方法
//	 * @author eric.wang
//	 * @date 2018年5月21日 下午4:56:19
//	 */
//	@Bean
//	public MultipartConfigElement mutipartConfigElement(@Value("${file.upload.maxFileSize}") String maxFileSize,
//			@Value("${file.upload.maxRequestSize}") String maxRequestSize) {
//
//		MultipartConfigFactory factory = new MultipartConfigFactory();
//
//		// 設置上傳大小
//		factory.setMaxFileSize(maxFileSize);
//
//		// 設置請求大小
//		factory.setMaxRequestSize(maxRequestSize);
//
//		// 設置上傳臨時文件地址
//		String tempLocation = System.getProperty("user.dir") + "/data/tmp";
//
//		File file = new File(tempLocation);
//
//		if (!file.exists()) {
//
//			file.mkdirs();
//		}
//
//		factory.setLocation(tempLocation);
//
//		return factory.createMultipartConfig();
//
//	}
//}
