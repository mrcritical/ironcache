package com.github.mrcritical.ironcache.serializers;

import java.io.IOException;

import org.joda.time.DateTimeZone;
import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

/**
 * Deserialize a Joda {@link LocalDateTime}.
 * 
 * @author pjarrell
 * 
 */
public class JodaDateTimeDeserializer extends JsonDeserializer<LocalDateTime> {

	private final DateTimeFormatter formatter = DateTimeFormat.forPattern(
			"yyyy-MM-dd'T'HH:mm:ss'Z'").withZone(DateTimeZone.UTC);

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.codehaus.jackson.map.JsonDeserializer#deserialize(org.codehaus
	 * .jackson .JsonParser, org.codehaus.jackson.map.DeserializationContext)
	 */
	@Override
	public LocalDateTime deserialize(final JsonParser parser,
			final DeserializationContext context) throws IOException,
			JsonProcessingException {
		return formatter.parseLocalDateTime(parser.getText());
	}

}