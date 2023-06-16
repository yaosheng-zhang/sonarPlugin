package com.zhangys.carplugin;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zhangys.carplugin.Service.RestTemplateToInterface;
import com.zhangys.carplugin.Utils.CppMethodExtractor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@SpringBootTest
class CarPluginApplicationTests {
	private final static String BASIC_PATH="/data/jenkins_home/";

	@Test
	void contextLoads() {
		String path = "misra_test:Rule_13.3_Advisory.c";
		String projectName = path.substring(0, path.indexOf(':'));
		String  fileName= path.substring(path.indexOf(':')+1);
		String adr = BASIC_PATH+projectName+'/'+fileName;
		System.out.println(adr);
	}
	@Autowired
	RestTemplate restTemplate;

	@Test
	void testRestTemplate() throws UnsupportedEncodingException {

		try {
			String username = "admin";
			String password = "mustang";
			String encoding = Base64.getEncoder().encodeToString((username + ":" + password).getBytes(StandardCharsets.UTF_8));

			HttpHeaders headers = new HttpHeaders();
			headers.set("Authorization", "Basic " + encoding);

			HttpEntity<String> entity = new HttpEntity<>(null, headers);

			String url = "http://192.168.3.13:19000/api/issues/search?componentKeys=c_test&s=FILE_LINE&resolved=false&tags=misra-c2012&ps=100&additionalFields=_all&timeZone=Asia%2FShanghai";

			RestTemplate restTemplate = new RestTemplate();
			ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
			String responseBody = response.getBody();
			ObjectMapper mapper = new ObjectMapper();
			Object jsonData = mapper.readValue(responseBody, Object.class);
			String jsonFormatted = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(jsonData);

			System.out.println("Response JSON: " + jsonFormatted);
		} catch (Exception e) {
			e.printStackTrace();
		}
		}



}
