package com.carbonmade.corybsa.kwadspots.mvp;

public interface BaseMvpPresenter<T extends BaseView> {
    /**
     * Called when view attached to presenter.
     *
     * @param view The view to attach.
     */
    void attach(T view);

    /**
     * Called when view is detached from presenter.
     */
    void detach();

    /**
     * @return {@code true} if view is attached to presenter.
     */
    boolean isAttached();
}
