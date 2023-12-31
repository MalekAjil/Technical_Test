package com.mk.technicaltest;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Map;

import javax.xml.transform.ErrorListener;

public class CustomRequest extends Request<JSONObject> {

    private final Response.Listener<JSONObject> listener;
    private final Map<String, String> params;

    public CustomRequest(int method, String url, Map<String, String> params,
                         Response.Listener<JSONObject> reponseListener, ErrorListener errorListener) {
        super(method, url, (Response.ErrorListener) errorListener);
        this.listener = reponseListener;
        this.params = params;
    }

    protected Map<String, String> getParams() {
        return params;
    };

    @Override
    protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
        try {
            String jsonString = new String(response.data,
                    HttpHeaderParser.parseCharset(response.headers));
            return Response.success(new JSONObject(jsonString),
                    HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException | JSONException e) {
            return Response.error(new ParseError(e));
        }
    }

    @Override
    protected void deliverResponse(JSONObject response) {
        // TODO Auto-generated method stub
        listener.onResponse(response);
    }
}
