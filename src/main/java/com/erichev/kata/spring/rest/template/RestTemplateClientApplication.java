package com.erichev.kata.spring.rest.template;

import com.erichev.kata.spring.rest.template.entity.User;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;

//@SpringBootApplication
public class RestTemplateClientApplication {

	static final String addressUrl = "http://94.198.50.185:7081/api/";
	static final RestTemplate restTemplate = new RestTemplate();

	static User userOne = new User(3L,"James","Brown", (byte) 20);
	static User userTwo = new User(3L,"Thomas","Shelby", (byte) 20);

	public static void main(String[] args) {
		System.out.println(stringOne(madeCookie()));
		//SpringApplication.run(RestTemplateClientApplication.class, args);
	}

	public static String stringOne(HttpHeaders headers) {
		return createUser(userOne,headers).getBody() + updateUser(userTwo, headers).getBody() + deleteUser(3, headers).getBody();
	}

	public static HttpHeaders madeCookie() {
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(List.of(MediaType.APPLICATION_JSON));
		headers.setContentType(MediaType.APPLICATION_JSON);
		String cookis = getAllUser();
		headers.add("Cookie", cookis);
		return headers;
	}

	public static String getAllUser() {
		ResponseEntity<String> responseEntity = restTemplate.exchange(addressUrl + "users", HttpMethod.GET, new HttpEntity<>(new User()), String.class);
		String user = responseEntity.getBody();
		System.out.println("response body - " + user);
		HttpHeaders responseHeaders = responseEntity.getHeaders();
		String cookie = responseHeaders.getFirst("Set-Cookie");
		System.out.println("Cookie: " + cookie);
		return cookie;
	}

	private static ResponseEntity<String> createUser(User user,HttpHeaders headers) {
		HttpEntity<User> requestEntity = new HttpEntity<>(user, headers);
		return restTemplate.exchange(addressUrl + "users", HttpMethod.POST, requestEntity, String.class);
	}

	private static ResponseEntity<String> updateUser(User user,HttpHeaders headers) {
		HttpEntity<User> requestEntity = new HttpEntity<>(user, headers);
		return restTemplate.exchange(addressUrl + "users", HttpMethod.PUT, requestEntity, String.class);

	}

	private static ResponseEntity<String> deleteUser(int id,HttpHeaders headers) {
		HttpEntity<User> requestEntity = new HttpEntity<>(headers);
		return restTemplate.exchange(addressUrl + "users/" + id, HttpMethod.DELETE, requestEntity, String.class);
	}

}