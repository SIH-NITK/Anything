import os
import zipfile
import tempfile

import click
from flask import current_app, g
from flask.cli import with_appcontext

from flaskr.db import get_db, close_db

def calculate(dataset_id):
    """Calculates all values associated with a dataset."""

    db = get_db()
    dataset = db.execute('SELECT * FROM dataset WHERE id = ?', (dataset_id, )).fetchone()

    dataset_filepath = os.path.join('uploads', dataset['dataset_file_name'])

    # Create temporary directory
    with tempfile.TemporaryDirectory(dir='uploads') as temp_dir:
        root_path = extract_zip(dataset_filepath, temp_dir)

    # There should be a 2D or 2 1D array that represent x and y values

    # Save to /results/dataset_id.csv

def extract_zip(dataset_filepath, temp_dir):
    """Extract zip file and return the root of extracted file."""
    with zipfile.ZipFile(dataset_filepath, 'r') as zip_ref:
        zip_ref.extractall(temp_dir)

    return os.path.join(temp_dir, os.listdir(temp_dir)[0])

@click.command('estimate')
@with_appcontext
def estimate():
    dataset_id = int(input('Enter dataset id: '))
    calculate(dataset_id)
    click.echo('Graph created')

def add_estimate_to_cli(app):
    """Register `estimate` with the application."""
    app.teardown_appcontext(close_db)
    app.cli.add_command(estimate)
