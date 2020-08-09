package com.loyalty.challenge.validation;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;
import com.loyalty.challenge.dto.IpDTO;

/**
 * Validates the ip in the body request from external clients.
 *
 * @author Lucas Kaseta
 */

@Component
public class IpValidator implements Validator {
	
	@Override
	public boolean supports(Class<?> aClass) {
		return IpDTO.class.equals(aClass);
	}
	
	@Override
	public void validate(Object o, Errors errors) {
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "ip", "Ip is empty");
		
		IpDTO ipDTO = (IpDTO) o;
		
		if (!validIp(ipDTO.getIp())) {
			errors.reject("Invalid Ip");
		}
	}
	
	private static boolean validIp(String ip) {
		try {
			if (ip == null || ip.isEmpty()) {
				return false;
			}
			
			String[] parts = ip.split("\\.");
			if (parts.length != 4) {
				return false;
			}
			
			for (String s : parts) {
				int i = Integer.parseInt(s);
				if ((i < 0) || (i > 255)) {
					return false;
				}
			}
			return !ip.endsWith(".");
		} catch (NumberFormatException nfe) {
			return false;
		}
	}
	
}
