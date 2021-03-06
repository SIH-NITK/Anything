from setuptools import find_packages, setup

setup(
    name='webserver',
    version='0.1',
    packages=find_packages(),
    include_package_data=True,
    zip_safe=False,
    install_requires=[
        'flask',
        'python-dotenv',
        'PIL',
        'numpy',
        'matplotlib',
        'seaborn',
        'zipfile'
    ],
)
