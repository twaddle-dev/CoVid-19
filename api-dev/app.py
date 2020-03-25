from flask import Flask
from flask_restful import Api

from resources.user import UserRegister



'''This is section 4 app.py file.'''
app = Flask(__name__)
api = Api(app)

api.add_resource(UserRegister, '/user/<string:name>')

# Name is only set to main when file is explicitly run (not on imports):
if __name__ == '__main__':
    from db import db
    db.init_app(app)
    app.run(port=5000, debug=True)
