package com.simplevote.api;

import java.io.IOException;

public interface LoginAPI {

    boolean validateUser(String email, String password) throws IOException;
}
