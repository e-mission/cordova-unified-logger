/*global cordova, module*/

var exec = require("cordova/exec")

var ULogger = {
    /* This is the only call to native code. For the others, we can just read
     * the database directly, which will (thoretically) reduce development
     * effort. It will also help us understand how to use one plugin as part of
     * another plugin (assuming such a thing is possible, and provide a good
     * exemplar for the usercache plugin.
     */
    LOG_TABLE: "logTable",
    KEY_ID: "ID",
    KEY_TS: "ts",
    KEY_LEVEL: "level",
    KEY_MESSAGE: "message",

    LEVEL_DEBUG: "DEBUG",
    LEVEL_INFO: "INFO",
    LEVEL_WARN: "WARN",
    LEVEL_ERROR: "ERROR",

    /*
     * Arguments:
     *  - tag: module or component name, such as "usercache" or "tracking"
     *  - errorCallback: function to pass any errors while logging.
     *    The expectation is that it takes a single argument.
     */

    log: function (level, message, errorCallback) {
        exec(null, errorCallback, "UnifiedLogger", "log", [level, message]);
    },

    clearAll: function(successCallback, errorCallback) {
        exec(null, errorCallback, "UnifiedLogger", "clear", []);
    },

    db: window.sqlitePlugin.openDatabase({
        name: "loggerDB",
        location: 0,
        createFromLocation: 1
    }),

    getMessagesFromIndex: function (startIndex, count, successCallback, errorCallback) {
        ULogger.db.transaction(function(tx) {
            var selQuery = "SELECT * FROM "+ULogger.LOG_TABLE+
                           " WHERE "+ULogger.KEY_ID+" > "+startIndex+
                           " ORDER BY "+ULogger.KEY_ID+" DESC LIMIT "+count;
            // Log statements in the logger don't go into the logger.
            // No infinite loop here!
            console.log("About to execute query "+selQuery+" against "+ULogger.LOG_TABLE);
            tx.executeSql(selQuery,
                [],
                function(tx, data) {
                    var resultList = [];
                    console.log("Result has "+data.rows.length+" rows");
                    for (i = 0; i < data.rows.length; i++) {
                        var currRow = data.rows.item(i);
                        currRow.fmt_time = moment.unix(currRow.ts).format("llll");
                        resultList.push(currRow);
                    }
                    successCallback(resultList);
                },
                function(e) {
                    errorCallback(e);
                });
        });
    },

    getMessagesForRange: function (startTime, endTime, successCallback, errorCallback) {
    }

}

module.exports = ULogger;
