package com.dn.ribbon.futrue;

public interface Command<T> {
    T run();

    T fallBack();
}
