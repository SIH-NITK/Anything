import functools
import os

from flask import (
    Blueprint, flash, g, redirect, render_template, url_for, request
)

from flaskr.db import get_db
from . import estimator

bp = Blueprint('dataset', __name__)

@bp.route('/', methods=['GET'])
def index():
    db = get_db()
    datasets = db.execute('SELECT * FROM dataset').fetchall()

    return render_template('dataset/index.html', datasets=datasets)

@bp.route('/new', methods=['GET'])
def new():
    return render_template('dataset/new.html')

@bp.route('/', methods=['POST'])
def create():
    name = request.form['name']
    description = request.form['description']
    tags = request.form['tags']
    files = request.form['files']

    dataset_file = request.files['dataset_file']
    dataset_file.save(os.path.join('uploads', dataset_file.filename))

    # Convert size in bytes to Mb
    dataset_file_size = os.path.getsize(os.path.join('uploads', dataset_file.filename)) / (1024 * 1024)

    db = get_db()
    error = None

    db.execute(
        'INSERT INTO dataset (name, description, tags, dataset_file_size, dataset_file_name, files) VALUES (?, ?, ?, ?, ?, ?)',
        (name, description, tags, dataset_file_size, dataset_file.filename, files)
    )

    db.commit()

    dataset_id = db.execute('SELECT * FROM dataset ORDER BY id DESC').fetchone()['id']
    estimator.calculate(dataset_id)

    return redirect(url_for('dataset.show', dataset_id=dataset_id))

@bp.route('/<int:dataset_id>', methods=['GET'])
def show(dataset_id):
    db = get_db()
    dataset = db.execute('SELECT * FROM dataset WHERE id = ?', (dataset_id,)).fetchone()
    graph_path = os.path.join('results', str(dataset_id) + '.png')
    return render_template('dataset/show.html', dataset=dataset, graph_path=graph_path)
