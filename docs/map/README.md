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

1. Add Colors: Use the fill tool to add colors to campus buildings, areas, or boundaries based.

2. Add Labels: Use the text tool to add building names or other annotations.

Save two versions of the map:

With Labels: ntut-colored-labeled.svg

Without Labels: ntut-colored.svg

## 5. Minify SVG content using SVGOMG

After editing with Inkscape, some editor metadata will be added into the SVG, making the file larger than necessary.
With [SVGOMG](https://svgomg.net/), we can remove editor data, cleanup whitespace and more to save space and decrease
loading times.

After minifying, our SVG is 64% smaller than the original file without sacrificing quality.

## 6. Add the SVG to the Android project

Place the minified `ntut-colored-min.svg` into `app/src/main/assets`. This is where the AndroidSVG library will read the
file from.
