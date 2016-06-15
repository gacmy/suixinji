package com.example.gacmy.suixinji.fragment;

import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gacmy.suixinji.R;
import com.example.gacmy.suixinji.activity.TagActivity;
import com.example.gacmy.suixinji.bean.MessageEvent;
import com.example.gacmy.suixinji.bean.NoteBean;
import com.example.gacmy.suixinji.dao.NoteDao;
import com.example.gacmy.suixinji.dao.TagDao;
import com.example.gacmy.suixinji.myview.CheckTextView;
import com.example.gacmy.suixinji.myview.FlowLayout;
import com.example.gacmy.suixinji.myview.circleimageview.CircleImageView;
import com.example.gacmy.suixinji.myview.dialogplus.DialogPlus;
import com.example.gacmy.suixinji.myview.dialogplus.DialogUtils;
import com.example.gacmy.suixinji.myview.dialogplus.OnClickListener;
import com.example.gacmy.suixinji.myview.dialogplus.ViewHolder;
import com.example.gacmy.suixinji.myview.fabreveallayout.FABRevealLayout;
import com.example.gacmy.suixinji.myview.fabreveallayout.OnRevealChangeListener;
import com.example.gacmy.suixinji.myview.richedittext.RichTextEditor;
import com.example.gacmy.suixinji.myview.toast.GacToast;
import com.example.gacmy.suixinji.utils.AppCompact;
import com.example.gacmy.suixinji.utils.bitmap.BitmapUtils;
import com.example.gacmy.suixinji.utils.date.DateUtils;
import com.example.gacmy.suixinji.utils.gson.GsonUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by gacmy on 2016/5/30.
 * 随心笔记Framgment
 */
public class SXBJFragment extends BaseFragment{
   private RichTextEditor richTextEditor;
    private FABRevealLayout fabRevealLayout;
    private ImageView civ_photo;
    private ImageView iv_back;
    private TextView tv_notitle;//笔记标题 时间
    private ImageView civ_takephoto;
    private ImageView civ_save;
    private ImageView civ_clear;
    private FlowLayout fl_addTag;
    private boolean init = true;
    private ImageView iv_tag;
    private String str_tagText;
    @Override
    public View getView(LayoutInflater inflater,ViewGroup container) {
        return inflater.inflate(R.layout.fragment_sxbj,container,false);
    }

    @Override
    public void initView(View view) {
        richTextEditor =mGetView(R.id.richet,view);
        fabRevealLayout = mGetView(R.id.fabreveallayout,view);
        tv_notitle = mGetView(R.id.tv_notetitle, view);
        civ_photo = mGetViewSetOnClick(R.id.civ_photo, view);
        setTint(civ_photo,R.drawable.ic_photo_white_24dp);
        iv_back = mGetViewSetOnClick(R.id.iv_back,view);
        setTint(iv_back,R.drawable.ic_exit_to_app_white_24dp);
        civ_save = mGetViewSetOnClick(R.id.civ_save,view);
        setTint(civ_save,R.drawable.ic_note_add_white_24dp);
        civ_clear = mGetViewSetOnClick(R.id.civ_clear,view);
        setTint(civ_clear,R.drawable.ic_delete_white_24dp);
        civ_takephoto = mGetViewSetOnClick(R.id.civ_takephoto,view);
        setTint(civ_takephoto,R.drawable.ic_camera_white_24dp);
        iv_tag = mGetViewSetOnClick(R.id.iv_tag,view);
        setTint(iv_tag,R.drawable.ic_tag);
        fl_addTag = mGetView(R.id.fl_addtag,view);

    }

