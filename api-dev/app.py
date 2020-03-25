import json
from compile_solidity_utils import w3
from flask import Flask, Response, request, jsonify
from marshmallow import Schema, fields, ValidationError

        address UserAddress;
        string location_latitude;
        string location_longitude;
        uint age;
        uint weight;

class UserSchema(Schema):
    userAddress = fields.String(required=True)
    location_latitude = fields.String(required=True)
    location_longitude = fields.String(required=True)
    age = fields.Integer(required=True)
    weight = fields.Integer(required=True)


app = Flask(__name__)


# api to set new user every api call
@app.route("/blockchain/user", methods=['POST'])
def transaction():
    
    w3.eth.defaultAccount = w3.eth.accounts[1]
    with open("api-dev/resources/data.json", 'r') as f:
        datastore = json.load(f)
    abi = datastore["abi"]
    contract_address = datastore["contract_address"]

    # Create the contract instance with the newly-deployed address
    user = w3.eth.contract(
        address=contract_address, abi=abi,
    )
    body = request.get_json()
    result, error = UserSchema().load(body)
    if error:        
        return jsonify(error), 422
    tx_hash = user.functions.setUser(
        result['userAddress'], result['location_latitude'], result['location_longitude'],
        result['age'] , , result['weight']
    )
    tx_hash = tx_hash.transact()
    # Wait for transaction to be mined...
    w3.eth.waitForTransactionReceipt(tx_hash)
    user_data = user.functions.getUser().call()
    return jsonify({"data": user_data}), 200

