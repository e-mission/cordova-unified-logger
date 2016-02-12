#import "BEMUnifiedLogger.h"
#import "DBLogging.h"

@implementation BEMUnifiedLogger

- (void)pluginInitialize
{
    // TODO: We should consider adding a create statement to the init, similar
    // to android - then it doesn't matter if the pre-populated database is not
    // copied over.
    NSLog(@"UnifiedLogger:pluginInitialize singleton -> initialize native DB");
    [[DBLogging database] log:@"finished init of iOS native code" atLevel:@"INFO"];
}

- (void)log:(CDVInvokedUrlCommand*)command
{
    NSString* callbackId = [command callbackId];
    @try {
        NSString* level = [[command arguments] objectAtIndex:0];
        NSString* message = [[command arguments] objectAtIndex:1];

        [[DBLogging database] log:message atLevel:level];
        CDVPluginResult* result = [CDVPluginResult
                                   resultWithStatus:CDVCommandStatus_OK];
        [self.commandDelegate sendPluginResult:result callbackId:callbackId];
    }
    @catch (NSException* e) {
        NSString* msg = [NSString stringWithFormat: @"While logging, error %@", e];
        CDVPluginResult* result = [CDVPluginResult
                                   resultWithStatus:CDVCommandStatus_ERROR
                                   messageAsString:msg];
        [self.commandDelegate sendPluginResult:result callbackId:callbackId];
    }
}

- (void)clear:(CDVInvokedUrlCommand*)command
{

    NSString* callbackId = [command callbackId];

    @try {
        [[DBLogging database] clear];
    }
    @catch (NSException* e) {
        NSString* msg = [NSString stringWithFormat: @"While clearing DB, error %@", e];
        CDVPluginResult* result = [CDVPluginResult
                                   resultWithStatus:CDVCommandStatus_ERROR
                                   messageAsString:msg];
        [self.commandDelegate sendPluginResult:result callbackId:callbackId];
    }
}

@end
