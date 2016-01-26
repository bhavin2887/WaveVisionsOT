package com.example.wavevisionsot;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

public class RecepieActivity extends Activity{

	private WaveDataStorage waveDataStorageObject = new WaveDataStorage(this);
	ArrayAdapter<String> adapter;
	
	EditText recepie_name;
	Button recepie_select,recepie_cancel;
	LinearLayout img_pressure_recepie,img_ot_light_recepie,img_temprature_recepie,img_lights_recepie,img_humidity_recepie,img_xray_recepie,ll_ane_recepie,ll_sur_recepie;
	Dialog dialog_ot_light, dialog_light,dialog_xray,dialog,dialog_temp,dialog_humi;
	final List<String> list3 = new ArrayList<String>();
	ListView listView;
	TextView txt_ox_low,txt_ox_high,txt_n2o_low,txt_n2o_high,txt_vac_low,txt_vac_high,txt_air_low,txt_air_high;
	RadioButton one;
	RadioButton two;
	RadioButton three;
	SharedPreferences sharedpreferences;	
	public static String MYPREFERENCE = "MyPreference";
	TextView text_recepie_active;
	String bool_one, bool_two, bool_three, bool_four;

	private final long startTime = 5000;
	private final long interval = 1000;
	private MalibuCountDownTimer countDownTimer;
	private boolean timerHasStarted = false;

	public class MalibuCountDownTimer extends CountDownTimer
	{

		public MalibuCountDownTimer(long startTime, long interval)
		{
			super(startTime, interval);
		}

		@Override
		public void onFinish()
		{
			//text.setText("Time's up!");
			//timeElapsedView.setText("Time Elapsed: " + String.valueOf(startTime));
			Log.i("TIME","Time's UP==============");
		}

