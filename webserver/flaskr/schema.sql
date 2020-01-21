DROP TABLE IF EXISTS dataset;

CREATE TABLE dataset (
  id           INTEGER PRIMARY KEY AUTOINCREMENT,
  name         TEXT,
  description  TEXT,
  tags         TEXT,
  dataset_size FLOAT,
  files        INTEGER
);
