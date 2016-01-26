package com.example.wavevisionsot;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener{

	private Button sur_stop,sur_reset;
	private Button btn_show_one,btn_show_two,btn_show_three,btn_show_four,btn_show_five,btn_show_six,btn_show_seven,btn_show_eight;
	private Button btn_dome1_up,btn_dome1_down,btn_dome2_up,btn_dome2_down;
	private LinearLayout btn_light1_up,btn_light2_up;
	private Button btn_ac_up,btn_ac_down,btn_xray1_up,btn_xray1_down,btn_xray2_up,btn_xray2_down;
	private Button ane_stop,ane_reset,real_alarm,btn_logo;
	private ImageView btn_door,btn_settings;
	private TextView sur_timerValue, ane_timerValue;
	
	private Handler sur_customHandler = new Handler();
	private long sur_startTime = 0L;
	long sur_timeInMilliseconds = 0L;
	long sur_timeSwapBuff = 0L;
	long sur_updatedTime = 0L;

	private Handler ane_customHandler = new Handler();
	private long ane_startTime = 0L;
	long ane_timeInMilliseconds = 0L;
	long ane_timeSwapBuff = 0L;
	long ane_updatedTime = 0L;

	public final static String PACKT_TAG = "com.packt";
	private native boolean openGPIO();
	private native Float GetTemp();
	private native void closeGPIO();
	private native boolean readGPIO(int header, int pin);
	private native void writeGPIO(int header, int pin, int val);
	private native int uartOpen(int device, int bdrate);						// HEADER 1		 
    private native boolean uartWrite(int uartFD, int length, byte data[]);		// HEADER 2 
    private native boolean uartRead(int uartFD, int length, byte data[]);		// HEADER 3
    private native void uartClose(int uartFD);									// HEADER 4
    private native void icone(int ic);							// THE NEW HEADERS ADDED RECENTYLY
    private native int reads(int id1);
    private native void ledon();	
    private native void ledoff();	
    TextView txt_pre;
    LinearLayout linear_dial,ll_warning,ll_recepie_name;
	Dialog dialog;
	Button one, two, three, four, five, six, seven, eight, nine, zero, equal, ce, del, dial, cancel,sur_alarm,ane_alarm;
	EditText disp;
	int op1;
	int op2;
	String optr;
	private WaveDataStorage waveDataStorageObject = new WaveDataStorage(this);
	ArrayAdapter<String> adapter;
	final List<String> list3 = new ArrayList<String>();
	ListView listView;
	Dialog dialog_recepie,dialog_setting;
	SharedPreferences sharedpreferences;
    private Handler handler;
	public static String MYPREFERENCE = "MyPreference";
	TextView tv_name;
    private native int myprot();
    int uartFD;																	//NEW VARIABLE THAT HOLDS FILE DESCRIPTORS 
	int val = 1;																//VAL IS NOW INT AND GLOBAL	
	static {
		System.loadLibrary("BBBAndroidHAL");									// LIBRARY NAME CHANGED
	}																			//JNI FOLDER IS CHANGED TOO

	boolean flag = false;
	boolean flag_warning = false;
	Timer timer0, timer1, timer2, timer3;
	Thread background0, background1, background2, background3;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		sur_timerValue = (TextView) findViewById(R.id.sur_timerValue);
		sur_stop = (Button) findViewById(R.id.sur_stop);
		sur_reset = (Button) findViewById(R.id.sur_reset);
		sur_alarm = (Button) findViewById(R.id.sur_alarm);
		
		ane_timerValue = (TextView) findViewById(R.id.ane_timerValue);
		ane_stop = (Button) findViewById(R.id.ane_stop);
		ane_reset = (Button) findViewById(R.id.ane_reset);
		ane_alarm = (Button) findViewById(R.id.ane_alarm);

		btn_show_one = (Button) findViewById(R.id.btn_show_one);
		btn_show_two = (Button) findViewById(R.id.btn_show_two);
		btn_show_three = (Button) findViewById(R.id.btn_show_three);
		btn_show_four = (Button) findViewById(R.id.btn_show_four);
		btn_show_five = (Button) findViewById(R.id.btn_show_five);
		btn_show_six = (Button) findViewById(R.id.btn_show_six);
		btn_show_seven = (Button) findViewById(R.id.btn_show_seven);
		btn_show_eight = (Button) findViewById(R.id.btn_show_eight);
		btn_dome1_up = (Button) findViewById(R.id.btn_dome1_up);
		btn_dome1_down = (Button) findViewById(R.id.btn_dome1_down);
		btn_dome2_up = (Button) findViewById(R.id.btn_dome2_up);
		btn_dome2_down= (Button) findViewById(R.id.btn_dome2_down);
		btn_light1_up= (LinearLayout) findViewById(R.id.btn_light1_up);
		btn_light2_up = (LinearLayout) findViewById(R.id.btn_light2_up);
		btn_ac_up = (Button) findViewById(R.id.btn_ac_up);
		btn_ac_down = (Button) findViewById(R.id.btn_ac_down);
		btn_xray1_up = (Button) findViewById(R.id.btn_xray1_up);
		btn_xray1_down = (Button) findViewById(R.id.btn_xray1_down);
		btn_xray2_up = (Button) findViewById(R.id.btn_xray2_up);
		btn_xray2_down = (Button) findViewById(R.id.btn_xray2_down);
		txt_pre = (TextView)findViewById(R.id.text_pressure);
		sharedpreferences = getSharedPreferences(MYPREFERENCE, Context.MODE_PRIVATE);
		tv_name= (TextView)findViewById(R.id.text_main_recepie);
		
		btn_door = (ImageView) findViewById(R.id.btn_door);
		btn_settings = (ImageView) findViewById(R.id.btn_setting);
		//btn_logo = (image) findViewById(R.id.btn_logo);
		
		linear_dial = (LinearLayout) findViewById(R.id.linear_dial);
		real_alarm = (Button) findViewById(R.id.real_alarm);
		ll_warning = (LinearLayout)findViewById(R.id.ll_main_warning);
		ll_recepie_name= (LinearLayout)findViewById(R.id.ll_recepie_name);
		uartFD = uartOpen(4, 9600);												//SERIAL DATA INITIATING FOR SENSOR DATA
		if(uartFD!=-1)															//CHECKING IF OPENED
		{
			Log.e(PACKT_TAG, "UART OPENED");
		}
		
		if(openGPIO()== false){
			Log.e("Open","OpenGPIO");
		}

		tv_name.setText(sharedpreferences.getString("recepie_id", "Default"));

		timer0 = new Timer();
        timer0.scheduleAtFixedRate(new TaskExampleRepeating0(), 250, 1000);
        
        timer1 = new Timer();
        timer1.scheduleAtFixedRate(new TaskExampleRepeating1(), 500, 1000);
        
        timer2 = new Timer();
        timer2.scheduleAtFixedRate(new TaskExampleRepeating2(), 750, 1000);
        
        timer3 = new Timer();
        timer3.scheduleAtFixedRate(new TaskExampleRepeating3(), 1000, 1000);			
        
		linear_dial.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog = new Dialog(MainActivity.this);
				dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
				dialog.setContentView(R.layout.dialog_keypad);

				one = (Button)dialog.findViewById(R.id.one);
				two = (Button)dialog.findViewById(R.id.two);
				three = (Button)dialog.findViewById(R.id.three);
				four = (Button)dialog.findViewById(R.id.four);
				five = (Button)dialog.findViewById(R.id.five);
				six = (Button)dialog.findViewById(R.id.six);
				seven = (Button)dialog.findViewById(R.id.seven);
				eight = (Button)dialog.findViewById(R.id.eight);
				nine = (Button)dialog.findViewById(R.id.nine);
				zero = (Button)dialog.findViewById(R.id.zero);
				ce = (Button)dialog.findViewById(R.id.ce);
				del = (Button)dialog.findViewById(R.id.del);
				disp = (EditText)dialog.findViewById(R.id.display);
				dial = (Button)dialog.findViewById(R.id.dial);
				cancel = (Button)dialog.findViewById(R.id.cancel);
				
				dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
				disp.setEnabled(false);
				
				disp.addTextChangedListener(new TextWatcher() {
		            @Override
		            public void onTextChanged(CharSequence s, int start, int before, int count) {
		            	System.out.println("=="+count);
		            	if(count == 2){
			            	Handler handler = new Handler(); 
						    handler.postDelayed(new Runnable() { 
						         public void run() { 
						        	 disp.setText("");
						         }
						    }, 1000); 
		            	}
		            }
		            @Override
		            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
		            }

		            @Override
		            public void afterTextChanged(Editable s) {
		            }
		        });
				one.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						System.out.println("onsystemoutput one");
						icone(1);
						
		        		//writeGPIO(9, 41, 1);
					    
		        		//Handler handler = new Handler(); 
					   // handler.postDelayed(new Runnable() { 
					  //       public void run() { 
					  //      		writeGPIO(9, 41, 0);
					  //       } 
					  //  }, 50); 
						Editable str = disp.getText();
						if (op2 != 0) {
							op2 = 0;
							disp.setText("");
						}
						str = str.append(one.getText());
						disp.setText(str);
					}
				});
				two.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						System.out.println("onsystemoutput two");
						icone(2);
						
		        		//writeGPIO(9, 42, 1);

					    //Handler handler = new Handler(); 
					    //handler.postDelayed(new Runnable() { 
					    //     public void run() { 
					  //      		writeGPIO(9, 42, 0);
					   //      } 
					   // }, 50); 
					    
						Editable str = disp.getText();
						if (op2 != 0) {
							op2 = 0;
							disp.setText("");
						}
						str = str.append(two.getText());
						disp.setText(str);
					}
				});
				three.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						System.out.println("onsystemoutput three");
						icone(3);
						Editable str = disp.getText();
						if (op2 != 0) {
							op2 = 0;
							disp.setText("");
						}
						str = str.append(three.getText());
						disp.setText(str);
					}
				});
				four.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						System.out.println("onsystemoutput four");
						icone(4);
						Editable str = disp.getText();
						if (op2 != 0) {
							op2 = 0;
							disp.setText("");
						}
						str = str.append(four.getText());
						disp.setText(str);
					}
				});
				five.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						System.out.println("onsystemoutput three");
						icone(5);
						Editable str = disp.getText();
						if (op2 != 0) {
							op2 = 0;
							disp.setText("");
						}
						str = str.append(five.getText());
						disp.setText(str);
						
					}
				});
				six.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						System.out.println("onsystemoutput three");
						icone(6);
						Editable str = disp.getText();
						if (op2 != 0) {
							op2 = 0;
							disp.setText("");
						}
						str = str.append(six.getText());
						disp.setText(str);
					}
				});
				seven.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						System.out.println("onsystemoutput three");
						icone(7);
						Editable str = disp.getText();
						if (op2 != 0) {
							op2 = 0;
							disp.setText("");
						}
						str = str.append(seven.getText());
						disp.setText(str);
					}
				});
				eight.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						System.out.println("onsystemoutput three");
						icone(8);
						Editable str = disp.getText();
						if (op2 != 0) {
							op2 = 0;
							disp.setText("");
						}
						str = str.append(eight.getText());
						disp.setText(str);
					}
				});
				nine.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						System.out.println("onsystemoutput three");
						icone(9);
						Editable str = disp.getText();
						if (op2 != 0) {
							op2 = 0;
							disp.setText("");
						}
						str = str.append(nine.getText());
						disp.setText(str);
					}
				});
				zero.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						System.out.println("onsystemoutput three");
						icone(0);
						Editable str = disp.getText();
						if (op2 != 0) {
							op2 = 0;
							disp.setText("");
						}
						str = str.append(zero.getText());
						disp.setText(str);
					}
				});
				ce.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						icone(10);
						disp.setText("");
					}
				});
				del.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						icone(11);
						if (disp.length() > 0) {
							disp.setText(disp.getText().toString().substring(0, disp.getText().length()-1));
						}
					}
				});
				dial.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						Toast.makeText(getApplicationContext(), "Dialing...", Toast.LENGTH_LONG).show();

					}
				});
				cancel.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						dialog.dismiss();
					}
				});
				dialog.show();
			}
		});
		
		btn_door.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				
			}
		});
		
		btn_settings.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				final Dialog dialog_setting = new Dialog(MainActivity.this);
				dialog_setting.requestWindowFeature(Window.FEATURE_NO_TITLE);
				dialog_setting.setContentView(R.layout.dialog_theme);
		        LinearLayout ll_classic = (LinearLayout)dialog_setting.findViewById(R.id.ll_classic);
		        LinearLayout ll_simple = (LinearLayout)dialog_setting.findViewById(R.id.ll_simple);
		        LinearLayout ll_standard = (LinearLayout)dialog_setting.findViewById(R.id.ll_standard);
		        final LinearLayout linear_color_theme = (LinearLayout)dialog_setting.findViewById(R.id.linear_color_theme);
				Button btn_ok = (Button)dialog_setting.findViewById(R.id.btn_ok_theme);
		        
		        ll_classic.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						linear_color_theme.setBackgroundResource(R.color.blue_cream);
					}
				});
		        ll_simple.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						linear_color_theme.setBackgroundColor(R.color.black);
					}
				});
		        ll_standard.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						linear_color_theme.setBackgroundColor(R.color.gray);
					}
				});
		        btn_ok.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						dialog_setting.dismiss();						
					}
				});
		        dialog_setting.show();	
			}
		});
		
		
		sur_alarm.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Calendar mcurrentTime = Calendar.getInstance();
	            int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
	            int minute = mcurrentTime.get(Calendar.MINUTE);
	            TimePickerDialog mTimePicker;
	            mTimePicker = new TimePickerDialog(MainActivity.this, new TimePickerDialog.OnTimeSetListener() {
	                @Override
	                public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
	                    //eReminderTime.setText( selectedHour + ":" + selectedMinute);
	                }
	            }, hour, minute, true);//Yes 24 hour time
	            mTimePicker.setTitle("Select Alarm Time for Surgery");
	            mTimePicker.show();
			}
		});
		
		ane_alarm.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				/*if(flag_warning){
					flag_warning = false;
					writeGPIO(8, 16, 1);
				}else{
					flag_warning = true;
					writeGPIO(8, 16, 0);
				}*/
				 // TODO Auto-generated method stub
	            Calendar mcurrentTime = Calendar.getInstance();
	            int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
	            int minute = mcurrentTime.get(Calendar.MINUTE);
	            TimePickerDialog mTimePicker;
	            mTimePicker = new TimePickerDialog(MainActivity.this, new TimePickerDialog.OnTimeSetListener() {
	                @Override
	                public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
	                    //eReminderTime.setText( selectedHour + ":" + selectedMinute);
	                }
	            }, hour, minute, true);//Yes 24 hour time
	            mTimePicker.setTitle("Select Alarm Time for Anesthesia");
	            mTimePicker.show();
			}
		});
		
		txt_pre.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				 	timer0 = new Timer();
			        timer0.scheduleAtFixedRate(new TaskExampleRepeating0(), 250, 1000);
			        
			        timer1 = new Timer();
			        timer1.scheduleAtFixedRate(new TaskExampleRepeating1(), 500, 1000);
			        
			        timer2 = new Timer();
			        timer2.scheduleAtFixedRate(new TaskExampleRepeating2(), 750, 1000);
			        
			        timer3 = new Timer();
			        timer3.scheduleAtFixedRate(new TaskExampleRepeating3(), 1000, 1000);				
			}
		});
		sur_stop.setOnClickListener(new View.OnClickListener() {

			public void onClick(View view) {
		        
				if (sur_stop.getText().equals("START")) {
					sur_startTime = SystemClock.uptimeMillis();
					sur_customHandler.postDelayed(sur_updateTimerThread, 0);
					sur_stop.setText("STOP");
				} else {
					sur_startTime = 0L;
					sur_timeSwapBuff += sur_timeInMilliseconds;
					sur_customHandler.removeCallbacks(sur_updateTimerThread);
					sur_stop.setText("START");
				}
			}
		});

		sur_reset.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {			
				//String status = readGPIO(9, 13) == true ? "ON" : "OFF";
				//sur_reset.setText("Button =" + status);
				
				sur_startTime = 0L;
				sur_timeSwapBuff = 0L;
				sur_updatedTime = 0L;
				sur_customHandler.removeCallbacks(sur_updateTimerThread);
				sur_stop.setText("START");
				sur_timerValue.setText("00:00:00");
			}
		});

		ane_stop.setOnClickListener(new View.OnClickListener() {

			public void onClick(View view) {
				if (ane_stop.getText().equals("START")) {
					ane_startTime = SystemClock.uptimeMillis();
					ane_customHandler.postDelayed(ane_updateTimerThread, 0);
					ane_stop.setText("STOP");
				} else {
					ane_startTime = 0L;
					ane_timeSwapBuff += ane_timeInMilliseconds;
					ane_customHandler.removeCallbacks(ane_updateTimerThread);
					ane_stop.setText("START");
				}
			}
		});

		ane_reset.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				ane_startTime = 0L;
				ane_timeSwapBuff = 0L;
				ane_updatedTime = 0L;
				ane_customHandler.removeCallbacks(ane_updateTimerThread);
				ane_stop.setText("START");
				ane_timerValue.setText("00:00:00");
			}
		});
		
		ll_warning.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				if(flag_warning){
					flag_warning = false;
					writeGPIO(8, 16, 1);
				}else{
					flag_warning = true;
					writeGPIO(8, 16, 0);
				}
				blinkText();
			}
		});
		ll_recepie_name.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				
				dialog_recepie = new Dialog(MainActivity.this);
				dialog_recepie.requestWindowFeature(Window.FEATURE_NO_TITLE);
				dialog_recepie.setContentView(R.layout.dialog_recepie_name);
		        listView = (ListView)dialog_recepie.findViewById(R.id.list);

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
						   tv_name.setText(itemValue);
						   dialog_recepie.dismiss();

		             }
		        }); 
				dialog_recepie.show();
			}
		});
		 
		 
		try {
			btn_show_one.setOnClickListener(this);
			btn_show_two.setOnClickListener(this);
			btn_show_three.setOnClickListener(this);
			btn_show_four.setOnClickListener(this);
			btn_show_five.setOnClickListener(this);
			btn_show_six.setOnClickListener(this);
			btn_show_seven.setOnClickListener(this);
			btn_show_eight.setOnClickListener(this);
			btn_dome1_up.setOnClickListener(this);
			btn_dome1_down.setOnClickListener(this);
			btn_dome2_up.setOnClickListener(this);
			btn_dome2_down.setOnClickListener(this);
			btn_light1_up.setOnClickListener(this);
			btn_light2_up.setOnClickListener(this);
			btn_ac_up.setOnClickListener(this);
			btn_ac_down.setOnClickListener(this);
			btn_xray1_up.setOnClickListener(this);
			btn_xray1_down.setOnClickListener(this);
			btn_xray2_up.setOnClickListener(this);
			btn_xray2_down.setOnClickListener(this);
		} catch (Exception e) {
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	private Runnable sur_updateTimerThread = new Runnable() {

		public void run() {

			sur_timeInMilliseconds = SystemClock.uptimeMillis() - sur_startTime;
			sur_updatedTime = sur_timeSwapBuff + sur_timeInMilliseconds;
			int secs = (int) (sur_updatedTime / 1000);
			int mins = secs / 60;
			int hour = mins / 60;
			secs = secs % 60;
			sur_timerValue.setText("" + String.format("%02d", hour) + ":"
					+ "" + String.format("%02d", mins) + ":"
					+ String.format("%02d", secs));
			sur_customHandler.postDelayed(this, 0);
		}
	};

	private Runnable ane_updateTimerThread = new Runnable() {

		public void run() {

			ane_timeInMilliseconds = SystemClock.uptimeMillis() - ane_startTime;
			ane_updatedTime = ane_timeSwapBuff + ane_timeInMilliseconds;
			int secs = (int) (ane_updatedTime / 1000);
			int mins = secs / 60;
			int hour = mins / 60;
			secs = secs % 60;
			ane_timerValue.setText("" + String.format("%02d", hour) + ":"
					+ "" + String.format("%02d", mins) + ":"
					+ String.format("%02d", secs));
			ane_customHandler.postDelayed(this, 0);
		}

	};
	
	class TaskExampleRepeating0 extends TimerTask{
	    public void run(){
	    	Log.i("Thread","Thread has been started 0");
	    	getData0();
	    }
	}
	class TaskExampleRepeating1 extends TimerTask{
	    public void run(){
	    	Log.i("Thread","Thread has been started 1");
	    	getData1();
	    }
	}
	class TaskExampleRepeating2 extends TimerTask{
	    public void run(){
	    	Log.i("Thread","Thread has been started 2");
	    	getData2();
	    }
	}
	class TaskExampleRepeating3 extends TimerTask{
	    public void run(){
	    	Log.i("Thread","Thread has been started 3");
	    	getData3();
	    }
	}
	
	private void getData0() {
          background0 = new Thread(new Runnable() {
              public void run() {
                  try {
                  	int val0 = reads(0);
					Log.i("DoinBg","=0="+val0);
                    threadMsg0(Integer.toString(val0));
                  } catch (Throwable t) {
                      Log.i("Animation", "Thread  exception " + t);
                  }
              }

              private void threadMsg0(String msg) {

                  if (!msg.equals(null) && !msg.equals("")) {
                      Message msgObj = handler0.obtainMessage();
                      Bundle b = new Bundle();
                      b.putString("message0", msg);
                      msgObj.setData(b);
                      handler0.sendMessage(msgObj);
                  }
              }
              private final Handler handler0 = new Handler(Looper.getMainLooper()) {

                  public void handleMessage(Message msg) {
                      String aResponse0 = msg.getData().getString("message0");
                      if ((null != aResponse0)) {
                        btn_show_one.setText("O2 = "+aResponse0);
                      }
                  }
              };
          });
          // Start Thread
          background0.start();
	}
	private void getData1() {
          background1 = new Thread(new Runnable() {
               
              public void run() {
                  try {
					int val1 = reads(1);
					Log.i("DoinBg","=1="+val1);
                    threadMsg1(Integer.toString(val1));

                  } catch (Throwable t) {
                      Log.i("Animation", "Thread  exception " + t);
                  }
              }

              private void threadMsg1(String msg) {

                  if (!msg.equals(null) && !msg.equals("")) {
                      Message msgObj = handler1.obtainMessage();
                      Bundle b = new Bundle();
                      b.putString("message1", msg);
                      msgObj.setData(b);
                      handler1.sendMessage(msgObj);
                  }
              }
             
              private final Handler handler1 = new Handler(Looper.getMainLooper()) {

                  public void handleMessage(Message msg) {
                      String aResponse1 = msg.getData().getString("message1");
                      if ((null != aResponse1)) {
      				  	btn_show_two.setText("N2O = "+aResponse1);
                      }
                  }
              };

          });
          // Start Thread
          background1.start();
	}
	private void getData2() {
          background2 = new Thread(new Runnable() {
		    public void run() {
		        try {
					int val2 = reads(2);
					Log.i("DoinBg","=2="+val2);	
					threadMsg2(Integer.toString(val2));
		
		        } catch (Throwable t) {
		            Log.i("Animation", "Thread  exception " + t);
		        }
		    }
		
		    private void threadMsg2(String msg) {
		
		        if (!msg.equals(null) && !msg.equals("")) {
		            Message msgObj = handler2.obtainMessage();
		            Bundle b = new Bundle();
		            b.putString("message2", msg);
		            msgObj.setData(b);
		            handler2.sendMessage(msgObj);
		        }
		    }
		  
		    private final Handler handler2 = new Handler(Looper.getMainLooper()) {
		
		        public void handleMessage(Message msg) {
		            String aResponse2 = msg.getData().getString("message2");
		            if ((null != aResponse2)) {
					  	btn_show_three.setText("AIR = "+aResponse2);
		            }
		        }
		    };
		
		});
		background2.start();
	}
	private void getData3() {
		background3 = new Thread(new Runnable() {
		     
		    public void run() {
		        try {
					int val3 = reads(3);
					Log.i("DoinBg","=3="+val3);	
		          threadMsg3(Integer.toString(val3));
		
		        } catch (Throwable t) {
		            Log.i("Animation", "Thread  exception " + t);
		        }
		    }
		
		    private void threadMsg3(String msg) {
		
		        if (!msg.equals(null) && !msg.equals("")) {
		            Message msgObj = handler3.obtainMessage();
		            Bundle b = new Bundle();
		            b.putString("message3", msg);
		            msgObj.setData(b);
		            handler3.sendMessage(msgObj);
		        }
		    }
		
		    private final Handler handler3 = new Handler(Looper.getMainLooper()) {

		        public void handleMessage(Message msg) {
		             
		            String aResponse3 = msg.getData().getString("message3");
		
		            if ((null != aResponse3)) {
		
					  	btn_show_four.setText("VAC = "+aResponse3);
		            }
		        }
		    };
		
		});
		// Start Thread
		background3.start();
    }
	
	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
			case R.id.btn_show_one:
			   	//System.out.println("btn_o2_main"+val);
			   	int sens = reads(3);
			   	System.out.println("btn_o2_main"+sens);
			   	btn_show_one.setText(sens+"");
			   	int sens1 = reads(2);
			   	System.out.println("btn_o2_main"+sens1);
			   	btn_show_two.setText(sens1+"");
			   	int sens2 = reads(1);
			   	System.out.println("btn_o2_main"+sens2);
			   	btn_show_three.setText(sens2+"");
			   	int sens3 = reads(0);
			   	System.out.println("btn_o2_main"+sens3);
			   	btn_show_four.setText(sens3+"");
				break;
			case R.id.btn_show_two:
				break;
			case R.id.btn_show_three:
				break;
			case R.id.btn_show_four:
				break;
			case R.id.btn_show_five:
				break;
			case R.id.btn_show_six:
				break;
			case R.id.btn_show_seven:
				break;
			case R.id.btn_show_eight:
				break;
			case R.id.btn_dome1_up:
						writeGPIO(9, 11, 1);
				break;
			case R.id.btn_dome1_down:
						writeGPIO(9, 11, 0);
				break;
			case R.id.btn_dome2_up:
						writeGPIO(9, 12, 1);
				break;
			case R.id.btn_dome2_down:
						writeGPIO(9, 12, 0);
				break;
			case R.id.btn_light1_up:
						writeGPIO(9, 13, 1);
				break;
			//case R.id.btn_light1_down:
				//		writeGPIO(9, 13, 0);
				//break;
			case R.id.btn_light2_up:
						writeGPIO(9, 14, 1);
				break;
			//case R.id.btn_light2_down:
				//		writeGPIO(9, 14, 0);
				//break;
			case R.id.btn_ac_up:
				break;
			case R.id.btn_ac_down:
				break;
			case R.id.btn_xray1_up:
					writeGPIO(9, 15, 1);
				break;
			case R.id.btn_xray1_down:
					writeGPIO(9, 15, 0);
				break;
			case R.id.btn_xray2_up:
						writeGPIO(9, 16, 1);
				break;
			case R.id.btn_xray2_down:
					writeGPIO(9, 16, 0);
				break;
			/*case R.id.btn_door:
				break;
			case R.id.btn_settings:
				break;
			case R.id.btn_logo:
				break;*/
		}
	}
	
	int i = 0;
	private void blinkText(){
        final Handler handler = new Handler();
        new Thread(new Runnable() {
            @Override
            public void run() {
            int timeToBlink = 1000;    //in ms
            try{
                Thread.sleep(timeToBlink);
            }catch (Exception e) {
                 
            }
            handler.post(new Runnable() {
                @Override
                    public void run() {
                	if(i%2==0){
                		ll_warning.setBackgroundResource(R.color.blue);
                	}else{
                		ll_warning.setBackgroundResource(R.color.red);
                	}
                	i++;
                    blinkText();
                }
                });
            }}).start();
   }
     
	@Override
	protected void onStop() {
	    Log.w("Wave1", "App stopped");
	    if(timer0 != null) {
	    	timer0.cancel();
	    	timer0 = null;
	    	background0.isAlive();
	     }
	    if(timer1 != null) {
	    	timer1.cancel();
	    	timer1 = null;
		    background1.interrupt();
	     }
	    if(timer2 != null) {
	    	timer2.cancel();
	    	timer2 = null;
		    background2.interrupt();
	     }
	    if(timer3 != null) {
	    	timer3.cancel();
	    	timer3 = null;
		    background3.interrupt();
	     }
	    super.onStop();
	}

	@Override
	protected void onDestroy() {
	    Log.w("Wave1", "App destoryed");
	    super.onDestroy();
	}
}