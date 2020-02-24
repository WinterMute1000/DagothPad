package com.morrowwind.redmountaiondagoth;

import androidx.appcompat.app.AppCompatActivity;

import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;


import java.lang.reflect.Field;

public class MainActivity extends AppCompatActivity implements View.OnClickListener
{
    static final int MAX_ROWS=10;
    static final int NUM_COLUMNS=5;
    private Field[] dagothField=R.raw.class.getFields();
    private int fieldLength=dagothField.length;
    private SoundPool dagothSoundPool=new SoundPool(fieldLength,AudioManager.STREAM_MUSIC,0);
    private int filedIndex=0;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        //InitializeSoundPool();
        setContentView(R.layout.activity_main);
        TableLayout padTable=(TableLayout)findViewById(R.id.padTable);
        makeTable(padTable);

    }

    /*private void InitializeSoundPool()
    {
    }
    */

    protected void makeTable(TableLayout padTable) //Make rows
    {
       int soundNum=fieldLength;
       //TableRow[] rows=new TableRow[MAX_ROWS];
       int numRows=0;

       for (int i=0;i< (soundNum % NUM_COLUMNS ==0 ? soundNum / NUM_COLUMNS : soundNum /NUM_COLUMNS +1);i++)
       {
           numRows++;

           if (numRows >= MAX_ROWS)
           {
               System.err.println("Can't make over MAX_ROWS! ");
               break;
           }

           TableRow newTr=new TableRow(this);
           makeTableRow(newTr);
           padTable.addView(newTr,new TableLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT));
       }
    }

    protected void makeTableRow(TableRow tr) // Make rows buttons
    {
        String resName="@raw";
        String packageName=this.getPackageName();

        for (int i=0;i<NUM_COLUMNS;i++)
        {
            if (filedIndex >=fieldLength)
            {
                    System.err.println("Can't make over fieldLength! ");
                    break;
            }

            String fileName=dagothField[filedIndex].getName();

            Button newButton=new Button(this);
            newButton.setText(fileName);
            newButton.setLayoutParams(new TableLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT));
            int soundId=getResources().getIdentifier(resName,fileName,packageName); //Get soundFiles ID
            newButton.setTag(dagothSoundPool.load(this,soundId,1)); //Save sound in the buttonTag
            newButton.setOnClickListener(this);

            tr.addView(newButton);

            filedIndex++;
        }
    }

    @Override
    public void onClick(View v)
    {

    }
}
