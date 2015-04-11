package pl.rr;

import java.util.Hashtable;

/**
 *
 * @author rykowskr
 */
public class Const {
    public static final int XML_ST = 256;// <?xml
    public static final int XML_END = 257;// ?>

    public static final int ZAM = 259;// >
    public static final int UKOS = 260;// /
    public static final int ROW = 261;// =


    public static final int NAPIS_DOWOLNY = 263;// wszystko bez <
    public static final int WAR_ATR = 264; // " ciag znakow oprocz < "
    public static final int OTW_SL_ID = 265; // </ ciag znakow a-z A-Z 0-9 . - _ bez liczb na pocz¹tku
    public static final int OTW_ID = 266; // < ciag znakow a-z A-Z 0-9 . - _ bez liczb na pocz¹tku

    public static final int NAPIS_SPECJALNY = 268; // ciag znakow bez spacji i <
    public static final int ERROR = -1;

    public static final int KONIEC = 11;

    public static Hashtable<Integer, String> ht = new Hashtable<Integer, String>();

    //do sygnalizacji bledow
    public static void wypelnij() {
        ht.put(256, "<?xml");
        ht.put(257, "?>");
        ht.put(259, "domkniêcia '>'");
        ht.put(260, "ukoœnika '/'");
        ht.put(261, "rownoœci '='");
        ht.put(263, "we wnêtrzu taga");
        ht.put(264, "wartoœci atrybutu");
        ht.put(265, "id tagu zamykaj¹cego");
        ht.put(266, "id tagu otwieraj¹cego");
        ht.put(268, "rodzaju atybutu");
        ht.put(11, "koñcu");


    }


}

