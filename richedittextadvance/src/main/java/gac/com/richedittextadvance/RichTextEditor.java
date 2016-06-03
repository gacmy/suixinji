package gac.com.richedittextadvance;

import android.animation.LayoutTransition;
import android.animation.LayoutTransition.TransitionListener;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

//import com.example.gacmy.suixinji.utils.ActivityUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 这是一个富文本编辑器，给外部提供insertImage接口，添加的图片跟当前光标所在位置有关
 * 
 * @author xmuSistone
 * 
 */
@SuppressLint({ "NewApi", "InflateParams" })
public class RichTextEditor extends ScrollView {
	private static final int EDIT_PADDING = 10; // edittext常规padding是10dp
	private static final int EDIT_FIRST_PADDING_TOP = 10; // 第一个EditText的paddingTop值
	private int viewTagIndex = 0; // 新生的view都会打一个tag，对每个view来说，这个tag是唯一的。
	private LinearLayout allLayout; // 这个是所有子view的容器，scrollView内部的唯一一个ViewGroup
	private LayoutInflater inflater;
	private OnKeyListener keyListener; // 所有EditText的软键盘监听器
	private OnClickListener btnListener; // 图片右上角红叉按钮监听器
	private OnFocusChangeListener focusListener; // 所有EditText的焦点监听listener
	private LineEditText lastFocusEdit; // 最近被聚焦的EditText
	private LayoutTransition mTransitioner; // 只在图片View添加或remove时，触发transition动画
	private int editNormalPadding = 0; //
	private int disappearingImageIndex = 0;
	private Context context;
	private List<LineEditText> list_et;
	public RichTextEditor(Context context) {
		this(context, null);
	}

	public RichTextEditor(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public RichTextEditor(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		inflater = LayoutInflater.from(context);
		this.context = context;
		list_et = new ArrayList<>();
		// 1. 初始化allLayout
		allLayout = new LinearLayout(context);
		allLayout.setOrientation(LinearLayout.VERTICAL);
		allLayout.setBackgroundColor(Color.WHITE);
		setupLayoutTransitions();
		LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT);
		addView(allLayout, layoutParams);

		// 2. 初始化键盘退格监听
		// 主要用来处理点击回删按钮时，view的一些列合并操作
		keyListener = new OnKeyListener() {

			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				Log.e("gac","onKey");
				if (event.getAction() == KeyEvent.ACTION_DOWN
						&& event.getKeyCode() == KeyEvent.KEYCODE_DEL) {
					Log.e("gac","onKey del");
					LineEditText edit = (LineEditText) v;
					onBackspacePress(edit);
					return false;
				}
				if(event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER){
					Log.e("gac","onKey enter");
					LineEditText editText = (LineEditText)v;
					int index = (int)editText.getTag();
					Log.e("gac","index:"+index);
					Log.e("gac","viewTagIndex:"+viewTagIndex);
					if(index == 0){
						setEnableEditText(list_et.get(1));
						return true;
					}
					if(index < viewTagIndex-1){
						setEnableEditText(list_et.get(index + 1));
					}else{
						Log.e("gac","create text");
						lastFocusEdit = createLineEditText("", true);
						setEnableEditText(list_et.get(index+1));
					}
					return true;
				}
				return false;
			}
		};

		// 3. 图片叉掉处理
		btnListener = new OnClickListener() {

			@Override
			public void onClick(View v) {
				RelativeLayout parentView = (RelativeLayout) v.getParent();
				onImageCloseClick(parentView);
			}
		};

