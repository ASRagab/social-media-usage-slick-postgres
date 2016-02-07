COPY staging_nyc_social_media_usage(agency, platform, url, sample_date, action)
    FROM 'staging_nyc_social_media_usage.csv' DELIMITER ',' CSV HEADER;

INSERT INTO agency(name)
    SELECT DISTINCT agency FROM staging_nyc_social_media_usage;

INSERT INTO platform(name)
    SELECT DISTINCT platform FROM staging_nyc_social_media_usage;
    
INSERT INTO social_media_usage_samples(agencyID, platformID, url, sample_date, actions)
    SELECT agency.id, platform.id, snsmu.url, snsmu.sample_date, snsmu.action
      FROM staging_nyc_social_media_usage AS snsmu
        INNER JOIN agency ON agency.name = snsmu.agency
        INNER JOIN platform ON platform.name = snsmu.platform;