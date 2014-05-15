package textanalyzer.logic;

import java.io.Serializable;

/**
 * Egy karakterekhez párosított alakzatok szerializálásához használható
 * objektum.
 * 
 * @author Istvánfi Zsolt
 */
public class CharMappingSaveData implements Serializable {

    private static final long serialVersionUID = 1985471333094078446L;

    public char ch;
    public String[] forms;
    public int[] rotations;

    public CharMappingSaveData(char ch, String[] forms, int[] rotations) {
	super();
	this.ch = ch;
	this.forms = forms;
	this.rotations = rotations;
    }

}
