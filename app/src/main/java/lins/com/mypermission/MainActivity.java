package lins.com.mypermission;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.rxpermisson.PermissionAppCompatActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import lins.com.mypermission.SolutionOne.ChkPermission;
import rx.Subscriber;


//该类主要是把所有需要的权限加入并判断，对于特殊权限，会立即做出询问，对于一般权限，触发后才会询问
public class MainActivity extends PermissionAppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();

    LocationManager locationManager;
    @BindView(R.id.btn)
    Button btn;
    @BindView(R.id.btn2)
    Button btn2;
    @BindView(R.id.btn3)
    Button btn3;
    private double latitude = 0.0;
    private double longitude = 0.0;
    private Location location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        checkPermission();
        //初始化位置服务
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
    }

    //用于获取地址权限的简易方法
    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if (location != null) {
            latitude = location.getLatitude();
            longitude = location.getLongitude();
            Log.e("success","location SUccess");

        }
        Log.e("aaa",latitude+"");
    }

    //执行权限检测，其中拍照拍摄权限会在软件启动的时候询问，而地理位置和拨打电话需要触发才会去询问
    private void checkPermission(){
        checkPermission(R.string.help, Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
                //Manifest.permission.CAMERA,
                Manifest.permission.CALL_PHONE)
                .subscribe(new Subscriber<Boolean>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onNext(Boolean aBoolean) {
                        if (aBoolean) {
                            Toast.makeText(MainActivity.this, "请求授权成功", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(MainActivity.this, "请求授权失败", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    @OnClick({R.id.btn, R.id.btn2, R.id.btn3})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn:
                //用于触发位置权限询问
                    getLocation();
                break;
            case R.id.btn2:
                //检测相机功能，也可在本类中的checkPermission方法中加如相机权限检测，以下是第二种方式
                ChkPermission.startCp(MainActivity.this);
                break;
            case R.id.btn3:
                //启动拨号，用于触发打电话权限询问
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_CALL);
                intent.setData(Uri.parse("tel:10086"));
                startActivity(intent);
                break;
        }
    }


}
