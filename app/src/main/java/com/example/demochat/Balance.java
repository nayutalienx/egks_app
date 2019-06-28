package com.example.demochat;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RemoteViews;
import android.widget.Toast;

import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Implementation of App Widget functionality.
 */
public class Balance extends AppWidgetProvider {

    static String CLICK_ACTION = "CLICKED";

    private static RemoteViews views;
    private static AppWidgetManager appWM;
    private static int appWID;
    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {
        Paper.init(context);
        appWM = appWidgetManager;
        appWID = appWidgetId;
        Intent intent = new Intent(context,Balance.class);
        intent.setAction(CLICK_ACTION);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context,0,intent,0);


        //CharSequence widgetText = context.getString(R.string.appwidget_text);
        // Construct the RemoteViews object
        views = new RemoteViews(context.getPackageName(), R.layout.balance);
        String tempResult = Paper.book().read("result");
        views.setTextViewText(R.id.appwidget_text, tempResult.trim());

        views.setOnClickPendingIntent(R.id.widget_update,pendingIntent);
        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onReceive(final Context context, Intent intent) {
        super.onReceive(context, intent);
        if(intent.getAction().equals(CLICK_ACTION)){
            Toast.makeText(context,"кнопка нажата!",Toast.LENGTH_SHORT).show();
            Paper.init(context);
            String card = Paper.book().read("cardNumber");

            Pattern pt = Pattern.compile("([0-9]{9})");
            Matcher mt = pt.matcher(card);

            if(mt.find()) {

                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl("https://shop.payberry.ru/Service/")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
                Api api = retrofit.create(Api.class);




                views.setTextViewText(R.id.appwidget_text, "Wait.");
                appWM.updateAppWidget(appWID, views);
                Call<PostResponse> call = api.createPost("РџСЂРѕРґРѕР»Р¶РёС‚СЊ",5,1,4253,card,"b_TC5wFhRn2nDwExumPPFRlJaLmAzlDJZP6qwoSCGHsHxZEQXl7jqT-YHWDxQtfAj55HF_v5w3Ow5G5I6UiT0HH5_4n2knPM54_Y6JvYhyk1");

                call.enqueue(new Callback<PostResponse>() {
                    @Override
                    public void onResponse(Call<PostResponse> call, Response<PostResponse> response) {
                        if(!response.isSuccessful()){
                            Toast.makeText(context,"Количество попыток закончилось. Пожалуйста, попробуйте позже.",Toast.LENGTH_SHORT).show();


                            return;
                        }
                        PostResponse postResponse = response.body();
                        String content = "";
                        content += postResponse.getMessage();
                        Pattern pt = Pattern.compile("(Остаток [0-9]{1,}.[0-9]{1,})");
                        Matcher mt = pt.matcher(content);
                        if (mt.find()) {
                            String res = mt.group(1).substring(7) + " руб.";
                            Paper.init(context);
                            Paper.book().write("result",res);


                            views.setTextViewText(R.id.appwidget_text, res.trim());
                            appWM.updateAppWidget(appWID, views);
                            Date date = new Date();
                            long ltime = date.getTime();
                            Paper.book().write("date",ltime);





                        } else {
                            //textViewResult.setText("404 not found");
                            Toast.makeText(context,"Номер карты не найден.",Toast.LENGTH_SHORT).show();
                        }


                    }

                    @Override
                    public void onFailure(Call<PostResponse> call, Throwable t) {
                        //textViewResult.setText("Error.");
                        Toast.makeText(context,"Пожалуйста, попробуйте позже.",Toast.LENGTH_SHORT).show();

                    }
                });





            } else {
                Toast.makeText(context,"Вы не ввели номер карты.",Toast.LENGTH_SHORT).show();
            }

        }

    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

}

