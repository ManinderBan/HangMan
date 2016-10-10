package com.maninder.newhangman;

/**
 * Created by Maninder on 02/10/16.
 */

public interface BaseView<T extends BasePresenter> {

    void setPresenter(T presenter);
}
