// @dart = 2.9

import 'package:pigeon/pigeon.dart';

class VideoItem {
  String videoUrl;
  int size;
}


@HostApi(dartHostTestHandler: 'TestHostVideoPlayerHelperApi')
abstract class VideoPlayerHelperApi {
  @async
  bool precacheVideos(List<VideoItem> videos);

  @async
  bool precacheVideo(VideoItem video);

  void preparePlayerAfterError(int textureId);
}
