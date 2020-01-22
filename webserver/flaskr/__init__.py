import os

from flask import Flask
from . import db
from . import dataset
from . import estimator

def create_app(test_config=None):
    """Create and configure application instance."""
    app = Flask(__name__, instance_relative_config=True)
    app.config.from_mapping(
        SECRET_KEY='dev',
        DATABASE=os.path.join(app.instance_path, 'flaskr.sqlite'),
    )

    if test_config is None:
        # load the instance config if it exists when not testing
        app.config.from_pyfile('config.py', silent=True)
    else:
        # Load the test config if passed in
        app.config.from_mapping(test_config)

    # Ensure the instance folder exists
    try:
        os.makedirs(app.instance_path)
    except OSError:
        pass

    @app.route('/predict')
    def predict():
        return 'Get your predictions here!'

    db.init_app(app)
    app.register_blueprint(dataset.bp)
    estimator.add_estimate_to_cli(app)

    return app
