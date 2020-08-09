package com.loyalty.challenge.deserializer;

import java.io.IOException;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.loyalty.challenge.model.Country;
import com.loyalty.challenge.model.Currency;

/**
 * Deserializes a JSON response into a {@link Country}.
 * Called from {@link com.loyalty.challenge.client.CountryClient}
 *
 * @author Lucas Kaseta
 */

public class CountryDeserializer extends JsonDeserializer<Country> {
	
	@Override
	public Country deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
		Country country = new Country();
		ObjectCodec oc = jsonParser.getCodec();
		JsonNode node = oc.readTree(jsonParser);
		country.setCountryName(node.get("name").textValue());
		country.setIsoCode(node.get("alpha2Code").textValue());
		country.setCurrencies(this.deserializeCurrencies(node));
		country.setLanguages(this.deserializeLanguages(node));
		country.setTimeZones(this.deserializeTimeZones(node));
		Iterator<JsonNode> latLng = node.get("latlng").elements();
		country.setLat(latLng.next().asDouble());
		country.setLng(latLng.next().asDouble());
		return country;
	}
	
	private List<ZoneOffset> deserializeTimeZones(JsonNode node) {
		List<ZoneOffset> result = new ArrayList<>();
		Iterator<JsonNode> timeZones = node.get("timezones").elements();
		while (timeZones.hasNext()) {
			result.add(this.resolveTimeZone(timeZones.next().asText()));
		}
		return result;
	}
	
	private ZoneOffset resolveTimeZone(String timeZone) {
		String time = timeZone.replace("UTC", "");
		return time.isEmpty() ? ZoneOffset.UTC : ZoneOffset.of(time);
	}
	
	private Map<String, String> deserializeLanguages(JsonNode node) {
		Map<String, String> result = new HashMap<>();
		Iterator<JsonNode> languages = node.get("languages").elements();
		while (languages.hasNext()) {
			JsonNode languageNode = languages.next();
			result.put(languageNode.get("iso639_1").asText(), languageNode.get("nativeName").asText());
		}
		return result;
	}
	
	private List<Currency> deserializeCurrencies(JsonNode node) {
		List<Currency> result = new ArrayList<>();
		Iterator<JsonNode> currencies = node.get("currencies").elements();
		while (currencies.hasNext()) {
			Currency currency = new Currency();
			JsonNode currencyNode = currencies.next();
			currency.setCode(currencyNode.get("code").asText());
			currency.setName(currencyNode.get("name").asText());
			result.add(currency);
		}
		return result;
	}
}
