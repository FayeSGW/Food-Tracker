package app.sql.java.connect;

import io.github.cdimascio.dotenv.*;

public class Config {
    public static Dotenv dotenv = Dotenv.configure().directory("./tracker/src/main/resources").ignoreIfMalformed().ignoreIfMissing().load();

    public Dotenv showEnv() {
        return dotenv;
    }
}
