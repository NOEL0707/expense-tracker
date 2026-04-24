package com.expensetracker.expensetracker.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.parameters.Parameter;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springdoc.core.customizers.OpenApiCustomizer;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Value("${server.servlet.context-path:}")
    private String contextPath;

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .servers(List.of(new Server()
                        .url(contextPath.isBlank() ? "/" : contextPath)
                        .description("Base path for the Expense Tracker API")))
                .components(new Components()
                        .addParameters("UserIdHeader", new Parameter()
                                .in("header")
                                .required(true)
                                .name("X-User-Id")
                                .description("User UUID used to scope expenses to a specific user")))
                .info(new Info()
                        .title("Expense Tracker API")
                        .version("1.0")
                        .description("API for managing expenses with seeded mock users and per-user scoping"));
    }

    @Bean
    public OpenApiCustomizer userHeaderCustomizer() {
        return openApi -> {
            if (openApi.getPaths() == null) {
                return;
            }

            openApi.getPaths().forEach((path, pathItem) -> {
            if (path.startsWith("/expenses")) {
                pathItem.readOperations().forEach(this::addUserHeaderIfMissing);
            }
        });
        };
    }

    private void addUserHeaderIfMissing(Operation operation) {
        if (operation.getParameters() == null || operation.getParameters().stream().noneMatch(this::isUserIdHeaderParameter)) {
            operation.addParametersItem(new Parameter().$ref("#/components/parameters/UserIdHeader"));
        }
    }

    private boolean isUserIdHeaderParameter(Parameter parameter) {
        return "#/components/parameters/UserIdHeader".equals(parameter.get$ref())
                || "X-User-Id".equalsIgnoreCase(parameter.getName());
    }
}
