package com.example.demochat;

import android.animation.ValueAnimator;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.AnimationDrawable;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewManager;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import br.com.simplepass.loading_button_lib.customViews.CircularProgressButton;
import br.com.simplepass.loading_button_lib.customViews.CircularProgressImageButton;
import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.PATCH;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,
        ManagerDialogAdd.ManagerDialogListener,
ManagerDialogEdit.ManagerDialogEditListener {


    private EditText egksNumber;
    private TextView textViewResult,dateView;
    private Retrofit retrofit;
    private DrawerLayout drawer;
    private Api api;
    private Spinner spinner;
    private long lastDate;
    private SimpleDateFormat parser;
    Toolbar toolbar;


    @Override
    public void editTexts(String name, String number, int id, String command) {

        Paper.init(this);
        Map<Integer,EgksCard> cardList = Paper.book().read("cardList");
        switch (command){

            case "edit":

                EgksCard toChangeCard = new EgksCard(name, number, cardList.get(id).getBalance(),cardList.get(id).getLastUpdate(), id);
                cardList.put(id,toChangeCard);
                break;

            case "delete":
                cardList.remove(id);

                break;


        }
        Paper.book().write("cardList", cardList);

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ManagerFragment()).commit();
    }

    @Override
    public void applyTexts(String name, String number) {


        Paper.init(this);
        if(Paper.exist("cardList")) {
            Map<Integer,EgksCard> cardList = Paper.book().read("cardList");
            int counter = Paper.book().read("counter");
            counter++;
            cardList.put(counter, new EgksCard(name,number,counter));
            Paper.book().write("cardList", cardList);
            Paper.book().write("counter",counter);
        } else {
            Map<Integer,EgksCard> cardList = new HashMap<Integer, EgksCard>();
            Paper.book().write("counter",0);
            cardList.put(0,new EgksCard(name,number, 0));
            Paper.book().write("cardList", cardList);
        }
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ManagerFragment()).commit();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);

//        parser = new SimpleDateFormat("MM:dd");


       setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        drawer = findViewById(R.id.drawer_layout);

        NavigationView navigationView = findViewById(R.id.nav_view);

        navigationView.setNavigationItemSelectedListener(this);

