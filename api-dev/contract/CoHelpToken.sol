pragma solidity >=0.5.8;

import "api-dev/dapp/library/stringUtils.sol";

contract CoHelpToken {

    struct user{
        address UserAddress;
        string location_latitude;
        string location_longitude;
        uint age;
        uint weight;
    }
    user user_obj;
        // set user public function
    // This is similar to persisting object in db.
    function setUser(address UserAddress, string location_latitude, string location_longitude, uint age, uint weight) public {
        user_obj = user({
            UserAddress:UserAddress, location_latitude:location_latitude, location_longitude:location_longitude, age:age, weight:weight
        });
    }

    // get user public function
    // This is similar to getting object from db.
    function getUser() public returns (string, string) {
        return (
            user_obj.UserAddress, user_obj.location_latitude, user_obj.location_longitude, user_obj.age, user_obj.weight
        );
    }
    mapping(address => uint) public balances;

    // Events allow clients to react to specific
    // contract changes you declare
    event Sent(address from, address to, uint amount);

    // Constructor code is only run when the contract
    // is created
    constructor() public {
        user_obj.UserAddress = msg.sender;
    }

    // Sends an amount of newly created coins to an address
    // Can only be called by the contract creator
    function mint(address receiver, uint amount) public {
        require(msg.sender == user_obj.UserAddress);
        require(amount < 1e60);
        balances[receiver] += amount;
    }

    // Sends an amount of existing coins
    // from any caller to an address
    function send(address receiver, uint amount) public {
        require(amount <= balances[msg.sender], "Insufficient balance.");
        balances[msg.sender] -= amount;
        balances[receiver] += amount;
        emit Sent(msg.sender, receiver, amount);
    }
}