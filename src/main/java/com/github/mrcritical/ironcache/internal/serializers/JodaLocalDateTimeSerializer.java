package com.github.mrcritical.ironcache.internal.serializers;

import java.io.IOException;

import org.joda.time.DateTimeZone;
import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

/**
 * Serialize a Joda {@link LocalDateTime}.
 *
 * @author pjarrell
 *
 */
public class JodaLocalDateTimeSerializer extends StdSerializer<LocalDateTime> {

	private final DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss'Z'").withZone(
			DateTimeZone.UTC);

	public JodaLocalDateTimeSerializer() {
		super(LocalDateTime.class);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.codehaus.jackson.map.JsonSerializer#serialize(java.lang.Object,
	 * org.codehaus.jackson.JsonGenerator,
	 * org.codehaus.jackson.map.SerializerProvider)
	 */
	@Override
	public void serialize(final LocalDateTime value, final JsonGenerator jgen, final SerializerProvider provider)
			throws IOException, JsonProcessingException {
		jgen.writeString(value.toString(formatter));
	}

}