package ar.com.thomas.mydailynews.util;

/**
 * Created by alejandrothomas on 6/26/16.
 */
public interface ResultListener<T>  {
    void finish(T result);
}
