import com.devskiller.jfairy.Fairy;
import com.devskiller.jfairy.producer.company.Company;
import com.devskiller.jfairy.producer.person.Person;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoCollection;
import org.bson.Document;
import java.util.ArrayList;
import java.util.List;

public class populateDB {

	public static void populate(int N) {

		long start = System.nanoTime();

		//connecting to MongoDB and to db "test"
		MongoClient mongoClient = new MongoClient("10.4.41.174");
		MongoDatabase database = mongoClient.getDatabase("test");

		//dropping collections first
		database.getCollection("Person").drop();
		database.getCollection("Company").drop();
		database.getCollection("Address").drop();

		//recreating collections
		database.createCollection("Person");
		database.createCollection("Company");
		database.createCollection("Address");

		//initialising collections
		MongoCollection<Document> people_col = database.getCollection("Person");
		MongoCollection<Document> companies_col = database.getCollection("Company");
		MongoCollection<Document> address_col = database.getCollection("Address");

		//creating document lists and
		// PK lists(used to check if a person/company already exists) for each collection
		List<Document> people = new ArrayList<Document>();
		List<String> passports = new ArrayList<String>();
		List<Document> companies = new ArrayList<Document>();
		List<String> comp_names = new ArrayList<String>();
		List<Document> addresses = new ArrayList<>();
		List<String> address_id = new ArrayList<>();

		Fairy fairy = Fairy.create();
		Person person;
		Company company;
		String id_address; // street+street#+flat#

		people.clear();
		companies.clear();
		address_id.clear();

		for(int i=0; i<N; i++){
			person = fairy.person();
			company = fairy.company();
			id_address = person.getAddress().getStreet()+
					person.getAddress().getStreetNumber()+
					person.getAddress().getApartmentNumber();

			//if document list doesn't already contain this person, then add him
			if (!passports.contains(person.getPassportNumber())) {
				passports.add(person.getPassportNumber());
				people.add(new Document("_id", person.getPassportNumber())
						.append("FirstName", person.getFirstName())
						.append("LastName", person.getLastName())
						.append("email", person.getEmail())
						.append("Age", person.getAge())
						.append("companyName", company.getName())
						.append("City", person.getAddress().getCity())
						.append("Country", person.getNationality().getCode()));
			}
			//same for companies
			if (!comp_names.contains(company.getName())){
				comp_names.add(company.getName());
				companies.add(new Document("_id", company.getName())
						.append("domain", company.getDomain())
						.append("email", company.getEmail())
						.append("url", company.getUrl()));
			}

			//and for addresses
			if (!address_id.contains(id_address)){
				address_id.add(id_address);
				addresses.add(new Document("_id", id_address)
							.append("Address", person.getAddress().toString()));
			}
		}

		//insertMany is much faster than using insertOne inside a loop
		people_col.insertMany(people);
		companies_col.insertMany(companies);
		address_col.insertMany(addresses);

		//closing connection
		mongoClient.close();
		long end = System.nanoTime();
		System.out.println("exec time " + (double)(end-start)/1000000);
	}


}