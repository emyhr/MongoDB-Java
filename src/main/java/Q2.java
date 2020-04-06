import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import com.mongodb.Block;
import com.mongodb.client.AggregateIterable;
import com.mongodb.client.model.Aggregates;
import com.mongodb.client.model.Projections;
import java.util.Arrays;
import java.util.List;

public class Q2 {

	public static void execute() {

		MongoClient mongoClient = new MongoClient("10.4.41.174");
		MongoDatabase database = mongoClient.getDatabase("test");

		MongoCollection<Document> people_col = database.getCollection("People");
		MongoCollection<Document> companies_col = database.getCollection("Companies");

		AggregateIterable<Document> q2 = companies_col.aggregate(Arrays.asList(
				new Document("$lookup", new Document()
						.append("from","People")
						.append("localField","Name")
						.append("foreignField","CompanyName")
						.append("as","employees")
				),
				new Document("$project", new Document()
						.append("Name",1)
						.append("employees", new Document("$size","$employees"))
				)
		));

		System.out.println("Q2: For each company, the name and the number of employees");
		for (Document d : q2 ) {
			System.out.println(d.get("Name") + " has " + d.get("employees")
					+ (d.getInteger("employees") == 1 ? " employee." : " employees."));
		}

	}
}
