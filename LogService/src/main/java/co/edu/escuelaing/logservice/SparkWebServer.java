package co.edu.escuelaing.logservice;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.ServerApi;
import com.mongodb.ServerApiVersion;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import java.util.ArrayList;
import java.util.List;
import org.bson.Document;
import org.bson.types.ObjectId;
import static spark.Spark.port;

/**
 *
 * @author cristian.forero-m
 */
public class SparkWebServer {
    public static void main(String... args){
          port(getPort());
        // Conexión a base de datos mongodb

        //URL para Atlasdb en la nube
        String connstr = "mongodb+srv://dbAdmin:opDM6ufM0sEVURr6@cluster0.tnqcz.mongodb.net/?retryWrites=true&w=majority";

        //URL para conexión local
        //String connstr ="mongodb://localhost:40000/?retryWrites=true&w=majority";
        //Crea objeto de tipo ConnectionString
        ConnectionString connectionString = new ConnectionString(connstr);

        //Crea objeto con configuraciones para el cliente mongo
        MongoClientSettings settings = MongoClientSettings.builder()
                .applyConnectionString(connectionString)
                .serverApi(ServerApi.builder()
                        .version(ServerApiVersion.V1)
                        .build())
                .build();

        //Crea una instancia del cliente mongo conectado a la base de datos
        MongoClient mongoClient = MongoClients.create(settings);

        //Obtiene una lista de objetos bson representando las base de datos disponibles
        // bson es una versión binaria de json creada para mejorar desempeño de mongo.
        List<Document> databases = mongoClient.listDatabases().into(new ArrayList<>());
        databases.forEach(db -> System.out.println(db.toJson()));

        //Obtener objeto base de datos. Si no existe lo crea
        MongoDatabase database = mongoClient.getDatabase("myFirstDatabase");
        //Obtener objeto colección. Si no existe lo crea
        MongoCollection<Document> customers = database.getCollection("customer");

        //Obtiene un iterable
        FindIterable<Document> iterable = customers.find();
        MongoCursor<Document> cursor = iterable.iterator();

        //Recorre el iterador obtenido del iterable
        while (cursor.hasNext()) {
            System.out.println(cursor.next());
        }

        //Crea un documento BSON con el cliente
        Document customer = new Document("_id", new ObjectId());
        customer.append("firstName", "Daniel");
        customer.append("lastName", "Benavides");
        customer.append("_class", "co.edu.escuelaing.mongodemo.Customer.Customer");

        //Agrega el nuevo cliente a la colección
        customers.insertOne(customer);

        //Lee el iterable directamente para imprimir documentos
        for (Document d : iterable) {
            System.out.println(d);
        }
    }

    private static int getPort() {
        if (System.getenv("PORT") != null) {
            return Integer.parseInt(System.getenv("PORT"));
        }
        return 4567;
    }
}
