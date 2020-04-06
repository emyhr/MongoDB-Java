import util.Utils;

public class Main {

	public static void main(String[] args) throws Exception {
		
		if (args.length < 1) {
			throw new Exception("Wrong number of parameters, usage: load N (number of documents to create) or Q1/Q2");
		}

		if (args[0].equals("populate")) {
			if (Utils.isNumber(args[1])) populateDB.populate(Integer.parseInt(args[1]));
		}
		else if (args[0].equals("Q1")) {
			Q1.execute();
		}
		else if (args[0].equals("Q2")) {
			Q2.execute();
		}
	}
	
}
