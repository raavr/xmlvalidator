package pl.rr;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 *
 * @author rykowskr
 */
public class Validator {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        StringBuffer cont = new StringBuffer();
        ArrayList<Integer> newLineIdxsList = new ArrayList<Integer>();

        try {
            FileReader in = new FileReader("sample_xml");
            int c, i = 1;
            char akt;
            newLineIdxsList.add(0);

            while ((c = in.read()) != -1) {
                akt = (char) c;
                if (akt == '\n') {
                    newLineIdxsList.add(i);
                }
                cont.append(akt);
                i++;
            }
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Parser parser = new Parser(cont.toString(), newLineIdxsList);
        parser.parser();}

}

