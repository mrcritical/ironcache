package com.github.mrcritical.ironcache.serializers;

import java.io.IOException;

import org.joda.time.DateTimeZone;
import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

/**
 * Serialize a Joda {@link LocalDateTime}.
 * 
 * @author pjarrell
 * 
 */
public class JodaDateTimeSerializer extends JsonSerializer<LocalDateTime> {

	private final DateTimeFormatter formatter = DateTimeFormat.forPattern(
			"yyyy-MM-dd'T'HH:mm:ss'Z'").withZone(DateTimeZone.UTC);

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.codehaus.jackson.map.JsonSerializer#serialize(java.lang.Object,
	 * org.codehaus.jackson.JsonGenerator,
	 * org.codehaus.jackson.map.SerializerProvider)
	 */
	@Override
	public void serialize(final LocalDateTime value, final JsonGenerator jgen,
			final SerializerProvider provider) throws IOException,
			JsonProcessingException {
		jgen.writeString(value.toString(formatter));
	}

}