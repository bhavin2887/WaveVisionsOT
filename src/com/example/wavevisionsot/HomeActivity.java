package com.example.wavevisionsot;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.CompoundButton;

public class HomeActivity extends Activity {

	LinearLayout ll_pressure, ll_recepie, ll_main, ll_time, ll_ot;
	LinearLayout ll_temp, ll_lights, ll_humidity, ll_hospital, ll_xray;
	SharedPreferences sharedpreferences;	
	public static String MYPREFERENCE = "MyPreference";
	// custom dialog
	Dialog dialog;
	TextView txt_ox_low,txt_ox_high;
	RadioButton one;
	RadioButton two;
	RadioButton three;
	int sec,min,hour,date,mon,year;  				// RTC variables
	private native int rtcyear();					//RTC Function prototype starts
	private native int rtcmonth();
	private native int rtcdate();
	private native int rtchour();
	private native int rtcmin();
	private native int rtcsec();				//RTC Function prototype ends
	Dialog dialog_ot_light,dialog_light,dialog_xray,dialog_ot_hospital,dialog_humi,dialog_temp;
	
	static {
		System.loadLibrary("BBBAndroidHAL");									// LIBRARY NAME CHANGED
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home);
		sharedpreferences = getSharedPreferences(MYPREFERENCE, Context.MODE_PRIVATE);
		Log.i("====", "============================================");
		ll_pressure = (LinearLayout) findViewById(R.id.img_pressure);
		ll_recepie = (LinearLayout) findViewById(R.id.img_recepie);
		ll_main = (LinearLayout) findViewById(R.id.img_main);
		ll_ot = (LinearLayout) findViewById(R.id.img_ot_light);
		ll_lights = (LinearLayout) findViewById(R.id.img_light);
		ll_xray = (LinearLayout) findViewById(R.id.img_xray);
		
		ll_time = (LinearLayout) findViewById(R.id.img_time);
		ll_temp = (LinearLayout) findViewById(R.id.img_temp);
		ll_humidity = (LinearLayout) findViewById(R.id.img_humidity);
		ll_hospital = (LinearLayout) findViewById(R.id.img_hospital);
		
		/*try {
			
		    Class<?> execClass = Class.forName("android.os.Exec");
		    Method createSubprocess = execClass.getMethod("createSubprocess", String.class, String.class, String.class, int[].class);
		    int[] pid = new int[1];
		    FileDescriptor fd = (FileDescriptor)createSubprocess.invoke(null, "/system/bin/ls", "/", null, pid);

		    BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(fd)));
		    String output = "";
		    try {
		        String line;
		        while ((line = reader.readLine()) != null) {
		            output += line + "\n";
		        }
		    }
		    catch (IOException e) {
	            Log.d("===","error=="+e.toString());
	            e.printStackTrace();
		    }finally{
	            Log.i("", "output = "+output);
		    }
		    
			
			Log.i("", "Try1");
            Process process = Runtime.getRuntime().exec("su");
            Log.i("", "Try2");
            DataOutputStream os = new DataOutputStream(process.getOutputStream());
            Log.i("", "Try3");
            os.writeBytes("date -s 20120419.024012; \n");
            Log.i("", "Try4");
            os.flush();
            Log.i("", "Try5");
            os.writeBytes("exit\n");
            Log.i("", "Try6");
            os.flush();
            Log.i("", "Try7");
            process.waitFor();
            Log.i("", "Try8");*/

            /*Log.i("", "Try1");
    		Calendar c = Calendar.getInstance();
            Log.i("", "Try2");
    		c.set(2014,11,11,11,11,11);
            Log.i("", "Try3");
    		AlarmManager am = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);
            Log.i("", "Try4");
    		am.setTime(c.getTimeInMillis());
            Log.i("", "Try5");

        } catch (Exception e) {
            Log.d("===","error=="+e.toString());
            e.printStackTrace();
        }*/
				
		/*year=rtcyear();					//RTC code starts
		mon=rtcmonth();
		date=rtcdate();
		hour=rtchour();
		min=rtcmin();
		sec=rtcsec();*/

		//Calendar c = Calendar.getInstance();
		//c.set(2014,11,11,11,11,11);
		//AlarmManager am = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
		//am.setTime(c.getTimeInMillis());					//RTC code ends
		//startActivity(new Intent(android.provider.Settings.ACTION_DATE_SETTINGS));
		
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
		String currentDateandTime = sdf.format(new Date());
		Log.i("====", "====="+currentDateandTime);

