/**
 * This file has 2 contracts: tokenRecipient and MyToken
*/


pragma solidity >=0.4.21;

// import library file
import "api-dev/contract/stringUtils.sol";



contract CovidToken { 
    // / Public variables of the token */
    // enum type variable to store user gender
    enum genderType{ male, female}
    
    // Actual user object which we will store.
    struct user{
        string name;
        string nationality;
        genderType gender;
        int age;
        int weight;
        int location_latitude;
        int location_longitude;
    }

    // user object
    user user_obj;
    
    //Internal function to conver genderType enum from string
    function getGenderFromString(string gender) internal returns   (genderType) {
        if(StringUtils.equal(gender, "male")) {
            return genderType.male;
        } else {
        return genderType.female;
        }
    }
    
    //Internal function to convert genderType enum to string
    function getGenderToString(genderType gender) internal returns (string) {
        if(gender == genderType.male) {
            return "male";
        } else {
            return "female";
    }
    }


    // set user public function
    // This is similar to persisting object in db.
    function setUser(string name, string gender, int age, int weight, int location_latitude, int location_longitude) public {
        genderType gender_type = getGenderFromString(gender);
        user_obj = user({name:name, gender: gender_type});
    }
  
    // get user public function
    // This is similar to getting object from db.
    function getUser() public returns (string, string,int , int, int,int) {
        return (user_obj.name, getGenderToString(user_obj.gender));
    }
  
}     