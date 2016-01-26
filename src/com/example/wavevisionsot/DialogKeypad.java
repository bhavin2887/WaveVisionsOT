package com.example.wavevisionsot;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class DialogKeypad extends Activity implements View.OnClickListener {
	Button one, two, three, four, five, six, seven, eight, nine, zero, equal,
			ce, del, dial, cancel;
	EditText disp;
	int op1;
	int op2;
	String optr;
	private native void icone();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.dialog_keypad);
		one = (Button) findViewById(R.id.one);
		two = (Button) findViewById(R.id.two);
		three = (Button) findViewById(R.id.three);
		four = (Button) findViewById(R.id.four);
		five = (Button) findViewById(R.id.five);
		six = (Button) findViewById(R.id.six);
		seven = (Button) findViewById(R.id.seven);
		eight = (Button) findViewById(R.id.eight);
		nine = (Button) findViewById(R.id.nine);
		zero = (Button) findViewById(R.id.zero);
		ce = (Button) findViewById(R.id.ce);
		del = (Button) findViewById(R.id.del);
		disp = (EditText) findViewById(R.id.display);
		try {
			one.setOnClickListener(this);
			two.setOnClickListener(this);
			three.setOnClickListener(this);
			four.setOnClickListener(this);
			five.setOnClickListener(this);
			six.setOnClickListener(this);
			seven.setOnClickListener(this);
			eight.setOnClickListener(this);
			nine.setOnClickListener(this);
			zero.setOnClickListener(this);
			equal.setOnClickListener(this);
			dial.setOnClickListener(this);
			cancel.setOnClickListener(this);
		} catch (Exception e) {
		}
	}

	public void operation() {
		if (optr.equals("+")) {
			op2 = Integer.parseInt(disp.getText().toString());
			disp.setText("");
			op1 = op1 + op2;
			disp.setText("Result : " + Integer.toString(op1));
		} else if (optr.equals("-")) {
			op2 = Integer.parseInt(disp.getText().toString());
			disp.setText("");
			op1 = op1 - op2;
			disp.setText("Result : " + Integer.toString(op1));
		} else if (optr.equals("*")) {
			op2 = Integer.parseInt(disp.getText().toString());
			disp.setText("");
			op1 = op1 * op2;
			disp.setText("Result : " + Integer.toString(op1));
		} else if (optr.equals("/")) {
			op2 = Integer.parseInt(disp.getText().toString());
			disp.setText("");
			op1 = op1 / op2;
			disp.setText("Result : " + Integer.toString(op1));
		}
	}

	@Override
	public void onClick(View arg0) {
		Editable str = disp.getText();
		switch (arg0.getId()) {
		case R.id.one:
			if (op2 != 0) {
				op2 = 0;
				disp.setText("");
			}
			str = str.append(one.getText());
			disp.setText(str);
			icone();
			break;
		case R.id.two:
			if (op2 != 0) {
				op2 = 0;
				disp.setText("");
			}
			str = str.append(two.getText());
			disp.setText(str);
			break;
		case R.id.three:
			if (op2 != 0) {
				op2 = 0;
				disp.setText("");
			}
			str = str.append(three.getText());
			disp.setText(str);
			break;
		case R.id.four:
			if (op2 != 0) {
				op2 = 0;
				disp.setText("");
			}
			str = str.append(four.getText());
			disp.setText(str);
			break;
		case R.id.five:
			if (op2 != 0) {
				op2 = 0;
				disp.setText("");
			}
			str = str.append(five.getText());
			disp.setText(str);
			break;
		case R.id.six:
			if (op2 != 0) {
				op2 = 0;
				disp.setText("");
			}
			str = str.append(six.getText());
			disp.setText(str);
			break;
		case R.id.seven:
			if (op2 != 0) {
				op2 = 0;
				disp.setText("");
			}
			str = str.append(seven.getText());
			disp.setText(str);
			break;
		case R.id.eight:
			if (op2 != 0) {
				op2 = 0;
				disp.setText("");
			}
			str = str.append(eight.getText());
			disp.setText(str);
			break;
		case R.id.nine:
			if (op2 != 0) {
				op2 = 0;
				disp.setText("");
			}
			str = str.append(nine.getText());
			disp.setText(str);
			break;
		case R.id.zero:
			if (op2 != 0) {
				op2 = 0;
				disp.setText("");
			}
			str = str.append(zero.getText());
			disp.setText(str);
			break;
		case R.id.ce:
			disp.setText("");
			break;
		case R.id.del:
			if (disp.length() > 0) {
				disp.setText(disp.getText().toString().substring(0, disp.getText().length()-1));
			}
			break;
		case R.id.dial:
			Toast.makeText(getApplicationContext(), "Dialing...", Toast.LENGTH_LONG).show();
			break;
		case R.id.cancel:
			break;
		}
	}
}
