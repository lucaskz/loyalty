package com.loyalty.challenge.service;

import org.springframework.stereotype.Service;
import com.loyalty.challenge.client.IpClient;
import com.loyalty.challenge.dto.CountryDTO;

@Service
public class IpServiceImpl implements IpService {
	
	private IpClient ipClient;
	
	public IpServiceImpl(IpClient ipClient) {
		this.ipClient = ipClient;
	}
	
	@Override
	public String findCountryCodeByIp(String ip) {
		CountryDTO countryDTO = this.ipClient.call(ip);
		return countryDTO.getCountryCode();
	}
	
}
