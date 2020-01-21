DROP TABLE IF EXISTS dataset;

CREATE TABLE dataset (
  id                INTEGER PRIMARY KEY AUTOINCREMENT,
  name              TEXT,
  description       TEXT,
  tags              TEXT,
  dataset_file_size FLOAT,
  dataset_file_name TEXT,
  files             INTEGER
);
