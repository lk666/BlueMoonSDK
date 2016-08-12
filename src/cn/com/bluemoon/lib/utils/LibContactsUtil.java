package cn.com.bluemoon.lib.utils;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;

/**
 * Created by ljl on 2016/8/12.
 */
public class LibContactsUtil {

    private static int mRequestCode;
    private static Activity context;

    public static void open(Activity activity, int requestCode) {
        mRequestCode = requestCode;
        context = activity;
        Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
        activity.startActivityForResult(intent, requestCode);
    }

    public static void open(Fragment fragment, int requestCode) {
        mRequestCode = requestCode;
        context = fragment.getActivity();
        Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
        fragment.startActivityForResult(intent, requestCode);
    }


    public static String[] getContactData(int requestCode, int resultCode, Intent data) {

        if (requestCode == mRequestCode && resultCode == Activity.RESULT_OK) {
            Uri contactData = data.getData();
            Cursor cursor = context.getContentResolver().query(contactData, null, null, null, null);
            if (cursor.moveToFirst()) {
                String name = cursor.getString(cursor
                        .getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                String number = cursor.getString(cursor
                        .getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                return new String[] {name, number};
            }
        }

        return new String[] {"", ""};
    }
}
