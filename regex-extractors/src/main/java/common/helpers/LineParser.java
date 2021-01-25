package common.helpers;

import java.util.Map;


/**
 * Converts a String to a Map<String, Object>
 * @author Samarth Bhargav
 *
 */
public interface LineParser
{

    /**
     * Converts a String to a Map<String, Object>.
     * <br> Return null if the conversion failed
     * @param string string to be parsed
     * @return parsed String in map
     */
    Map<String, Object> parse(String string);
}
