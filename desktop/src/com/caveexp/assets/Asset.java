package com.caveexp.assets;

import com.badlogic.gdx.utils.Disposable;

public abstract class Asset<T> implements Disposable {
    public T asset;
    public String path;
    public Asset(String path) {
        this.path = path;
    }
    public abstract T load();
    public abstract T placeholder();
}
