package com.morrowwind.redmountaiondagoth;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.graphics.Point;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.TypedValue;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;


import java.lang.reflect.Field;

public class MainActivity extends AppCompatActivity implements View.OnClickListener
{
    static final int MAX_ROWS=10;
    static final int NUM_COLUMNS=3;
    private Field[] dagothField=R.raw.class.getFields();
    private int fieldLength=dagothField.length;
    private SoundPool dagothSoundPool;
    private int preSound=-1;
    private int filedIndex=0;

    @Override
    protected  void onStart()
    {
        super.onStart();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build();
            dagothSoundPool = new SoundPool.Builder().setAudioAttributes(audioAttributes).setMaxStreams(fieldLength).build();
        }
        else {
            dagothSoundPool = new SoundPool(fieldLength, AudioManager.STREAM_NOTIFICATION, 0);
        }

        TableLayout padTable=findViewById(R.id.padTable);
        padTable.setShrinkAllColumns(true);
        padTable.setStretchAllColumns(true);
        makeTable(padTable);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }


    protected void makeTable(TableLayout padTable) //Make rows
    {
       int soundNum=fieldLength;
       //TableRow[] rows=new TableRow[MAX_ROWS];
       int numRows=0;

       padTable.setBackgroundResource(R.drawable.background);
       for (int i=0;i< (soundNum % NUM_COLUMNS ==0 ? soundNum / NUM_COLUMNS : soundNum /NUM_COLUMNS +1);i++)
       {
           numRows++;

           if (numRows >= MAX_ROWS)
           {
               System.err.println("Can't make over MAX_ROWS! ");
               break;
           }

           TableRow newTr=new TableRow(this);
           TableLayout.LayoutParams p=new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.MATCH_PARENT);
           p.weight = 1;
           padTable.addView(newTr,p);
           makeTableRow(newTr);
       }
    }

    protected void makeTableRow(TableRow tr) // Make rows buttons
    {
        String dirName="raw";
        String packageName=this.getPackageName();

        for (int i=0;i<NUM_COLUMNS;i++)
        {
            if (filedIndex >=fieldLength)
            {
                    System.err.println("Can't make over fieldLength! ");
                    break;
            }

            String fileName=dagothField[filedIndex].getName();
            String fileResourceName="@raw/"+fileName;

            Button newButton=new Button(this);
            newButton.setTransformationMethod(null);
            newButton.setText(makeButtonText(fileName));
            newButton.setHeight(tr.getHeight());
            newButton.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
            int soundId=getResources().getIdentifier(fileResourceName,dirName,packageName); //Get soundFiles ID
            newButton.setTag(dagothSoundPool.load(this,soundId,1)); //Save sound in the buttonTag
            newButton.setOnClickListener(this);

            tr.addView(newButton);
            newButton.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,TableRow.LayoutParams.MATCH_PARENT));

            filedIndex++;
        }
    }

    protected String makeButtonText(String fileName)
    {
        String buttonText=new String();
        buttonText=fileName;

        buttonText.toLowerCase();
        buttonText=buttonText.substring(0,1).toUpperCase()+buttonText.substring(1).replace('_',' ');

        if(fileName.endsWith("god1"))
            buttonText=buttonText.replace('1','!');
        else if(fileName.endsWith("god2"))
        {
            buttonText = buttonText.replace('2', '!');
            buttonText += '!';
        }
        else if(fileName=="sweet_nerevar")
            buttonText+="♥"; //Dagoth loves nerevar?

        return buttonText;
    }

    @Override
    public void onClick(View v) //if view is button, play music!
    {
        if(v instanceof Button)
        {
            if(preSound !=-1)
                dagothSoundPool.stop(preSound);

            preSound=dagothSoundPool.play((int) v.getTag(), 1.0F, 1.0F, 1, 0, 1.0F);
        }
    }

    @Override
    protected void onStop()
    {
        super.onStop();

        dagothSoundPool.release();
        dagothSoundPool=null;
    }

}
