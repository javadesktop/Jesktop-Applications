package jcdsee.imagebrowser;

/** ImageBrowser implements this interface in
 * order to listen to the image loading thread
 */
public interface ImageLoaderListener {
  /**
   * This is called when an image has been loaded
   * @param info the image that was loaded
   * @return void
   */
  public void imageLoaded( ImageInfo info );
}
