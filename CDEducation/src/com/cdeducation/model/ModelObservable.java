package com.cdeducation.model;

import android.database.DataSetObserver;
import java.util.ArrayList;
import java.util.List;

/**
 * @author t77yq @2014-07-18.
 */
public abstract class ModelObservable {

    private List<DataSetObserver> observers = new ArrayList<DataSetObserver>();

    public ModelObservable() {}


    public void registerDataSetObserver(DataSetObserver observer) {
        observers.add(observer);
    }

    public void unregisterDataSetObserver(DataSetObserver observer) {
        observers.remove(observer);
    }

    public void unregisterAll() {
        observers.clear();
    }

    public void notifyDataSetChanged() {
        for (DataSetObserver observer : observers) {
            observer.onChanged();
        }
    }

    public void notifyDataSetInvalidated() {
        for (DataSetObserver observer : observers) {
            observer.onInvalidated();
        }
    }
}
