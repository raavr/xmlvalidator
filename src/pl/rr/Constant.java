package pl.rr;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author rykowskr
 */
public class Constant {
    public static final int XML_START = 256;// <?xml
    public static final int XML_END = 257;// ?>
    public static final int GREATERTHAN = 259;// >
    public static final int SLASH = 260;// /
    public static final int EQ = 261;// =
    public static final int ANY_STRING = 263;// everything without <
    public static final int ATTR_VALUE = 264; // " String without < "
    public static final int LESSTHAN_SLASH_ID = 265; // </ String a-z A-Z 0-9 . - _ without digits at the beginning
    public static final int LESSTHAN_ID = 266; // < String a-z A-Z 0-9 . - _  without digits at the beginning
    public static final int ATTR_NAME = 268; // String without space and <
    public static final int END = 11;

    public static final Map<Integer, String> hm;

    //for error msgs
    static {
        HashMap<Integer, String> _hm = new HashMap<>();
        _hm.put(256, "<?xml");
        _hm.put(257, "?>");
        _hm.put(259, "domkniêcia '>'");
        _hm.put(260, "ukoœnika '/'");
        _hm.put(261, "rownoœci '='");
        _hm.put(263, "we wnêtrzu taga");
        _hm.put(264, "wartoœci atrybutu");
        _hm.put(265, "id tagu zamykaj¹cego");
        _hm.put(266, "id tagu otwieraj¹cego");
        _hm.put(268, "rodzaju atybutu");
        _hm.put(11, "koñcu");
        hm = Collections.unmodifiableMap(_hm);
    }



}

