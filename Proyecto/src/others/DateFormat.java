/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package others;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author Yo
 */
public class DateFormat {

    public static final SimpleDateFormat SDF = new SimpleDateFormat("dd/MM/yyyy");

    public static String format(Date d) {
        return SDF.format(d);
    }

    public static Date format(String s) throws ParseException {
        return SDF.parse(s);
    }

}