		focusListener = new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus) {
					lastFocusEdit = (LineEditText) v;
				}
			}
		};
		initRichEditText(context);
		//createFirstEditText();

	}

	private void setEnableEditText(EditText et){
		et.setEnabled(true);
		et.clearFocus();
		et.requestFocus();
	}
	private void initRichEditText(Context context){
		int srceentHeight = ActivityUtils.getScreenHeight(context);
		Log.e("gac","screentHeight:"+srceentHeight);
		//editNormalPadding = dip2px(EDIT_PADDING);
		createTitleEditText();
		lastFocusEdit = createLineEditText("请输入正文", true);
		for(int i = 0; i < 10; i++){
			createLineEditText("",false);
		}

		//lastFocusEdit = firstEdit;
	}

	private void createTitleEditText(){
		LineEditText editText = (LineEditText) inflater.inflate(R.layout.edit_item1,
				null);
		editText.setOnKeyListener(keyListener);
		editText.setTag(viewTagIndex++);
		editText.setPadding(editNormalPadding, dip2px(EDIT_FIRST_PADDING_TOP), editNormalPadding, 0);
		editText.setHint("请输入标题");
		editText.setTextSize(18);
		editText.getPaint().setFakeBoldText(true);//设置粗体
		editText.setEnabled(true);
		editText.setOnFocusChangeListener(focusListener);
		list_et.add(editText);
		LinearLayout.LayoutParams firstEditParam = new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		allLayout.addView(editText, firstEditParam);
	}
	private LineEditText createLineEditText(String hint,boolean enable) {
		LineEditText editText = (LineEditText) inflater.inflate(R.layout.edit_item1,
				null);
		editText.setOnKeyListener(keyListener);
		editText.setTag(viewTagIndex++);

		editText.setPadding(editNormalPadding, dip2px(EDIT_FIRST_PADDING_TOP), editNormalPadding, 0);
		editText.setHint(hint);
		editText.setEnabled(enable);
		editText.setOnFocusChangeListener(focusListener);
		list_et.add(editText);
		LinearLayout.LayoutParams firstEditParam = new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		allLayout.addView(editText, firstEditParam);
		return editText;
	}
	private void createFirstEditText(){
		LinearLayout.LayoutParams firstEditParam = new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		editNormalPadding = dip2px(EDIT_PADDING);
		EditText firstEdit = createEditText("input here",
				dip2px(EDIT_FIRST_PADDING_TOP));
		allLayout.addView(firstEdit, firstEditParam);
		//lastFocusEdit = firstEdit;
	}
	//清空所有内容
	public void clearAllText(){
		if(allLayout != null){
			allLayout.removeAllViews();
			createFirstEditText();
		}
	}
	private List<EditData> list_editData;
	//根据内容填充richEditText
	public void setRichEditText(List<EditData> list){
		this.list_editData = list;
		if(list == null || list.size() == 0){
			return;
		}
		if(allLayout != null){
			allLayout.removeAllViews();
		}
		for(int i = 0; i < list.size(); i++){
			Log.e("gac","**************************");
			String text = list.get(i).inputStr;
			String imagePath = list.get(i).imagePath;
			if(!TextUtils.isEmpty(text)){
				Log.e("gac","insert text");
				addEdiTextAtIndexFoucus(allLayout.getChildCount(), text);
				Log.e("gac", "inserttext index:" + allLayout.getChildCount());

			}
			if(!TextUtils.isEmpty(imagePath)){
				Log.e("gac","insert img");
				inserImagesetContent(imagePath);
			}
			Log.e("gac","**************************");
		}
		addEdiTextAtIndexFoucus(allLayout.getChildCount(), "");
	}
	/**
	 * 处理软键盘backSpace回退事件
	 * 
	 * @param editTxt
	 *            光标所在的文本输入框
	 */
	private void onBackspacePress(LineEditText editTxt) {
		int startSelection = editTxt.getSelectionStart();
		// 只有在光标已经顶到文本输入框的最前方，在判定是否删除之前的图片，或两个View合并
		if (startSelection == 0) {
			int editIndex = allLayout.indexOfChild(editTxt);
			View preView = allLayout.getChildAt(editIndex - 1); // 如果editIndex-1<0,
																// 则返回的是null
			if (null != preView) {
				if (preView instanceof RelativeLayout) {
					// 光标EditText的上一个view对应的是图片
					onImageCloseClick(preView);
				} else if (preView instanceof EditText) {
					// 光标EditText的上一个view对应的还是文本框EditText
					setEnableEditText((LineEditText)preView);

				}
			}
		}
	}

	/**
	 * 处理图片叉掉的点击事件
	 * 
	 * @param view
	 *            整个image对应的relativeLayout view
	 * @type 删除类型 0代表backspace删除 1代表按红叉按钮删除
	 */
	private void onImageCloseClick(View view) {
		if (!mTransitioner.isRunning()) {
			disappearingImageIndex = allLayout.indexOfChild(view);
			allLayout.removeView(view);
		}
	}

	/**
	 * 生成文本输入框
	 */
	private EditText createEditText(String hint, int paddingTop) {
		EditText editText = (EditText) inflater.inflate(R.layout.edit_item1,
				null);
		editText.setOnKeyListener(keyListener);
		editText.setTag(viewTagIndex++);
		editText.setPadding(editNormalPadding, paddingTop, editNormalPadding, 0);
		editText.setHint(hint);
		editText.setOnFocusChangeListener(focusListener);
		return editText;
	}

	/**
	 * 生成图片View
	 */
	private RelativeLayout createImageLayout() {
		RelativeLayout layout = (RelativeLayout) inflater.inflate(
				R.layout.edit_imageview, null);
		layout.setTag(viewTagIndex++);
		View closeView = layout.findViewById(R.id.image_close);
		closeView.setTag(layout.getTag());
		closeView.setOnClickListener(btnListener);
		return layout;
	}

	/**
	 * 根据绝对路径添加view
	 * 
	 * @param imagePath
	 */
	public void insertImage(String imagePath) {
		Log.e("gac", "iamgePath:" + imagePath);
		Bitmap bmp = getScaledBitmap(imagePath, getWidth());
		if(bmp == null){

			//Log.e("gac","bmp is null");
			return;
		}else{
			//Log.e("gac","bmp is not null");
		}
		insertImage(bmp, imagePath);
	}

	//重新填充List<EditData>内容 的时候 采用这个方法
	private void inserImagesetContent(String imagePath){
		Log.e("gac","iamgePath:"+imagePath);
		int screenWidth = ActivityUtils.getScreenWidth(context);
		Log.e("gac","getWidth:"+ screenWidth);
		Bitmap bmp = getScaledBitmap(imagePath, screenWidth);
		if(bmp == null){

			Log.e("gac","bmp is null");
			return;
		}else{
			Log.e("gac","bmp is not null");
		}
		Log.e("gac","insertImage index:"+allLayout.getChildCount());
		addImageViewAtIndexInstant(allLayout.getChildCount(), bmp, imagePath);
	}
	/**
	 * 插入一张图片
	 */
	private void insertImage(Bitmap bitmap, String imagePath) {
		String lastEditStr = lastFocusEdit.getText().toString();
		int cursorIndex = lastFocusEdit.getSelectionStart();

		String editStr1 = lastEditStr.substring(0, cursorIndex).trim();
		int lastEditIndex = allLayout.indexOfChild(lastFocusEdit);//图片插入的位置
		int foucseditindex = (int)lastFocusEdit.getTag();//EditText list集合中的位置
		Log.e("gac","lastEditIndex:"+foucseditindex);
		if (lastEditStr.length() == 0 || editStr1.length() == 0) {
			if(lastEditIndex == 0){
				//防止标题栏上面插入图片
				lastEditIndex = 1;
				setEnableEditText(list_et.get(1));
				addImageViewAtIndex(lastEditIndex, bitmap, imagePath,1);
			}else{
				// 如果EditText为空，或者光标已经顶在了editText的最前面，则直接插入图片，并且EditText下移即可
				addImageViewAtIndex(lastEditIndex, bitmap, imagePath,foucseditindex);
			}

		} else {
			//edittext有内容的时候 则在下面添加图片
			// 当前editext是否是最后一个edittext 是则创建新的edittext 在
			if (allLayout.getChildCount() - 1 == lastEditIndex) {
				setEnableEditText(createLineEditText("", true));
				addImageViewAtIndex(lastEditIndex + 1, bitmap, imagePath,foucseditindex+1);
			} else {
				addImageViewAtIndex(lastEditIndex + 1, bitmap, imagePath,foucseditindex+1);
			}

//			lastFocusEdit.requestFocus();
//			lastFocusEdit.setSelection(editStr1.length(), editStr1.length());
		}
		hideKeyBoard();
	}

	/**
	 * 隐藏小键盘
	 */
	public void hideKeyBoard() {
		InputMethodManager imm = (InputMethodManager) getContext()
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(lastFocusEdit.getWindowToken(), 0);
	}

	/**
	 * 在特定位置插入EditText
	 * 
	 * @param index
	 *            位置
	 * @param editStr
	 *            EditText显示的文字
	 */
	private void addEditTextAtIndex(final int index, String editStr) {
		EditText editText2 = createEditText("", getResources()
				.getDimensionPixelSize(R.dimen.edit_padding_top));
		editText2.setText(editStr);

		// 请注意此处，EditText添加、或删除不触动Transition动画
		allLayout.setLayoutTransition(null);
		allLayout.addView(editText2, index);
		allLayout.setLayoutTransition(mTransitioner); // remove之后恢复transition动画
	}
    private void addEdiTextAtIndexFoucus(final int index,String  editStr){
		EditText editText2 = createEditText("", getResources()
				.getDimensionPixelSize(R.dimen.edit_padding_top));

		editText2.setText(editStr);
		//lastFocusEdit = editText2;
		// 请注意此处，EditText添加、或删除不触动Transition动画
		allLayout.setLayoutTransition(null);
		allLayout.addView(editText2, index);
		allLayout.setLayoutTransition(mTransitioner); // remove之后恢复transition动画
	}
	//立即插入图片 不进行延时处理
	private void addImageViewAtIndexInstant(final int index, Bitmap bmp,
											String imagePath) {
		final RelativeLayout imageLayout = createImageLayout();
		DataImageView imageView = (DataImageView) imageLayout
				.findViewById(R.id.edit_imageView);
		imageView.setImageBitmap(bmp);
		imageView.setBitmap(bmp);
		imageView.setAbsolutePath(imagePath);

		// 调整imageView的高度
		int imageHeight = getWidth() * bmp.getHeight() / bmp.getWidth();
		RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, imageHeight);
		imageView.setLayoutParams(lp);
		allLayout.addView(imageLayout, index);

	}
	/**
	 * 在特定位置添加ImageView
	 */
	private void addImageViewAtIndex(final int index, Bitmap bmp,
			String imagePath,int foucsindex) {
		final RelativeLayout imageLayout = createImageLayout();
		DataImageView imageView = (DataImageView) imageLayout
				.findViewById(R.id.edit_imageView);
		imageView.setImageBitmap(bmp);
		imageView.setBitmap(bmp);
		imageView.setAbsolutePath(imagePath);

		// 调整imageView的高度
		int imageHeight = getWidth() * bmp.getHeight() / bmp.getWidth();
		RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, imageHeight);
		imageView.setLayoutParams(lp);
		setEnableEditText(list_et.get(foucsindex));
		// onActivityResult无法触发动画，此处post处理
		allLayout.postDelayed(new Runnable() {
			@Override
			public void run() {
				allLayout.addView(imageLayout, index);
			}
		}, 200);
	}

	/**
	 * 根据view的宽度，动态缩放bitmap尺寸
	 * 
	 * @param width
	 *            view的宽度
	 */
	private Bitmap getScaledBitmap(String filePath, int width) {
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(filePath, options);
		int sampleSize = options.outWidth > width ? options.outWidth / width
				+ 1 : 1;
		options.inJustDecodeBounds = false;
		options.inSampleSize = sampleSize;
		return BitmapFactory.decodeFile(filePath, options);
	}

	/**
	 * 初始化transition动画
	 */
	private void setupLayoutTransitions() {
		mTransitioner = new LayoutTransition();
		allLayout.setLayoutTransition(mTransitioner);
		mTransitioner.addTransitionListener(new TransitionListener() {

			@Override
			public void startTransition(LayoutTransition transition,
					ViewGroup container, View view, int transitionType) {

			}

			@Override
			public void endTransition(LayoutTransition transition,
					ViewGroup container, View view, int transitionType) {
				if (!transition.isRunning()
						&& transitionType == LayoutTransition.CHANGE_DISAPPEARING) {
					// transition动画结束，合并EditText
					// mergeEditText();
				}
			}
		});
		mTransitioner.setDuration(300);
	}



	/**
	 * dp和pixel转换
	 * 
	 * @param dipValue
	 *            dp值
	 * @return 像素值
	 */
	public int dip2px(float dipValue) {
		float m = getContext().getResources().getDisplayMetrics().density;
		return (int) (dipValue * m + 0.5f);
	}

	/**
	 * 对外提供的接口, 生成编辑数据上传
	 */
	public List<EditData> buildEditData() {
		List<EditData> dataList = new ArrayList<EditData>();
		int num = allLayout.getChildCount();
		for (int index = 0; index < num; index++) {
			View itemView = allLayout.getChildAt(index);
			EditData itemData = new EditData();
			if (itemView instanceof EditText) {
				EditText item = (EditText) itemView;
				itemData.inputStr = item.getText().toString();
			} else if (itemView instanceof RelativeLayout) {
				DataImageView item = (DataImageView) itemView
						.findViewById(R.id.edit_imageView);
				itemData.imagePath = item.getAbsolutePath();
				//itemData.bitmap = item.getBitmap();
			}
			dataList.add(itemData);
		}

		return dataList;
	}


	public class EditData {
		public String inputStr;
		public String imagePath;
		//public Bitmap bitmap;
		public EditData(){

		}
		public EditData(String text,String path){
			this.inputStr = text;
			this.imagePath = path;
		}
	}
}
