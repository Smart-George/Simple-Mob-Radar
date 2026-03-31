# Simple Mob Radar

A Fabric **1.21.10** client-focused mod that adds a craftable **Mob Radar** item.

## Features
- Tracks mobs within **30 blocks** when the Mob Radar is held in either hand.
- Tracks through walls/darkness by scanning nearby entities directly client-side.
- Shows target info in HUD cards:
  - Mob name
  - Distance (blocks)
  - Health
  - Tracking crosshair icon
- Applies glowing outlines to tracked mobs for easy visibility.

## Recipe
The recipe matches your example pattern and is included as JSON and a visual PNG guide:

- JSON: `data/simple_mob_radar/recipes/mob_radar.json`
- Image: `assets/simple_mob_radar/textures/gui/crafting_recipe.base64.txt`

Pattern:

```text
R G R
I C I
I I I
```

`R` = Redstone, `G` = Glass, `I` = Iron Ingot, `C` = Compass

## Build the JAR
From project root:

```bash
gradle wrapper
./gradlew build
```

Output jar:

`build/libs/simple-mob-radar-1.0.0.jar`

## Assets you can replace
- `assets/simple_mob_radar/textures/item/mob_radar.base64.txt`
- `assets/simple_mob_radar/textures/gui/tracking_crosshair.base64.txt`
- `assets/simple_mob_radar/icon.base64.txt`
- `assets/simple_mob_radar/textures/gui/crafting_recipe.base64.txt`


## Asset placeholders
PNG files are intentionally removed in this repo snapshot. Only `.base64.txt` placeholders are kept so no binary image files are committed.
