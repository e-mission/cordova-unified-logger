/*global cordova, module*/

var exec = require("cordova/exec")

var ULogger = {
    /* This is the only call to native code. For the others, we can just read
     * the database directly, which will (thoretically) reduce development
     * effort. It will also help us understand how to use one plugin as part of
     * another plugin (assuming such a thing is possible, and provide a good
     * exemplar for the usercache plugin.
     */
    var LOG_TABLE = "logTable";
    var KEY_ID = "ID";
    var KEY_TS = "ts";
    var KEY_LEVEL = "level";
    var KEY_MESSAGE = "message";

    var LEVEL_DEBUG = "DEBUG";
    var LEVEL_INFO = "INFO";
    var LEVEL_WARN = "WARN";
    var LEVEL_ERROR = "ERROR";

    /*
     * Arguments:
     *  - tag: module or component name, such as "usercache" or "tracking"
     *  - errorCallback: function to pass any errors while logging.
     *    The expectation is that it takes a single argument.
     */

    log: function (level, message, errorCallback) {
        exec(null, errorCallback, "UnifiedLogger", "log", [level, message]);
    }

    clearAll: function(successCallback, errorCallback) {
        exec(null, errorCallback, "UnifiedLogger", "clear", []);
    }

    db: function() {
        return window.sqlitePlugin.openDatabase({
            name: "loggerDB",
            location: 0,
            createFromLocation: 1
        });
    }

    getMessagesFromIndex: function (startIndex, count, successCallback, errorCallback) {
        db().transaction(function(tx) {
            var selQuery = "SELECT * FROM "+LOG_TABLE+
                           " WHERE "+KEY_ID+" > "+startIndex+
                           " ORDER BY "+KEY_ID+" DESC LIMIT "+count;
            // Log statements in the logger don't go into the logger.
            // No infinite loop here!
            console.log("About to execute query "+selQuery+" against "+LOG_TABLE);
            tx.executeSql(selQuery,
                [],
                function(tx, data) {
                    successCallback(data);
                },
                function(e) {
                    errorCallback(e);
                });
        });
    }

    getMessagesForRange: function (startTime, endTime, successCallback, errorCallback) {
    }

}

module.exports = ULogger;
