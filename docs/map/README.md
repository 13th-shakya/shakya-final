# How the map was created

## 1. Find the NTUT campus boundary

Visit the [OpenStreetMap](https://www.openstreetmap.org/) website and search for "National Taipei University of Technology". Click on the area to view the details and find the relation ID (4790876).

## 2. Fetch OpenStreetMap data using Overpass API

Run the following query in the [Overpass Turbo](https://overpass-turbo.eu/) web interface to fetch the data for the map:

```overpass
[out:xml][timeout:25];
relation(4790876);
map_to_area->.a;
(
  way(area.a);
  relation(area.a);
);
(._;>;); out geom;
```

Export the data as a GeoJSON file and save it as `ntut.geojson`.

## 3. Use Mapshaper to export SVG

Use the [Mapshaper](https://mapshaper.org/) package to convert the GeoJSON file to an SVG file:

Run the `npx` command to download and run Mapshaper:

```bash
npx mapshaper ntut.geojson -o ntut.svg
```

This command renders the Overpass API result and exports it as SVG.

## 4. Add colors and labels to the SVG using Inkscape

## 5. Add the SVG to the Android project
