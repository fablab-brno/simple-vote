package com.simplevote.webservice;

import ch.qos.logback.classic.Logger;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.simplevote.DataSources;
import com.simplevote.db.Actions;
import com.simplevote.tools.Tools;
import com.simplevote.types.User;
import org.eclipse.jetty.http.HttpStatus;
import org.slf4j.LoggerFactory;

import java.util.Map;

import static spark.Spark.*;

/**
 * Created by tyler on 4/20/17.
 */
public class Endpoints {

    public static Logger log = (Logger) LoggerFactory.getLogger(Endpoints.class);

    public static void status() {

        get("/version", (req, res) -> {
            return "{\"version\":\"" + DataSources.PROPERTIES.getProperty("version") + "\"}";
        });

        before((req, res) -> {
            res.header("Access-Control-Allow-Origin", "*");
            res.header("Access-Control-Allow-Credentials", "true");
            res.header("Access-Control-Allow-Headers", "Origin, Content-Type, Accept, token, X-Requested-With");
            res.header("Access-Control-Allow-Methods", "POST, GET, PUT, DELETE, OPTIONS");
            Tools.dbInit();
        });

        after((req, res) -> {
            res.header("Content-Encoding", "gzip");
            Tools.dbClose();
        });

    }

    public static void user() {

        post("/login", (req, res) -> {

            Map<String, String> vars = Tools.createMapFromReqBody(req.body());

            String userOrEmail = vars.get("usernameOrEmail");
            String password = vars.get("password");

            User userObj = Actions.login(userOrEmail, password);

            return userObj.getJwt();

        });

    }

    public static void poll() {
        post("/create_poll", (req, res) -> {
            Long userId = Tools.getUserFromJWTHeader(req).getId();

            return Actions.createPoll(userId).toJson(false);
        });
    }

    public static void exceptions() {

        exception(Exception.class, (e, req, res) -> {
            e.printStackTrace();
            log.error(req.uri());
            Tools.dbClose();
            res.status(HttpStatus.BAD_REQUEST_400);
            res.body(e.getMessage());
        });
    }

}
