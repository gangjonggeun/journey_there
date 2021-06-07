
package com.example.journey_there;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import static android.os.Environment.DIRECTORY_PICTURES;


public class MainActivity extends AppCompatActivity {
    private static final int RESULTACTIVITY_CODE = 300;
    private static final int REQUEST_IMAGE_CAPTURE = 672;
    private static final int REQUEST_GALLERY = 673;
    private static final float LIMIT_PER = (float) 75.0;
    private static final int NUM_PAGES = 2;

    private String imageFilePath;
    private Uri photoUri;

    private final String TAG = "MyActivity";
    private Bitmap rbitmap;

    private ViewPager2 viewPager;
    private FragmentStateAdapter pagerAdapter;
    private ImageButton cameraBtn, themaBtn;
    private View dialogView, result_dialogView;
    private Button by_cameraBtn, by_forderBtn;
    private MediaScanner mMediaScanner;

    private int result_label;
    private float result_per;

    private User user;

    private Boolean thema_possible;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewPager = findViewById(R.id.pager);
        pagerAdapter = new ScreenSlidePagerAdapter(this);
        viewPager.setAdapter(pagerAdapter);

        cameraBtn = (ImageButton) findViewById(R.id.cameraBtn);
        themaBtn = (ImageButton) findViewById(R.id.themaBtn);


        mMediaScanner = MediaScanner.getInstance(getApplicationContext());

        FileInputStream inf = null;

        try {
            inf = openFileInput("user.txt");
            ObjectInputStream o = new ObjectInputStream(inf);
            user = (User) o.readObject();

            o.close();
            inf.close();

            thema_possible = true;
        } catch (IOException e) {
            thema_possible = false;

            TedPermission.with(getApplicationContext())
                    .setPermissionListener(permissionListener)
                    .setRationaleMessage("카메라 권한이 필요합니다.")
                    .setDeniedMessage("거부하셨습니다.")
                    .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
                    .check();

        } catch (ClassNotFoundException e) {
            Toast.makeText(getApplicationContext(), " CNF 오류", Toast.LENGTH_SHORT).show();
        }


        cameraBtn.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogView = (View) View.inflate(MainActivity.this, R.layout.selcte_dialog, null);
                AlertDialog.Builder dlg = new AlertDialog.Builder(MainActivity.this);
                dlg.setView(dialogView);
                dlg.show();


