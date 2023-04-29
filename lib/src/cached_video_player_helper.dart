import 'package:cached_video_player/src/video_player_helper_api.dart';

class CachedVideoPlayerHelper {
  CachedVideoPlayerHelper._privateConstructor();

  static final CachedVideoPlayerHelper _instance =
      CachedVideoPlayerHelper._privateConstructor();

  static CachedVideoPlayerHelper get instance => _instance;

  VideoPlayerHelperApi _api = VideoPlayerHelperApi();

  Future<bool> precacheVideo(String videoUrl) {
    return _api.precacheVideo(videoUrl);
  }

  Future<bool> precacheVideos(List<String> videoUrlList) {
    return _api.precacheVideos(videoUrlList);
  }

  void preparePlayerAfterError(int textureId) {
    _api.preparePlayerAfterError(textureId);
  }
}
