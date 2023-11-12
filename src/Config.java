package src;

import io.github.cdimascio.dotenv.*;

public class Config {
    public static Dotenv dotenv = Dotenv.configure().directory("/src").ignoreIfMalformed().ignoreIfMissing().load();
}
