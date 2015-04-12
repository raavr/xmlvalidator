package pl.rr;

/**
 * @author rykowskr
 */
public enum Gramma {

    XML_START, // <?xml
    XML_END, // ?>
    GREATERTHAN, // >
    SLASH, // /
    EQ, // =
    ANY_STRING, // everything without <
    ATTR_VALUE, // " String without < "
    LESSTHAN_SLASH_ID, // </ String a-z A-Z 0-9 . - _ without digits at the beginning
    LESSTHAN_ID, // < String a-z A-Z 0-9 . - _  without digits at the beginning
    ATTR_NAME, // String without space and <
    END,

    ERROR,
    TAG_NOT_EQUALS;
}
