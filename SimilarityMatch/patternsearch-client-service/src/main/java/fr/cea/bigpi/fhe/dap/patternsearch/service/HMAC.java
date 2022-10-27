package fr.cea.bigpi.fhe.dap.patternsearch.service;

import org.apache.commons.codec.digest.HmacUtils;
import org.springframework.stereotype.Service;

@Service
public class HMAC {
	
	public String hmacWithApacheCommons(String algorithm, String data, String key) {
		String hmac = new HmacUtils(algorithm, key).hmacHex(data);
		return hmac;
	}

}
