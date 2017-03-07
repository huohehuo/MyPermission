package lins.com.mypermission.SolutionOne;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;

import butterknife.BindView;
import butterknife.ButterKnife;
import lins.com.mypermission.R;

//此类可能会是只能处理一个Activity的单一权限问题，主要举个例子来判断权限
public class ChkPermission extends AppCompatActivity {

    private int xiangji = 3;
    private File sdcardTempFile = new File("/mnt/sdcard/", "tmp_pic_" + SystemClock.currentThreadTimeMillis() + ".jpg");
    private static final int REQUEST_CODE = 0;//请求码
    private Bitmap bmp;

    @BindView(R.id.iv_loc)
    ImageView ivLoc;
    private PermissionsChecker permissionsChecker;
    //所需的全部权限
    static final String[] PERMISSIONS = new String[]{

            Manifest.permission.CAMERA
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_perssion);
        ButterKnife.bind(this);
        permissionsChecker = new PermissionsChecker(this);

        //缺少权限时，进入权限配置界面
        if (permissionsChecker.lacksPermissions(PERMISSIONS)) {
            startPermissionsActivity();
        }
        //这里会调用拍照功能，所以以上代码是用于判断是否获取了拍照权限
        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        Uri u = Uri.fromFile(sdcardTempFile);
        intent.putExtra(MediaStore.Images.Media.ORIENTATION, 0);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, u);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, xiangji);
    }


    public static void startCp(Context context) {
        Intent intent = new Intent(context, ChkPermission.class);
        context.startActivity(intent);

    }

    @Override
    protected void onResume() {
        super.onResume();
        //缺少权限时，进入权限配置界面
        if (permissionsChecker.lacksPermissions(PERMISSIONS)) {
            startPermissionsActivity();
        }
    }

    private void startPermissionsActivity() {
        PermissionsActivity.startActivityForResult(this, REQUEST_CODE, PERMISSIONS);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //拒绝时，关闭页面，缺少主要权限，无法进行
        if (requestCode == REQUEST_CODE && resultCode == PermissionsActivity.PERMISSIONS_DENIED) {
            Toast.makeText(ChkPermission.this, "相机权限获取成功", Toast.LENGTH_SHORT).show();
            finish();

        }
    }
}
