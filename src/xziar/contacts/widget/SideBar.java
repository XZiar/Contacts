/*
 * Copyright (c) 2015 ����.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package xziar.contacts.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

/**
 * �����
 */
public class SideBar extends View
{
	// �����¼�
	private OnTouchingLetterChangedListener onTouchingLetterChangedListener;
	// 26����ĸ
	public static char[] b = { '#', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I',
			'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V',
			'W', 'X', 'Y', 'Z' };
	private static final ColorDrawable cdHind = new ColorDrawable(0x00000000);
	private static final ColorDrawable cdShow = new ColorDrawable(0x13161316);
	private int choose = -1;// ѡ��
	private Paint paint = new Paint();

	private TextView mTextDialog;

	public void setTextView(TextView mTextDialog)
	{
		this.mTextDialog = mTextDialog;
	}

	public SideBar(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
	}

	public SideBar(Context context, AttributeSet attrs)
	{
		super(context, attrs);
	}

	public SideBar(Context context)
	{
		super(context);
	}

	/**
	 * ��д�������
	 */
	protected void onDraw(Canvas canvas)
	{
		super.onDraw(canvas);
		// ��ȡ����ı䱳����ɫ.
		int height = getHeight();// ��ȡ��Ӧ�߶�
		int width = getWidth(); // ��ȡ��Ӧ���

		float singleHeight = (height * 1f) / b.length;// ��ȡÿһ����ĸ�ĸ߶�
		singleHeight = (height * 1f - singleHeight / 2) / b.length;
		for (int i = 0; i < b.length; i++)
		{
			paint.setColor(Color.rgb(86, 86, 86));
			paint.setTypeface(Typeface.DEFAULT);
			paint.setAntiAlias(true);
			paint.setTextSize(23);

			// x��������м�-�ַ�����ȵ�һ��.
			float xPos = width / 2
					- paint.measureText(String.valueOf(b[i])) / 2;
			float yPos = singleHeight * i + singleHeight;
			canvas.drawText(String.valueOf(b[i]), xPos, yPos, paint);
			paint.reset();// ���û���
		}
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent event)
	{
		final int action = event.getAction();
		final float y = event.getY();// ���y����
		final int oldChoose = choose;
		final OnTouchingLetterChangedListener listener = onTouchingLetterChangedListener;
		final int c = (int) (y / getHeight() * b.length);// ���y������ռ�ܸ߶ȵı���*b����ĳ��Ⱦ͵��ڵ��b�еĸ���.

		switch (action)
		{
		case MotionEvent.ACTION_UP:
			setBackground(cdHind);
			choose = -1;//
			invalidate();
			if (mTextDialog != null)
			{
				mTextDialog.setVisibility(View.INVISIBLE);
			}
			break;

		default:
			setBackground(cdShow);
			if (oldChoose != c)
			{
				if (c >= 0 && c < b.length)
				{
					if (listener != null)
					{
						listener.onTouchingLetterChanged(b[c]);
					}
					if (mTextDialog != null)
					{
						mTextDialog.setText(String.valueOf(b[c]));
						mTextDialog.setVisibility(View.VISIBLE);
					}

					choose = c;
					invalidate();
				}
			}

			break;
		}
		return true;
	}

	/**
	 * ���⹫���ķ���
	 *
	 * @param onTouchingLetterChangedListener
	 */
	public void setOnTouchingLetterChangedListener(
			OnTouchingLetterChangedListener onTouchingLetterChangedListener)
	{
		this.onTouchingLetterChangedListener = onTouchingLetterChangedListener;
	}

	public interface OnTouchingLetterChangedListener
	{
		void onTouchingLetterChanged(char s);
	}
}