                by_cameraBtn = (Button) dialogView.findViewById(R.id.byCameraBtn);
                by_forderBtn = (Button) dialogView.findViewById(R.id.byForlderBtn);
                //스마트카메라
                by_cameraBtn.setOnClickListener(new Button.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        if (intent.resolveActivity(getPackageManager()) != null) {
                            File photoFile = null;
                            try {
                                photoFile = createImageFile();
                            } catch (IOException e) {

                            }

                            if (photoFile != null) {
                                photoUri = FileProvider.getUriForFile(getApplicationContext(), getPackageName(), photoFile);
                                intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                                startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
                            }
                        }

                    }
                });


                by_forderBtn.setOnClickListener(new Button.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent loadintent = new Intent(Intent.ACTION_PICK); //갤러리 불러오기 인텐트
                        loadintent.setType(MediaStore.Images.Media.CONTENT_TYPE); //타입 결정
                        loadintent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        loadintent.setType("image/*"); //이미지 형태만 가져오기
                        startActivityForResult(Intent.createChooser(loadintent, "Select Picture"), REQUEST_GALLERY);
                    }
                });
            }
        });
        themaBtn.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (thema_possible){
                    Intent intent = new Intent(getApplicationContext(), themaActivity.class);
                    startActivity(intent);
                }
                else
                    Toast.makeText(getApplicationContext(), " 스마트 렌즈를 한번 실행해 주세요.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    //이미지 파일 저장 설정
    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "TEST_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,
                ".jpg",
                storageDir
        );
        imageFilePath = image.getAbsolutePath();
        return image;
    }

    //카메라 결과 받기 및 저장 및 다이얼로그 띄우기
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULTACTIVITY_CODE && resultCode == RESULT_OK) {
            thema_possible = data.getBooleanExtra("thema_possible",thema_possible);
        }

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            final Bitmap bitmap = BitmapFactory.decodeFile(imageFilePath);
            ExifInterface exif = null;

            try {
                exif = new ExifInterface(imageFilePath);
            } catch (IOException e) {
                e.printStackTrace();
            }

            int exifOrientation;
            int exifDegree;

            if (exif != null) {
                exifOrientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
                exifDegree = exifOrientationToDegress(exifOrientation);
            } else {
                exifDegree = 0;
            }

            String result = "";
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HHmmss", Locale.getDefault());
            Date curDate = new Date(System.currentTimeMillis());
            String filename = formatter.format(curDate);

            String strFolderName = Environment.getExternalStoragePublicDirectory(DIRECTORY_PICTURES) + File.separator + "Journey There" + File.separator;
            File file = new File(strFolderName);
            if (!file.exists())
                file.mkdirs();

            File f = new File(strFolderName + "/" + filename + ".png");
            result = f.getPath();

            FileOutputStream fOut = null;
            try {
                fOut = new FileOutputStream(f);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                result = "Save Error fOut";
            }

            // 비트맵 사진 폴더 경로에 저장
            rotate(bitmap, exifDegree).compress(Bitmap.CompressFormat.PNG, 70, fOut);

            rbitmap = rotate(bitmap, exifDegree);
            try {
                fOut.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                fOut.close();
                mMediaScanner.mediaScanning(strFolderName + "/" + filename + ".png");
            } catch (IOException e) {
                e.printStackTrace();
                result = "File close Error";
            }

        } else if (requestCode == REQUEST_GALLERY && resultCode == RESULT_OK) {
            try {
                rbitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), data.getData());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        MyTfliteInterpreter();

        if (result_per >= LIMIT_PER && result_label != 9) {
            result_dialogView = (View) View.inflate(MainActivity.this, R.layout.result_iv_diaog, null);
            AlertDialog.Builder dlg = new AlertDialog.Builder(MainActivity.this);
            dlg.setView(result_dialogView);

            ImageView iv_result = (ImageView) result_dialogView.findViewById(R.id.iv_result);


            Bitmap result_bitmap = Bitmap.createScaledBitmap(rbitmap, 900, 1200, false);
            iv_result.setImageBitmap(result_bitmap);


            dlg.setPositiveButton("확인", new DialogInterface.OnClickListener(){
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if(dialog != null)
                        dialog.dismiss();

                    Intent intent = new Intent(getApplicationContext(), resultActivity.class);
                    intent.putExtra("result_label", result_label);
                    intent.putExtra("thema_possible",thema_possible);
                    startActivityForResult(intent, RESULTACTIVITY_CODE);
                }
            });

            dlg.setNegativeButton("취소",null);
            dlg.show();
        } else {
            Toast.makeText(getApplicationContext(), "사진이 정확하지 않습니다. 다시 시도해주세요."
                    , Toast.LENGTH_SHORT).show();
        }


    }

    private int exifOrientationToDegress(int exifOrientation) {
        if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_90) {
            return 90;
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_180) {
            return 180;
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_270) {
            return 270;
        }
        return 0;
    }

    private Bitmap rotate(Bitmap bitmap, float degree) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

    //카메라 권한 설정
    PermissionListener permissionListener = new PermissionListener() {
        @Override
        public void onPermissionGranted() {
            Toast.makeText(getApplicationContext(), "권한이 허용됨", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onPermissionDenied(ArrayList<String> deniedPermissions) {
            Toast.makeText(getApplicationContext(), "권한이 거부됨", Toast.LENGTH_SHORT).show();
        }
    };


    //*****************************************************************************
    private static final String MODEL_PATH = "model_unquant.tflite";
    private static final boolean QUANT = false;
    private static final String LABEL_PATH = "labels.txt";
    private static final int INPUT_SIZE = 224;
    private Classifier classifier;
    private Executor executor = Executors.newSingleThreadExecutor();


    private void MyTfliteInterpreter() {
        Bitmap result_bitmap = Bitmap.createScaledBitmap(rbitmap, INPUT_SIZE, INPUT_SIZE, false);


        TensorFlowImageClassifier tf = new TensorFlowImageClassifier();
        try {
            classifier = tf.create(getAssets(),
                    MODEL_PATH,
                    LABEL_PATH,
                    INPUT_SIZE,
                    QUANT);
        } catch (final Exception e) {
            throw new RuntimeException("Error", e);
        }
        final List<Classifier.Recognition> results = classifier.recognizeImage(result_bitmap);

        String result_str = results.toString();
        Log.d(TAG, "결과 " + result_str);

        result_label = Integer.parseInt(result_str.substring(2, 3));

        int findex = result_str.indexOf("(");
        int sindex = result_str.indexOf(")");

        result_per = Float.parseFloat(result_str.substring(findex + 1, sindex - 1));
    }


    //뷰페이져 프래그먼트 동기화
    @Override
    public void onBackPressed() {
        if (viewPager.getCurrentItem() == 0) {
            super.onBackPressed();
        } else {
            viewPager.setCurrentItem(viewPager.getCurrentItem() - 1);
        }
    }


    private class ScreenSlidePagerAdapter extends FragmentStateAdapter {
        public ScreenSlidePagerAdapter(FragmentActivity fa) {
            super(fa);
        }

        @Override
        public Fragment createFragment(int position) {
            switch (position) {
                case 0:
                    return new FragmentGallery1();
                case 1:
                    return new FragmentGallery2();
            }
            return new FragmentGallery1();
        }

        @Override
        public int getItemCount() {
            return NUM_PAGES;
        }
    }
}