//        Paper.init(this);
//        Map<Integer,EgksCard> cardList1 = Paper.book().read("cardList");
//        EgksCard card = cardList1.get(2);
//        card.setBalance("670r");
//        cardList1.put(card.getId(),card);
//        Paper.book().write("cardList",cardList1);




        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        toggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.colorPrimary));
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        AnimationDrawable animationDrawable = (AnimationDrawable) drawer.getBackground();
        animationDrawable.setEnterFadeDuration(2000);
        animationDrawable.setExitFadeDuration(4000);
        animationDrawable.start();
        spinner = findViewById(R.id.spinner);


        Paper.init(this);
        if(Paper.book().exist("cardList")){

            Map<Integer,EgksCard> cardList = Paper.book().read("cardList");



            if(cardList.size() == 0){

                ((ViewManager)spinner.getParent()).removeView(spinner);
                toolbar.setTitle("Добавьте карту");


            } else {

                ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this, R.layout.spinner_item, getNamesHolder(cardList));
                spinner.getBackground().setColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_ATOP);
                spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_item);
                spinner.setAdapter(spinnerArrayAdapter);
                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        if(aniButton!=null) aniButton.revertAnimation();
                        TextView tv = findViewById(R.id.textView);
                        final TextView balance = findViewById(R.id.textView5);
                        TextView upd = findViewById(R.id.textView9);
                        EgksCard usersCard = getCardByName(parent.getItemAtPosition(position).toString());
                        tv.setText(usersCard.getNumber());
                        balance.setText(usersCard.getBalance());

                        Animation anim1 = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.slide_up1);
                        balance.startAnimation(anim1);
                        upd.startAnimation(anim1);
                        tv.startAnimation(anim1);

                        bus = findViewById(R.id.bus);
                        bidlo = findViewById(R.id.bidlo);
                        troll = findViewById(R.id.troll);
                        ship = findViewById(R.id.ship);

                        bus.startAnimation(anim1);
                        bidlo.startAnimation(anim1);
                        troll.startAnimation(anim1);
                        ship.startAnimation(anim1);

                        ValueAnimator animator = new ValueAnimator();
                        int temp = 0;
                        if(usersCard.getBalance().length() == 2){
                            temp = Integer.parseInt(usersCard.getBalance().trim().substring(0, 1));
                        } else {
                            temp = Integer.parseInt(usersCard.getBalance().trim().substring(0, usersCard.getBalance().length() - 1));
                        }
                        animator.setObjectValues(0, temp);
                        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                            @Override
                            public void onAnimationUpdate(ValueAnimator animation) {
                                balance.setText(String.format("%d\u20BD",animation.getAnimatedValue()));
                            }
                        });
                        animator.setDuration(1000);
                        animator.start();

                        ValueAnimator[] count = new ValueAnimator[3];
                        count[0] = new ValueAnimator();
                        count[1] = new ValueAnimator();
                        count[2] = new ValueAnimator();
                        count[0].setObjectValues(0,temp/17);
                        count[1].setObjectValues(0,temp/16);
                        count[2].setObjectValues(0,temp/22);

                        count[0].addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                            @Override
                            public void onAnimationUpdate(ValueAnimator animation) {
                                bus.setText(String.format("%d",animation.getAnimatedValue()));
                                bidlo.setText(String.format("%d",animation.getAnimatedValue()));
                            }
                        });

                        count[1].addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                            @Override
                            public void onAnimationUpdate(ValueAnimator animation) {
                                troll.setText(String.format("%d",animation.getAnimatedValue()));

                            }
                        });

                        count[2].addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                            @Override
                            public void onAnimationUpdate(ValueAnimator animation) {
                                ship.setText(String.format("%d",animation.getAnimatedValue()));
                            }
                        });
                        count[0].setDuration(1000);
                        count[1].setDuration(1000);
                        count[2].setDuration(1000);

                        count[0].start();
                        count[1].start();
                        count[2].start();

                        if (usersCard.getLastUpdate() != null) {
                            upd.setText(usersCard.getLastUpdate());
                        } else {
                            upd.setText("Обновитесь!");
                        }



                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
            }
        }
        else{
            ((ViewManager)spinner.getParent()).removeView(spinner);
            toolbar.setTitle("Добавьте карту");
        }

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new MainFragment()).commit();

        navigationView.setCheckedItem(R.id.nav_main);

        retrofit = new Retrofit.Builder()
                .baseUrl("https://shop.payberry.ru/Service/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        api = retrofit.create(Api.class);








    }


    private ArrayList<String> getNamesHolder(Map<Integer,EgksCard> cardLs){
        ArrayList<String> result = new ArrayList<String>();
        Iterator iterator = cardLs.entrySet().iterator();
        while (iterator.hasNext()){
            Map.Entry pair = (Map.Entry) iterator.next();
            EgksCard temp = (EgksCard)pair.getValue();
            result.add(temp.getName());
        }
        return result;
    }
    private EgksCard getCardByName(String name){
        Paper.init(this);
        HashMap<Integer,EgksCard> cardList = Paper.book().read("cardList");
        Iterator iterator = cardList.entrySet().iterator();
        while(iterator.hasNext()){
            Map.Entry pair = (Map.Entry)iterator.next();

            EgksCard tempCard = (EgksCard)pair.getValue();
            if(tempCard.getName().equals(name)) return tempCard;
        }
        return null;
    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        ViewManager spinnerView = (ViewManager) spinner.getParent();
        switch (menuItem.getItemId()){
            case R.id.nav_manager:
                toolbar.setTitle("Менеджер карт");
                spinnerView = (ViewManager) spinner.getParent();
                try {
                    spinnerView.removeView(spinner);
                }
                catch (Exception e){
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ManagerFragment()).commit();
                break;
            case R.id.nav_payment:
                toolbar.setTitle("Онлайн оплата");
                spinnerView = (ViewManager) spinner.getParent();
                try {
                    spinnerView.removeView(spinner);
                }
                catch (Exception e){
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new PaymentFragment()).commit();
                break;
            case R.id.nav_main:

                try {


                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new MainFragment()).commit();
                    toolbar.setTitle("");
                    Paper.init(this);
                    if (Paper.book().exist("cardList")) {
                        HashMap<Integer,EgksCard> cardList = Paper.book().read("cardList");

                        if(cardList.size() == 0){
                            //((ViewManager)spinner.getParent()).removeView(spinner);
                            toolbar.setTitle("Добавьте карту");
                        } else {


                            ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this, R.layout.spinner_item, getNamesHolder(cardList));
                            spinner.setAdapter(spinnerArrayAdapter);
                            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                    if(aniButton!=null) aniButton.revertAnimation();
                                    TextView tv = findViewById(R.id.textView);
                                    final TextView balance = findViewById(R.id.textView5);
                                    TextView lastUpd = findViewById(R.id.textView9);
                                    Animation anim1 = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.slide_up1);

                                    EgksCard usersCard = getCardByName(parent.getItemAtPosition(position).toString());
                                    tv.setText(usersCard.getNumber());
                                    balance.setText(usersCard.getBalance());
                                    balance.startAnimation(anim1);
                                    lastUpd.startAnimation(anim1);
                                    tv.startAnimation(anim1);

                                    bus = findViewById(R.id.bus);
                                    bidlo = findViewById(R.id.bidlo);
                                    troll = findViewById(R.id.troll);
                                    ship = findViewById(R.id.ship);

                                    bus.startAnimation(anim1);
                                    bidlo.startAnimation(anim1);
                                    troll.startAnimation(anim1);
                                    ship.startAnimation(anim1);

                                    ValueAnimator animator = new ValueAnimator();
                                    int temp = 0;
                                    if(usersCard.getBalance().length() == 2){
                                        temp = Integer.parseInt(usersCard.getBalance().trim().substring(0, 1));
                                    } else {
                                        temp = Integer.parseInt(usersCard.getBalance().trim().substring(0, usersCard.getBalance().length() - 1));
                                    }
                                    animator.setObjectValues(0, temp);
                                    animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                                        @Override
                                        public void onAnimationUpdate(ValueAnimator animation) {
                                            balance.setText(String.format("%d\u20BD",animation.getAnimatedValue()));
                                        }
                                    });
                                    animator.setDuration(1000);
                                    animator.start();

                                    ValueAnimator[] count = new ValueAnimator[3];
                                    count[0] = new ValueAnimator();
                                    count[1] = new ValueAnimator();
                                    count[2] = new ValueAnimator();
                                    count[0].setObjectValues(0,temp/17);
                                    count[1].setObjectValues(0,temp/16);
                                    count[2].setObjectValues(0,temp/22);

                                    count[0].addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                                        @Override
                                        public void onAnimationUpdate(ValueAnimator animation) {
                                            bus.setText(String.format("%d",animation.getAnimatedValue()));
                                            bidlo.setText(String.format("%d",animation.getAnimatedValue()));
                                        }
                                    });

                                    count[1].addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                                        @Override
                                        public void onAnimationUpdate(ValueAnimator animation) {
                                            troll.setText(String.format("%d",animation.getAnimatedValue()));

                                        }
                                    });

                                    count[2].addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                                        @Override
                                        public void onAnimationUpdate(ValueAnimator animation) {
                                            ship.setText(String.format("%d",animation.getAnimatedValue()));
                                        }
                                    });
                                    count[0].setDuration(1000);
                                    count[1].setDuration(1000);
                                    count[2].setDuration(1000);

                                    count[0].start();
                                    count[1].start();
                                    count[2].start();




                                    if (usersCard.getLastUpdate() != null) {
                                        lastUpd.setText(usersCard.getLastUpdate());
                                    } else {
                                        lastUpd.setText("Обновитесь!");
                                    }


                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> parent) {

                                }
                            });
                            toolbar.addView(spinner);


                        }





                    } else {
                        toolbar.setTitle("Добавьте карту");
                    }

                } catch (Exception e){}
                break;


        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed(){
        if(drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START);
        } else {
        super.onBackPressed();
        }
    }


    public String getTimeFormat(long lastDate){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("Europe/Moscow"));

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(lastDate);
        String viewDate = simpleDateFormat.format(calendar.getTime());
        return viewDate;
    }

    CircularProgressImageButton aniButton;
    TextView tv,tv9,troll,bus,ship,bidlo;

    public void loadMainAcitivity(){
        aniButton = (CircularProgressImageButton) findViewById(R.id.aniBtn);

        AsyncTask<String,String,String> asyncTask = new AsyncTask<String, String, String>() {
            @Override
            protected String doInBackground(String... voids) {
                try {
                    Thread.sleep(5000);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return "done";
            }

            @Override
            protected void onPostExecute(String s){
                aniButton.doneLoadingAnimation(Color.parseColor("#56BF7B"), BitmapFactory.decodeResource(getResources(), R.drawable.ic_done_white_48dp));
                Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_up);
                TextView balance = findViewById(R.id.textView5);
                balance.startAnimation(animation);
                Animation anim = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.slide_up);
                Animation anim1 = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.slide_up1);
                tv = findViewById(R.id.textView5);
                tv9 = findViewById(R.id.textView9);
                bus = findViewById(R.id.bus);
                bidlo = findViewById(R.id.bidlo);
                troll = findViewById(R.id.troll);
                ship = findViewById(R.id.ship);

//                bus.setText(String.format("%d",(500/17)));
//                bidlo.setText(String.format("%d",(500/17)));
//                troll.setText(String.format("%d",(500/16)));
//                ship.setText(String.format("%d",(500/22)));
                tv9.setText("10.05.2019 19:29");
                //tv.startAnimation(anim);
                tv.startAnimation(anim1);
                tv9.startAnimation(anim1);
                bus.startAnimation(anim1);
                bidlo.startAnimation(anim1);
                troll.startAnimation(anim1);
                ship.startAnimation(anim1);
                ValueAnimator animator = new ValueAnimator();

                animator.setObjectValues(0, 500);
                animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        tv.setText(String.format("%d\u20BD",animation.getAnimatedValue()));
                    }
                });
                animator.setDuration(1000);
                animator.start();

                ValueAnimator[] count = new ValueAnimator[3];
                count[0] = new ValueAnimator();
                count[1] = new ValueAnimator();
                count[2] = new ValueAnimator();
                count[0].setObjectValues(0,500/17);
                count[1].setObjectValues(0,500/16);
                count[2].setObjectValues(0,500/22);

                count[0].addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        bus.setText(String.format("%d",animation.getAnimatedValue()));
                        bidlo.setText(String.format("%d",animation.getAnimatedValue()));
                    }
                });

                count[1].addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        troll.setText(String.format("%d",animation.getAnimatedValue()));

                    }
                });

                count[2].addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        ship.setText(String.format("%d",animation.getAnimatedValue()));
                    }
                });
                count[0].setDuration(1000);
                count[1].setDuration(1000);
                count[2].setDuration(1000);

                count[0].start();
                count[1].start();
                count[2].start();
            }
        };
        aniButton.startAnimation();

        asyncTask.execute();
    }

    public void loadCard(View view){
        String temp = spinner.getSelectedItem().toString();
        EgksCard card = getCardByName(temp);




        aniButton = (CircularProgressImageButton) findViewById(R.id.aniBtn);

        aniButton.startAnimation();

        createPost(card);



//        String temp = egksNumber.getText().toString();
//        Pattern pt = Pattern.compile("([0-9]{9})");
//        Matcher mt = pt.matcher(temp);
//
//        if(mt.find()){
//
//
//                Paper.init(this);
//                Paper.book().write("cardNumber",temp);
//                createPost(temp);
//
//
//        } else {
//            Toast.makeText(MainActivity.this,"Номер карты состоит из 9 цифр!",Toast.LENGTH_SHORT).show();
//        }


    }

    public void addCard(View view){

        ManagerDialogAdd managerDialogAdd = new ManagerDialogAdd();
        managerDialogAdd.show(getSupportFragmentManager(),"Adding dialog");


    }






    private void createPost(final EgksCard card){
        //textViewResult.setText("Loading...");


            Call<PostResponse> call = api.createPost("РџСЂРѕРґРѕР»Р¶РёС‚СЊ",5,1,4253,card.getNumber() ,"b_TC5wFhRn2nDwExumPPFRlJaLmAzlDJZP6qwoSCGHsHxZEQXl7jqT-YHWDxQtfAj55HF_v5w3Ow5G5I6UiT0HH5_4n2knPM54_Y6JvYhyk1");

            call.enqueue(new Callback<PostResponse>() {
                @Override
                public void onResponse(Call<PostResponse> call, Response<PostResponse> response) {
                    if(!response.isSuccessful()){
                        Toast.makeText(MainActivity.this,"Количество попыток закончилось. Пожалуйста, попробуйте позже.",Toast.LENGTH_SHORT).show();
                        aniButton.doneLoadingAnimation(Color.parseColor("#ff5959"), BitmapFactory.decodeResource(getResources(), R.mipmap.fail));


                        return;
                    }
                    aniButton.doneLoadingAnimation(Color.parseColor("#56BF7B"), BitmapFactory.decodeResource(getResources(), R.drawable.ic_done_white_48dp));

                    PostResponse postResponse = response.body();
                    String content = "";
                    content += postResponse.getMessage();
                    Pattern pt = Pattern.compile("(Остаток [0-9]{1,}.[0-9]{1,})");
                    Matcher mt = pt.matcher(content);
                    if (mt.find()) {
                        String res = mt.group(1).substring(7);  //" \u20BD"
                        int tempL = res.length();
                        String lastRes = res.substring( 0, tempL-3);
                        Paper.init(getApplicationContext());
//                    Date date = new Date();
//                    long ltime = date.getTime();


                        card.setBalance(lastRes);
                        Date date = new Date();
                        long ltime = date.getTime();
                        card.setLastUpdate(getTimeFormat(ltime));
                        Map<Integer,EgksCard> cardList = Paper.book().read("cardList");
                        cardList.put(card.getId(),card);
                        Paper.book().write("cardList",cardList);


                        final TextView balance = findViewById(R.id.textView5);
                        TextView lastUpd = findViewById(R.id.textView9);
                        Animation anim1 = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.slide_up1);

                        balance.setText(card.getBalance());
                        lastUpd.setText(card.getLastUpdate());
                        balance.startAnimation(anim1);
                        lastUpd.startAnimation(anim1);


                        bus = findViewById(R.id.bus);
                        bidlo = findViewById(R.id.bidlo);
                        troll = findViewById(R.id.troll);
                        ship = findViewById(R.id.ship);

                        bus.startAnimation(anim1);
                        bidlo.startAnimation(anim1);
                        troll.startAnimation(anim1);
                        ship.startAnimation(anim1);

                        ValueAnimator animator = new ValueAnimator();
                        int temp = 0;
                        if(card.getBalance().length() == 2){
                            temp = Integer.parseInt(card.getBalance().trim().substring(0, 1));
                        } else {
                            temp = Integer.parseInt(card.getBalance().trim().substring(0, card.getBalance().length() - 1));
                        }
                        animator.setObjectValues(0, temp);
                        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                            @Override
                            public void onAnimationUpdate(ValueAnimator animation) {
                                balance.setText(String.format("%d\u20BD",animation.getAnimatedValue()));
                            }
                        });
                        animator.setDuration(1000);
                        animator.start();

                        ValueAnimator[] count = new ValueAnimator[3];
                        count[0] = new ValueAnimator();
                        count[1] = new ValueAnimator();
                        count[2] = new ValueAnimator();
                        count[0].setObjectValues(0,temp/17);
                        count[1].setObjectValues(0,temp/16);
                        count[2].setObjectValues(0,temp/22);

                        count[0].addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                            @Override
                            public void onAnimationUpdate(ValueAnimator animation) {
                                bus.setText(String.format("%d",animation.getAnimatedValue()));
                                bidlo.setText(String.format("%d",animation.getAnimatedValue()));
                            }
                        });

                        count[1].addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                            @Override
                            public void onAnimationUpdate(ValueAnimator animation) {
                                troll.setText(String.format("%d",animation.getAnimatedValue()));

                            }
                        });

                        count[2].addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                            @Override
                            public void onAnimationUpdate(ValueAnimator animation) {
                                ship.setText(String.format("%d",animation.getAnimatedValue()));
                            }
                        });
                        count[0].setDuration(1000);
                        count[1].setDuration(1000);
                        count[2].setDuration(1000);

                        count[0].start();
                        count[1].start();
                        count[2].start();



                        //String viewD = getTimeFormat(ltime);
                        //dateView.setText(String.format("Последнее обновление %s", viewD));
//
//                    Intent intent = new Intent(getApplicationContext(), Balance.class);
//                    intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
//                    int[] ids = AppWidgetManager.getInstance(getApplication())
//                            .getAppWidgetIds(new ComponentName(getApplication(), Balance.class));
//                    intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids);
//                    sendBroadcast(intent);

                    } else {
                        //textViewResult.setText("404 not found");
                        Toast.makeText(MainActivity.this,"Номер карты не найден.",Toast.LENGTH_SHORT).show();
                        aniButton.doneLoadingAnimation(Color.parseColor("#ff5959"), BitmapFactory.decodeResource(getResources(), R.mipmap.fail));

                    }
                }
                @Override
                public void onFailure(Call<PostResponse> call, Throwable t) {
                    //textViewResult.setText("Error.");
                    Toast.makeText(MainActivity.this,"Пожалуйста, попробуйте позже.",Toast.LENGTH_SHORT).show();
                    aniButton.doneLoadingAnimation(Color.parseColor("#ff5959"), BitmapFactory.decodeResource(getResources(), R.mipmap.fail));


                }



            });












    }




}


