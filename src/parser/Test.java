package parser;

import java.io.File;
import java.io.FileInputStream;

public class Test {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String fileName;
		if (args.length == 1) {
			fileName = args[0];
		} else {
			fileName = "/Users/xixi/Documents/workspace/TestSolr/src/parser/test.txt";
		}
		try {
			QueryParser t = new QueryParser(new FileInputStream(new File(fileName)));
			ASTStart root = t.Start();
			root.dump(">");
		} catch (Exception e) {
			System.out.println("Oops.");
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}

}