    private void setTint(ImageView iv,int resId){
        AppCompact.setTint(getActivity(), iv, resId, R.color.white, R.color.colorPrimary);
    }
    //viewpager 里的fragment 可见时候用这个方法
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(isVisibleToUser){
            if(init){
                Log.e("gac", "fragment:is visible");
                setRichTextEditor();//在Fragment可见的时候设置 内容
                init = false;
            }

        }
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    private void testData(){
        List<RichTextEditor.EditData> list = new ArrayList<>();
        RichTextEditor.EditData e1 = richTextEditor. new EditData("...",null);
        RichTextEditor.EditData e2 = richTextEditor. new EditData(null,"/storage/emulated/0/UCDownloads/Screenshot/tmp/TMPSNAPSHOT1464669689067.jpg");
        RichTextEditor.EditData e3 = richTextEditor. new EditData("1111",null);
        RichTextEditor.EditData e4 = richTextEditor. new EditData(null,"/storage/emulated/0/UCDownloads/Screenshot/tmp/TMPSNAPSHOT1464669689067.jpg");
        RichTextEditor.EditData e5 = richTextEditor. new EditData("000",null);
        list.add(e1);
        list.add(e2);
        list.add(e3);
        list.add(e4);
        list.add(e5);
        richTextEditor.setRichEditText(list);
    }
    //从相册获取图片
    private void gallery(){
         BitmapUtils.gallery(getActivity());
    }

    //从相机获取图片
    private void camera(){
        BitmapUtils.camera(getActivity());
    }

    private void saveNote(){
        Log.e("gac", "**********saveNote***************");
        String notetitle = tv_notitle.getText().toString();
        if(notetitle.equals(getActivity().getString(R.string.nonote))){
            saveRichText();
        }else{
            updateRichText();
        }
    }
    //保存富文本
    private void saveRichText(){
        if(isRichTextEmpty()){
            GacToast.makeText(getActivity(),getActivity().getString(R.string.empty),GacToast.ERROR).show();
            return;
        }
        List<RichTextEditor.EditData> list = richTextEditor.buildEditData();
        String content = GsonUtils.list2JsonStr(list);
        NoteBean bean = new NoteBean();
        bean.setContent(content);
        String datetime = DateUtils.getTodayDate();
        bean.setDatetime(datetime);
        tv_notitle.setText(datetime);
        new NoteDao(getActivity()).insertBean(bean);
    }

