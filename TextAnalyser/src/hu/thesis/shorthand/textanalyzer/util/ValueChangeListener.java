package hu.thesis.shorthand.textanalyzer.util;

/**
 * Interfész értékváltozások figyelésére.
 * 
 * @author Istvánfi Zsolt
 */
public interface ValueChangeListener<T> {

    void valueChange(T newValue);
}
