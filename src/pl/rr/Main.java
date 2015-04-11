package pl.rr;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 *
 * @author rykowskr
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        StringBuffer cont = new StringBuffer();
        ArrayList<Integer> list = new ArrayList<Integer>(); //lista indeksow, od ktorych zaczyna sie nowy wiersz
        //do obslugi bledow

        try {

            FileReader in = new FileReader("C:\\Users\\Rafal\\IdeaProjects\\XMLValidator\\file");
            int c;
            char akt;
            int i = 1;
            list.add(0);

            while ((c = in.read()) != -1) {
                akt = (char) c;
                if (akt == '\n') {
                    list.add(i);
                }
                cont.append((char) c);
                i++;
            }
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String s = cont.toString();
        Parser parser = new Parser(s, list);
        char k = '-';
        String str = "" + k;
        System.out.println("Wynik: " + str.matches("[0-35-79.]"));

        try {

            parser.parser();

        } catch (java.lang.OutOfMemoryError a) {
            parser.wypisz();
        } catch (java.lang.ArrayIndexOutOfBoundsException b) {
            parser.wypisz();
        } catch (java.lang.StringIndexOutOfBoundsException c) {
            parser.wypisz();
        }
    }
}

