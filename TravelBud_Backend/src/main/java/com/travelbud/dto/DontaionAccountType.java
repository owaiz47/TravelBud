package com.travelbud.dto;

import com.fasterxml.jackson.annotation.JsonValue;

public enum DontaionAccountType {
	UPI, PHONEPE, PAYTM, GOOGLEPAY, AMAZONPAY, PAYPAL;

	@JsonValue
	public int toValue() {
		return ordinal();
	}
	
	public String getImage() {
		if(this == DontaionAccountType.UPI) {
			return "https://upload.wikimedia.org/wikipedia/commons/thumb/e/e1/UPI-Logo-vector.svg/1024px-UPI-Logo-vector.svg.png";
		}else if(this == DontaionAccountType.PHONEPE) {
			return "https://i.pinimg.com/736x/19/29/17/1929176785bcaf86ef6518447e5f6914.jpg";
		}else if(this == DontaionAccountType.PAYTM) {
			return "https://cdn-icons-png.flaticon.com/512/825/825454.png";
		}else if(this == DontaionAccountType.GOOGLEPAY) {
			return "https://www.nicepng.com/png/detail/769-7692873_download-google-pay-logo-png.png";
		}else if(this == DontaionAccountType.AMAZONPAY) {
			return "https://cdn4.iconfinder.com/data/icons/circle-payment/32/payment_006-amazon-512.png";
		}else if(this == DontaionAccountType.PAYPAL) {
			return "http://pngimg.com/uploads/paypal/paypal_PNG22.png";
		}else {
			return "";
		}
	}
}
