CREATE TABLE staging_nyc_social_media_usage
(
    agency VARCHAR(200),
    platform VARCHAR(100),
    url VARCHAR(500),
    sample_date DATE,
    action INTEGER
);

CREATE TABLE agency
(
    id SERIAL PRIMARY KEY,
    name VARCHAR(200)
);

CREATE TABLE platform
(
    id SERIAL PRIMARY KEY,
    name VARCHAR(100)
);

CREATE TABLE social_media_usage_samples
(
    id INTEGER DEFAULT nextval('social_media_usage_samples_id_seq'::regclass) NOT NULL,
    agencyid INTEGER NOT NULL,
    platformid INTEGER,
    url VARCHAR(500),
    sample_date DATE,
    actions INTEGER,
    create_date DATE DEFAULT now(),
    CONSTRAINT social_media_usage_samples_pkey PRIMARY KEY (id, agencyid),
    CONSTRAINT social_media_usage_samples_agencyid_fkey FOREIGN KEY (agencyid) REFERENCES agency (id),
    CONSTRAINT social_media_usage_samples_platformid_fkey FOREIGN KEY (platformid) REFERENCES platform (id)
);
CREATE UNIQUE INDEX uq_agency_url_sample ON social_media_usage_samples (agencyid, url, sample_date);
