package com.carbonmade.corybsa.kwadspots.mvp;

public abstract class BasePresenter<V> {
    protected final V view;

    protected BasePresenter(V view) {
        this.view = view;
    }

    public void start() {}

    public void stop() {}
}
