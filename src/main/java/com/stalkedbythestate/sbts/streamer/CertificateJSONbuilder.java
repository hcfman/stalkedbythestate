package com.stalkedbythestate.sbts.streamer;

// Copyright (c) 2021 Kim Hendrikse

import com.stalkedbythestate.sbts.json.CertificateJSON;
import com.stalkedbythestate.sbts.json.Dn;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.MessageDigest;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.security.interfaces.RSAPublicKey;

public final class CertificateJSONbuilder {
    private static final Logger logger = LoggerFactory
            .getLogger(CertificatesGetJSON.class);

    private void byte2hex(byte b, StringBuffer buf) {
        char[] hexChars = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                'A', 'B', 'C', 'D', 'E', 'F'};
        int high = ((b & 0xf0) >> 4);
        int low = (b & 0x0f);
        buf.append(hexChars[high]);
        buf.append(hexChars[low]);
    }

    private String toHexString(byte[] block) {
        StringBuffer buf = new StringBuffer();
        int len = block.length;
        for (int i = 0; i < len; i++) {
            byte2hex(block[i], buf);
            if (i < len - 1) {
                buf.append(":");
            }
        }
        return buf.toString();
    }

    private String getCertFingerPrint(String mdAlg, Certificate cert)
            throws Exception {
        byte[] encCertInfo = cert.getEncoded();
        MessageDigest md = MessageDigest.getInstance(mdAlg);
        byte[] digest = md.digest(encCertInfo);
        return toHexString(digest);
    }


    public CertificateJSON build(String alias, X509Certificate x509Certificate) {
        CertificateJSON certificateJSON = new CertificateJSON();

        certificateJSON.setAlias(alias);
        certificateJSON.setValidFrom(x509Certificate
                .getNotBefore().toString());
        certificateJSON.setValidTo(x509Certificate
                .getNotAfter().toString());

        String issuerDnString = x509Certificate.getIssuerDN()
                .toString();
        Dn issuer = Dn.parseDn(issuerDnString);
        certificateJSON.setIssuer(issuer);

        String ownerDnString = x509Certificate.getSubjectDN()
                .toString();
        Dn owner = Dn.parseDn(ownerDnString);
        certificateJSON.setOwner(owner);

        certificateJSON.setKeyAlg(x509Certificate
                .getPublicKey().getAlgorithm());
        if (x509Certificate.getPublicKey() instanceof RSAPublicKey) {
            RSAPublicKey rsaPublicKey = (RSAPublicKey) x509Certificate
                    .getPublicKey();
            certificateJSON.setKeySize(rsaPublicKey
                    .getModulus().bitLength());
        }
        certificateJSON.setSerial(x509Certificate
                .getSerialNumber().toString(16).toUpperCase()
                .replaceAll("((?!^.)..)(?=(?:..)*$)", ":$1")
                .replaceAll("^(.(?:...)*)$", "0$1"));

        try {
            certificateJSON
                    .setFingerprint("MD5: "
                            + getCertFingerPrint("MD5",
                            x509Certificate)
                            + "\nSHA: "
                            + getCertFingerPrint("SHA",
                            x509Certificate));
        } catch (Exception e1) {
            logger.error("Exception setting cert fingerprint");
            e1.printStackTrace();
        }

        return certificateJSON;
    }
}
