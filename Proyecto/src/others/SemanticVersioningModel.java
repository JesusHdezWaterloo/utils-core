/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package others;

import java.io.Serializable;
import java.util.StringTokenizer;

/**
 *
 * @author Yo
 */
public class SemanticVersioningModel implements Serializable, Comparable<SemanticVersioningModel> {

    private int mayor;
    private int minor;
    private int bug;

    public SemanticVersioningModel() {
    }

    public SemanticVersioningModel(String version) {
        StringTokenizer l = new StringTokenizer(version, ".");
        mayor = Integer.parseInt(l.nextToken());
        minor = Integer.parseInt(l.nextToken());
        bug = Integer.parseInt(l.nextToken());
    }

    public SemanticVersioningModel(int mayor, int minor, int bug) {
        this.mayor = mayor;
        this.minor = minor;
        this.bug = bug;
    }

    public int getMayor() {
        return mayor;
    }

    public void setMayor(int mayor) {
        this.mayor = mayor;
    }

    public int getMinor() {
        return minor;
    }

    public void setMinor(int minor) {
        this.minor = minor;
    }

    public int getBug() {
        return bug;
    }

    public void setBug(int bug) {
        this.bug = bug;
    }

    @Override
    public String toString() {
        return mayor + "." + minor + "." + bug;
    }

    @Override
    public int compareTo(SemanticVersioningModel o) {
        int compMayor = Integer.compare(mayor, o.getMayor());
        int compMinor = Integer.compare(minor, o.getMinor());
        int compBug = Integer.compare(bug, o.getBug());
        if (compMayor == 0) {
            if (compMinor == 0) {
                return compBug;
            } else {
                return compMinor;
            }
        } else {
            return compMayor;
        }
    }

}
