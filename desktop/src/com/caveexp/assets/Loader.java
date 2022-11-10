package com.caveexp.assets;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import java.util.HashMap;

public class Loader {
    private static final HashMap<String, Asset<?>> assets = new HashMap<>();
    public static final Class<Texture> IMAGE = Texture.class;
    public static final Class<Sound> SOUND = Sound.class;
    public static void load() {
        for (String name : assets.keySet()) {
            try {
                Asset asset = assets.get(name);
                asset.asset = asset.load();
            }
            catch (Exception e) {
                System.out.println("Error reading asset " + name);
                e.printStackTrace();
            }
        }
    }
    public static <T> T get(String name, Class<T> type) {
        Asset asset = assets.get(name);
        if (asset == null) throw new RuntimeException("Asset not found: " + name);
        if (asset.asset == null) asset.asset = asset.placeholder();
        return type.cast(asset.asset);
    }
    public static void dispose() {
        for (String name : assets.keySet()) {
            assets.get(name).dispose();
        }
    }
    public static void register() {
        assets.put("pixel", new Asset<Texture>(null) {
            public void dispose() {
                asset.dispose();
            }
            public Texture load() {
                Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGB888);
                pixmap.drawPixel(0, 0, 0xFFFFFFFF);
                return new Texture(pixmap);
            }
            public Texture placeholder() {
                return null;
            }
        });
        assets.put("blank", new Asset<Texture>(null) {
            public void dispose() {
                asset.dispose();
            }
            public Texture load() {
                Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
                pixmap.drawPixel(0, 0, 0x00000000);
                return new Texture(pixmap);
            }
            public Texture placeholder() {
                return null;
            }
        });
        assets.put("test", new ImageAsset("assets/images/test.png"));
        assets.put("logo", new ImageAsset("assets/images/logo.png"));
        assets.put("bronze", new ImageAsset("assets/images/items/bronze.png"));
        assets.put("iron", new ImageAsset("assets/images/items/iron.png"));
        assets.put("stick", new ImageAsset("assets/images/items/stick.png"));
        assets.put("coal", new ImageAsset("assets/images/items/coal.png"));
        assets.put("diamond", new ImageAsset("assets/images/items/diamond.png"));
        assets.put("gold", new ImageAsset("assets/images/items/gold.png"));
        assets.put("moltentin", new ImageAsset("assets/images/items/moltentin.png"));
        assets.put("moltencopper", new ImageAsset("assets/images/items/moltencopper.png"));
        assets.put("silver", new ImageAsset("assets/images/items/silver.png"));
        assets.put("platinum", new ImageAsset("assets/images/items/platinum.png"));
        assets.put("rawcopper", new ImageAsset("assets/images/items/rawcopper.png"));
        assets.put("rawtin", new ImageAsset("assets/images/items/rawtin.png"));
        assets.put("stone", new ImageAsset("assets/images/items/stone.png"));
        assets.put("torch", new ImageAsset("assets/images/items/torch.png"));
        assets.put("potion", new ImageAsset("assets/images/items/potion.png"));
        assets.put("pickaxe", new ImageAsset("assets/images/items/pickaxe.png"));
        assets.put("sword", new ImageAsset("assets/images/items/sword.png"));
        assets.put("swordstick", new ImageAsset("assets/images/items/swordstick.png"));
        assets.put("ore", new ImageAsset("assets/images/tiles/ore.png"));
        assets.put("oreoverlay", new ImageAsset("assets/images/tiles/overlay.png"));
        assets.put("lava", new ImageAsset("assets/images/tiles/lava.png"));
        assets.put("player", new ImageAsset("assets/images/sprites/player.png"));
        assets.put("playerwalk", new ImageAsset("assets/images/sprites/playerwalk.png"));
        assets.put("spider", new ImageAsset("assets/images/sprites/spider.png"));
        assets.put("lock", new ImageAsset("assets/images/sprites/lock.png"));
        assets.put("snd_break", new SoundAsset("assets/sounds/break.wav"));
        assets.put("snd_damage", new SoundAsset("assets/sounds/damage.wav"));
        assets.put("snd_destroy", new SoundAsset("assets/sounds/destroy.wav"));
        assets.put("snd_drink", new SoundAsset("assets/sounds/drink.wav"));
        assets.put("snd_pickup", new SoundAsset("assets/sounds/pickup.wav"));
        assets.put("snd_torch", new SoundAsset("assets/sounds/torch.wav"));
        assets.put("croc_pickaxe", new ImageAsset("assets/images/items/croc_pickaxe.png"));
    }
}