		ll_pressure.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				
				dialog = new Dialog(HomeActivity.this);
				dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
				dialog.setContentView(R.layout.dialog_pressure);
				
				Button pressure_one = (Button) dialog.findViewById(R.id.btn_ox_low_up);
				Button pressure_two = (Button) dialog.findViewById(R.id.btn_ox_low_down);
				Button pressure_three = (Button) dialog.findViewById(R.id.btn_ox_hi_up);
				Button pressure_four = (Button) dialog.findViewById(R.id.btn_ox_hi_down);
				RadioGroup op_radioGroup=(RadioGroup)dialog.findViewById(R.id.radioGroup1);

				one = (RadioButton)dialog.findViewById(R.id.radio0);
				two = (RadioButton)dialog.findViewById(R.id.radio1);
				three = (RadioButton)dialog.findViewById(R.id.radio2);
				
				Button ok = (Button) dialog.findViewById(R.id.btn_ok_pressure);
				Button cancel = (Button) dialog.findViewById(R.id.btn_cancel_pressure);

				txt_ox_low = (TextView)dialog.findViewById(R.id.txt_ox_low);
				txt_ox_high = (TextView)dialog.findViewById(R.id.txt_ox_high);
				txt_ox_low.setText(sharedpreferences.getString("op_radio_low", "30 PSI"));
				txt_ox_high.setText(sharedpreferences.getString("op_radio_high", "50 PSI"));
				
