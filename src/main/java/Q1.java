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


public class Q1 {


	public static void execute() {

		Block<Document> printBlock = new Block<Document>() {
			@Override
			public void apply(final Document document) {
				System.out.println(document.toJson());
			}
		};

		MongoClient mongoClient = new MongoClient("10.4.41.174");
		MongoDatabase database = mongoClient.getDatabase("test");

		MongoCollection<Document> people_col = database.getCollection("People");
		MongoCollection<Document> companies_col = database.getCollection("Companies");

		people_col.aggregate(Arrays.asList(
				Aggregates.project(Projections.fields(
						Projections.excludeId(),
						Projections.include("FirstName", "LastName"))))).forEach(printBlock);

		AggregateIterable<Document> q1 = people_col.aggregate(Arrays.asList(
				new Document("$lookup", new Document()
						.append("from","Companies")
						.append("localField","companyName")
						.append("foreignField","Name")
						.append("as","company")
				)
		));

		System.out.println("Q1: For each person, its name an its company name");
		for (Document d : q1 ) {
			Document company = (Document) d.get("company", List.class).get(0);
			System.out.println(d.get("FirstName") + " " + d.get("LastName") + " works at " + company.get("domain"));
		}

		mongoClient.close();
	}

}
