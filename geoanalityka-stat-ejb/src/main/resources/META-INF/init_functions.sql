CREATE OR REPLACE FUNCTION get_road_segment(
  x_coord NUMERIC,
  y_coord NUMERIC,
  epsg INT,
  road_classes INT[]
) RETURNS INT LANGUAGE plpgsql AS $$
DECLARE
  radius INT := 2000;
  result INT;
BEGIN
  SELECT source INTO result FROM drogi_pkt
  WHERE ST_DWithin(geom, ST_Transform(ST_GeomFromText('POINT(' || $1 || ' ' || $2 || ')', $3), 3857), radius)
  AND class_id = ANY($4)
  ORDER BY ST_Distance(geom, ST_Transform(ST_GeomFromText('POINT(' || $1 || ' ' ||
  $2 || ')', $3), 3857)) LIMIT 1;

  RETURN result;
END
$$;

CREATE OR REPLACE FUNCTION get_service_area_polygon (
  x_coord NUMERIC,
  y_coord NUMERIC,
  epsg INT,
  road_classes INT[],
  road_segment INT,
  driving_time INT,
  cost_column VARCHAR,
  rev_cost_column VARCHAR
) RETURNS TEXT LANGUAGE plpgsql AS $$
DECLARE
  service_area_polygon_geojson TEXT;
BEGIN
  SELECT St_AsGeoJSON(ST_Transform(ST_ConcaveHull(ST_Collect(geom),0.8), 4326)) INTO service_area_polygon_geojson FROM drogi_pkt WHERE id IN(
    SELECT edge FROM pgr_drivingDistance('
      SELECT id AS id, 
        source::int4 AS source, 
        target::int4 AS target, 
        ' || $7 || '::float8 AS cost,
        ' || $8 || '::float8 as reverse_cost
      FROM drogi_pkt WHERE ST_DWithin(geom,ST_Transform(ST_GeomFromText(''POINT(' || $1 || ' ' || $2 || ')'',' || $3 || '),3857),((1.8*10)*2333)+180) 
      AND class_id IN(' || array_to_string($4, ',') || ')', $5, $6));
  RETURN service_area_polygon_geojson;
END
$$;

CREATE OR REPLACE FUNCTION service_area(
  travel_time INT, --minutes,
  x_coord NUMERIC,
  y_coord NUMERIC,
  epsg INT,
  travel_type CHAR --available values: W, B, C(Walk, Bicycle, Car)
) RETURNS TEXT LANGUAGE plpgsql AS $$
DECLARE
  result TEXT := '';
  road_classes INT[];
  cost_column VARCHAR;
  rev_cost_column VARCHAR;
  road_segment INT;
  service_area_polygon_geojson TEXT;
BEGIN
  -- WALK
  IF $5 = 'W' THEN
    road_classes := ARRAY[104,105,106,107,108,124,109.125,123,110,111,112,113,202,114,118,119,120,122,117];
    cost_column := 'cost_ped_m';
    rev_cost_column := 'cost_ped_m';
  -- BICYCLE
  ELSIF $5 = 'B' THEN
    road_classes := ARRAY[104,105,106,107,108,124,109,125,123,110,11,112,113,202,114,118,119,120,117];
    cost_column := 'cost_bike_m';
    rev_cost_column := 'cost_bike_m';
  -- CAR
  ELSIF $5 = 'C' THEN
    road_classes := ARRAY[101,102,104,105,106,107,108,124,109,125,123,110,111,112];
    cost_column := 'cost_m';
    rev_cost_column := 'cost_m_rev';
  END IF;

  SELECT get_road_segment($2, $3, $4, road_classes) INTO road_segment;
  SELECT get_service_area_polygon($2, $3, $4, road_classes, road_segment, $1, cost_column, rev_cost_column) INTO service_area_polygon_geojson;
  RETURN service_area_polygon_geojson;
END
$$;
