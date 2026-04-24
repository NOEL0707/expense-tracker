package com.expensetracker.expensetracker;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class ExpensetrackerApplicationTests {

	@Autowired
	private ObjectMapper objectMapper;

	@LocalServerPort
	private int port;

	private final HttpClient httpClient = HttpClient.newHttpClient();

	@Test
	void contextLoads() {
	}

	@Test
	@DisplayName("returns seeded mock users")
	void returnsSeededMockUsers() throws Exception {
		HttpResponse<String> response = httpClient.send(HttpRequest.newBuilder(URI.create(url("/api/users"))).GET().build(), HttpResponse.BodyHandlers.ofString());
		List<Map<String, Object>> users = objectMapper.readValue(response.body(), new TypeReference<>() {});

		assertThat(response.statusCode()).isEqualTo(200);
		assertThat(users).hasSize(3);
		assertThat(users.getFirst().get("id")).isNotNull();
		assertThat(users.getFirst().get("email")).isNotNull();
	}

	@Test
	@DisplayName("requires X-User-Id header for expense reads")
	void requiresUserHeaderForExpenseReads() throws Exception {
		HttpResponse<String> response = httpClient.send(HttpRequest.newBuilder(URI.create(url("/api/expenses"))).GET().build(), HttpResponse.BodyHandlers.ofString());
		Map<String, Object> body = objectMapper.readValue(response.body(), new TypeReference<>() {});

		assertThat(response.statusCode()).isEqualTo(400);
		assertThat(body).containsEntry("error", "Missing required header: X-User-Id");
	}

	@Test
	@DisplayName("returns only expenses for the requested user")
	void returnsOnlyUserExpenses() throws Exception {
		HttpRequest request = HttpRequest.newBuilder(URI.create(url("/api/expenses")))
				.header("X-User-Id", "11111111-1111-1111-1111-111111111111")
				.GET()
				.build();
		HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
		List<Map<String, Object>> expenses = objectMapper.readValue(response.body(), new TypeReference<>() {});

		assertThat(response.statusCode()).isEqualTo(200);
		assertThat(expenses).hasSize(2);
		assertThat(expenses.getFirst().get("category")).isNotNull();
	}

	@Test
	@DisplayName("deduplicates create requests per user and idempotency key")
	void deduplicatesCreateRequestsPerUserAndIdempotencyKey() throws Exception {
		String payload = """
				{
				  "amount": 4200,
				  "category": "Office",
				  "description": "Notebook",
				  "date": "2024-02-01"
				}
				""";

		HttpRequest createRequest = HttpRequest.newBuilder(URI.create(url("/api/expenses")))
				.header("Content-Type", "application/json")
				.header("X-User-Id", "22222222-2222-2222-2222-222222222222")
				.header("Idempotency-Key", "same-request")
				.POST(HttpRequest.BodyPublishers.ofString(payload))
				.build();

		HttpResponse<String> firstCreate = httpClient.send(createRequest, HttpResponse.BodyHandlers.ofString());
		HttpResponse<String> secondCreate = httpClient.send(createRequest, HttpResponse.BodyHandlers.ofString());
		Map<String, Object> firstBody = objectMapper.readValue(firstCreate.body(), new TypeReference<>() {});
		Map<String, Object> secondBody = objectMapper.readValue(secondCreate.body(), new TypeReference<>() {});

		assertThat(firstCreate.statusCode()).isEqualTo(201);
		assertThat(secondCreate.statusCode()).isEqualTo(201);
		assertThat(firstBody).containsEntry("category", "Office");
		assertThat(secondBody).containsEntry("category", "Office");
		assertThat(firstBody.get("id")).isEqualTo(secondBody.get("id"));

		HttpRequest listRequest = HttpRequest.newBuilder(URI.create(url("/api/expenses")))
				.header("X-User-Id", "22222222-2222-2222-2222-222222222222")
				.GET()
				.build();
		HttpResponse<String> expensesResponse = httpClient.send(listRequest, HttpResponse.BodyHandlers.ofString());
		List<Map<String, Object>> expenses = objectMapper.readValue(expensesResponse.body(), new TypeReference<>() {});

		assertThat(expensesResponse.statusCode()).isEqualTo(200);
		assertThat(expenses).hasSize(2);
	}

	private String url(String path) {
		return "http://localhost:" + port + path;
	}
}
