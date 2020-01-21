package com.example.sih;


import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ServerRequest<T> extends Request<T> {
    private static final String TAG = ServerRequest.class.getName();
    private static final String PROTOCOL_CHARSET = "UTF-8";

    // For Multipart request
    private final String twoHyphens = "--";
    private final String lineEnd = "\r\n";
    private final String boundary = "apiclient-" + System.currentTimeMillis();

    private Class type;
    private String body;
    private String bodyContentType;
    private Map<String, String> headers;
    private Map<String, String> params;
    private List<String> params2;
    private Map<String, DataPart> data;
    private Response.Listener<T> responseListener;
    private Response.ErrorListener errorListener;

    public ServerRequest(int method, String url, Class type,
                         Response.Listener<T> responseListener,
                         Response.ErrorListener errorListener) {
        super(method, url, errorListener);
        this.type = type;
        this.responseListener = responseListener;
        this.errorListener = errorListener;
    }

    public ServerRequest<T> withHeaders(Map<String, String> headers) {
        this.headers = headers;
        return this;
    }

    @Override
    public String toString() {
        return "ServerRequest{" +
                "body='" + body + '\'' +
                ", bodyContentType='" + bodyContentType + '\'' +
                ", headers=" + headers +
                ", params=" + params +
                '}';
    }

    public ServerRequest<T> withParams(Map<String, String> params) {
        this.params = params;
        return this;
    }

    public ServerRequest<T> withParams(List<String> params2) {
        this.params2 = params2;
        this.params = new HashMap<>();
        return this;
    }

    public ServerRequest<T> withBody(String body) {
        this.body = body;
        return this;
    }

    public ServerRequest<T> withBody(JSONObject body) {
        this.body = body.toString();
        bodyContentType = "application/json";
        return this;
    }

    public ServerRequest<T> withData(Map<String, DataPart> data) {
        this.data = data;
        bodyContentType = "multipart/form-data;boundary=" + boundary;
        return this;
    }

    public ServerRequest<T> setBodyContentType(String bodyContentType) {
        this.bodyContentType = bodyContentType;
        return this;
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        if (headers == null)
            headers = new HashMap<>();
        if (type == JSONObject.class || type == JSONArray.class) {
            headers.put("Accept", "application/json");
        }
        return headers.isEmpty() ? super.getHeaders() : headers;
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return params == null ? super.getParams() : params;
    }

    @Override
    public byte[] getBody() throws AuthFailureError {
        try {
            if (body == null) {
                if (getParams() != null ) {
                    if (getBodyContentType().contains("multipart/form-data")) {
                        ByteArrayOutputStream bos = new ByteArrayOutputStream();
                        DataOutputStream dos = new DataOutputStream(bos);

                        try {
                            Map<String, String> params = getParams();
                            if (params != null && params.size() > 0) {
                                textParse(dos, params, getParamsEncoding());
                            }

                            Map<String, DataPart> data = getByteData();
                            if (data != null && data.size() > 0) {
                                dataParse(dos, data);
                            }

                            dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

                            return bos.toByteArray();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else {
                        StringBuilder encodedParams = new StringBuilder();

                        if(params2  == null)
                        {
                            try {
                                for (Map.Entry<String, String> entry : params.entrySet()) {
                                    encodedParams.append(URLEncoder.encode(entry.getKey(), PROTOCOL_CHARSET));
                                    encodedParams.append('=');
                                    encodedParams.append(URLEncoder.encode(entry.getValue(), PROTOCOL_CHARSET));
                                    encodedParams.append('&');
                                }
                                return encodedParams.toString().getBytes(PROTOCOL_CHARSET);
                            } catch (UnsupportedEncodingException e) {
                                Log.wtf(TAG, "Encoding not supported with params: " + PROTOCOL_CHARSET, e);
                            }
                        }else{

                            try{
                                for(String s : params2) {
                                    String[] e = s.split("=");
                                    encodedParams.append(URLEncoder.encode(e[0],PROTOCOL_CHARSET));
                                    encodedParams.append('=');
                                    encodedParams.append(URLEncoder.encode(e[1],PROTOCOL_CHARSET));
                                    encodedParams.append('&');
                                }
                                return encodedParams.toString().getBytes(PROTOCOL_CHARSET);
                            } catch (UnsupportedEncodingException e) {
                                Log.wtf(TAG, "Encoding not supported with params2: " + PROTOCOL_CHARSET, e);
                            }

                        }


                    }
                }
            } else {
                return body.getBytes(PROTOCOL_CHARSET);
            }
        } catch (UnsupportedEncodingException e) {
            Log.wtf(TAG, "Encoding not supported: " + e.getMessage());
        }
        return null;
    }

    @Override
    public String getBodyContentType() {
        if (bodyContentType == null) {
            return super.getBodyContentType();
        }
        return bodyContentType + ";charset=" + PROTOCOL_CHARSET;
    }

    @SuppressWarnings("unchecked")
    @Override
    protected Response<T> parseNetworkResponse(NetworkResponse response) {
        String data = "";
        try {
            data = new String(response.data,
                    HttpHeaderParser.parseCharset(response.headers));
            if (type == String.class) {
                return Response.success((T) data,
                        HttpHeaderParser.parseCacheHeaders(response));
            } else if (type == JSONObject.class) {
                return Response.success((T) new JSONObject(data),
                        HttpHeaderParser.parseCacheHeaders(response));
            } else if (type == JSONArray.class) {
                return Response.success((T) new JSONArray(data),
                        HttpHeaderParser.parseCacheHeaders(response));
            } else if (type == NetworkResponse.class) {
                return Response.success((T) response,
                        HttpHeaderParser.parseCacheHeaders(response));
            } else if (type == Integer.class) {
                return Response.success((T) Integer.valueOf(response.statusCode),
                        HttpHeaderParser.parseCacheHeaders(response));
            }
            return Response.error(new ParseError(new Throwable("Type error")));
        } catch (UnsupportedEncodingException e) {
            Log.wtf(TAG, "Encoding not supported: " + e.getMessage());
            return Response.error(new ParseError(e));
        } catch (JSONException e) {
            Log.e(TAG, "Bad json : " + e.getMessage());
            Log.e(TAG, "URL : " + this.getUrl());
            Log.e(TAG, "Content : " + data);
            Log.e(TAG, "Request Headers : " + (this.headers == null ? "null" : this.headers.toString()));
            Log.e(TAG, "Response Headers : " + response.headers);
            return Response.error(new ParseError(e));
        }
    }

    @Override
    protected void deliverResponse(T response) {
        responseListener.onResponse(response);
    }

    @Override
    public void deliverError(VolleyError error) {
        errorListener.onErrorResponse(error);
    }

    // For Multipart Request

    protected Map<String, DataPart> getByteData() {
        return data;
    }

    private void textParse(DataOutputStream dataOutputStream, Map<String, String> params, String encoding) throws IOException {
        try {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                buildTextPart(dataOutputStream, entry.getKey(), entry.getValue());
            }
        } catch (UnsupportedEncodingException uee) {
            throw new RuntimeException("Encoding not supported: " + encoding, uee);
        }
    }

    private void dataParse(DataOutputStream dataOutputStream, Map<String, DataPart> data) throws IOException {
        for (Map.Entry<String, DataPart> entry : data.entrySet()) {
            buildDataPart(dataOutputStream, entry.getValue(), entry.getKey());
        }
    }

    private void buildTextPart(DataOutputStream dataOutputStream, String parameterName, String parameterValue) throws IOException {
        dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);
        dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"" + parameterName + "\"" + lineEnd);
        dataOutputStream.writeBytes(lineEnd);
        dataOutputStream.writeBytes(parameterValue + lineEnd);
    }

    private void buildDataPart(DataOutputStream dataOutputStream, DataPart dataFile, String inputName) throws IOException {
        dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);
        dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"" +
                inputName + "\"; filename=\"" + dataFile.getFileName() + "\"" + lineEnd);
        if (dataFile.getType() != null && !dataFile.getType().trim().isEmpty()) {
            dataOutputStream.writeBytes("Content-Type: " + dataFile.getType() + lineEnd);
        }
        dataOutputStream.writeBytes(lineEnd);

        ByteArrayInputStream fileInputStream = new ByteArrayInputStream(dataFile.getContent());
        int bytesAvailable = fileInputStream.available();

        int maxBufferSize = 1024 * 1024;
        int bufferSize = Math.min(bytesAvailable, maxBufferSize);
        byte[] buffer = new byte[bufferSize];

        int bytesRead = fileInputStream.read(buffer, 0, bufferSize);

        while (bytesRead > 0) {
            dataOutputStream.write(buffer, 0, bufferSize);
            bytesAvailable = fileInputStream.available();
            bufferSize = Math.min(bytesAvailable, maxBufferSize);
            bytesRead = fileInputStream.read(buffer, 0, bufferSize);
        }

        dataOutputStream.writeBytes(lineEnd);
    }

    public static class DataPart {
        private String fileName;
        private byte[] content;
        private String type;

        public DataPart() {
        }

        public DataPart(String name, byte[] data) {
            fileName = name;
            content = data;
        }

        public DataPart(String name, byte[] data, String mimeType) {
            fileName = name;
            content = data;
            type = mimeType;
        }

        public String getFileName() {
            return fileName;
        }

        public void setFileName(String fileName) {
            this.fileName = fileName;
        }

        public byte[] getContent() {
            return content;
        }

        public void setContent(byte[] content) {
            this.content = content;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }
    }
}