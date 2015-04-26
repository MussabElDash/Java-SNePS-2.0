package SnepsLog;


import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;

import java_cup.Lexer;
import java_cup.parser;
import java_cup.runtime.Symbol;

public class AP {

	public static void h()
	{
		System.out.println("hi");
	}

	public static void main(String[] args) {

		String inFile = "Sample1.in";

		if (args.length > 1) {
			inFile = args[0];
		}

		try {
			FileInputStream fis = new FileInputStream(inFile);
			BufferedInputStream bis = new BufferedInputStream(fis);
			DataInputStream dis = new DataInputStream(bis);

			BufferedWriter writer = new BufferedWriter(new FileWriter("Sample1.out"));

			parser parser = new parser(new Lexer(dis));
			Symbol res = parser.parse();

			String value = (String)res.value;
			writer.write(value);

			System.out.println("Done");

			fis.close();
			bis.close();
			dis.close();
			writer.close();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
}
