package com.example.journey_there;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class galleryActivity extends AppCompatActivity {
    private ImageView gallery_iv[];
    private TextView gallery_nametxt[], gallery_ex[];
    private int pragment;
    private ImageButton homeBtn;

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gallery_layout);

        Intent intent = getIntent();
        pragment = intent.getIntExtra("gallery", 0);

        homeBtn = (ImageButton) findViewById(R.id.ghomeBtn);

        gallery_iv = new ImageView[4];

        gallery_ex = new TextView[4];
        gallery_nametxt = new TextView[4];

        gallery_iv[0] = (ImageView) findViewById(R.id.gallery_iv1);
        gallery_iv[1] = (ImageView) findViewById(R.id.gallery_iv2);
        gallery_iv[2] = (ImageView) findViewById(R.id.gallery_iv3);
        gallery_iv[3] = (ImageView) findViewById(R.id.gps_iv);

        gallery_nametxt[0] = (TextView) findViewById(R.id.nameTxt);
        gallery_nametxt[1] = (TextView) findViewById(R.id.exTxt1_1);
        gallery_nametxt[2] = (TextView) findViewById(R.id.exTxt2_1);
        gallery_nametxt[3] = (TextView) findViewById(R.id.exTxt3_1);

        gallery_ex[0] = (TextView) findViewById(R.id.exTxt);
        gallery_ex[1] = (TextView) findViewById(R.id.exTxt1_2);
        gallery_ex[2] = (TextView) findViewById(R.id.exTxt2_2);
        gallery_ex[3] = (TextView) findViewById(R.id.exTxt3_2);


        Integer[][] gallery_image = {
                {R.drawable.gallery_sokcho_1, R.drawable.gallery_sokcho_2, R.drawable.gallery_sokcho_3, R.drawable.gallery_sokcho_gps},
                {R.drawable.gallery_boreong_1, R.drawable.gallery_boreong_2, R.drawable.gallery_boreong_3, R.drawable.gallery_boreong_gps}
        };


        String[][] gallery_name = {
                {"속초 해수욕장", "해수욕장과 포토존", "아바이마을", "엑스포 광장"},
                {"보령 군헌 갯벌", "군헌 갯벌", "갯벌 체험장", "짚 트랙"}
        };

        String[][] gallery_text = {
                {
                        "#강원도 속초시 조양동 #해변스타그램  #스릴",
                        "경사가 완만하고 수심이 얕어 여름의 인기코스 포토존과 가족단위 여행자들 급증!",
                        "전쟁때 피난자들이 정착한 마을. 통일의 상징적 의미와 특별한 먹거리들과 정겨운 분위기",
                        "속초에서 가장 큰 공원이자 광장! 전동 오토바이를 빌려 자유롭게 누벼보자."
                },
                {
                        "#충남 보령 #갯벌 체험 # 머드 축제",
                        "갯벌에서 머드로 놀아보자 발이 푹푹빠지고 옷이 더러워져도 OK! 오늘은 머드축제니까!",
                        "바지락 채취를 체험할 수 있는 곳. 조개도 캐고 추억도 쌓아보자",
                        "짚트랙으로 하늘을 날아보자! 비다위를 활강하며 대천의 전경을 한 눈에 보자!"

                }
        };
        Bitmap result_bitmap;
        int set_count = 4;

        for (int i = 0; i < set_count; i++) {
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), gallery_image[pragment][i]);
            if (i < set_count - 1)
                result_bitmap = Bitmap.createScaledBitmap(bitmap, 900, 1000, false);
            else
                result_bitmap = Bitmap.createScaledBitmap(bitmap, 1500, 1000, false);
            gallery_iv[i].setImageBitmap(result_bitmap);

            gallery_nametxt[i].setText(gallery_name[pragment][i]);
            gallery_ex[i].setText(gallery_text[pragment][i]);
        }


        homeBtn.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });
    }
}
