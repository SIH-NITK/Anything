import os
import zipfile
import tempfile

import click
from flask import current_app, g
from flask.cli import with_appcontext

from flaskr.db import get_db, close_db

import cv2
import numpy as np
import os
import matplotlib.pyplot as plt

def calculate(dataset_id):
    """Calculates all values associated with a dataset."""

    db = get_db()
    dataset = db.execute('SELECT * FROM dataset WHERE id = ?', (dataset_id, )).fetchone()

    dataset_filepath = os.path.join('uploads', dataset['dataset_file_name'])

    # Create temporary directory
    with tempfile.TemporaryDirectory(dir='uploads') as temp_dir:
        root_path = extract_zip(dataset_filepath, temp_dir)

        nvdi_density = []
        image_files = sorted(os.listdir(root_path))

        for image_file in image_files:
            # Read image in grey scale
            image = cv2.imread(os.path.join(root_path, image_file), cv2.IMREAD_GRAYSCALE)

            # Select pixels which greater than threshold
            _, image = cv2.threshold(image, np.mean(image), 255, cv2.THRESH_BINARY)

            # Invert image
            image = cv2.bitwise_not(image)

            # Calculate number of crop pixels per pixel
            nvdi_density.append(cv2.countNonZero(image)/(image.shape[0] * image.shape[1]))
            # nvdi_density.append(cv2.countNonZero(image)/(image.shape[0] * image.shape[1]))

        plt.plot(range(len(image_files)), nvdi_density, 'ro-')
        plt.savefig(os.path.join('flaskr', 'static', 'results', str(dataset_id) + '.png'))


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