				op_radioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {
					@Override
					public void onCheckedChanged(RadioGroup group, int checkedId) {
						new oxygen_level().execute(group);
					}
				});
				
				ok.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
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
						//String val = txt_ox_low.getText().toString();
						if(txt_ox_low.getText().toString().contains("PSI")){
							float val = Float.parseFloat(txt_ox_low.getText().toString().substring(0, txt_ox_low.length()-4));
							val+=1;
							txt_ox_low.setText(val+ " PSI");
						}else if(txt_ox_low.getText().toString().contains("BAR")){
							float val = Float.parseFloat(txt_ox_low.getText().toString().substring(0, txt_ox_low.length()-4));
							val+=1;
							txt_ox_low.setText(val+ " BAR");
						}else{
							float val = Float.parseFloat(txt_ox_low.getText().toString().substring(0, txt_ox_low.length()-5));
							val+=1;
							txt_ox_low.setText(val+ " mmHg");
						}
					}
				});
				pressure_two.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						if(txt_ox_low.getText().toString().contains("PSI")){
							float val = Float.parseFloat(txt_ox_low.getText().toString().substring(0, txt_ox_low.length()-4));
							val-=1;
							txt_ox_low.setText(val+ " PSI");
						}else if(txt_ox_low.getText().toString().contains("BAR")){
							float val = Float.parseFloat(txt_ox_low.getText().toString().substring(0, txt_ox_low.length()-4));
							val-=1;
							txt_ox_low.setText(val+ " BAR");
						}else{
							float val = Float.parseFloat(txt_ox_low.getText().toString().substring(0, txt_ox_low.length()-5));
							val-=1;
							txt_ox_low.setText(val+ " mmHg");
						}
					}
				});
				pressure_three.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						if(txt_ox_high.getText().toString().contains("PSI")){
							float val = Float.parseFloat(txt_ox_high.getText().toString().substring(0, txt_ox_high.length()-4));
							val+=1;
							txt_ox_high.setText(val+ " PSI");
						}else if(txt_ox_high.getText().toString().contains("BAR")){
							float val = Float.parseFloat(txt_ox_high.getText().toString().substring(0, txt_ox_high.length()-4));
							val+=1;
							txt_ox_high.setText(val+ " BAR");
						}else{
							float val = Float.parseFloat(txt_ox_high.getText().toString().substring(0, txt_ox_high.length()-5));
							val+=1;
							txt_ox_high.setText(val+ " mmHg");
						}
					}
				});
				pressure_four.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						if(txt_ox_high.getText().toString().contains("PSI")){
							float val = Float.parseFloat(txt_ox_high.getText().toString().substring(0, txt_ox_high.length()-4));
							val-=1;
							txt_ox_high.setText(val+ " PSI");
						}else if(txt_ox_high.getText().toString().contains("BAR")){
							float val = Float.parseFloat(txt_ox_high.getText().toString().substring(0, txt_ox_high.length()-4));
							val-=1;
							txt_ox_high.setText(val+ " BAR");
						}else{
							float val = Float.parseFloat(txt_ox_high.getText().toString().substring(0, txt_ox_high.length()-5));
							val-=1;
							txt_ox_high.setText(val+ " mmHg");
						}
					}
				});
				
				if(sharedpreferences.getInt("op_radio", 0) == 1){
					   one.setChecked(true);
				}else if(sharedpreferences.getInt("op_radio", 0) == 2){
						two.setChecked(true);
				}else{
						three.setChecked(true);	
				}
				
				dialog.show();
			
			}
		});
		
		ll_main.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent ite = new Intent(getApplicationContext(),MainActivity.class);
				startActivity(ite);
			}
		});
		
		ll_time.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(new Intent(android.provider.Settings.ACTION_DATE_SETTINGS));
			}
		});
		
		ll_temp.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog_temp = new Dialog(HomeActivity.this);
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
		
		ll_humidity.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				
				dialog_humi = new Dialog(HomeActivity.this);
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
		
		ll_hospital.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog_ot_hospital = new Dialog(HomeActivity.this);
				dialog_ot_hospital.requestWindowFeature(Window.FEATURE_NO_TITLE);
				dialog_ot_hospital.setContentView(R.layout.dialog_hospital);
		
				Button btn_ok = (Button)dialog_ot_hospital.findViewById(R.id.btn_ok_hospital);
				btn_ok.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						dialog_ot_hospital.dismiss();
					}
				});
				dialog_ot_hospital.show();
			}
		});
		
		ll_ot.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog_ot_light = new Dialog(HomeActivity.this);
				dialog_ot_light.requestWindowFeature(Window.FEATURE_NO_TITLE);
				dialog_ot_light.setContentView(R.layout.dialog_ot);
		
				Switch one = (Switch)dialog_ot_light.findViewById(R.id.switch1_ot_light);
				Switch two = (Switch)dialog_ot_light.findViewById(R.id.switch2_ot_light);
				Button btn_ok = (Button)dialog_ot_light.findViewById(R.id.btn_ok_ot);

				dialog_ot_light.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
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
						dialog_ot_light.dismiss();
					}
				});
				dialog_ot_light.show();

			}
		});
		
		ll_lights.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog_light = new Dialog(HomeActivity.this);
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
		
		ll_xray.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog_xray = new Dialog(HomeActivity.this);
				dialog_xray.requestWindowFeature(Window.FEATURE_NO_TITLE);
				dialog_xray.setContentView(R.layout.dialog_xray);
		
				Switch one = (Switch)dialog_xray.findViewById(R.id.switch1_xray);
				Switch two = (Switch)dialog_xray.findViewById(R.id.switch2_xray);
				Button btn_ok = (Button)dialog_xray.findViewById(R.id.btn_ok_xray);

				dialog_xray.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
				
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
						dialog_xray.dismiss();
					}
				});
				dialog_xray.show();

			}
		});
		
		ll_recepie.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				
				Intent ite = new Intent(getApplicationContext(),RecepieActivity.class);
				startActivity(ite);
				
				//new setTime().execute();

				//new getSound().execute();
				
				/*MediaPlayer mPlayer2;
				mPlayer2= MediaPlayer.create(getApplicationContext(), R.raw.winner_sound);
				mPlayer2.start();*/
				        
				//startActivity(new Intent(android.provider.Settings.ACTION_DATE_SETTINGS));
