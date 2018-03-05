package com.abs.fabric.listener;

import java.io.IOException;

import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.ASN1Integer;
import org.bouncycastle.asn1.ASN1Object;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.DEROctetString;
import org.bouncycastle.asn1.DERSequence;

import com.abs.exception.BusinessException;
import com.abs.exception.ExceptionEnum;
import com.abs.util.SHAHashUtil;
import com.google.protobuf.ByteString;

public class ASN1BlockHeader extends ASN1Object {
	private ASN1Integer number;
	private DEROctetString previousHash;
	private DEROctetString dataHash;
	
	public ASN1BlockHeader(long number, ByteString previousHash, ByteString dataHash) {
		this.number = new ASN1Integer(number);
		this.previousHash = new DEROctetString(previousHash.toByteArray());
		this.dataHash = new DEROctetString(dataHash.toByteArray());
	}
	
	@Override
	public ASN1Primitive toASN1Primitive() {
		ASN1EncodableVector vector = new ASN1EncodableVector();
		vector.add(number);
		vector.add(previousHash);
		vector.add(dataHash);
		return new DERSequence(vector);
	}
	
	public String getSHA256Hash() throws BusinessException {
	    try {
            return SHAHashUtil.encryptSHA256(this.toASN1Primitive().getEncoded());
        } catch (IOException e) {
            throw new BusinessException(ExceptionEnum.BLOCK_HASH_COMPUTE_ERROR);
        }
	}
}
