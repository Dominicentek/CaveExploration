package com.caveexp.assets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;

public class SoundAsset extends Asset<Sound> {
    public SoundAsset(String path) {
        super(path);
    }
    public Sound load() {
        return Gdx.audio.newSound(Gdx.files.internal(path));
    }
    public Sound placeholder() {
        return null;
    }
    public void dispose() {
        asset.dispose();
    }
}
