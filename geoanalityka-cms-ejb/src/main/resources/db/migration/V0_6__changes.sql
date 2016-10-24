ALTER TABLE login_attempts ADD COLUMN ip VARCHAR;
ALTER TABLE demographic_analyses DROP CONSTRAINT demographic_analyses_traveltime_check;
ALTER TABLE demographic_analyses RENAME COLUMN traveltime TO travel_time_or_distance;
ALTER TABLE demographic_analyses ADD CONSTRAINT demographic_analyses_travel_time_or_distance_check CHECK (travel_time_or_distance >= 1 AND travel_time_or_distance <= 100000);