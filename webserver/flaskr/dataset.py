import functools

from flask import (
    Blueprint, flash, g, redirect, render_template, url_for
)

from flaskr.db import get_db

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
    db = get_db()
    error = None

    db.execute(
        'INSERT INTO dataset (name, description) VALUES (?, ?)',
        (name, description)
    )

    db.commit()

    flash('Dataset added')
    return redirect(url_for('dataset.index'))

@bp.route('/<int:dataset_id>', methods=['GET'])
def show(dataset_id):
    pass
