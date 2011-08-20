package com.zipwhip.api.signals;

/**
 * Created by IntelliJ IDEA.
 * User: jed
 * Date: 6/28/11
 * Time: 11:44 AM
 * <p/>
 * An interface that creates/generates a signal from a given data structure.
 */
public interface SignalParser<T> {

    /**
     * From this given object, parse out a signal object.
     *
     * @param object The object that came from the connection.
     * @return The signal that is represented by this object.
     * @throws Exception If this object cannot be parsed into a signal.
     */
    Signal parseSignal(T object) throws Exception;

}
