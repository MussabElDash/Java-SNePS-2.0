/**
 * This class was used as a trial to write a .java file that can be used to create classes on the fly for
 * the user defined semantic type .. NOT WORKING (This feature was not successfully implemented in the current
 * version).
 */
package sneps;

import java.io.*;

public class Program {

File JClass = new File("JClass.java");
public static BufferedWriter out = null;

    public static void main(String[] args) {
        try {
            out = new BufferedWriter(new FileWriter("JClass.java"));
            out.write("public abstract class JClass {");
            out.newLine();
            out.newLine();
            out.write("    public void printSomething(String a) {");
            out.newLine();
            out.write("        System.out.println(a);");
            out.newLine();
            out.write("    }");
            out.newLine();
            out.write("}");
            out.close();
        } catch (IOException e)
        {
            System.exit(-1);
        }

        //Somehow import JClass.java as a class here

    }

}
