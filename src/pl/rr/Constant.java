package pl.rr;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author rykowskr
 */
public class Constant {
    public static final int XML_START = 1;// <?xml
    public static final int XML_END = 2;// ?>
    public static final int GREATERTHAN = 3;// >
    public static final int SLASH = 4;// /
    public static final int EQ = 5;// =
    public static final int ANY_STRING = 6;// everything without <
    public static final int ATTR_VALUE = 7; // " String without < "
    public static final int LESSTHAN_SLASH_ID = 8; // </ String a-z A-Z 0-9 . - _ without digits at the beginning
    public static final int LESSTHAN_ID = 9; // < String a-z A-Z 0-9 . - _  without digits at the beginning
    public static final int ATTR_NAME = 10; // String without space and <
    public static final int END = 11;
    public static final int ERROR = -1;
    public static final int TAG_NOT_EQUALS = -2;

    public static final Map<Integer, String> hm;
    //for error msgs
    static {
        HashMap<Integer, String> _hm = new HashMap<>();
        _hm.put(XML_START, "<?xml");
        _hm.put(XML_END, "?>");
        _hm.put(GREATERTHAN, "domknięcia '>'");
        _hm.put(SLASH, "ukośnika '/'");
        _hm.put(EQ, "rowności '='");
        _hm.put(ANY_STRING, "we wnętrzu taga");
        _hm.put(ATTR_VALUE, "wartości atrybutu");
        _hm.put(LESSTHAN_SLASH_ID, "id tagu zamykającego");
        _hm.put(LESSTHAN_ID, "id tagu otwierającego");
        _hm.put(ATTR_NAME, "rodzaju atybutu");
        _hm.put(END, "końcu");
        hm = Collections.unmodifiableMap(_hm);
    }



}

