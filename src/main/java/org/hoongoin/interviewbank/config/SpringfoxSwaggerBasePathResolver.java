package org.hoongoin.interviewbank.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.servers.Server;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import springfox.documentation.oas.web.OpenApiTransformationContext;
import springfox.documentation.oas.web.WebMvcOpenApiTransformationFilter;
import springfox.documentation.spi.DocumentationType;

import javax.servlet.http.HttpServletRequest;

import java.util.List;

@Component
public class SpringfoxSwaggerBasePathResolver implements WebMvcOpenApiTransformationFilter {

	@Value("${spring.swagger.server.url}")
	private String url;

	@Override
	public OpenAPI transform(OpenApiTransformationContext<HttpServletRequest> context) {
		OpenAPI openApi = context.getSpecification();
		Server localServer = new Server();
		localServer.setUrl(url);
		openApi.setServers(List.of(localServer));
		return openApi;
	}

	@Override
	public boolean supports(DocumentationType documentationType) {
		return documentationType.equals(DocumentationType.OAS_30);
	}
}
