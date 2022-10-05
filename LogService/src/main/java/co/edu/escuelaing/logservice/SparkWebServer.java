package co.edu.escuelaing.logservice;

import static spark.Spark.get;
import static spark.Spark.port;

/**
 *
 * @author cristian.forero-m
 */
public class SparkWebServer {
    public static void main(String... args){
          port(getPort());
          get("hello", (req,res) -> "Hello Docker!");
    }

    private static int getPort() {
        if (System.getenv("PORT") != null) {
            return Integer.parseInt(System.getenv("PORT"));
        }
        return 4567;
    }
}
