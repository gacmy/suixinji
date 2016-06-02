package com.example.gacmy.suixinji.fragment;

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
import com.example.gacmy.suixinji.bean.MessageEvent;
import com.example.gacmy.suixinji.bean.NoteBean;
import com.example.gacmy.suixinji.dao.NoteDao;
import com.example.gacmy.suixinji.myview.circleimageview.CircleImageView;
import com.example.gacmy.suixinji.myview.fabreveallayout.FABRevealLayout;
import com.example.gacmy.suixinji.myview.fabreveallayout.OnRevealChangeListener;
import com.example.gacmy.suixinji.myview.richedittext.RichTextEditor;
import com.example.gacmy.suixinji.myview.toast.GacToast;
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
    private CircleImageView civ_photo;
    private ImageView iv_back;
    private TextView tv_notitle;//笔记标题 时间
    private CircleImageView civ_takephoto;
    private CircleImageView civ_save;
    private CircleImageView civ_clear;

    @Override
    public View getView(LayoutInflater inflater,ViewGroup container) {
        return inflater.inflate(R.layout.fragment_sxbj,container,false);
    }

    @Override
    public void initView(View view) {
        richTextEditor =mGetView(R.id.richet,view);
        fabRevealLayout = mGetView(R.id.fabreveallayout,view);
        tv_notitle = mGetView(R.id.tv_notetitle,view);
        civ_photo = mGetViewSetOnClick(R.id.civ_photo,view);
        iv_back = mGetViewSetOnClick(R.id.iv_back,view);
        civ_save = mGetViewSetOnClick(R.id.civ_save,view);
        civ_clear = mGetViewSetOnClick(R.id.civ_clear,view);
        civ_takephoto = mGetViewSetOnClick(R.id.civ_takephoto,view);
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
        bean.setDatetime(DateUtils.getTodayDate());
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
                richTextEditor.clearAllText();
                break;
        }
    }

    @Override
    public void initEvent() {
        configureFABReveal();
        EventBus.getDefault().register(this);
        setRichTextEditor();
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

    @Subscribe
    public void onEventMainThread(MessageEvent event) {
        String msg =  event.getMsg();
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
        List<RichTextEditor.EditData> list = richTextEditor.buildEditData();
        if(list == null || list.size() == 0){
            return true;
        }
        for(int i = 0; i < list.size(); i++){
            if(TextUtils.isEmpty(list.get(i).inputStr) || !TextUtils.isEmpty(list.get(i).imagePath)){
                return false;
            }
        }
        return true;
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
