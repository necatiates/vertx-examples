package apdprueba;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.security.SignatureException;
import java.util.ArrayList;
import java.util.List;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

/**
 *
 * Available functions: - newinvoice - get_status - get_exchange -
 * get_banks_by_country - confirmation
 */
/**
 * Class of AstroPay Streamline
 *
 * @author Santiago del Puerto (santiago@astropay.com)
 * @version 1.0
 *
 */
public class AstroPayStreamline {

    /**
     * ************************
     * Merchant configuration *
     * ***************************
     */
    private String x_login = "M8FPl158UR";
    private String x_trans_key = "cRpFSzCjfS";

    private String x_login_for_webpaystatus = "HxIJBVZf9u";
    private String x_trans_key_for_webpaystatus = "9r3QvPFsKT";

    private String secret_key = "buyb7h8c3ABCWaB5g1VwIgXSGEa33qwRR";

    private boolean sandbox = true;
    /**
     * *******************************
     * End of Merchant configuration *
         ********************************
     */

    /**
     * ***************************************************
     * ---- PLEASE DON'T CHANGE ANYTHING BELOW HERE ---- *
         ****************************************************
     */
    String[] url = {"", "", "", ""};
    /*
     url[0]:newinvoice
     url[1]:status
     url[2]:exchange
     url[3]:banks
     */

    private int errors = 0;

    final String formatter = "{0,10}{1,16}";

    static final String HMAC_SHA1_ALGORITHM = "HmacSHA1";

    public void construct() {
        this.errors = 0;
        this.url[0] = "https://astropaycard.com/api_curl/streamline/newinvoice";
        this.url[1] = "https://astropaycard.com/apd/webpaystatus";
        this.url[2] = "https://astropaycard.com/apd/webcurrencyexchange";
        this.url[3] = "https://astropaycard.com/api_curl/apd/get_banks_by_country";

        if (this.sandbox) {
            this.url[0] = "http://sandbox.astropaycard.com/api_curl/streamline/newinvoice";
            this.url[1] = "http://sandbox.astropaycard.com/apd/webpaystatus";
            this.url[2] = "http://sandbox.astropaycard.com/apd/webcurrencyexchange";
            this.url[3] = "http://sandbox.astropaycard.com/api_curl/apd/get_banks_by_country";
        }
    }

    public String newinvoice(String invoice, float amount, String bank, String country, String iduser, String cpf, String name, String email, String currency, String description, String bdate, String address, String zip, String city, String state, String return_url, String confirmation_url) throws SignatureException, Exception// = "json")
    {

        List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
        urlParameters.add(new BasicNameValuePair("x_login", this.x_login));
        urlParameters.add(new BasicNameValuePair("x_trans_key", this.x_trans_key));
        urlParameters.add(new BasicNameValuePair("x_invoice", invoice));
        urlParameters.add(new BasicNameValuePair("x_amount", Float.toString(amount)));
        urlParameters.add(new BasicNameValuePair("x_bank", bank));
        urlParameters.add(new BasicNameValuePair("x_country", country));
        urlParameters.add(new BasicNameValuePair("x_iduser", iduser));
        urlParameters.add(new BasicNameValuePair("x_cpf", cpf));
        urlParameters.add(new BasicNameValuePair("x_name", name));
        urlParameters.add(new BasicNameValuePair("x_mail", email));
        urlParameters.add(new BasicNameValuePair("x_currency", currency));
        urlParameters.add(new BasicNameValuePair("x_description", description));
        urlParameters.add(new BasicNameValuePair("x_bdate", bdate));
        urlParameters.add(new BasicNameValuePair("x_address", address));
        urlParameters.add(new BasicNameValuePair("x_zip", zip));
        urlParameters.add(new BasicNameValuePair("x_city", city));
        urlParameters.add(new BasicNameValuePair("x_state", state));
        urlParameters.add(new BasicNameValuePair("x_return", return_url));
        urlParameters.add(new BasicNameValuePair("x_confirmation", confirmation_url));

        String message_to_control = invoice + "V" + amount +"I" + iduser + "2" + bank + "1" + cpf + "H" + bdate + "G" + email + "Y" + zip + "A" + address + "P" + city + "S" + state + "P";

        String control = GetSign(this.secret_key, message_to_control);//hash_hmac("sha256", pack('A*', $message_to_control), pack('A*', this.secret_key));
        control = control.toUpperCase();

        urlParameters.add(new BasicNameValuePair("control", control));

        String response = this.curl(this.url[0], urlParameters);
        return response;
    }

