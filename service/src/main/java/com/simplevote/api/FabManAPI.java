package com.simplevote.api;

import com.simplevote.DataSources;

import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class FabManAPI extends AbstractRestLoginAPI {


    private static final String FABMAN_API_URL_KEY = "fabman.api.url";
    private static final String FABMAN_LOGIN_API = "user/login";

    @Override
    protected URL getURL() throws MalformedURLException {
        return new URL(DataSources.PROPERTIES.getProperty(FABMAN_API_URL_KEY) + FABMAN_LOGIN_API);
    }

    @Override
    protected int getFailedLoginResponseCode() {
        return 422;
    }

    @Override
    protected Object getBody(String email, String password) {
        return new FabManAPI.LoginCall(email, password);
    }

    private static class LoginCall {

        private String emailAddress;
        private String password;

        public LoginCall(String emailAddress, String password) {
            this.emailAddress = emailAddress;
            this.password = password;
        }

        public String getEmailAddress() {
            return emailAddress;
        }

        public String getPassword() {
            return password;
        }
    }


}
