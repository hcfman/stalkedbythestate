package com.stalkedbythestate.sbts.sbtsdevice.config;

import java.util.ArrayList;
import java.util.List;

public enum EncryptionType {
	ENC_PLAIN, ENC_TLS, ENC_SSL;

	public static EncryptionType set(String value) throws RuntimeException {
		if (value.equals("ENC_PLAIN"))
			return ENC_PLAIN;
		if (value.equals("ENC_TLS"))
			return ENC_TLS;
		if (value.equals("ENC_SSL"))
			return ENC_SSL;
		throw new RuntimeException(
				"Invalid value specified for EncryptionType Enum: " + value);
	}

	public String getEncryptionType() {
		return this.toString().replaceAll("^ENC_", "").replaceAll("_", " ");
	}

	public static List<String> getEncryptionTypeList() {
		List<String> encryptionTypeList = new ArrayList<String>();
		for (EncryptionType encryptionType : EncryptionType.values()) {
			encryptionTypeList.add(encryptionType.toString()
					.replaceAll("^ENC_", "").replaceAll("_", " "));
		}

		return encryptionTypeList;
	}

}