		@Override
		public void onTick(long millisUntilFinished)
		{
			//text.setText("Time remain:" + millisUntilFinished);
			//timeElapsed = startTime - millisUntilFinished;
			//timeElapsedView.setText("Time Elapsed: " + String.valueOf(timeElapsed));
			Log.i("TIME","Time remain:" + millisUntilFinished);
		}
	}
			
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_recepie);
		
		recepie_name = (EditText)findViewById(R.id.recepie_name);
		recepie_select = (Button)findViewById(R.id.recepie_select);
		recepie_cancel = (Button)findViewById(R.id.recepie_cancel);
		sharedpreferences = getSharedPreferences(MYPREFERENCE, Context.MODE_PRIVATE);

		img_pressure_recepie = (LinearLayout)findViewById(R.id.img_pressure_recepie);
		img_ot_light_recepie = (LinearLayout)findViewById(R.id.img_ot_light_recepie);
		img_temprature_recepie = (LinearLayout)findViewById(R.id.img_temprature_recepie);
		img_lights_recepie = (LinearLayout)findViewById(R.id.img_lights_recepie);
		img_humidity_recepie = (LinearLayout)findViewById(R.id.img_humidity_recepie);
		img_xray_recepie = (LinearLayout)findViewById(R.id.img_xray_recepie);
		ll_ane_recepie = (LinearLayout)findViewById(R.id.ll_ane_recepie);
		ll_sur_recepie = (LinearLayout)findViewById(R.id.ll_sur_recepie);
		
		text_recepie_active = (TextView)findViewById(R.id.text_recepie_active);
        listView = (ListView)findViewById(R.id.list);
		
        text_recepie_active.setText(sharedpreferences.getString("recepie_id", "Default"));

        waveDataStorageObject.open();
        list3.clear();
        Cursor cursor3 = waveDataStorageObject.getTableData(WaveDataStorage.DATABASE_DOCTOR_D);
		if (cursor3.moveToFirst() && cursor3.getCount() >=1) {
		    do {
				 list3.add(cursor3.getString(cursor3.getColumnIndex(WaveDataStorage.DOC_NAME)));
		    } while (cursor3.moveToNext());
		    adapter = new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_list_item_1, android.R.id.text1, list3);
		    listView.setAdapter(adapter); 
	   	}
        waveDataStorageObject.close();
        
        listView.setOnItemClickListener(new OnItemClickListener() {
              @Override
              public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
  				
	               int itemPosition = position;
	               String  itemValue    = (String) listView.getItemAtPosition(position);
	               Toast.makeText(getApplicationContext(),"Position :"+itemPosition+"  ListItem : " +itemValue , Toast.LENGTH_LONG).show();
	               SharedPreferences.Editor editor = sharedpreferences.edit();
				   editor.putString("recepie_id", ""+itemValue);
				   editor.commit();
				   text_recepie_active.setText(itemValue);
              }
         }); 
        
        
        recepie_select.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				
				waveDataStorageObject.open();
         		ContentValues cv = new ContentValues();
			  	cv.put(WaveDataStorage.DOC_NAME, recepie_name.getText().toString());
				waveDataStorageObject.insert(WaveDataStorage.DATABASE_DOCTOR_D, cv);
				
				list3.add(recepie_name.getText().toString());
				if(list3.size()>=2){
					adapter.notifyDataSetChanged();
				}else{
				    adapter = new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_list_item_1, android.R.id.text1, list3);
				    listView.setAdapter(adapter); 
				}
				
				recepie_name.setText("");
				waveDataStorageObject.close();
			}
		});
        
        
        img_pressure_recepie.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				dialog = new Dialog(RecepieActivity.this);
				dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
				dialog.setContentView(R.layout.dialog_pressure);
				
				Button pressure_one = (Button) dialog.findViewById(R.id.btn_ox_low_up);
				Button pressure_two = (Button) dialog.findViewById(R.id.btn_ox_low_down);
				Button pressure_three = (Button) dialog.findViewById(R.id.btn_ox_hi_up);
				Button pressure_four = (Button) dialog.findViewById(R.id.btn_ox_hi_down);
				
				Button pressure_five = (Button) dialog.findViewById(R.id.btn_n2o_hi_up);
				Button pressure_six = (Button) dialog.findViewById(R.id.btn_n2o_high_down);
				Button pressure_seven = (Button) dialog.findViewById(R.id.btn_n2o_low_up);
				Button pressure_eight = (Button) dialog.findViewById(R.id.btn_n2o_low_down);
				
				Button pressure_nine = (Button) dialog.findViewById(R.id.btn_vac_low_up);
				Button pressure_ten = (Button) dialog.findViewById(R.id.btn_vac_low_down);
				Button pressure_eleven = (Button) dialog.findViewById(R.id.btn_vac_hi_up);
				Button pressure_twelve = (Button) dialog.findViewById(R.id.btn_vac_hi_down);
				
				Button pressure_thirteen = (Button) dialog.findViewById(R.id.btn_air_hi_up);
				Button pressure_fourteen = (Button) dialog.findViewById(R.id.btn_air_hi_down);
				Button pressure_fifteen = (Button) dialog.findViewById(R.id.btn_air_low_up);
				Button pressure_sixteen = (Button) dialog.findViewById(R.id.btn_air_low_down);
				
				
				RadioButton zero = (RadioButton)dialog.findViewById(R.id.radio0);
				RadioButton one = (RadioButton)dialog.findViewById(R.id.radio1);
				RadioButton two = (RadioButton)dialog.findViewById(R.id.radio2);
				RadioButton three = (RadioButton)dialog.findViewById(R.id.radio3);
				RadioButton four = (RadioButton)dialog.findViewById(R.id.radio4);
				RadioButton five = (RadioButton)dialog.findViewById(R.id.radio5);
				RadioButton six = (RadioButton)dialog.findViewById(R.id.radio6);
				RadioButton seven = (RadioButton)dialog.findViewById(R.id.radio7);
				RadioButton eight = (RadioButton)dialog.findViewById(R.id.radio8);
				RadioButton nine = (RadioButton)dialog.findViewById(R.id.radio9);
				RadioButton ten = (RadioButton)dialog.findViewById(R.id.radio10);
				RadioButton eleven = (RadioButton)dialog.findViewById(R.id.radio11);
				
				
				Button ok = (Button) dialog.findViewById(R.id.btn_ok_pressure);
				Button cancel = (Button) dialog.findViewById(R.id.btn_cancel_pressure);
				
				txt_ox_low = (TextView)dialog.findViewById(R.id.txt_ox_low);
				txt_ox_high = (TextView)dialog.findViewById(R.id.txt_ox_high);
				txt_n2o_low = (TextView)dialog.findViewById(R.id.txt_n2o_low);
				txt_n2o_high = (TextView)dialog.findViewById(R.id.txt_n2o_high);
				txt_vac_low = (TextView)dialog.findViewById(R.id.txt_vac_low);
				txt_vac_high = (TextView)dialog.findViewById(R.id.txt_vac_high);
				txt_air_low = (TextView)dialog.findViewById(R.id.txt_air_low);
				txt_air_high = (TextView)dialog.findViewById(R.id.txt_air_hi);
				
				txt_ox_low.setText(sharedpreferences.getString("op_radio_low", "30 PSI"));
				txt_ox_high.setText(sharedpreferences.getString("op_radio_high", "50 PSI"));
				
				RadioGroup op_radioGroup1=(RadioGroup)dialog.findViewById(R.id.radioGroup1);
				op_radioGroup1.setOnCheckedChangeListener(new OnCheckedChangeListener() {
					@Override
					public void onCheckedChanged(RadioGroup group, int checkedId) {
						new oxygen_level().execute(group);
					}
				});
				
				RadioGroup op_radioGroup2=(RadioGroup)dialog.findViewById(R.id.radioGroup2);
				op_radioGroup2.setOnCheckedChangeListener(new OnCheckedChangeListener() {
					@Override
					public void onCheckedChanged(RadioGroup group, int checkedId) {
						new n2o_level().execute(group);
					}
				});
				
				RadioGroup op_radioGroup3=(RadioGroup)dialog.findViewById(R.id.radioGroup3);
				op_radioGroup3.setOnCheckedChangeListener(new OnCheckedChangeListener() {
					@Override
					public void onCheckedChanged(RadioGroup group, int checkedId) {
						new vac_level().execute(group);
					}
				});
				
				RadioGroup op_radioGroup4=(RadioGroup)dialog.findViewById(R.id.radioGroup4);
				op_radioGroup4.setOnCheckedChangeListener(new OnCheckedChangeListener() {
					@Override
					public void onCheckedChanged(RadioGroup group, int checkedId) {
						new air_level().execute(group);
					}
				});
				
				ok.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						
						waveDataStorageObject.open();

		         		ContentValues cv = new ContentValues();
		         		cv.put(WaveDataStorage.OXY_LOW, txt_ox_low.getText().toString());
					  	cv.put(WaveDataStorage.OXY_HIGH, txt_ox_high.getText().toString());
		         		
					  	cv.put(WaveDataStorage.N2O_LOW, txt_n2o_low.getText().toString());
					  	cv.put(WaveDataStorage.N2O_HIGH, txt_n2o_high.getText().toString());
					  	
					  	cv.put(WaveDataStorage.VAC_LOW, txt_vac_low.getText().toString());
					  	cv.put(WaveDataStorage.VAC_HIGH, txt_vac_high.getText().toString());
					  	
					  	cv.put(WaveDataStorage.AIR_LOW, txt_air_low.getText().toString());
					  	cv.put(WaveDataStorage.AIR_HIGH, txt_air_high.getText().toString());
					  	
						waveDataStorageObject.saveDocDetails(cv, sharedpreferences.getString("recepie_id", "Default"));

						dialog.dismiss();
					}
				});
				
				cancel.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						dialog.dismiss();
					}
				});

				
				pressure_one.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						//if(compareOne(txt_ox_high.getText().toString(),txt_ox_low.getText().toString())){
							if(txt_ox_low.getText().toString().contains("PSI")){
								int val = Integer.parseInt(txt_ox_low.getText().toString().substring(0, txt_ox_low.length()-4));
								val+=1;
								txt_ox_low.setText(val+ " PSI");
							}else if(txt_ox_low.getText().toString().contains("BAR")){
								int val = Integer.parseInt(txt_ox_low.getText().toString().substring(0, txt_ox_low.length()-4));
								val+=1;
								txt_ox_low.setText(val+ " BAR");
							}else{
								int val = Integer.parseInt(txt_ox_low.getText().toString().substring(0, txt_ox_low.length()-5));
								val+=1;
								txt_ox_low.setText(val+ " mmHg");
							}
						//}else{
							
						//}
					}
				});
				
				pressure_two.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						if(txt_ox_low.getText().toString().contains("PSI")){
							int val = Integer.parseInt(txt_ox_low.getText().toString().substring(0, txt_ox_low.length()-4));
							val-=1;
							txt_ox_low.setText(val+ " PSI");
						}else if(txt_ox_low.getText().toString().contains("BAR")){
							int val = Integer.parseInt(txt_ox_low.getText().toString().substring(0, txt_ox_low.length()-4));
							val-=1;
							txt_ox_low.setText(val+ " BAR");
						}else{
							int val = Integer.parseInt(txt_ox_low.getText().toString().substring(0, txt_ox_low.length()-5));
							val-=1;
							txt_ox_low.setText(val+ " mmHg");
						}
					}
				});
				pressure_three.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						if(txt_ox_high.getText().toString().contains("PSI")){
							int val = Integer.parseInt(txt_ox_high.getText().toString().substring(0, txt_ox_high.length()-4));
							val+=1;
							txt_ox_high.setText(val+ " PSI");
						}else if(txt_ox_high.getText().toString().contains("BAR")){
							int val = Integer.parseInt(txt_ox_high.getText().toString().substring(0, txt_ox_high.length()-4));
							val+=1;
							txt_ox_high.setText(val+ " BAR");
						}else{
							int val = Integer.parseInt(txt_ox_high.getText().toString().substring(0, txt_ox_high.length()-5));
							val+=1;
							txt_ox_high.setText(val+ " mmHg");
						}
					}
				});
				pressure_four.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						if(txt_ox_high.getText().toString().contains("PSI")){
							int val = Integer.parseInt(txt_ox_high.getText().toString().substring(0, txt_ox_high.length()-4));
							val-=1;
							txt_ox_high.setText(val+ " PSI");
						}else if(txt_ox_high.getText().toString().contains("BAR")){
							int val = Integer.parseInt(txt_ox_high.getText().toString().substring(0, txt_ox_high.length()-4));
							val-=1;
							txt_ox_high.setText(val+ " BAR");
						}else{
							int val = Integer.parseInt(txt_ox_high.getText().toString().substring(0, txt_ox_high.length()-5));
							val-=1;
							txt_ox_high.setText(val+ " mmHg");
						}
					}
				});
				
				pressure_five.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
							if(txt_n2o_low.getText().toString().contains("PSI")){
								int val = Integer.parseInt(txt_n2o_low.getText().toString().substring(0, txt_n2o_low.length()-4));
								val+=1;
								txt_n2o_low.setText(val+ " PSI");
							}else if(txt_n2o_low.getText().toString().contains("BAR")){
								int val = Integer.parseInt(txt_n2o_low.getText().toString().substring(0, txt_n2o_low.length()-4));
								val+=1;
								txt_n2o_low.setText(val+ " BAR");
							}else{
								int val = Integer.parseInt(txt_n2o_low.getText().toString().substring(0, txt_n2o_low.length()-5));
								val+=1;
								txt_n2o_low.setText(val+ " mmHg");
							}
					}
				});
				
				pressure_six.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						if(txt_n2o_low.getText().toString().contains("PSI")){
							int val = Integer.parseInt(txt_n2o_low.getText().toString().substring(0, txt_n2o_low.length()-4));
							val-=1;
							txt_n2o_low.setText(val+ " PSI");
						}else if(txt_n2o_low.getText().toString().contains("BAR")){
							int val = Integer.parseInt(txt_n2o_low.getText().toString().substring(0, txt_n2o_low.length()-4));
							val-=1;
							txt_n2o_low.setText(val+ " BAR");
						}else{
							int val = Integer.parseInt(txt_n2o_low.getText().toString().substring(0, txt_n2o_low.length()-5));
							val-=1;
							txt_n2o_low.setText(val+ " mmHg");
						}
					}
				});
				
				
				pressure_seven.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						if(txt_n2o_high.getText().toString().contains("PSI")){
							int val = Integer.parseInt(txt_n2o_high.getText().toString().substring(0, txt_n2o_high.length()-4));
							val+=1;
							txt_n2o_high.setText(val+ " PSI");
						}else if(txt_n2o_high.getText().toString().contains("BAR")){
							int val = Integer.parseInt(txt_n2o_high.getText().toString().substring(0, txt_n2o_high.length()-4));
							val+=1;
							txt_n2o_high.setText(val+ " BAR");
						}else{
							int val = Integer.parseInt(txt_n2o_high.getText().toString().substring(0, txt_n2o_high.length()-5));
							val+=1;
							txt_n2o_high.setText(val+ " mmHg");
						}
					}
				});
				
				pressure_eight.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						if(txt_n2o_high.getText().toString().contains("PSI")){
							int val = Integer.parseInt(txt_n2o_high.getText().toString().substring(0, txt_n2o_high.length()-4));
							val-=1;
							txt_n2o_high.setText(val+ " PSI");
						}else if(txt_n2o_high.getText().toString().contains("BAR")){
							int val = Integer.parseInt(txt_n2o_high.getText().toString().substring(0, txt_n2o_high.length()-4));
							val-=1;
							txt_n2o_high.setText(val+ " BAR");
						}else{
							int val = Integer.parseInt(txt_n2o_high.getText().toString().substring(0, txt_n2o_high.length()-5));
							val-=1;
							txt_n2o_high.setText(val+ " mmHg");
						}
					}
				});
				
						
				pressure_nine.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
							if(txt_vac_low.getText().toString().contains("PSI")){
								int val = Integer.parseInt(txt_vac_low.getText().toString().substring(0, txt_vac_low.length()-4));
								val+=1;
								txt_vac_low.setText(val+ " PSI");
							}else if(txt_vac_low.getText().toString().contains("BAR")){
								int val = Integer.parseInt(txt_vac_low.getText().toString().substring(0, txt_vac_low.length()-4));
								val+=1;
								txt_vac_low.setText(val+ " BAR");
							}else{
								int val = Integer.parseInt(txt_vac_low.getText().toString().substring(0, txt_vac_low.length()-5));
								val+=1;
								txt_vac_low.setText(val+ " mmHg");
							}
					}
				});
				
				pressure_ten.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						if(txt_vac_low.getText().toString().contains("PSI")){
							int val = Integer.parseInt(txt_vac_low.getText().toString().substring(0, txt_vac_low.length()-4));
							val-=1;
							txt_vac_low.setText(val+ " PSI");
						}else if(txt_vac_low.getText().toString().contains("BAR")){
							int val = Integer.parseInt(txt_vac_low.getText().toString().substring(0, txt_vac_low.length()-4));
							val-=1;
							txt_vac_low.setText(val+ " BAR");
						}else{
							int val = Integer.parseInt(txt_vac_low.getText().toString().substring(0, txt_vac_low.length()-5));
							val-=1;
							txt_vac_low.setText(val+ " mmHg");
						}
					}
				});
				
				
				pressure_eleven.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						if(txt_vac_high.getText().toString().contains("PSI")){
							int val = Integer.parseInt(txt_vac_high.getText().toString().substring(0, txt_vac_high.length()-4));
							val+=1;
							txt_vac_high.setText(val+ " PSI");
						}else if(txt_vac_high.getText().toString().contains("BAR")){
							int val = Integer.parseInt(txt_vac_high.getText().toString().substring(0, txt_vac_high.length()-4));
							val+=1;
							txt_vac_high.setText(val+ " BAR");
						}else{
							int val = Integer.parseInt(txt_vac_high.getText().toString().substring(0, txt_vac_high.length()-5));
							val+=1;
							txt_vac_high.setText(val+ " mmHg");
						}
					}
				});
				
				pressure_twelve.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						if(txt_vac_high.getText().toString().contains("PSI")){
							int val = Integer.parseInt(txt_vac_high.getText().toString().substring(0, txt_vac_high.length()-4));
							val-=1;
							txt_vac_high.setText(val+ " PSI");
						}else if(txt_vac_high.getText().toString().contains("BAR")){
							int val = Integer.parseInt(txt_vac_high.getText().toString().substring(0, txt_vac_high.length()-4));
							val-=1;
							txt_vac_high.setText(val+ " BAR");
						}else{
							int val = Integer.parseInt(txt_vac_high.getText().toString().substring(0, txt_vac_high.length()-5));
							val-=1;
							txt_vac_high.setText(val+ " mmHg");
						}
					}
				});
				
						
						pressure_thirteen.setOnClickListener(new OnClickListener() {
							@Override
							public void onClick(View v) {
									if(txt_air_low.getText().toString().contains("PSI")){
										int val = Integer.parseInt(txt_air_low.getText().toString().substring(0, txt_air_low.length()-4));
										val+=1;
										txt_air_low.setText(val+ " PSI");
									}else if(txt_air_low.getText().toString().contains("BAR")){
										int val = Integer.parseInt(txt_air_low.getText().toString().substring(0, txt_air_low.length()-4));
										val+=1;
										txt_air_low.setText(val+ " BAR");
									}else{
										int val = Integer.parseInt(txt_air_low.getText().toString().substring(0, txt_air_low.length()-5));
										val+=1;
										txt_air_low.setText(val+ " mmHg");
									}
							}
						});
						
						pressure_fourteen.setOnClickListener(new OnClickListener() {
							@Override
							public void onClick(View v) {
								if(txt_air_low.getText().toString().contains("PSI")){
									int val = Integer.parseInt(txt_air_low.getText().toString().substring(0, txt_air_low.length()-4));
									val-=1;
									txt_air_low.setText(val+ " PSI");
								}else if(txt_air_low.getText().toString().contains("BAR")){
									int val = Integer.parseInt(txt_air_low.getText().toString().substring(0, txt_air_low.length()-4));
									val-=1;
									txt_air_low.setText(val+ " BAR");
								}else{
									int val = Integer.parseInt(txt_air_low.getText().toString().substring(0, txt_air_low.length()-5));
									val-=1;
									txt_air_low.setText(val+ " mmHg");
								}
							}
						});
						
						
						pressure_fifteen.setOnClickListener(new OnClickListener() {
							@Override
							public void onClick(View v) {
								if(txt_air_high.getText().toString().contains("PSI")){
									int val = Integer.parseInt(txt_air_high.getText().toString().substring(0, txt_air_high.length()-4));
									val+=1;
									txt_air_high.setText(val+ " PSI");
								}else if(txt_air_high.getText().toString().contains("BAR")){
									int val = Integer.parseInt(txt_air_high.getText().toString().substring(0, txt_air_high.length()-4));
									val+=1;
									txt_air_high.setText(val+ " BAR");
								}else{
									int val = Integer.parseInt(txt_air_high.getText().toString().substring(0, txt_air_high.length()-5));
									val+=1;
									txt_air_high.setText(val+ " mmHg");
								}
							}
						});
						
						pressure_sixteen.setOnClickListener(new OnClickListener() {
							@Override
							public void onClick(View v) {
								if(txt_air_high.getText().toString().contains("PSI")){
									int val = Integer.parseInt(txt_air_high.getText().toString().substring(0, txt_air_high.length()-4));
									val-=1;
									txt_air_high.setText(val+ " PSI");
								}else if(txt_air_high.getText().toString().contains("BAR")){
									int val = Integer.parseInt(txt_air_high.getText().toString().substring(0, txt_air_high.length()-4));
									val-=1;
									txt_air_high.setText(val+ " BAR");
								}else{
									int val = Integer.parseInt(txt_air_high.getText().toString().substring(0, txt_air_high.length()-5));
									val-=1;
									txt_air_high.setText(val+ " mmHg");
								}
							}
						});
												
				if(sharedpreferences.getInt("op_radio", 0) == 1){
					   zero.setChecked(true);
				}else if(sharedpreferences.getInt("op_radio", 0) == 2){
						one.setChecked(true);
				}else{
						two.setChecked(true);	
				}

				if(sharedpreferences.getInt("n2o_radio", 0) == 1){
					   three.setChecked(true);
				}else if(sharedpreferences.getInt("n2o_radio", 0) == 2){
						four.setChecked(true);
				}else{
						five.setChecked(true);	
				}
				
				if(sharedpreferences.getInt("vac_radio", 0) == 1){
					   six.setChecked(true);
				}else if(sharedpreferences.getInt("vac_radio", 0) == 2){
						seven.setChecked(true);
				}else{
						eight.setChecked(true);	
				}
				
				if(sharedpreferences.getInt("air_radio", 0) == 1){
					   nine.setChecked(true);
				}else if(sharedpreferences.getInt("air_radio", 0) == 2){
						ten.setChecked(true);
				}else{
						eleven.setChecked(true);	
				}
				
				dialog.show();
			}
		});
        
        img_ot_light_recepie.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog_ot_light = new Dialog(RecepieActivity.this);
				dialog_ot_light.requestWindowFeature(Window.FEATURE_NO_TITLE);
				dialog_ot_light.setContentView(R.layout.dialog_ot);

				final Switch one = (Switch)dialog_ot_light.findViewById(R.id.switch1_ot_light);
				final Switch two = (Switch)dialog_ot_light.findViewById(R.id.switch2_ot_light);
				Button btn_ok = (Button)dialog_ot_light.findViewById(R.id.btn_ok_ot);
				dialog_ot_light.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
				
				waveDataStorageObject.open();
				bool_three = "false";
				bool_four = "false";
				Cursor cursor2 = waveDataStorageObject.getDocDetails(sharedpreferences.getString("recepie_id", "Default"));
				if (cursor2.moveToFirst() && cursor2.getCount() >=1) {
					String val_ot_l1 = cursor2.getString(cursor2.getColumnIndex(WaveDataStorage.DOC_OT_LIGHT1));
					String val_ot_l2 = cursor2.getString(cursor2.getColumnIndex(WaveDataStorage.DOC_OT_LIGHT2));
					
				    if(val_ot_l1 != null && val_ot_l1.equals("true"))
				    	one.setChecked(true);
				    else 
				    	one.setChecked(false);
				    
				    if(val_ot_l2 != null && val_ot_l2.equals("true"))
				    	two.setChecked(true);
				    else 
				    	two.setChecked(false);
			   	}
				
				waveDataStorageObject.close();

				one.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						if (((Switch) v).isChecked()) {
							bool_three = "true";
				        }else {
				        	bool_three = "false";
				        }
					}
				});
				two.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						if (((Switch) v).isChecked()) {
							bool_four = "true";
				        }else {
				        	bool_four = "false";
				        }
					}
				});
				btn_ok.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						waveDataStorageObject.open();
		         		ContentValues cv = new ContentValues();
					  	cv.put(WaveDataStorage.DOC_OT_LIGHT1, bool_three);
					  	cv.put(WaveDataStorage.DOC_OT_LIGHT2, bool_four);
						waveDataStorageObject.saveDocDetails(cv, sharedpreferences.getString("recepie_id", "Default"));
						waveDataStorageObject.close();
						dialog_ot_light.dismiss();
					}
				});
				dialog_ot_light.show();
			}
		});
        
        img_temprature_recepie.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog_temp = new Dialog(RecepieActivity.this);
				dialog_temp.requestWindowFeature(Window.FEATURE_NO_TITLE);
				dialog_temp.setContentView(R.layout.dialog_temperature);
				RadioGroup op_radioGroup=(RadioGroup)dialog_temp.findViewById(R.id.radioG_temp);
				RadioButton one = (RadioButton)dialog_temp.findViewById(R.id.radio_fer);
				RadioButton two = (RadioButton)dialog_temp.findViewById(R.id.radio_cel);
				Button btn_ok = (Button)dialog_temp.findViewById(R.id.btn_ok_temp);
				dialog_temp.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
				
				op_radioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {
					@Override
					public void onCheckedChanged(RadioGroup group, int checkedId) {
					   Integer getId = group.getCheckedRadioButtonId();
					   //getId == one.getId();
					}
				});
				
				btn_ok.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						dialog_temp.dismiss();
					}
				});
				dialog_temp.show();
			}
		});
        
        img_lights_recepie.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog_light = new Dialog(RecepieActivity.this);
				dialog_light.requestWindowFeature(Window.FEATURE_NO_TITLE);
				dialog_light.setContentView(R.layout.dialog_lights);
		
				Switch one = (Switch)dialog_light.findViewById(R.id.switch1_light);
				Switch two = (Switch)dialog_light.findViewById(R.id.switch2_light);
				Button btn_ok = (Button)dialog_light.findViewById(R.id.btn_ok_light);

				dialog_light.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
				one.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						if (((Switch) v).isChecked()) {
							Log.i("", "EVEN");
				        }else {
							Log.i("", "ODD");
				        }
					}
				});
				two.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						if (((Switch) v).isChecked()) {
							Log.i("", "EVEN");
				        }else {
							Log.i("", "ODD");
				        }
					}
				});
				btn_ok.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						dialog_light.dismiss();
					}
				});
				dialog_light.show();
			}
		});
        
        img_humidity_recepie.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog_humi = new Dialog(RecepieActivity.this);
				dialog_humi.requestWindowFeature(Window.FEATURE_NO_TITLE);
				dialog_humi.setContentView(R.layout.dialog_humidity);

				Button btn_ok = (Button)dialog_humi.findViewById(R.id.btn_ok_humidity);
				
				dialog_humi.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
				
				btn_ok.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						dialog_humi.dismiss();
					}
				});
				dialog_humi.show();
			}
		});
        
        img_xray_recepie.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog_xray = new Dialog(RecepieActivity.this);
				dialog_xray.requestWindowFeature(Window.FEATURE_NO_TITLE);
				dialog_xray.setContentView(R.layout.dialog_xray);
		
				final Switch one = (Switch)dialog_xray.findViewById(R.id.switch1_xray);
				final Switch two = (Switch)dialog_xray.findViewById(R.id.switch2_xray);
				Button btn_ok = (Button)dialog_xray.findViewById(R.id.btn_ok_xray);
				dialog_xray.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

				waveDataStorageObject.open();
				
				Cursor cursor2 = waveDataStorageObject.getDocDetails(sharedpreferences.getString("recepie_id", "Default"));
				if (cursor2.moveToFirst() && cursor2.getCount() >=1) {
					String val_xray1 = cursor2.getString(cursor2.getColumnIndex(WaveDataStorage.DOC_XRAY1));
					String val_xray2 = cursor2.getString(cursor2.getColumnIndex(WaveDataStorage.DOC_XRAY2));
					
				    if(val_xray1 != null && val_xray1.equals("true")){
				    	one.setChecked(true);
				    	bool_one = "true";
				    }else{
				    	one.setChecked(false);
				    	bool_one = "false";
				    }
				    
				    if(val_xray2 != null && val_xray2.equals("true")){
				    	two.setChecked(true);
				    	bool_two = "true";
				    }else{
				    	two.setChecked(false);
				    	bool_two = "false";
				    }
			   	}
				
				waveDataStorageObject.close();

				
				one.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						if (((Switch) v).isChecked()) {
					    	bool_one = "true";
				        }else {
				        	bool_one = "false";
				        }
					}
				});
				two.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						if (((Switch) v).isChecked()) {
					    	bool_two = "true"; 
				        }else {
				        	bool_two = "false";
				        }
					}
				});
				btn_ok.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						waveDataStorageObject.open();
		         		ContentValues cv = new ContentValues();
					  	cv.put(WaveDataStorage.DOC_XRAY1, bool_one);
					  	cv.put(WaveDataStorage.DOC_XRAY2, bool_two);
						waveDataStorageObject.saveDocDetails(cv, sharedpreferences.getString("recepie_id", "Default"));
						dialog_xray.dismiss();
					}
				});
				dialog_xray.show();
			}
		});
        ll_ane_recepie.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
	            TimePickerDialog mTimePicker;
	            mTimePicker = new TimePickerDialog(RecepieActivity.this, new TimePickerDialog.OnTimeSetListener() {
	                @Override
	                public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
	                    //eReminderTime.setText( selectedHour + ":" + selectedMinute);
	                	
	                	int sec = selectedMinute * 1000;
	                	int hour = selectedHour * 600000;
	                	Log.i("LOG","hour"+hour);
	                	Log.i("LOG","SEC="+sec);
	            		countDownTimer = new MalibuCountDownTimer(sec+hour, interval);
	            		
	            		if (!timerHasStarted)
	    				{
	    					countDownTimer.start();
	    					timerHasStarted = true;
	    				}else{
	    					countDownTimer.cancel();
	    					timerHasStarted = false;
	    				}
	            		
	            		waveDataStorageObject.open();
	            		ContentValues cv = new ContentValues();
		         		cv.put(WaveDataStorage.ANE_TIME, sec+hour);
						waveDataStorageObject.saveDocDetails(cv, sharedpreferences.getString("recepie_id", "Default"));
						waveDataStorageObject.close();
	                }
	            }, 01, 00, false);//Yes 24 hour time
	            //mTimePicker.setTitle("Select Alarm for Anesthesia");  
	            mTimePicker.setMessage("           HH          MM");
	            mTimePicker.show();
				
			}
		});
        
        ll_sur_recepie.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				 TimePickerDialog mTimePicker;
		            mTimePicker = new TimePickerDialog(RecepieActivity.this, new TimePickerDialog.OnTimeSetListener() {
		                @Override
		                public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
		                    //eReminderTime.setText( selectedHour + ":" + selectedMinute);
		                	
		                	int sec = selectedMinute * 1000;
		                	int hour = selectedHour * 600000;
		                	Log.i("LOG","hour"+hour);
		                	Log.i("LOG","SEC="+sec);
		                	
		                	waveDataStorageObject.open();
		            		ContentValues cv = new ContentValues();
			         		cv.put(WaveDataStorage.SUR_TIME, sec+hour);
							waveDataStorageObject.saveDocDetails(cv, sharedpreferences.getString("recepie_id", "Default"));
							waveDataStorageObject.close();
		                }
		            }, 01, 00, true);//Yes 24 hour time
		            //mTimePicker.setTitle("Select Alarm for Anesthesia");  
		            mTimePicker.setMessage("           HH          MM");
		            mTimePicker.show();
			}
		});
	}
	
	
	private class oxygen_level extends AsyncTask<RadioGroup, String, Integer> {

		//  private String resp;
		  private ProgressDialog dialog_prog;
		  @Override
		  protected void onPreExecute() {
			  dialog_prog = new ProgressDialog(RecepieActivity.this);
			  dialog_prog.setMessage("Please wait...");
			  dialog_prog.show();
		  }

		  @Override
		  protected Integer doInBackground(RadioGroup... group) {
		   publishProgress("Sleeping..."); // Calls onProgressUpdate()
		   Integer getId = group[0].getCheckedRadioButtonId();
		   return getId;
		  }
		  
		  @Override
		  protected void onPostExecute(Integer getId) {
			  try {
					String txt_ox_low_string = txt_ox_low.getText().toString();
					String txt_ox_high_string = txt_ox_high.getText().toString();
					if(txt_ox_low_string.toString().contains("PSI")){
						if(getId == one.getId()){
						}else if (getId == two.getId()){
							txt_ox_low_string = txt_ox_low_string.substring(0, txt_ox_low_string.length()-4);
							txt_ox_high_string = txt_ox_high_string.substring(0, txt_ox_high_string.length()-4);
							int myVal = PsiToBar(Integer.parseInt(txt_ox_low_string));
							int myVal2 = PsiToBar(Integer.parseInt(txt_ox_high_string));
							txt_ox_low.setText(myVal+" BAR");
							txt_ox_high.setText(myVal2+" BAR");
							
				            SharedPreferences.Editor editor = sharedpreferences.edit();
							editor.putString("op_radio_low", txt_ox_low.getText().toString());
							editor.putString("op_radio_high", txt_ox_high.getText().toString());
							editor.putInt("op_radio", 2);
							editor.commit();
						}else if (getId == three.getId()){
							txt_ox_low_string = txt_ox_low_string.substring(0, txt_ox_low_string.length()-4);
							txt_ox_high_string = txt_ox_high_string.substring(0, txt_ox_high_string.length()-4);
							int myVal = PsiTommHg(Integer.parseInt(txt_ox_low_string));
							int myVal2 = PsiTommHg(Integer.parseInt(txt_ox_high_string));
							txt_ox_low.setText(myVal+" mmHg");
							txt_ox_high.setText(myVal2+" mmHg");

							SharedPreferences.Editor editor = sharedpreferences.edit();
							editor.putString("op_radio_low", txt_ox_low.getText().toString());
							editor.putString("op_radio_high", txt_ox_high.getText().toString());
							editor.putInt("op_radio", 3);
							editor.commit();
						}
					}else if(txt_ox_low_string.toString().contains("BAR")){
						if(getId == one.getId()){
							txt_ox_low_string = txt_ox_low_string.substring(0, txt_ox_low_string.length()-4);
							txt_ox_high_string = txt_ox_high_string.substring(0, txt_ox_high_string.length()-4);
							int myVal = BarToPsi(Integer.parseInt(txt_ox_low_string));
							int myVal2 = BarToPsi(Integer.parseInt(txt_ox_high_string));
							txt_ox_low.setText(myVal+" PSI");
							txt_ox_high.setText(myVal2+" PSI");
							SharedPreferences.Editor editor = sharedpreferences.edit();
							editor.putString("op_radio_low", txt_ox_low.getText().toString());
							editor.putString("op_radio_high", txt_ox_high.getText().toString());
							editor.putInt("op_radio", 1);
							editor.commit();
						}else if (getId == two.getId()){
						}else if (getId == three.getId()){
							txt_ox_low_string = txt_ox_low_string.substring(0, txt_ox_low_string.length()-4);
							txt_ox_high_string = txt_ox_high_string.substring(0, txt_ox_high_string.length()-4);
							int myVal = BarTommHg(Integer.parseInt(txt_ox_low_string));
							int myVal2 = BarTommHg(Integer.parseInt(txt_ox_high_string));
							txt_ox_low.setText(myVal+" mmHg");
							txt_ox_high.setText(myVal2+" mmHg");
							SharedPreferences.Editor editor = sharedpreferences.edit();
							editor.putString("op_radio_low", txt_ox_low.getText().toString());
							editor.putString("op_radio_high", txt_ox_high.getText().toString());
							editor.putInt("op_radio", 3);
							editor.commit();
						}
					}else{
						if(getId == one.getId()){
							txt_ox_low_string = txt_ox_low_string.substring(0, txt_ox_low_string.length()-5);
							txt_ox_high_string = txt_ox_high_string.substring(0, txt_ox_high_string.length()-5);
							int myVal = mmHgToPsi(Integer.parseInt(txt_ox_low_string));
							int myVal2 = mmHgToPsi(Integer.parseInt(txt_ox_high_string));
							txt_ox_low.setText(myVal+" PSI");
							txt_ox_high.setText(myVal2+" PSI");
							SharedPreferences.Editor editor = sharedpreferences.edit();
							editor.putString("op_radio_low", txt_ox_low.getText().toString());
							editor.putString("op_radio_high", txt_ox_high.getText().toString());
							editor.putInt("op_radio", 1);
							editor.commit();
						}else if (getId == two.getId()){
							txt_ox_low_string = txt_ox_low_string.substring(0, txt_ox_low_string.length()-5);
							txt_ox_high_string = txt_ox_high_string.substring(0, txt_ox_high_string.length()-5);
							int myVal = mmHgToBar(Integer.parseInt(txt_ox_low_string));
							int myVal2 = mmHgToBar(Integer.parseInt(txt_ox_high_string));
							txt_ox_low.setText(myVal+" BAR");
							txt_ox_high.setText(myVal2+" BAR");
							SharedPreferences.Editor editor = sharedpreferences.edit();
							editor.putString("op_radio_low", txt_ox_low.getText().toString());
							editor.putString("op_radio_high", txt_ox_high.getText().toString());
							editor.putInt("op_radio", 2);
							editor.commit();
						}else if (getId == three.getId()){
						}
					}
			   } catch (Exception e) {
			    e.printStackTrace();
			//    resp = e.getMessage();
			   }
			  if (dialog_prog.isShowing()) {
		            dialog_prog.dismiss();
		        }
		  }
		 }
	
	private class n2o_level extends AsyncTask<RadioGroup, String, Integer> {

		//  private String resp;
		  private ProgressDialog dialog_prog;
		  @Override
		  protected void onPreExecute() {
			  dialog_prog = new ProgressDialog(RecepieActivity.this);
			  dialog_prog.setMessage("Please wait...");
			  dialog_prog.show();
		  }

		  @Override
		  protected Integer doInBackground(RadioGroup... group) {
		   publishProgress("Sleeping..."); // Calls onProgressUpdate()
		   Integer getId = group[0].getCheckedRadioButtonId();
		   return getId;
		  }
		  
		  @Override
		  protected void onPostExecute(Integer getId) {
			  try {
					String txt_n2o_low_string = txt_n2o_low.getText().toString();
					String txt_n2o_high_string = txt_n2o_high.getText().toString();
					if(txt_n2o_low_string.toString().contains("PSI")){
						if(getId == one.getId()){
						}else if (getId == two.getId()){
							txt_n2o_low_string = txt_n2o_low_string.substring(0, txt_n2o_low_string.length()-4);
							txt_n2o_high_string = txt_n2o_high_string.substring(0, txt_n2o_high_string.length()-4);
							int myVal = PsiToBar(Integer.parseInt(txt_n2o_low_string));
							int myVal2 = PsiToBar(Integer.parseInt(txt_n2o_high_string));
							txt_n2o_low.setText(myVal+" BAR");
							txt_n2o_high.setText(myVal2+" BAR");
							
				            SharedPreferences.Editor editor = sharedpreferences.edit();
							editor.putString("n2o_radio_low", txt_n2o_low.getText().toString());
							editor.putString("n2o_radio_high", txt_n2o_high.getText().toString());
							editor.putInt("n2o_radio", 2);
							editor.commit();
						}else if (getId == three.getId()){
							txt_n2o_low_string = txt_n2o_low_string.substring(0, txt_n2o_low_string.length()-4);
							txt_n2o_high_string = txt_n2o_high_string.substring(0, txt_n2o_high_string.length()-4);
							int myVal = PsiTommHg(Integer.parseInt(txt_n2o_low_string));
							int myVal2 = PsiTommHg(Integer.parseInt(txt_n2o_high_string));
							txt_n2o_low.setText(myVal+" mmHg");
							txt_n2o_high.setText(myVal2+" mmHg");

							SharedPreferences.Editor editor = sharedpreferences.edit();
							editor.putString("n2o_radio_low", txt_n2o_low.getText().toString());
							editor.putString("n2o_radio_high", txt_n2o_high.getText().toString());
							editor.putInt("n2o_radio", 3);
							editor.commit();
						}
					}else if(txt_n2o_low_string.toString().contains("BAR")){
						if(getId == one.getId()){
							txt_n2o_low_string = txt_n2o_low_string.substring(0, txt_n2o_low_string.length()-4);
							txt_n2o_high_string = txt_n2o_high_string.substring(0, txt_n2o_high_string.length()-4);
							int myVal = BarToPsi(Integer.parseInt(txt_n2o_low_string));
							int myVal2 = BarToPsi(Integer.parseInt(txt_n2o_high_string));
							txt_n2o_low.setText(myVal+" PSI");
							txt_n2o_high.setText(myVal2+" PSI");
							SharedPreferences.Editor editor = sharedpreferences.edit();
							editor.putString("n2o_radio_low", txt_n2o_low.getText().toString());
							editor.putString("n2o_radio_high", txt_n2o_high.getText().toString());
							editor.putInt("n2o_radio", 1);
							editor.commit();
						}else if (getId == two.getId()){
						}else if (getId == three.getId()){
							txt_n2o_low_string = txt_n2o_low_string.substring(0, txt_n2o_low_string.length()-4);
							txt_n2o_high_string = txt_n2o_high_string.substring(0, txt_n2o_high_string.length()-4);
							int myVal = BarTommHg(Integer.parseInt(txt_n2o_low_string));
							int myVal2 = BarTommHg(Integer.parseInt(txt_n2o_high_string));
							txt_n2o_low.setText(myVal+" mmHg");
							txt_n2o_high.setText(myVal2+" mmHg");
							SharedPreferences.Editor editor = sharedpreferences.edit();
							editor.putString("n2o_radio_low", txt_n2o_low.getText().toString());
							editor.putString("n2o_radio_high", txt_n2o_high.getText().toString());
							editor.putInt("n2o_radio", 3);
							editor.commit();
						}
					}else{
						if(getId == one.getId()){
							txt_n2o_low_string = txt_n2o_low_string.substring(0, txt_n2o_low_string.length()-5);
							txt_n2o_high_string = txt_n2o_high_string.substring(0, txt_n2o_high_string.length()-5);
							int myVal = mmHgToPsi(Integer.parseInt(txt_n2o_low_string));
							int myVal2 = mmHgToPsi(Integer.parseInt(txt_n2o_high_string));
							txt_n2o_low.setText(myVal+" PSI");
							txt_n2o_high.setText(myVal2+" PSI");
							SharedPreferences.Editor editor = sharedpreferences.edit();
							editor.putString("n2o_radio_low", txt_n2o_low.getText().toString());
							editor.putString("n2o_radio_high", txt_n2o_high.getText().toString());
							editor.putInt("n2o_radio", 1);
							editor.commit();
						}else if (getId == two.getId()){
							txt_n2o_low_string = txt_n2o_low_string.substring(0, txt_n2o_low_string.length()-5);
							txt_n2o_high_string = txt_n2o_high_string.substring(0, txt_n2o_high_string.length()-5);
							int myVal = mmHgToBar(Integer.parseInt(txt_n2o_low_string));
							int myVal2 = mmHgToBar(Integer.parseInt(txt_n2o_high_string));
							txt_n2o_low.setText(myVal+" BAR");
							txt_n2o_high.setText(myVal2+" BAR");
							SharedPreferences.Editor editor = sharedpreferences.edit();
							editor.putString("n2o_radio_low", txt_n2o_low.getText().toString());
							editor.putString("n2o_radio_high", txt_n2o_high.getText().toString());
							editor.putInt("n2o_radio", 2);
							editor.commit();
						}else if (getId == three.getId()){
						}
					}
			   } catch (Exception e) {
			    e.printStackTrace();
			//    resp = e.getMessage();
			   }
			  if (dialog_prog.isShowing()) {
		            dialog_prog.dismiss();
		        }
		  }
		 }
	
	
	private class vac_level extends AsyncTask<RadioGroup, String, Integer> {

		//  private String resp;
		  private ProgressDialog dialog_prog;
		  @Override
		  protected void onPreExecute() {
			  dialog_prog = new ProgressDialog(RecepieActivity.this);
			  dialog_prog.setMessage("Please wait...");
			  dialog_prog.show();
		  }

		  @Override
		  protected Integer doInBackground(RadioGroup... group) {
		   publishProgress("Sleeping..."); // Calls onProgressUpdate()
		   Integer getId = group[0].getCheckedRadioButtonId();
		   return getId;
		  }
		  
		  @Override
		  protected void onPostExecute(Integer getId) {
			  try {
					String txt_vac_low_string = txt_vac_low.getText().toString();
					String txt_vac_high_string = txt_vac_high.getText().toString();
					if(txt_vac_low_string.toString().contains("PSI")){
						if(getId == one.getId()){
						}else if (getId == two.getId()){
							txt_vac_low_string = txt_vac_low_string.substring(0, txt_vac_low_string.length()-4);
							txt_vac_high_string = txt_vac_high_string.substring(0, txt_vac_high_string.length()-4);
							int myVal = PsiToBar(Integer.parseInt(txt_vac_low_string));
							int myVal2 = PsiToBar(Integer.parseInt(txt_vac_high_string));
							txt_vac_low.setText(myVal+" BAR");
							txt_vac_high.setText(myVal2+" BAR");
							
				            SharedPreferences.Editor editor = sharedpreferences.edit();
							editor.putString("vac_radio_low", txt_vac_low.getText().toString());
							editor.putString("vac_radio_high", txt_vac_high.getText().toString());
							editor.putInt("vac_radio", 2);
							editor.commit();
						}else if (getId == three.getId()){
							txt_vac_low_string = txt_vac_low_string.substring(0, txt_vac_low_string.length()-4);
							txt_vac_high_string = txt_vac_high_string.substring(0, txt_vac_high_string.length()-4);
							int myVal = PsiTommHg(Integer.parseInt(txt_vac_low_string));
							int myVal2 = PsiTommHg(Integer.parseInt(txt_vac_high_string));
							txt_vac_low.setText(myVal+" mmHg");
							txt_vac_high.setText(myVal2+" mmHg");

							SharedPreferences.Editor editor = sharedpreferences.edit();
							editor.putString("vac_radio_low", txt_vac_low.getText().toString());
							editor.putString("vac_radio_high", txt_vac_high.getText().toString());
							editor.putInt("vac_radio", 3);
							editor.commit();
						}
					}else if(txt_vac_low_string.toString().contains("BAR")){
						if(getId == one.getId()){
							txt_vac_low_string = txt_vac_low_string.substring(0, txt_vac_low_string.length()-4);
							txt_vac_high_string = txt_vac_high_string.substring(0, txt_vac_high_string.length()-4);
							int myVal = BarToPsi(Integer.parseInt(txt_vac_low_string));
							int myVal2 = BarToPsi(Integer.parseInt(txt_vac_high_string));
							txt_vac_low.setText(myVal+" PSI");
							txt_vac_high.setText(myVal2+" PSI");
							SharedPreferences.Editor editor = sharedpreferences.edit();
							editor.putString("vac_radio_low", txt_vac_low.getText().toString());
							editor.putString("vac_radio_high", txt_vac_high.getText().toString());
							editor.putInt("vac_radio", 1);
							editor.commit();
						}else if (getId == two.getId()){
						}else if (getId == three.getId()){
							txt_vac_low_string = txt_vac_low_string.substring(0, txt_vac_low_string.length()-4);
							txt_vac_high_string = txt_vac_high_string.substring(0, txt_vac_high_string.length()-4);
							int myVal = BarTommHg(Integer.parseInt(txt_vac_low_string));
							int myVal2 = BarTommHg(Integer.parseInt(txt_vac_high_string));
							txt_vac_low.setText(myVal+" mmHg");
							txt_vac_high.setText(myVal2+" mmHg");
							SharedPreferences.Editor editor = sharedpreferences.edit();
							editor.putString("vac_radio_low", txt_vac_low.getText().toString());
							editor.putString("vac_radio_high", txt_vac_high.getText().toString());
							editor.putInt("vac_radio", 3);
							editor.commit();
						}
					}else{
						if(getId == one.getId()){
							txt_vac_low_string = txt_vac_low_string.substring(0, txt_vac_low_string.length()-5);
							txt_vac_high_string = txt_vac_high_string.substring(0, txt_vac_high_string.length()-5);
							int myVal = mmHgToPsi(Integer.parseInt(txt_vac_low_string));
							int myVal2 = mmHgToPsi(Integer.parseInt(txt_vac_high_string));
							txt_vac_low.setText(myVal+" PSI");
							txt_vac_high.setText(myVal2+" PSI");
							SharedPreferences.Editor editor = sharedpreferences.edit();
							editor.putString("vac_radio_low", txt_vac_low.getText().toString());
							editor.putString("vac_radio_high", txt_vac_high.getText().toString());
							editor.putInt("vac_radio", 1);
							editor.commit();
						}else if (getId == two.getId()){
							txt_vac_low_string = txt_vac_low_string.substring(0, txt_vac_low_string.length()-5);
							txt_vac_high_string = txt_vac_high_string.substring(0, txt_vac_high_string.length()-5);
							int myVal = mmHgToBar(Integer.parseInt(txt_vac_low_string));
							int myVal2 = mmHgToBar(Integer.parseInt(txt_vac_high_string));
							txt_vac_low.setText(myVal+" BAR");
							txt_vac_high.setText(myVal2+" BAR");
							SharedPreferences.Editor editor = sharedpreferences.edit();
							editor.putString("vac_radio_low", txt_vac_low.getText().toString());
							editor.putString("vac_radio_high", txt_vac_high.getText().toString());
							editor.putInt("vac_radio", 2);
							editor.commit();
						}else if (getId == three.getId()){
						}
					}
			   } catch (Exception e) {
			    e.printStackTrace();
			//    resp = e.getMessage();
			   }
			  if (dialog_prog.isShowing()) {
		            dialog_prog.dismiss();
		        }
		  }
		 }
	
	private class air_level extends AsyncTask<RadioGroup, String, Integer> {

		//  private String resp;
		  private ProgressDialog dialog_prog;
		  @Override
		  protected void onPreExecute() {
			  dialog_prog = new ProgressDialog(RecepieActivity.this);
			  dialog_prog.setMessage("Please wait...");
			  dialog_prog.show();
		  }

		  @Override
		  protected Integer doInBackground(RadioGroup... group) {
		   publishProgress("Sleeping..."); // Calls onProgressUpdate()
		   Integer getId = group[0].getCheckedRadioButtonId();
		   return getId;
		  }
		  
		  @Override
		  protected void onPostExecute(Integer getId) {
			  try {
					String txt_air_low_string = txt_air_low.getText().toString();
					String txt_air_high_string = txt_air_high.getText().toString();
					if(txt_air_low_string.toString().contains("PSI")){
						if(getId == one.getId()){
						}else if (getId == two.getId()){
							txt_air_low_string = txt_air_low_string.substring(0, txt_air_low_string.length()-4);
							txt_air_high_string = txt_air_high_string.substring(0, txt_air_high_string.length()-4);
							int myVal = PsiToBar(Integer.parseInt(txt_air_low_string));
							int myVal2 = PsiToBar(Integer.parseInt(txt_air_high_string));
							txt_air_low.setText(myVal+" BAR");
							txt_air_high.setText(myVal2+" BAR");
							
				            SharedPreferences.Editor editor = sharedpreferences.edit();
							editor.putString("air_radio_low", txt_air_low.getText().toString());
							editor.putString("air_radio_high", txt_air_high.getText().toString());
							editor.putInt("air_radio", 2);
							editor.commit();
						}else if (getId == three.getId()){
							txt_air_low_string = txt_air_low_string.substring(0, txt_air_low_string.length()-4);
							txt_air_high_string = txt_air_high_string.substring(0, txt_air_high_string.length()-4);
							int myVal = PsiTommHg(Integer.parseInt(txt_air_low_string));
							int myVal2 = PsiTommHg(Integer.parseInt(txt_air_high_string));
							txt_air_low.setText(myVal+" mmHg");
							txt_air_high.setText(myVal2+" mmHg");

							SharedPreferences.Editor editor = sharedpreferences.edit();
							editor.putString("air_radio_low", txt_air_low.getText().toString());
							editor.putString("air_radio_high", txt_air_high.getText().toString());
							editor.putInt("air_radio", 3);
							editor.commit();
						}
					}else if(txt_air_low_string.toString().contains("BAR")){
						if(getId == one.getId()){
							txt_air_low_string = txt_air_low_string.substring(0, txt_air_low_string.length()-4);
							txt_air_high_string = txt_air_high_string.substring(0, txt_air_high_string.length()-4);
							int myVal = BarToPsi(Integer.parseInt(txt_air_low_string));
							int myVal2 = BarToPsi(Integer.parseInt(txt_air_high_string));
							txt_air_low.setText(myVal+" PSI");
							txt_air_high.setText(myVal2+" PSI");
							SharedPreferences.Editor editor = sharedpreferences.edit();
							editor.putString("air_radio_low", txt_air_low.getText().toString());
							editor.putString("air_radio_high", txt_air_high.getText().toString());
							editor.putInt("air_radio", 1);
							editor.commit();
						}else if (getId == two.getId()){
						}else if (getId == three.getId()){
							txt_air_low_string = txt_air_low_string.substring(0, txt_air_low_string.length()-4);
							txt_air_high_string = txt_air_high_string.substring(0, txt_air_high_string.length()-4);
							int myVal = BarTommHg(Integer.parseInt(txt_air_low_string));
							int myVal2 = BarTommHg(Integer.parseInt(txt_air_high_string));
							txt_air_low.setText(myVal+" mmHg");
							txt_air_high.setText(myVal2+" mmHg");
							SharedPreferences.Editor editor = sharedpreferences.edit();
							editor.putString("air_radio_low", txt_air_low.getText().toString());
							editor.putString("air_radio_high", txt_air_high.getText().toString());
							editor.putInt("air_radio", 3);
							editor.commit();
						}
					}else{
						if(getId == one.getId()){
							txt_air_low_string = txt_air_low_string.substring(0, txt_air_low_string.length()-5);
							txt_air_high_string = txt_air_high_string.substring(0, txt_air_high_string.length()-5);
							int myVal = mmHgToPsi(Integer.parseInt(txt_air_low_string));
							int myVal2 = mmHgToPsi(Integer.parseInt(txt_air_high_string));
							txt_air_low.setText(myVal+" PSI");
							txt_air_high.setText(myVal2+" PSI");
							SharedPreferences.Editor editor = sharedpreferences.edit();
							editor.putString("air_radio_low", txt_air_low.getText().toString());
							editor.putString("air_radio_high", txt_air_high.getText().toString());
							editor.putInt("air_radio", 1);
							editor.commit();
						}else if (getId == two.getId()){
							txt_air_low_string = txt_air_low_string.substring(0, txt_air_low_string.length()-5);
							txt_air_high_string = txt_air_high_string.substring(0, txt_air_high_string.length()-5);
							int myVal = mmHgToBar(Integer.parseInt(txt_air_low_string));
							int myVal2 = mmHgToBar(Integer.parseInt(txt_air_high_string));
							txt_air_low.setText(myVal+" BAR");
							txt_air_high.setText(myVal2+" BAR");
							SharedPreferences.Editor editor = sharedpreferences.edit();
							editor.putString("air_radio_low", txt_air_low.getText().toString());
							editor.putString("air_radio_high", txt_air_high.getText().toString());
							editor.putInt("air_radio", 2);
							editor.commit();
						}else if (getId == three.getId()){
						}
					}
			   } catch (Exception e) {
			    e.printStackTrace();
			//    resp = e.getMessage();
			   }
			  if (dialog_prog.isShowing()) {
		            dialog_prog.dismiss();
		        }
		  }
		 }
	
	private int BarToPsi(int txt_ox_low) {
		int myval = (int) (txt_ox_low * 14.5);
		//String.format("%.2f", myval);
		return (int) Math.round(myval);
	}
	
	private int PsiToBar(int txt_ox_low) {
		int myval = (int) (txt_ox_low * 0.069);
		//String.format("%.2f", myval);
		return myval;
	}
	
	private int PsiTommHg(int txt_ox_low) {
		int myval = (int) (txt_ox_low * 51.7);
		//String.format("%.2f", myval);
		return myval;
	}
	
	private int mmHgToPsi(int txt_ox_low) {
		int myval = (int) (txt_ox_low * 0.019);
		return (int) Math.round(myval);
	}
	
	private int BarTommHg(int txt_ox_low) {
		int myval = (int) (txt_ox_low * 750);
		//String.format("%.2f", myval);
		return myval;
	}
	
	private int mmHgToBar(int txt_ox_low) {
		int myval = (int) (txt_ox_low * 0.00133);
		//String.format("%.2f", myval);
		return myval;
	}
	
	private boolean compareOne(String high, String low) {
		int val1 = Integer.parseInt(txt_ox_high.getText().toString().substring(0, txt_ox_low.length()-4));
		int val2 = Integer.parseInt(txt_ox_low.getText().toString().substring(0, txt_ox_low.length()-4));

		if(val1>val2)
			return true;
		else
			return false;
	}
}
