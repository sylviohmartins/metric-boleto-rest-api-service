package io.sylviohmartins.metric.configuration;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import java.net.URL;

@Getter
@Setter
@Validated
@Component
@ConfigurationProperties(prefix = "application")
public class ApplicationConfiguration {

	@Valid
	private final Metadata metadata = new Metadata();

	@Getter
	@Setter
	public static class Metadata {

		@NotNull
		private URL url;

		@NotBlank
		private String name;

		@NotNull
		private URL supportUrl;

		@NotBlank
		private String organizationName;

		@NotBlank
		private String organizationAddress;

		@NotBlank
		private String mailFrom;

	}

}
