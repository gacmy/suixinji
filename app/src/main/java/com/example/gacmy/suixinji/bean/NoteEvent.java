package com.example.gacmy.suixinji.bean;

import com.example.gacmy.suixinji.myview.richedittext.RichTextEditor;

import java.util.List;

/**
 * Created by Administrator on 2016/6/13.
 */
public class NoteEvent {
   public  List<RichTextEditor.EditData> notedate;

    public List<RichTextEditor.EditData> getNotedate() {
        return notedate;
    }

    public void setNotedate(List<RichTextEditor.EditData> notedate) {
        this.notedate = notedate;
    }
}
