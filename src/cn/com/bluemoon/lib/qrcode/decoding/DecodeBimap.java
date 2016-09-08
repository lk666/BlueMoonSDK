package cn.com.bluemoon.lib.qrcode.decoding;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;

import java.util.Hashtable;
import java.util.Vector;

public class DecodeBimap {

	public static Result decodeFile(Bitmap bitmap)
	{

		Result rawResult = null;
		try {
			
			MultiFormatReader multiFormatReader = new MultiFormatReader();


			Hashtable<DecodeHintType, Object> hints = new Hashtable<DecodeHintType, Object>(2);

			Vector<BarcodeFormat> decodeFormats = new Vector<BarcodeFormat>();
			if (decodeFormats == null || decodeFormats.isEmpty()) {
				decodeFormats = new Vector<BarcodeFormat>();

				decodeFormats.addAll(DecodeFormatManager.ONE_D_FORMATS);
				decodeFormats.addAll(DecodeFormatManager.QR_CODE_FORMATS);
				decodeFormats.addAll(DecodeFormatManager.DATA_MATRIX_FORMATS);
			}
			hints.put(DecodeHintType.POSSIBLE_FORMATS, decodeFormats);

			multiFormatReader.setHints(hints);

			rawResult = multiFormatReader
					.decodeWithState(new BinaryBitmap(new HybridBinarizer(
							new BitmapLuminanceSource(bitmap))));
		} catch (NotFoundException e) {
			e.printStackTrace();
			return null;
		}
		
		return rawResult;
	}
	
	public static Result decodeFile(String path)
	{
		return decodeFile(BitmapFactory.decodeFile(path));
	}
}
