package com.app.precared.utils;

import android.util.Log;
import android.webkit.MimeTypeMap;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.app.precared.interfaces.Constants;

import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.StringBody;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;

/*
Ver : 1.0
This class handles the multipart api calls
 * Created by Prashant 23/07/2016
 */

public class CustomMultipartRequest extends StringRequest {
    private static final String TAG = CustomMultipartRequest.class.getSimpleName();
    private final Response.Listener<String> mListener;
    private final Map<String, String> mStringPart;
    private final Map<String, String> mHeaderPart;
    private final Map<String, File> mFilePart;
    private MultipartEntityBuilder mEntityBuilder = MultipartEntityBuilder.create();
    private List<String> mFilePath;

    /**
     * constructor for support
     */
    public CustomMultipartRequest(int method, String url, Response.Listener<String> listener, Response.ErrorListener errorListener, Map<String, String> mStringPart, Map<String, String> mHeaderPart, List<String> filePath) {
        super(method, url, listener, errorListener);
        mListener = listener;
        this.mStringPart = mStringPart;
        this.mHeaderPart = mHeaderPart;
        this.mFilePath = filePath;
        mEntityBuilder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
        mEntityBuilder.setLaxMode().setBoundary("xx").setCharset(Charset.forName("UTF-8"));
        buildMultipartFileEntityList();
        buildMultipartTextEntity();
        mFilePart = null;
    }


    /**
     * constructor for update profile
     */
    public CustomMultipartRequest(int method, String url, Response.Listener<String> listener, Response.ErrorListener errorListener, Map<String, String> mStringPart, Map<String, String> mHeaderPart, Map<String, File> mFilePart) {
        super(method, url, listener, errorListener);
        mListener = listener;
        this.mStringPart = mStringPart;
        this.mHeaderPart = mHeaderPart;
        this.mFilePart = mFilePart;
        mEntityBuilder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
        mEntityBuilder.setLaxMode().setBoundary("xx").setCharset(Charset.forName("UTF-8"));
        buildMultipartProfileEntity();
        buildMultipartTextEntity();
    }



    /**
     * Return the mime-type of a file. If not mime-type can be found, it returns null.
     * http://stackoverflow.com/a/8591230
     *
     * @param file The file to check for it's mime-type.
     * @return The mime-type as a string.
     */
    private static String getMimeType(File file) {
        String type = null;
        String extension = MimeTypeMap.getFileExtensionFromUrl(file.getAbsolutePath());
        if (extension != null) {
            MimeTypeMap mime = MimeTypeMap.getSingleton();
            type = mime.getMimeTypeFromExtension(extension);
        }
        return type;
    }

    /**
     * build Multipart File Entity List for profile
     */
    private void buildMultipartProfileEntity() {
        Log.d(TAG, "Size: " + mFilePart.size());
        for (Map.Entry<String, File> entry : mFilePart.entrySet()) {
            String key = entry.getKey();
            Log.d(TAG, "key: " + key);
            File file;
            if (key.equals(Constants.PHOTO_UPOLOAD_KEY)) {
                file = entry.getValue();
                file = new File(ScalingUtilities.compressImage(file.getPath()));
            } else {
                file = entry.getValue();
            }
            Log.d(TAG, "value: " + file.getName());
            String mimeType = getMimeType(file);
            mEntityBuilder.addBinaryBody(key, file, ContentType.create(mimeType), file.getName());

        }
    }

    /**
     * build Multipart text Entity List for all
     */
    private void buildMultipartTextEntity() {
        for (Map.Entry<String, String> entry : mStringPart.entrySet()) {
            String key = entry.getKey();
            Log.d(TAG, "key: " + key);
            String value = entry.getValue();
            Log.d(TAG, "value: " + value);
            if (key != null && value != null) {
                try {
                    mEntityBuilder.addPart(key, new StringBody(value, ContentType.create("text/plain", Charset.forName("UTF-8"))));
                } catch (Exception e) {
                    Log.e(TAG, "UnsupportedEncodingException");
                }
            }

        }
    }

    /**
     * build Multipart File Entity List for support
     */
    private void buildMultipartFileEntityList() {
        Log.d(TAG, "size: " + mFilePath.size());
        if (mFilePath.size() > 0) {
            for (int i = 0; i < mFilePath.size(); i++) {
                try {
                    String key = Constants.SellerAddRequestKey.PHOTO_IMAGE;
                    File file = new File(ScalingUtilities.compressImage(mFilePath.get(i)));
                    Log.d(TAG, "key: " + key);
                    Log.d(TAG, "file: " + file.getName());
                    String mimeType = getMimeType(file);
                    mEntityBuilder.addBinaryBody(key, file, ContentType.create(mimeType), file.getName());

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public String getBodyContentType() {
        Log.d(TAG, "Body Content Type: " + mEntityBuilder.build().getContentType().getValue());
        return mEntityBuilder.build().getContentType().getValue();
    }

    @Override
    public byte[] getBody() throws AuthFailureError {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            mEntityBuilder.build().writeTo(bos);
        } catch (IOException e) {
            Log.e(TAG, "IOException writing to ByteArrayOutputStream");
        }
        return bos.toByteArray();
    }


    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        return mHeaderPart;
    }

    @Override
    protected Response<String> parseNetworkResponse(NetworkResponse response) {
        try {
            String jsonString = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
            return Response.success(jsonString, HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return Response.error(new ParseError(e));
        }
    }

    @Override
    protected void deliverResponse(String response) {
        mListener.onResponse(response);
    }

}