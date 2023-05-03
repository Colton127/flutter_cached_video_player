// Autogenerated from Pigeon (v3.2.9), do not edit directly.
// See also: https://pub.dev/packages/pigeon
#import "MessagesHelper.h"
#import <Flutter/Flutter.h>

#if !__has_feature(objc_arc)
#error File requires ARC to be enabled.
#endif

static NSDictionary<NSString *, id> *wrapResult(id result, FlutterError *error) {
  NSDictionary *errorDict = (NSDictionary *)[NSNull null];
  if (error) {
    errorDict = @{
        @"code": (error.code ?: [NSNull null]),
        @"message": (error.message ?: [NSNull null]),
        @"details": (error.details ?: [NSNull null]),
        };
  }
  return @{
      @"result": (result ?: [NSNull null]),
      @"error": errorDict,
      };
}
static id GetNullableObject(NSDictionary* dict, id key) {
  id result = dict[key];
  return (result == [NSNull null]) ? nil : result;
}
static id GetNullableObjectAtIndex(NSArray* array, NSInteger key) {
  id result = array[key];
  return (result == [NSNull null]) ? nil : result;
}



@interface VideoPlayerHelperApiCodecReader : FlutterStandardReader
@end
@implementation VideoPlayerHelperApiCodecReader
@end

@interface VideoPlayerHelperApiCodecWriter : FlutterStandardWriter
@end
@implementation VideoPlayerHelperApiCodecWriter
@end

@interface VideoPlayerHelperApiCodecReaderWriter : FlutterStandardReaderWriter
@end
@implementation VideoPlayerHelperApiCodecReaderWriter
- (FlutterStandardWriter *)writerWithData:(NSMutableData *)data {
  return [[VideoPlayerHelperApiCodecWriter alloc] initWithData:data];
}
- (FlutterStandardReader *)readerWithData:(NSData *)data {
  return [[VideoPlayerHelperApiCodecReader alloc] initWithData:data];
}
@end

NSObject<FlutterMessageCodec> *VideoPlayerHelperApiGetCodec() {
  static dispatch_once_t sPred = 0;
  static FlutterStandardMessageCodec *sSharedObject = nil;
  dispatch_once(&sPred, ^{
    VideoPlayerHelperApiCodecReaderWriter *readerWriter = [[VideoPlayerHelperApiCodecReaderWriter alloc] init];
    sSharedObject = [FlutterStandardMessageCodec codecWithReaderWriter:readerWriter];
  });
  return sSharedObject;
}


void VideoPlayerHelperApiSetup(id<FlutterBinaryMessenger> binaryMessenger, NSObject<VideoPlayerHelperApi> *api) {
  {
    FlutterBasicMessageChannel *channel =
      [[FlutterBasicMessageChannel alloc]
        initWithName:@"dev.flutter.pigeon.VideoPlayerHelperApi.precacheVideos"
        binaryMessenger:binaryMessenger
        codec:VideoPlayerHelperApiGetCodec()        ];
    if (api) {
      NSCAssert([api respondsToSelector:@selector(precacheVideosVideos:completion:)], @"VideoPlayerHelperApi api (%@) doesn't respond to @selector(precacheVideosVideos:completion:)", api);
      [channel setMessageHandler:^(id _Nullable message, FlutterReply callback) {
        NSArray *args = message;
        NSArray<NSString *> *arg_videos = GetNullableObjectAtIndex(args, 0);
        [api precacheVideosVideos:arg_videos completion:^(NSNumber *_Nullable output, FlutterError *_Nullable error) {
          callback(wrapResult(output, error));
        }];
      }];
    }
    else {
      [channel setMessageHandler:nil];
    }
  }
  {
    FlutterBasicMessageChannel *channel =
      [[FlutterBasicMessageChannel alloc]
        initWithName:@"dev.flutter.pigeon.VideoPlayerHelperApi.precacheVideo"
        binaryMessenger:binaryMessenger
        codec:VideoPlayerHelperApiGetCodec()        ];
    if (api) {
      NSCAssert([api respondsToSelector:@selector(precacheVideoVideoUrl:completion:)], @"VideoPlayerHelperApi api (%@) doesn't respond to @selector(precacheVideoVideoUrl:completion:)", api);
      [channel setMessageHandler:^(id _Nullable message, FlutterReply callback) {
        NSArray *args = message;
        NSString *arg_videoUrl = GetNullableObjectAtIndex(args, 0);
        [api precacheVideoVideoUrl:arg_videoUrl completion:^(NSNumber *_Nullable output, FlutterError *_Nullable error) {
          callback(wrapResult(output, error));
        }];
      }];
    }
    else {
      [channel setMessageHandler:nil];
    }
  }
}