    //更新数据库富文本
    private void updateRichText(){
        if(isRichTextEmpty()){
            GacToast.makeText(getActivity(),getActivity().getString(R.string.empty),GacToast.ERROR).show();
            return;
        }
        List<RichTextEditor.EditData> list = richTextEditor.buildEditData();
        String content = GsonUtils.list2JsonStr(list);
        NoteBean bean = new NoteBean();
        bean.setContent(content);
        bean.setDatetime(DateUtils.getTodayDate());
        String datetime = tv_notitle.getText().toString();
        bean.setDatetime(datetime);
        new NoteDao(getActivity()).updateBean("datetime = ?", new String[]{datetime}, bean);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.civ_photo:
                gallery();
                break;
            case R.id.iv_back:
                backMainView();
                break;
            case R.id.civ_takephoto:
                camera();
                break;
            case R.id.civ_save:
                saveNote();
                break;
            case R.id.civ_clear:
                clearRichText();
                break;
            case R.id.iv_tag:
                addTag();
                break;
        }
    }

    private void addTag(){
        showTagDialog();

    }


    //显示标签的Dialog
    private void showTagDialog(){
        View contentView = LayoutInflater.from(getActivity()).inflate(R.layout.flowlayout,null);
        final ViewHolder holder = new ViewHolder(contentView);

        final FlowLayout fl = (FlowLayout)contentView.findViewById(R.id.fl_tag);
        String tagStr = new TagDao(getContext()).getTagstr();
        //初始化标签内容
        if(!TextUtils.isEmpty(tagStr)){
            fl.addTags(tagStr);
        }
        //设置选中的标签内容
        if(!TextUtils.isEmpty(str_tagText)){
            fl.setCheckedTags(str_tagText);
        }
        DialogUtils.showOnlyContentDialog(getActivity(), holder, new OnClickListener() {
            @Override
            public void onClick(DialogPlus dialog, View view) {
                if (view.getId() == R.id.dialog_ok) {
                    dialog.dismiss();
                    Log.e("gac", "string:" + fl.getTagStr());
                    str_tagText = fl.getTagStr();
                    addUserTag(str_tagText);
                } else if (view.getId() == R.id.dialog_cancel) {
                    dialog.dismiss();
                }else if(view.getId() == R.id.dialog_tag_eidt){
                    startActivity(new Intent(getActivity(), TagActivity.class));
                }
            }
        });
    }

    //添加用户自己笔记的标签
    private void addUserTag(String text){
        fl_addTag.removeAllViews();
        if(TextUtils.isEmpty(text)){
            return;
        }
        String[] tagText = text.split(":");
        for(int i = 0; i < tagText.length; i++) {
            TextView tv4 = new TextView(getActivity());
            tv4.setText(tagText[i]);
            tv4.setPadding(5, 5, 5, 5);
            tv4.setTextSize(16);
            tv4.setBackgroundResource(R.drawable.textborder);
            fl_addTag.addView(tv4);
        }
    }
    private void clearRichText(){
        richTextEditor.clearAllText();
        tv_notitle.setText(getString(R.string.nonote));
    }
    @Override
    public void initEvent() {
        configureFABReveal();
        EventBus.getDefault().register(this);

    }

    //fab reavel 监听事件 切换界面
    private void configureFABReveal() {
        fabRevealLayout.setOnRevealChangeListener(new OnRevealChangeListener() {
            @Override
            public void onMainViewAppeared(FABRevealLayout fabRevealLayout, View mainView) {
                showMainViewItems();
            }

            @Override
            public void onSecondaryViewAppeared(final FABRevealLayout fabRevealLayout, View secondaryView) {
                showSecondaryViewItems();
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    //接收MainTabActivity 发送的图片uri
    @Subscribe
    public void onEventMainThread(MessageEvent event) {
        String msg = event.getMsg();
        Log.e("gac", msg);
        if(!TextUtils.isEmpty(msg)){
            richTextEditor.insertImage(msg);
        }
        Toast.makeText(getActivity(), msg, Toast.LENGTH_LONG).show();
    }

    private void setRichTextEditor(){
       List<NoteBean> list = new NoteDao(getActivity()).getBeanList("select * from " + NoteDao.TABLE_NAME, null);
        if(list == null || list.size() == 0){
            return;
        }
        bubbleSort(list);
        NoteBean maxBean = list.get(list.size() - 1);
        tv_notitle.setText(maxBean.getDatetime());
        String content = maxBean.getContent();
        Log.e("gac","content:"+content);
        List<RichTextEditor.EditData> listdata = GsonUtils.jsonStr2List(content);
        richTextEditor.setRichEditText(listdata);
    }

    //时间进行冒泡排序
    public  void bubbleSort(List<NoteBean> list) {
        NoteBean temp; // 记录临时中间值
        int size =list.size(); // 数组大小
        for (int i = 0; i < size - 1; i++) {
            for (int j = i + 1; j < size; j++) {
                if (DateUtils.compareDataTime(list.get(i).getDatetime(),list.get(j).getDatetime()) == -1 ) { // 交换两数的位置
                    temp = list.get(i);
                    list.get(i).setDatetime(list.get(j).getDatetime());
                    list.get(i).setContent(list.get(j).getContent());
                    list.get(i).setId(list.get(j).getId());

                    list.get(j).setDatetime(temp.getDatetime());
                    list.get(j).setId(temp.getId());
                    list.get(j).setContent(temp.getContent());

                }
            }
        }
    }
    //revellayout 返回主界面
    private void backMainView(){
        fabRevealLayout.revealMainView();
    }

    private void showMainViewItems() {
        scale(tv_notitle, 50);
        scale(civ_save,100);
        scale(civ_clear, 150);
    }

    private boolean isRichTextEmpty(){
        boolean flag = false;
        List<RichTextEditor.EditData> list = richTextEditor.buildEditData();
        if(list == null || list.size() == 0){
            flag = true;
        }
        for(int i = 0; i < list.size(); i++){
            if((list.get(i).inputStr !=null && !TextUtils.isEmpty(list.get(i).inputStr.trim())
            || !TextUtils.isEmpty(list.get(i).imagePath))){
                flag = false;
                break;
            }else{
                flag = true;
            }
        }
        Log.e("gac","rich text empty:"+flag);
        return flag;
    }

    private void showSecondaryViewItems() {
        scale(civ_photo, 100);
        scale(iv_back, 150);
        scale(civ_takephoto,50);
    }

    private void scale(View view, long delay){
        view.setScaleX(0);
        view.setScaleY(0);
        view.animate()
                .scaleX(1)
                .scaleY(1)
                .setDuration(500)
                .setStartDelay(delay)
                .setInterpolator(new OvershootInterpolator())
                .start();
    }

}
