package utils;

import ws.empirelimo.R;

import com.nabancard.sdkadvanced.CustomizeSDKAdvanced;
import com.nabancard.sdkadvanced.SDKAdvancedCallbacks;

import android.content.Context;
import android.graphics.Color;

public class PayAnywhere {

	public Context context;
	public CustomizeSDKAdvanced sdkAdvanced;
	public String merchantId;
	public String loginId;
	public String username;
	public String password;

	public PayAnywhere(Context context, CustomizeSDKAdvanced sdkAdvanced,
			String merchantId, String loginId, String username, String password) {

		this.context = context;

		sdkAdvanced = CustomizeSDKAdvanced.getInstance(context,
				(SDKAdvancedCallbacks) context);
		this.sdkAdvanced = sdkAdvanced;
		this.merchantId = merchantId;
		this.loginId = loginId;
		this.username = username;
		this.password = password;
		customizeSDKAdvanced();

	}

	private void customizeSDKAdvanced() {

		// Mandatory Fields
		sdkAdvanced.setMerchantId(merchantId);
		sdkAdvanced.setLoginId(loginId);
		sdkAdvanced.setUsername(username);
		sdkAdvanced.setPassword(password);
		sdkAdvanced.setApplicationName(context.getResources().getString(
				R.string.app_name));

		// UI Fields
		sdkAdvanced.setBackButtonDrawable(
				context.getResources().getDrawable(R.drawable.back), context
						.getResources().getDrawable(R.drawable.back_hover));
		sdkAdvanced.setBackgroundColorEnabled(false);
		sdkAdvanced.setBackgroundColor(Color.RED);
		sdkAdvanced.setPortraitBackgroundDrawable(context.getResources()
				.getDrawable(R.drawable.portrait_temp));
		sdkAdvanced.setLandscapeBackgroundDrawable(context.getResources()
				.getDrawable(R.drawable.landscape_temp));
		sdkAdvanced.setChargeButtonDrawable(
				context.getResources().getDrawable(R.drawable.charge), context
						.getResources().getDrawable(R.drawable.charge_hover));
		sdkAdvanced.setEnterCardButtonDrawable(context.getResources()
				.getDrawable(R.drawable.entermanually), context.getResources()
				.getDrawable(R.drawable.entermanually_hover));
		sdkAdvanced.setOkButtonDrawable(
				context.getResources().getDrawable(R.drawable.ok_long), context
						.getResources().getDrawable(R.drawable.ok_long_hover));
		sdkAdvanced.setMerchantLogoPortraitDrawable(context.getResources()
				.getDrawable(R.drawable.merchant_logo));
		sdkAdvanced.setNoThanksPortraitDrawable(context.getResources()
				.getDrawable(R.drawable.nothanks), null);
		sdkAdvanced.setEmailReceiptPortraitDrawable(context.getResources()
				.getDrawable(R.drawable.emailreceipt), context.getResources()
				.getDrawable(R.drawable.emailreceipt_hover));
		sdkAdvanced.setVoidDrawable(
				context.getResources().getDrawable(R.drawable.voidlong), null);
		sdkAdvanced.setRefundDrawable(
				context.getResources().getDrawable(R.drawable.refund), null);
		sdkAdvanced.setOverlayDrawables(null, null, null, null);

		sdkAdvanced.setEmailReceiptEnabled(true);
		sdkAdvanced.setSignatureScreenEnabled(true);
		sdkAdvanced.setSignatureScreenEnabledForTransactionGreaterThan25(true);

	}

}