    public String get_status(String invoice) throws Exception {
        List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
        urlParameters.add(new BasicNameValuePair("x_login", this.x_login_for_webpaystatus));
        urlParameters.add(new BasicNameValuePair("x_trans_key", this.x_trans_key_for_webpaystatus));
        urlParameters.add(new BasicNameValuePair("x_invoice", invoice));
        //Mandatory
        String response = this.curl(this.url[1], urlParameters);
        return response;
    }

    public String get_exchange(String country, float amount) throws Exception {
        List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
        urlParameters.add(new BasicNameValuePair("x_login", this.x_login_for_webpaystatus));
        urlParameters.add(new BasicNameValuePair("x_country", country));
        urlParameters.add(new BasicNameValuePair("x_amount", Float.toString(amount)));

        String response = this.curl(this.url[2], urlParameters);
        return response;
    }

    public String get_banks_by_country(String country, String type) throws Exception {
        List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();

        urlParameters.add(new BasicNameValuePair("x_login", this.x_login));
        urlParameters.add(new BasicNameValuePair("x_trans_key", this.x_trans_key));
        urlParameters.add(new BasicNameValuePair("country_code", country));
        urlParameters.add(new BasicNameValuePair("type", type));

        String response = this.curl(this.url[3], urlParameters);
        return response;
    }

    /**
     * END OF PUBLIC INTERFACE
     */
    private String curl(String url, List<NameValuePair> post_values) throws Exception {
        String post_string = "";
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        CloseableHttpResponse post_response = null;
        String serviceResponse = null;
        try {

            String post_string_tst = "";
            for(NameValuePair onePair : post_values) {
                post_string_tst += onePair.getName() + "=" + onePair.getValue() + "&";
            }
            post_string_tst = post_string_tst.substring(0, post_string_tst.length()-1);
            System.out.println(post_string_tst);

            HttpPost request = new HttpPost("https://sandbox.astropaycard.com/api_curl/streamline/newinvoice");
            UrlEncodedFormEntity params = new UrlEncodedFormEntity(post_values);
            request.addHeader("content-type", "application/x-www-form-urlencoded, charset=utf-8");
            request.setEntity(params);
            post_response = httpClient.execute(request);
// handle response here...
        } catch (Exception ex) {
            // handle exception here
        } finally {
            serviceResponse = EntityUtils.toString(post_response.getEntity());
            httpClient.close();
            post_response.close();
        }
        return serviceResponse;
    }

    public String confirmation(String result, String x_invoice, String x_iduser, String x_description, String x_document, String x_bank, String x_payment_type, String x_bank_name, float x_amount, String x_control) throws Exception {
        String post_response = this.get_status(x_invoice);
        //String[] response_array = post_response.split("|");
        //Must verify parameters
        return post_response;
    }

    public  String GetSign(String key, String message) throws Exception {
        javax.crypto.Mac mac = javax.crypto.Mac.getInstance("HmacSHA256");
        byte[] keyBytes = key.getBytes("UTF-8");
        System.out.println("Key bytes: " + bytesToHex(keyBytes));
        byte[] messageBytes = message.getBytes("UTF-8");
        System.out.println("Message bytes: " + bytesToHex(messageBytes));
        mac.init(new javax.crypto.spec.SecretKeySpec(keyBytes, mac.getAlgorithm()));
        return bytesToHex(mac.doFinal(messageBytes));
    }
    final protected  char[] hexArray = "0123456789ABCDEF".toCharArray();
    public String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for ( int j = 0; j < bytes.length; j++ ) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }

}
