import os

from flask import g

from flaskr.db import get_db

def calculate(dataset_id):
    """Calculates all values associated with a dataset."""

    db = get_db()
    dataset = db.execute('SELECT * FROM dataset WHERE id = ?', (dataset_id, )).fetchone()

    print("Estimator for the win!")

    dataset_filepath = os.path.join('/uploads', dataset['dataset_file_name'])

    # Run your magic
    # There should be a 2D or 2 1D array that represent x and y values

    # Save to /results/dataset_id.csv
