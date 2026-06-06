package SetImageUtil

import android.content.res.AssetManager
import android.graphics.BitmapFactory
import android.widget.ImageView
import java.io.IOException

/**
 * パーツに対して画像を設定。
 * @param imageName 画像のファイル名。
 * @param target 画像を設定するImageView。
 * @param assets 画像の取得元となるassets。
 */
fun setImageToImageView(imageName: String, target: ImageView, assets: AssetManager) {
    try {
        val instream = assets.open(imageName);
        val bitmap = BitmapFactory.decodeStream(instream);
        target.setImageBitmap(bitmap);
    } catch (e: IOException) {
        e.printStackTrace();
    }
}