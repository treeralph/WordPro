const express = require("express");
const bodyParser = require("body-parser");
const AWS = require("aws-sdk");
const cors = require("cors");

const app = express();
app.use(bodyParser.json());
app.use(cors({
  origin: '*',
}));

const COGNITO_USER_POOL_ID = "ap-northeast-2_QT4P2AbO5"; // Replace with your Cognito User Pool ID
const COGNITO_APP_CLIENT_ID = "6u6g5ojdham6icvkb0v04r4dgu"; // Replace with your Cognito App Client ID

AWS.config.region = "ap-northeast-2"; // Replace with your AWS region, e.g., "us-east-1"
const CognitoIdentityServiceProvider = AWS.CognitoIdentityServiceProvider;
const cognito = new CognitoIdentityServiceProvider();

const server = app.listen(3002, () => {
    console.log("Server start: post:3002");
});

app.post("/signin", async (req, res) => {
  var reqBody_signin = req.body;
  var username_signin = reqBody_signin.username;
  var password_signin = reqBody_signin.password;
  const authParams = {
    AuthFlow: "USER_PASSWORD_AUTH",
    ClientId: COGNITO_APP_CLIENT_ID,
    AuthParameters: {
      USERNAME: username_signin,
      PASSWORD: password_signin,
    },
  };
  try {
    const authResult = await cognito.initiateAuth(authParams).promise();
    res.json({ success: true, message: "Login successful", data: authResult });
  } catch (error) {
    res.status(400).json({ success: false, message: "Login failed", error });
  }
});

app.post("/confirm", async (req, res) => {
  var reqBody_confirm = req.body;
  var username_confirm = reqBody_confirm.username;
  var confirmationCode_confirm = reqBody_confirm.confirmationCode;

  if (!username_confirm || !confirmationCode_confirm) {
    return res.status(400).send("Missing required parameters");
  }

  try {
    const params = {
      ClientId: COGNITO_APP_CLIENT_ID,
      Username: username_confirm,
      ConfirmationCode: confirmationCode_confirm,
    };

    const result = await cognito.confirmSignUp(params).promise();
    res.status(200).send("User confirmed successfully");
  } catch (error) {
    console.error(error);
    res.status(500).send("Error during confirmation");
  }
});

app.post("/signup", async (req, res) => {
  var reqBody_signup = req.body;
  var username_signup = reqBody_signup.username;
  var password_signup = reqBody_signup.password;
  var nickname_signup = reqBody_signup.nickname;

  if (!username_signup || !password_signup || !nickname_signup) {
    return res.status(400).send("Missing required parameters");
  }

  try {
    const params = {
      ClientId: COGNITO_APP_CLIENT_ID,
      Username: username_signup,
      Password: password_signup,
      UserAttributes: [
        {
          Name: nickname_signup,
        },
      ],
    };

    const result = await cognito.signUp(params).promise();
    res.status(200).send(result);
  } catch (error) {
    console.error(error);
    res.status(500).send("Error during signup");
  }
});

