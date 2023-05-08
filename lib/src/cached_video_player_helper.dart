import 'package:cached_video_player/src/video_player_helper_api.dart';


class CachedVideoPlayerHelper {
  CachedVideoPlayerHelper._privateConstructor();

  static final CachedVideoPlayerHelper _instance =
      CachedVideoPlayerHelper._privateConstructor();

  static CachedVideoPlayerHelper get instance => _instance;

  VideoPlayerHelperApi _api = VideoPlayerHelperApi();

  Future<bool> precacheVideo(VideoItem video) {
    return _api.precacheVideo(video);
  }

  Future<bool> precacheVideos(List<VideoItem> videoList) {
    return _api.precacheVideos(videoList);
  }

  void preparePlayerAfterError(int textureId) {
    _api.preparePlayerAfterError(textureId);
  }
}
