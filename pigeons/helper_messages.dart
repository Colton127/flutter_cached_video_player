import 'package:pigeon/pigeon.dart';

@HostApi(dartHostTestHandler: 'TestHostVideoPlayerHelperApi')
abstract class VideoPlayerHelperApi {
  @async
  bool precacheVideos(List<String> videos);

  @async
  bool precacheVideo(String videoUrl);
}
