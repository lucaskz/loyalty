package com.loyalty.challenge.transformer;

import static com.loyalty.challenge.constant.CurrencyConstants.BASE_CURRENCY_EXCHANGE;

import java.text.SimpleDateFormat;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;
import com.loyalty.challenge.dto.TrackingDTO;
import com.loyalty.challenge.model.Currency;
import com.loyalty.challenge.model.Tracking;

@Component
public class TrackingTransformer {
	
	private static SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
	private static SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm:ss");
	private static final String ZONE_OFFSET_ZERO = "+00:00";
	
	public TrackingDTO transform(Tracking tracking) {
		TrackingDTO trackingDTO = new TrackingDTO();
		trackingDTO.setIp(tracking.getIp());
		Date date = new Date(tracking.getDate().toInstant().toEpochMilli());
		trackingDTO.setIsoCode(tracking.getCountry().getIsoCode());
		trackingDTO.setDate(String.format("%s %s", dateFormat.format(date), timeFormat.format(date)));
		trackingDTO.setCountry(String.format("%s (%s)", tracking.getCountry().getCountryName(), tracking.getCountry().getCountryCode()));
		trackingDTO.setEstimatedDistance(String.format("%s kms", Double.valueOf(tracking.getCountry().getDistance()).intValue()));
		trackingDTO.setCurrencies(this.transformCurrencies(tracking.getCountry().getCurrencies()));
		trackingDTO.setLanguages(this.transformLanguages(tracking.getCountry().getLanguages()));
		trackingDTO.setTimes(this.transformTimes(tracking.getDate(), tracking.getCountry().getTimeZones()));
		return trackingDTO;
	}
	
	private List<String> transformTimes(OffsetDateTime trackedOffsetDateTime, List<ZoneOffset> timeZones) {
		List<String> result = new ArrayList<>();
		timeZones.forEach(zoneOffset -> {
			Date date = new Date(trackedOffsetDateTime.withOffsetSameInstant(zoneOffset).toInstant().toEpochMilli());
			result.add(String.format("%s %s(%s UTC)", dateFormat.format(date), timeFormat.format(date), this.resolveZoneOffset(zoneOffset)));
		});
		return result;
	}
	
	private String resolveZoneOffset(ZoneOffset zoneOffset) {
		if (ZoneOffset.UTC.equals(zoneOffset)) {
			return ZONE_OFFSET_ZERO;
		}
		return zoneOffset.getId();
	}
	
	private List<String> transformLanguages(Map<String, String> languages) {
		List<String> result = new ArrayList<>();
		languages.forEach((name, code) -> result.add(String.format("%s (%s)", name, code)));
		return result;
	}
	
	private List<String> transformCurrencies(List<Currency> currencies) {
		List<String> result = new ArrayList<>();
		currencies.forEach(c -> result.add(this.resolveQuoteMessage(c)));
		return result;
	}
	
	private String resolveQuoteMessage(Currency c) {
		if (c.getQuote() != null) {
			return String.format("%s (1 %s = %s %s)", c.getCode(), c.getCode(), c.getQuote(), BASE_CURRENCY_EXCHANGE);
		}
		return String.format("%s", c.getCode());
	}
}