/*
				MediaPlayer mp;
	            mp = MediaPlayer.create(getApplicationContext(), R.raw.winner_sound);
	            mp.setOnCompletionListener(new OnCompletionListener() {

	                @Override
	                public void onCompletion(MediaPlayer mp) {
	                    // TODO Auto-generated method stub
	                    mp.reset();
	                    mp.release();
	                    mp=null;
	                }

	            });
	            mp.start();
	            
	            /*
                try {
                    if (mp.isPlaying()) {
                        mp.stop();
                        mp.release();
                        mp = MediaPlayer.create(context, R.raw.sound);
                    } mp.start();
                } catch(Exception e) { e.printStackTrace(); }*/
			}
		});
	}
	
	private class getSound extends AsyncTask<String, String, String> {
		  Integer val[]= new Integer[10];
		  @Override
		  protected void onPreExecute() {
			  Log.i("onPreExe","==");
		  }

		  @Override
		  protected String doInBackground(String... group) {
		   	System.out.println("doInBackground1 GetMethod");
		   	
			AudioPlayer ap = new AudioPlayer("winner_sound.mp3", getApplicationContext());
			ap.playAudio();
			
		   return "0";
		  }
		  
		  @Override
		  protected void onPostExecute(String val) {
			  try {
				  	//TODO Keep all the UI Changes over Here after you get any data
			        System.out.println("IntoPostExecute");
			   } catch (Exception e) {
			    e.printStackTrace();
			   // resp = e.getMessage();
			   }
		  }
		 }	
	
	private class setTime extends AsyncTask<String, String, String> {
		  Integer val[]= new Integer[10];
		  @Override
		  protected void onPreExecute() {
			  Log.i("onPreExe","==");
		  }

		  @Override
		  protected String doInBackground(String... group) {
		   	System.out.println("setTime setTime");
		   	
		   	/*try {
	            Process process = Runtime.getRuntime().exec("su");
	            DataOutputStream os = new DataOutputStream(process.getOutputStream());
	            os.writeBytes("date -s 20160419.024012; \n");
	    		Log.i("====1","===== SET DATE");
	        } catch (Exception e) {
	            Log.d("ERROR","error=="+e.toString());
	            e.printStackTrace();
	        }*/
		   	
		   	Calendar c = Calendar.getInstance();
			c.set(2014,11,11,11,11,11);
			AlarmManager am = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);
			am.setTime(c.getTimeInMillis());					//RTC code ends
			
		   return "0";
		  }
		  
		  @Override
		  protected void onPostExecute(String val) {
			  try {
				  	//TODO Keep all the UI Changes over Here after you get any data
			        System.out.println("setTime");
			   } catch (Exception e) {
			    e.printStackTrace();
			   // resp = e.getMessage();
			   }
		  }
		 }
	
	
	private class oxygen_level extends AsyncTask<RadioGroup, String, Integer> {

		//  private String resp;
		  private ProgressDialog dialog_prog;
		  @Override
		  protected void onPreExecute() {
			  dialog_prog = new ProgressDialog(HomeActivity.this);
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
							float myVal = PsiToBar(Float.parseFloat(txt_ox_low_string));
							float myVal2 = PsiToBar(Float.parseFloat(txt_ox_high_string));
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
							float myVal = PsiTommHg(Float.parseFloat(txt_ox_low_string));
							float myVal2 = PsiTommHg(Float.parseFloat(txt_ox_high_string));
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
							float myVal = BarToPsi(Float.parseFloat(txt_ox_low_string));
							float myVal2 = BarToPsi(Float.parseFloat(txt_ox_high_string));
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
							float myVal = BarTommHg(Float.parseFloat(txt_ox_low_string));
							float myVal2 = BarTommHg(Float.parseFloat(txt_ox_high_string));
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
							float myVal = mmHgToPsi(Float.parseFloat(txt_ox_low_string));
							float myVal2 = mmHgToPsi(Float.parseFloat(txt_ox_high_string));
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
							float myVal = mmHgToBar(Float.parseFloat(txt_ox_low_string));
							float myVal2 = mmHgToBar(Float.parseFloat(txt_ox_high_string));
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
	
	private float BarToPsi(float txt_ox_low) {
		float myval = (float) (txt_ox_low * 0.0689);
		return (int) Math.round(myval);
	}
	
	private float PsiToBar(float txt_ox_low) {
		
		float myval = (float) (txt_ox_low * 14.5037);
		String.format("%.2f", myval);
		return myval;
	}
	
	private float PsiTommHg(float txt_ox_low) {
		
		float myval = (float) (txt_ox_low * 51.7149);
		String.format("%.2f", myval);
		return myval;
	}
	
	private float mmHgToPsi(float txt_ox_low) {
		
		float myval = (float) (txt_ox_low * 0.0193);
		return (int) Math.round(myval);
	}
	
	private float BarTommHg(float txt_ox_low) {
		
		float myval = (float) (txt_ox_low * 750.0616);
		String.format("%.2f", myval);
		return myval;
	}
	
	private float mmHgToBar(float txt_ox_low) {
		
		float myval = (float) (txt_ox_low * 0.0013);
		String.format("%.2f", myval);
		return myval;
	}
	/*float myval = (float) (txt_ox_low * 0.06894);
	float myval = (float) (txt_ox_low * 14.50377);
	float myval = (float) (txt_ox_low * 51.71493);
	float myval = (float) (txt_ox_low * 0.01933);
	float myval = (float) (txt_ox_low * 750.06168);
	float myval = (float) (txt_ox_low * 0.00133);*/
	
	@Override
	protected void onStop() {
	    Log.w("Wave", "App stopped");
	    super.onStop();
	}

	@Override
	protected void onDestroy() {
	    Log.w("Wave", "App destoryed");
	    super.onDestroy();
	}

}
