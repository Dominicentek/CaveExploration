package com.caveexp.game;

import com.badlogic.gdx.audio.Sound;
import com.caveexp.assets.Loader;
import com.caveexp.util.Registry;

public class SoundEvent {
    private final Sound sound;
    private SoundEvent(Sound sound) {
        this.sound = sound;
    }
    public void play() {
        sound.play();
    }
    public static void register() {
        Registry.SOUNDS.register("break", new SoundEvent(Loader.get("snd_break", Loader.SOUND)));
        Registry.SOUNDS.register("damage", new SoundEvent(Loader.get("snd_damage", Loader.SOUND)));
        Registry.SOUNDS.register("destroy", new SoundEvent(Loader.get("snd_destroy", Loader.SOUND)));
        Registry.SOUNDS.register("drink", new SoundEvent(Loader.get("snd_drink", Loader.SOUND)));
        Registry.SOUNDS.register("pickup", new SoundEvent(Loader.get("snd_pickup", Loader.SOUND)));
        Registry.SOUNDS.register("torch", new SoundEvent(Loader.get("snd_torch", Loader.SOUND)));
    }
}