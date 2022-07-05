package com.video.modules.sms;

import com.video.modules.convert.ConvertEnFa;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Random;

@Component
public class SmsVerification {


    private final ConvertEnFa convertEnFa;


    private String USER_AGENT = "Mozilla/5.0";

    @Autowired
    public SmsVerification(ConvertEnFa convertEnFa) {
        this.convertEnFa = convertEnFa;
    }

    public String sendSmsVerification(String mobile,String vcode) throws IOException {

        String token = this.getToken();
        mobile = mobileNumberCorrection(mobile);


        String jsonParameterSTR = "{"
                + " \"ParameterArray\":["
                + "{ \"Parameter\": \"VerificationCode\",\"ParameterValue\": \"" + vcode + "\"}"
                + "],"
                + "\"Mobile\":\"" + mobile + "\","
                + "\"TemplateId\":\"21827\""
                + "}";
        String url = "http://RestfulSms.com/api/UltraFastSend";
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("POST");
        con.setRequestProperty("User-Agent", USER_AGENT);
        con.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
        con.setRequestProperty("x-sms-ir-secure-token", token);
        // For POST only - START
        con.setDoOutput(true);
        OutputStream os = con.getOutputStream();
        System.out.println(jsonParameterSTR.getBytes());
        os.write(jsonParameterSTR.getBytes());
        os.flush();
        os.close();
        // For POST only - END

        int responseCode = con.getResponseCode();
        JSONObject resJson = new JSONObject();

        if (responseCode == HttpURLConnection.HTTP_CREATED) { //success



            BufferedReader in = new BufferedReader(new InputStreamReader(
                    con.getInputStream()));
            String inputLine;
            StringBuffer responseStr = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                responseStr.append(inputLine);
            }
            in.close();
            // print result
            System.out.println(responseStr.toString());
            JSONObject res = new JSONObject(responseStr.toString());


            boolean IsSuccessful = res.getBoolean("IsSuccessful");

            if (IsSuccessful) {
                resJson.put("code", 200);
                resJson.put("status", "success");
                resJson.put("message", "کد تایید با موفقیت ارسال شد.");

            } else {
                resJson.put("code", 401);
                resJson.put("status", "fail");
                resJson.put("message", "متاسفانه مشکلی پیش آمده است");

            }


        } else {
            resJson.put("code", 401);
            resJson.put("status", "fail");
            resJson.put("message", "متاسفانه مشکلی پیش آمده است");

            // sms send fail
        }
        return resJson.toString();
    }

    public int generateCode() {
        Random r = new Random(System.currentTimeMillis());
        return ((1 + r.nextInt(2)) * 10000 + r.nextInt(10000));
    }

    public String mobileNumberCorrection(String number) {

        String mobile = number.trim();
        mobile = mobile.replaceAll("\\s+", "").replace("+98", "");
        mobile = mobile.replace("+98", "");
        if (mobile.startsWith("0098")) {
            mobile = mobile.replaceFirst("0098", "");
        }
        if (mobile.startsWith("98")) {
            mobile = mobile.replaceFirst("98", "");
        }
        if (!mobile.startsWith("0")) {
            mobile = "0" + mobile;
        }

        return convertEnFa.arabicToDecimal(mobile);
    }

    public static String getToken() throws IOException {
        String token = "";
        URL obj = new URL("http://RestfulSms.com/api/Token");
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("POST");
        String POST_PARAMS = "{ "
                + "  \"UserApiKey\": \"74d27b1ff515c40a4ea87b1f\", "
                + "  \"SecretKey\": \"kalashahr123!@#\" "
                + "}";
        con.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
        // For POST only - START
        con.setDoOutput(true);
        OutputStream os = con.getOutputStream();
        os.write(POST_PARAMS.getBytes());
        os.flush();
        os.close();
        // For POST only - END

        int responseCode = con.getResponseCode();
        System.out.println("POST Response Code :: " + responseCode);

        if (responseCode == HttpURLConnection.HTTP_CREATED) { //success
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            // print result
            System.out.println(response.toString());
            JSONObject ret = new JSONObject(response.toString());
            token = ret.getString("TokenKey");
        } else {
            System.out.println("POST request not worked");
        }
        return token;
    }
}
