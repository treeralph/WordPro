/**
 * 0502 01fixed
 */
const express = require("express");
const bodyParser = require("body-parser");
const jsonParser = bodyParser.json();
const app = express();
const mysql = require("mysql2");
const connection = mysql.createConnection({
    host: "54.180.94.51",
    user: "wordpro_admin",
    password: "qlqjs940",
    database: "wordpro",
});
connection.connect((err) => {
    if(err) throw err;
    console.log("MySQL DB connect: success");
});
const admin = require("firebase-admin");
const serviceAccount = require("./res/wordpro-f1384-firebase-adminsdk-34vl5-2cff244f7e.json");
const server = app.listen(3001, () => {
    console.log("Server start: post:3001");
    });
admin.initializeApp({
    credential: admin.credential.cert(serviceAccount),
});

app.post("/api/sentence/upload", jsonParser, async(req, res) => {
    
    var reqBody = req.body;
    var sentence = reqBody.sentence;
    var word = reqBody.word;
    var wordIndex = reqBody.word_index;
    var cheatSheet = reqBody.cheat_sheet;
    var path = reqBody.path;
    var uid = reqBody.uid;

    var sentenceQuery = "insert into sentences(sentence, word, word_index, cheat_sheet, path, uid) values('" + 
        sentence + "', '" + 
        word + "', '" +
        wordIndex + "', '" + 
        cheatSheet + "', '" + 
        path + "', '" + 
        uid + "')";
    var resObj = {
        message: "",
    };

    connection.query(sentenceQuery, async(error, results, field) => {
        if(error){
            res.statusCode = 500;
            resObj.message = "Error occur executing query: " + error.message;
            res.send(resObj);
        }else{
            resObj.message = "Query Execution: Success: " + results;
            res.send(resObj);
        }
    });
});

async function queryOperator(sqlQuery) {
    return new Promise((resolve, reject) => {
        connection.query(sqlQuery, async(error, results, field) => {
            if(error){
                reject(error);
            }else{
                resolve(results);
            }
        });
    });
}

app.post("/api/team/make", jsonParser, async(req, res) => {
    const reqBody = req.body;
    const sql_queries = Object.entries(reqBody.sql_queries);
    const users = Object.entries(reqBody.users);
    const team_name = reqBody.team_name;
    const make_team_request_user = reqBody.make_team_request_user;
    const num_sentence = sql_queries.length;
    let team_identifier;
        var resObj = {
        message: "",
    };
    connection.beginTransaction((err) => {
        if (err) {
            res.status(500).send('Error starting transaction');
            return;
        }
        var tempUser = make_team_request_user + "/";
        for(let i=0; i<users.length; i++){
            tempUser = tempUser + users[i][1];
        }
        var team_make_qeury = "insert into teams(team_name, users) values('" + team_name + "', '" + tempUser + "')";
        connection.query(team_make_qeury, async(error, results, field) => {
            if(error){
                connection.rollback(() => {
                    console.log("app.post(/api/test)_connection.query: team_make_query: error occur: " + error.message);
                    resObj.message = "Error occur executing query: " + error.message;
                    res.status(500).send(resObj);
                });
                return;
            }else{
                team_identifier = results.insertId;
                for(let i=0; i<sql_queries.length; i++){
                    var sql_query = sql_queries[i][1];
                    var head = sql_query.substring(0, sql_query.length-2);
                    var tail = sql_query.substring(sql_query.length-2);
                    var refined_sql_query = head + team_identifier + tail;
                    console.log("app.post(/api/test)_connection.query: refined_query: " + refined_sql_query);
                    await queryOperator(refined_sql_query)
                        .then((result) => {
                            console.log("Query Execution: Success: " + refined_sql_query);
                        }).catch((error) => {
                            connection.rollback(() => {
                                console.log("app.post(/api/test)_connection.query: sentence_query: error occur: " + error.message);
                                resObj.message = "Error occur executing query: " + error.message;
                                res.status(500).send(resObj);
                            });
                            return;
                        });
                }
                console.log("All Sentences are uploaded"); //
                connection.commit(async (error) => {
                    if(err){
                        connection.rollback(() => {
                            console.log("app.post(/api/test)_connection.query: sentence_query: error occur: " + error.message);
                            resObj.message = "Error occur executing query: " + error.message;
                            res.status(500).send(resObj);
                        });
                        return;
                    }
                    // Make Team: Success
                    for(let i=0; i<=users.length; i++){
                        var user;
                        if(i < users.length){
                            user = users[i][1];
                        }else{
                            user = make_team_request_user;
                        }
                        var userQuery = "select * from users where nickname='" + user + "'";
                        await queryOperator(userQuery)
                            .then((result) => {
                                console.log("result: ", result);
                                var device_token = result[0].device_token;
                                console.log("app.post(/api/test)_connection.commit: read user from db: Success: device_token: " + device_token);
                                var payload = {
                                    token: device_token,
                                    notification: {
                                        title: "Invitation: " + team_name,
                                        body: "Enter into!",
                                    },
                                    data: {
                                        wFCM: "INVITE_TEAM",
                                        team_identifier: team_identifier.toString(), 
                                        make_team_request_user: make_team_request_user,
                                        team_name: team_name,
                                        num_sentence: num_sentence.toString(),
                                    },
                                };
                                admin.messaging().send(payload)
                                    .then((response) => {
                                        console.log("Push Notification to device_token: " + device_token + ": Seccess: ", response);
                                    })
                                    .catch((error) => {
                                        console.log("Push Notification to device_token: " + device_token + ": Failure: ", error);
                                    });
                            }).catch((error) => {
                                console.log("app.post(/api/test)_connection.commit: read user from db: Failure: " + error.message);
                            });
                    }

                    console.log("Make Team: " + team_name + ": success");
                    res.statusCode = 200;
                    resObj.message = "Make Team: success";
                    res.send(resObj);
                })
            }
        });
    })
});

app.get("/api/jun", async(req, res) => {
    resObj = {
        statusCode: 200,
        body: {
            message: 'All Right',
        },
    };
    res.send(resObj);
});

app.get("/api/fcm/team/make/:team/:device_token", async(req, res) => {
    console.log("/api/fcm/team/make/", team, "/", device_token, " fcm request");
    let{
        team,
        device_token
    } = req.params;
    payload = {
        token: device_token,
        notification: {
            title: "Invitation: " + team,
            body: "Enter into!",
        },
    };
    admin.messaging().send(payload)
        .then((response) => {
            console.log("Push Notification to device_token:" + device_token + ": Success: ", response);
            resObj = {
                statusCode: 200,
                body: {
                    message: response,
                },
            };
            res.send(resObj);
        })
        .catch((err) => {
            console.log("Push Notification to device_token:" + device_token + ": Failure: ", err);
            
            resObj = {
                statusCode: 500,
                body: {
                    message: err,
                },
            };
            res.send(resObj);
        });
});