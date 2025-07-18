package com.example.appblessfuture;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class MultipartRequest extends Request<NetworkResponse> {

    private final String boundary = "----AndroidBoundary" + System.currentTimeMillis();
    private final String mimeType = "multipart/form-data; boundary=" + boundary;

    private final Response.Listener<NetworkResponse> listener;
    private final Response.ErrorListener errorListener;

    private final Map<String, String> params;
    private final byte[] fileData;
    private final String fileName;


    public MultipartRequest(String url, String nome, String setor, byte[] fileData, String fileName,
                            Response.Listener<NetworkResponse> listener,
                            Response.ErrorListener errorListener) {
        super(Method.POST, url, errorListener);
        this.listener = listener;
        this.errorListener = errorListener;
        this.params = new HashMap<>();
        this.params.put("nome", nome);
        this.params.put("setor", setor);
        this.fileData = fileData;
        this.fileName = fileName;
    }

    @Override
    public String getBodyContentType() {
        return mimeType;
    }

    @Override
    public byte[] getBody() throws AuthFailureError {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                bos.write(("--" + boundary + "\r\n").getBytes());
                bos.write(("Content-Disposition: form-data; name=\"" + entry.getKey() + "\"\r\n\r\n").getBytes());
                bos.write((entry.getValue() + "\r\n").getBytes());
            }

            bos.write(("--" + boundary + "\r\n").getBytes());
            bos.write(("Content-Disposition: form-data; name=\"arquivo\"; filename=\"" + fileName + "\"\r\n").getBytes());
            bos.write(("Content-Type: application/pdf\r\n\r\n").getBytes());
            bos.write(fileData);
            bos.write("\r\n".getBytes());

            bos.write(("--" + boundary + "--\r\n").getBytes());

            return bos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected Response<NetworkResponse> parseNetworkResponse(NetworkResponse response) {
        return Response.success(response, HttpHeaderParser.parseCacheHeaders(response));
    }

    @Override
    protected void deliverResponse(NetworkResponse response) {
        listener.onResponse(response);
    }

    @Override
    public void deliverError(com.android.volley.VolleyError error) {
        errorListener.onErrorResponse(error);
    }
}
