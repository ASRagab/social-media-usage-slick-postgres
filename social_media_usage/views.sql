DROP VIEW IF EXISTS view_smus_max_actions_on_date;
CREATE VIEW view_smus_max_actions_on_date AS
  SELECT DISTINCT
    a.name,
    t.maxDate,
    t.maxActions,
    smus.url
  FROM (
         SELECT
           agencyid,
           MAX(actions)     maxActions,
           MAX(sample_date) maxDate
         FROM social_media_usage_samples
         GROUP BY agencyid
       ) t
    INNER JOIN agency a ON a.id = t.agencyid
    INNER JOIN social_media_usage_samples smus ON smus.agencyid = t.agencyid AND smus.actions = t.maxActions;