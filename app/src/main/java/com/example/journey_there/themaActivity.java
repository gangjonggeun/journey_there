package com.example.journey_there;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;

public class themaActivity extends AppCompatActivity {
    private Gallery gallery1, gallery2;
    private Integer thema_results[] = {
            R.drawable.thema_coast,
            R.drawable.thema_metrocity,
            R.drawable.thema_bridge,
            R.drawable.thema_cliff,
            R.drawable.thema_harbor,
            R.drawable.thema_lighthouse,
            R.drawable.thema_mountain,
            R.drawable.thema_mudflat,
            R.drawable.thema_waterfall
    };
    private String g1_txt[] = {
            "충남 대천해수욕장",
            "서울 국제금융센터",
            "전남 천사대교",
            "전북 채성강",
            "인천 왕산마리나",
            "제주 우도등대",
            "서울 북한산 국립공원",
            "안산 대부도 갯벌",
            "경기도 재인폭포"
    };
    private static Integer[] ranked_image;
    private static String[] g1_exam;
    private ImageButton homeBtn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.thema_layout);

        gallery1 = (Gallery) findViewById(R.id.gallery1);
        gallery2 = (Gallery) findViewById(R.id.gallery2);


        homeBtn = (ImageButton) findViewById(R.id.thema_homeBtn);

        homeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        User user = null;
        try {
            FileInputStream inf = null;
            inf = openFileInput("user.txt");
            ObjectInputStream o = new ObjectInputStream(inf);
            user = (User) o.readObject();

            o.close();
            inf.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        int[] user_favorite = user.getFavorite();
        int[] favorite_rank = new int[9];
        ranked_image = new Integer[4];
        g1_exam = new String[4];

        favorite_rank = getRanked(user_favorite, favorite_rank);

        for (int i = 0; i < favorite_rank.length; i++)
            Log.e("ranked : ", favorite_rank[i] + " ");

        for (int i = 0; i < favorite_rank.length; i++) {
            if (favorite_rank[i] == 1) {
                ranked_image[0] = thema_results[i];
                g1_exam[0] = g1_txt[i];
            } else if (favorite_rank[i] == 2) {
                ranked_image[1] = thema_results[i];
                g1_exam[1] = g1_txt[i];
            } else if (favorite_rank[i] == 3) {
                ranked_image[2] = thema_results[i];
                g1_exam[2] = g1_txt[i];
            } else if (favorite_rank[i] == 4) {
                ranked_image[3] = thema_results[i];
                g1_exam[3] = g1_txt[i];
            }
        }

        MyGalleryAdapter galAdapter = new MyGalleryAdapter(this);
        MyGalleryAdapter2 galAdapter2 = new MyGalleryAdapter2(this);

        gallery1.setAdapter(galAdapter);
        gallery2.setAdapter(galAdapter2);
    }

    public int[] getRanked(int[] arr, int[] ranked) {
        for (int i = 0; i < arr.length; i++) {
            ranked[i] = 0;

            for (int j = 0; j < arr.length; j++) {
                if (arr[i] < arr[j]) {
                    ranked[i]++;
                } else if (arr[i] == arr[j]) {
                    ranked[j] = ranked[j] + 1;
                }
            }
        }

        return ranked;
    }

    public class MyGalleryAdapter<g1> extends BaseAdapter {
        private Context context;

        Integer[] g1 = ranked_image;
        String[] g1_ex = g1_exam;

        public MyGalleryAdapter(Context c) {
            context = c;
        }


        @Override
        public int getCount() {
            return g1.length;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ImageView imageView = new ImageView(context);
            imageView.setLayoutParams(new Gallery.LayoutParams(500, 700));
            imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
            imageView.setPadding(5, 5, 5, 5);

            imageView.setImageResource(g1[position]);

            final int pos = position;

            imageView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {

                        View dialogView = (View) View.inflate(themaActivity.this, R.layout.result_iv_diaog, null);
                        AlertDialog.Builder dlg = new AlertDialog.Builder(themaActivity.this);
                        dlg.setView(dialogView);


                        TextView ex = (TextView) dialogView.findViewById(R.id.result_txt);
                        ImageView iv_result = (ImageView) dialogView.findViewById(R.id.iv_result);


                        ex.setText(g1_ex[pos]);
                        ex.setGravity(Gravity.CENTER);

                        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), g1[pos]);
                        Bitmap result_bitmap = Bitmap.createScaledBitmap(bitmap, 900, 1200, false);
                        iv_result.setImageBitmap(result_bitmap);


                        dlg.setPositiveButton("확인", null);

                        dlg.show();

                    return false;
                }

            });


            return imageView;
        }
    }

    public class MyGalleryAdapter2 extends BaseAdapter {
        private Context context;

        Integer[] g2 = {R.drawable.thema_summer_hampyung, R.drawable.thema_summer_mangsang, R.drawable.thema_summer_songjiho, R.drawable.thema_summer_songjong};
        String[] g2_ex = {"전남 함평 함평해수욕장", "강원 동해시 망상해수욕장", "강원 고성 송지호 해수욕장", "부산 해운대 송정 해수욕장"};

        public MyGalleryAdapter2(Context c) {
            context = c;
        }

        @Override
        public int getCount() {
            return g2.length;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ImageView imageView = new ImageView(context);
            imageView.setLayoutParams(new Gallery.LayoutParams(500, 700));
            imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
            imageView.setPadding(5, 5, 5, 5);

            imageView.setImageResource(g2[position]);

            final int pos = position;

            imageView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {

                    View dialogView = (View) View.inflate(themaActivity.this, R.layout.result_iv_diaog, null);
                    AlertDialog.Builder dlg = new AlertDialog.Builder(themaActivity.this);
                    dlg.setView(dialogView);


                    TextView ex = (TextView) dialogView.findViewById(R.id.result_txt);
                    ImageView iv_result = (ImageView) dialogView.findViewById(R.id.iv_result);


                    ex.setText(g2_ex[pos]);
                    ex.setGravity(Gravity.CENTER);

                    Bitmap bitmap = BitmapFactory.decodeResource(getResources(), g2[pos]);
                    Bitmap result_bitmap = Bitmap.createScaledBitmap(bitmap, 900, 1200, false);
                    iv_result.setImageBitmap(result_bitmap);


                    dlg.setPositiveButton("확인", null);

                    dlg.show();

                    return false;
                }

            });

            return imageView;
        }

    }
}
