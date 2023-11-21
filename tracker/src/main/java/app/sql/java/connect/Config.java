package app.sql.java.connect;

import io.github.cdimascio.dotenv.*;

public class Config {
    public static Dotenv dotenv = Dotenv.configure().directory("./tracker/src/main/resources").ignoreIfMalformed().ignoreIfMissing().load();

    public Dotenv showEnv() {
        return dotenv;
    }


    public static String url;
    public static void setDBUrl(String set) {
        url = set;
    }

    public static String getDBUrl() {
        return url;
    }
}
