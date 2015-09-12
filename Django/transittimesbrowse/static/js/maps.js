console.log("Loading map funcs");
map = {
    mapPoint: function(map_id, x, y, zoom) {
	console.log("Mapping point");
        var map = new OpenLayers.Map(map_id);
        var osm = new OpenLayers.Layer.OSM("OpenStreetMap Map");
        var fromProjection = new OpenLayers.Projection("EPSG:4326");
        var toProjection = new OpenLayers.Projection("EPSG:900913");
        var position = new OpenLayers.LonLat(x, y).transform(fromProjection, toProjection);
        map.addLayer(osm);

        var markers = new OpenLayers.Layer.Markers( "Markers" );
        map.addLayer(markers);
        markers.addMarker(new OpenLayers.Marker(position));

        map.setCenter(position, zoom);
    },
    mapLines: function(map_id, lines_wkt, draw) {
	console.log("Mapping line");
        var map = new OpenLayers.Map(map_id);
        var osm = new OpenLayers.Layer.OSM("OpenStreetMap Map");
        var fromProjection = new OpenLayers.Projection("EPSG:4326");
        var toProjection = new OpenLayers.Projection("EPSG:900913");
        map.addLayer(osm);

        var style = OpenLayers.Util.extend({}, OpenLayers.Feature.Vector.style['default']);
        style.strokeColor = "red";
        style.strokeColor = "red";
        style.fillColor = "red";
        style.pointRadius = 10;
        style.strokeWidth = 3;
        style.rotation = 45;
        style.strokeLinecap = "butt";
        var vectorLayer = new OpenLayers.Layer.Vector("Line", {'style': style});
        map.addLayer(vectorLayer);
        var wkt = new OpenLayers.Format.WKT({
            'internalProjection': toProjection,
            'externalProjection': fromProjection,
            'style': style });
        var maxBounds = null;
        var maxSize = 0;
        for(line in lines_wkt){
            var lineVector = wkt.read(lines_wkt[line]);
            var bounds = lineVector.geometry.getBounds();
            var size = bounds.getSize().w*bounds.getSize().h;
            if(size>maxSize){
                maxSize = size;
                maxBounds = bounds;
            }
	    if(draw){
                vectorLayer.addFeatures([lineVector]);
	    }
        }
        map.zoomToExtent(maxBounds);
       
	// var markers = new OpenLayers.Layer.Markers( "Markers" );
        // map.addLayer(markers);
        // {% for stop in stops %}
        //    var position = new OpenLayers.LonLat({{stop.point.x}}, {{stop.point.y}}).transform(fromProjection, toProjection);
	//    markers.addMarker(new OpenLayers.Marker(position));
        // {% endfor %}
    }
}
