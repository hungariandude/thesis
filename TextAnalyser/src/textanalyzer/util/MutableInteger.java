package textanalyzer.util;

/**
 * Módosítható Integer osztály.
 * 
 * @author Istvánfi Zsolt
 */
public class MutableInteger extends Number implements
	Comparable<MutableInteger> {

    private static final long serialVersionUID = -7331599700598564082L;

    private int value;

    public MutableInteger() {
	value = 0;
    }

    public MutableInteger(int value) {
	this.value = value;
    }

    public void add(int amount) {
	value += amount;
    }

    @Override
    public int compareTo(MutableInteger o) {
	return this.value > o.value ? 1 : (this.value == o.value ? 0 : -1);
    }

    @Override
    public double doubleValue() {
	return value;
    }

    @Override
    public boolean equals(Object obj) {
	if (this == obj) {
	    return true;
	}
	if (obj == null) {
	    return false;
	}
	if (getClass() != obj.getClass()) {
	    return false;
	}
	MutableInteger other = (MutableInteger) obj;
	if (value != other.value) {
	    return false;
	}
	return true;
    }

    @Override
    public float floatValue() {
	return value;
    }

    public int getValue() {
	return value;
    }

    @Override
    public int hashCode() {
	return value;
    }

    @Override
    public int intValue() {
	return value;
    }

    @Override
    public long longValue() {
	return value;
    }

    public void setValue(int value) {
	this.value = value;
    }

}
