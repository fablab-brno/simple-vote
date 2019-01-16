package com.simplevote.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.simplevote.DataSources;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MtmAPI extends AbstractRestLoginAPI {

    private static final String MTM_API_URL_KEY = "mattermost.api.url";
    private static final String MTM_LOGIN_API = "users/login";

    @Override
    protected URL getURL() throws MalformedURLException {
        return new URL(DataSources.PROPERTIES.getProperty(MTM_API_URL_KEY) + MTM_LOGIN_API);
    }

    @Override
    protected int getFailedLoginResponseCode() {
        return HttpURLConnection.HTTP_UNAUTHORIZED;
    }

    @Override
    protected Object getBody(String email, String password) {
        return new LoginCall(email, password);
    }

    private static class LoginCall {

        private String login_id;
        private String password;

        public LoginCall(String login_id, String password) {
            this.login_id = login_id;
            this.password = password;
        }

        public String getLogin_id() {
            return login_id;
        }

        public String getPassword() {
            return password;
        }
    }
}
