package com.spe.checker;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Created by mzennis on 9/19/16.
 */
public class PreferenceHelper {

    public static final String ANDROIDID = "androidID";
    public static final String EMAIL_ADDRESS = "customerEmail";
    public static final String ON_BACKPRESS = "back";
    public static String EMAIL = "email";
    public static String DATA_TRANSACTION = "data_transaction";
    public static String LANG = "lang";
    public static String AMOUNT = "amount";
    public static String FORCE_LOGIN = "re-login";
    public static String BANK_ACC_NUMBER = "bank_acc_number";
    public static String BANK_ACC_HOLDER_NAME = "bank_acc_holder_name";
    public static String BRANCH = "branch";
    public static String BANK_CODE_REG = "bank_code_reg";
    public static String ADDRESS_LINE1 = "address_line1";
    public static String ADDRESS_LINE2 = "address_line2";
    public static String ACC_ADMIN_NAME = "acc_admin_name";
    public static String ACC_ADMIN_PHONE = "acc_admin_phone";
    public static String BUSINESS_EMAIL = "business_email";
    public static String BUSINESS_PHONE = "business_phone";
    public static String CITY_NAME = "city_name";
    public static String PROVINCE_NAME = "province_name";
    public static String POSTAL_CODE = "postal_code";
    public static String MERCHANT_NAME = "merchant_name";
    public static String MID = "merchant_id";
    public static final String SIGNATURE = "viSpgaObP+ZOOUtUeXEyOvxrG1M=\n";

    public static String VERIFICATION_CODE = "verification_code";
    public static String BANK_CODE = "bank_code";

    public static String BANK_ACCOUNT_DATA = "bank_account_data";
    public static String MERCHANT_INFO_DATA = "merchant_info_data";
    public static String BILL_ID = "bill_id";
    public static String REF_ID = "ref_id";

    public static String DATA_CITY = "data_city";
    public static String DATA_PROVINCE = "data_province";
    public static String DATA_BANK = "data_bank";

    private SharedPreferences sharedPreferences;

    public PreferenceHelper(Context context) {
        sharedPreferences = getSharedPreference(context);
    }

    public SharedPreferences getSharedPreference(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    public void putString(String key, String isi) {
        sharedPreferences.edit().putString(key, isi).apply();
    }

    public void putInt(String key, int num) {
        sharedPreferences.edit().putInt(key, num).apply();
    }

    public String getString(String key) {
        return sharedPreferences.getString(key, null);
    }

    public int getInt(String key) {
        return sharedPreferences.getInt(key, 0);
    }

    public void logout() {
        Map<String, ?> keys = sharedPreferences.getAll();

        for (Map.Entry<String, ?> entry : keys.entrySet()) {
            sharedPreferences.edit().remove(entry.getKey()).apply();
        }
    }

    public void clear(String key) {
        sharedPreferences.edit().remove(key).apply();
    }

    public void clear() {
        sharedPreferences.edit().clear().apply();
    }

    public void saveBusinessEmail(String consumerEmail) {
        putString(EMAIL_ADDRESS, consumerEmail);
    }

    public String getBusinessEmail() {
        return getString(EMAIL_ADDRESS);
    }

    public void clearSession() {
        this.clear(PreferenceHelper.BANK_ACC_NUMBER);
        this.clear(PreferenceHelper.BANK_ACC_HOLDER_NAME);
        this.clear(PreferenceHelper.BRANCH);
        this.clear(PreferenceHelper.BANK_CODE_REG);
        this.clear(PreferenceHelper.ADDRESS_LINE1);
        this.clear(PreferenceHelper.ADDRESS_LINE2);
        this.clear(PreferenceHelper.ACC_ADMIN_NAME);
        this.clear(PreferenceHelper.ACC_ADMIN_PHONE);
        this.clear(PreferenceHelper.BUSINESS_EMAIL);
        this.clear(PreferenceHelper.BUSINESS_PHONE);
        this.clear(PreferenceHelper.CITY_NAME);
        this.clear(PreferenceHelper.PROVINCE_NAME);
        this.clear(PreferenceHelper.POSTAL_CODE);
        this.clear(PreferenceHelper.MERCHANT_NAME);
        this.clear(PreferenceHelper.BANK_CODE);
        this.clear(PreferenceHelper.VERIFICATION_CODE);
        this.clear(PreferenceHelper.BANK_ACCOUNT_DATA);
        this.clear(PreferenceHelper.MERCHANT_INFO_DATA);
        this.clear(PreferenceHelper.EMAIL_ADDRESS);
        this.clear(PreferenceHelper.EMAIL);
    }

}
