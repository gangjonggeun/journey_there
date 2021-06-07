package com.example.journey_there;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

//import org.tensorflow.lite.Interpreter;

public class resultActivity extends AppCompatActivity {
    private static final int RESULTACTIVITY_CODE = 300;
    private ImageButton home_btn;
    private ImageView result_iv[];
    private TextView result_name[], result_txt[];
    private ImageButton right_btn, left_btn;
    private ViewFlipper flipper;

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.result_layout);

        result_iv = new ImageView[3];
        result_name = new TextView[3];
        result_txt = new TextView[3];

        result_iv[0] = (ImageView) findViewById(R.id.result_iv1);
        result_name[0] = (TextView) findViewById(R.id.result_name1);
        result_txt[0] = (TextView) findViewById(R.id.result_text1);

        result_iv[1] = (ImageView) findViewById(R.id.result_iv2);
        result_name[1] = (TextView) findViewById(R.id.result_name2);
        result_txt[1] = (TextView) findViewById(R.id.result_text2);

        result_iv[2] = (ImageView) findViewById(R.id.result_iv3);
        result_name[2] = (TextView) findViewById(R.id.result_name3);
        result_txt[2] = (TextView) findViewById(R.id.result_text3);

        home_btn = (ImageButton) findViewById(R.id.homeBtn);
        right_btn = (ImageButton) findViewById(R.id.reuslt_rightBtn);
        left_btn = (ImageButton) findViewById(R.id.reuslt_leftBtn);
        flipper = (ViewFlipper) findViewById(R.id.flipper);

        final Intent intent = getIntent();
        final int result_label = intent.getIntExtra("result_label", 0);
        boolean thema_possible = intent.getBooleanExtra("thema_possible", false);

        User user = null;
        if (thema_possible == false) {
            user = new User();
        } else {
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
        }

        user.favorite_up(result_label);
        thema_possible = true;

        FileOutputStream out = null;
        ObjectOutputStream oout = null;

        try {
            out = openFileOutput("user.txt", Context.MODE_PRIVATE);
            oout = new ObjectOutputStream(out);

            oout.writeObject(user);
            oout.close();
            out.close();
        } catch (IOException e) {
            Toast.makeText(getApplicationContext(), " 오류1", Toast.LENGTH_SHORT).show();
        }

        Intent outIntent = new Intent(getApplicationContext(), MainActivity.class);
        outIntent.putExtra("thema_possible", thema_possible);
        setResult(RESULT_OK, outIntent);
         /*
         0 coast
        1 metrocity
        2 bridge
        3 cliff
        4 harbor
        5 lighthouse
        6 mountain
        7 mudflat
        8 waterfall
        */
        Integer[][] result_image = {
                {R.drawable.result_coast_gangreung, R.drawable.result_coast_huapjae, R.drawable.result_coast_mongdol},
                {R.drawable.result_metrocity_busan, R.drawable.result_metrocity_lotte, R.drawable.result_metrocity_songdo},
                {R.drawable.result_bridge_gwangan, R.drawable.result_bridge_incheon, R.drawable.result_bridge_seohae},
                {R.drawable.result_costalcliff_baksugijeong, R.drawable.result_costalcliff_jusangjeoli, R.drawable.result_costalcliff_songaksan},
                {R.drawable.result_harbor_bokhang, R.drawable.result_harbor_haewoondae, R.drawable.result_harbor_incheon},
                {R.drawable.result_lighthouse_acungdo, R.drawable.result_lighthouse_oruckdo, R.drawable.result_lighthouse_ull},
                {R.drawable.result_mountain_girisan, R.drawable.result_mountain_hanrasan, R.drawable.result_mountain_sulaksan},
                {R.drawable.result_mudflat_gangwha, R.drawable.result_mudflat_jinsanli, R.drawable.result_mudflat_uaesu},
                {R.drawable.result_waterfall_cungiyeon, R.drawable.result_waterfall_gapung, R.drawable.result_waterfall_sulaksan}
        };

        final String[][] result_str = {
                {"강릉 경포 해수욕장", "제주도 협제 해수욕장", "거제 몽돌 해수욕장"}, //0
                {"부산 마린 시티", "서울 롯데 타워", "송도 동북 아트 센터"}, //1
                {"부산 광안 대교", "인천 대교", "충남 서해 대교"}, //2
                {"제주 박수기정", "제주 주상절리대", "제주 송악산둘레길"}, //3
                {"목포 북항", "해운대 요트 경기장", "인천 크루즈 터미널"}, //4
                {"전북 어청도 등대", "부산 오륙도 등대", "율산 울기 등대"}, //5
                {"경남 지리산 천왕봉", "제주 한라산", "강원 설악산 국립공원"}, //6
                {"강화 동막 갯벌", "충남 진산리 갯벌", "전남 여수 노을마을"}, //7
                {"제주 천지연 폭포", "경기 가평 용소폭포", "강원 설악 대승폭포"}, //8
        };

        String[][] result_text = {
                {"#숨이 트이는 #인프라 #힐링", "#한림 #일몰 #액티비티", "#거제 #제트스키 #흑진주"},
                {"#LCT #달맞이고개 #커플", "#야경 #석촌호수 #서울SKY", "#호수 #초고층 #산책로"},
                {"#감성 #야경 #불꽃축제", "#노을 #5대대교 #인천공항", "#함상공원 "},
                {"#절벽 #포토 #해변", "#친구 #맑음 #둘레길", "#커플 #가을 #등산"},
                {"#해상공원 #감성 #맛집", "#수영 #더베이101 #요트", "#크루즈 #상하이 #선박"},
                {"#푸른파다 #섬 #드라마", "#Yolo #무인등대 #갈매기", "#노을 #공원 #소나무"},
                {"#천왕봉 #산스타그램 #반달곰", "#사계절 #산걷기 #정기", "오세암 #등산 #설악"},
                {"#영화제 #장어 #세계5대", "#조개잡이 #아이들과", "#노을 #체험 #가족"},
                {"#맑음 #여름 #별", "#단풍 #다이빙 #깊은물", "#효도관광 #시원한 #여울"}
        };
        for (int i = 0; i < 3; i++) {
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), result_image[result_label][i]);
            Bitmap result_bitmap = Bitmap.createScaledBitmap(bitmap, 900, 850, false);
            result_iv[i].setImageBitmap(result_bitmap);
            // result_iv[i].setImageResource( result_image[result_label][i]);
            result_name[i].setText(result_str[result_label][i]);
            result_txt[i].setText(result_text[result_label][i]);
        }

        left_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                flipper.showPrevious();
            }
        });
        right_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                flipper.showNext();
            }
        });
        home_btn.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View view) {
                finish();
            }
        });


        final Intent intenta = new Intent(Intent.ACTION_VIEW);

        result_iv[0].setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                intenta.setData(Uri.parse("https://www.google.com/search?q=" + result_str[result_label][0]));
                startActivity(intenta);
                return false;
            }
        });
        result_iv[1].setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                intenta.setData(Uri.parse("https://www.google.com/search?q=" + result_str[result_label][1]));
                startActivity(intenta);
                return false;
            }
        });
        result_iv[2].setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                intenta.setData(Uri.parse("https://www.google.com/search?q=" + result_str[result_label][2]));
                startActivity(intenta);
                return false;
            }
        });
    }
}
