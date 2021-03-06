package com.smartsense.taxinearyou.utill;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;

public class MultipartRequestJson extends Request<JSONObject> {

    private MultipartEntity entity = new MultipartEntity();
    private Response.Listener<JSONObject> mListener;
    private  File file;
    private final HashMap<String, String> params;

    public MultipartRequestJson(String url, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener,
                                File file, HashMap<String, String> params) {
        super(Method.POST, url, errorListener);
//        FILE_PART_NAME = param;
        mListener = listener;
        this.file = file;
        this.params = params;
        buildMultipartEntity();
    }

    private void buildMultipartEntity() {
//        for (int i = 0; i < file.length; i++) {

        String FILE_PART_NAME = "profilePic";
        entity.addPart(FILE_PART_NAME, new FileBody(file));
//        }
        try {
            for (String key : params.keySet()) {
                entity.addPart(key, new StringBody(params.get(key)));
            }
        } catch (UnsupportedEncodingException e) {
            VolleyLog.e("UnsupportedEncodingException");
        }
    }

    @Override
    public String getBodyContentType() {
        return entity.getContentType().getValue();
    }

    @Override
    public byte[] getBody() throws AuthFailureError {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            entity.writeTo(bos);
        } catch (IOException e) {
            VolleyLog.e("IOException writing to ByteArrayOutputStream");
        }
        return bos.toByteArray();
    }

    /**
     * copied from Android StringRequest class
     */
    @Override
    protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
        String parsed;
        try {
            parsed = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
            return Response.success(new JSONObject(parsed), HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        } catch (JSONException je) {
            return Response.error(new ParseError(je));
        }
    }

    @Override
    protected void deliverResponse(JSONObject response) {
        mListener.onResponse(response);
    }
}