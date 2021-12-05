package com.example.diary.write;

public class diary {
    private int diaryId;    //存在数据库里对应记录的id，后面查看日记内容就是通过id去数据库取出日记内容
    private String time;
    private String title;
    //    private int[] moodsOptions;
    private int moodType;

    public diary(int diaryId,String time, String title, int[] moods){
        this.diaryId = diaryId;
        this.time = time;
        this.title = title;
//        this.moodsOptions=moods;
        this.moodType=-1;//初始值为-1，后续会修改
    }

    public int getDiaryId(){
        return diaryId;
    }

    public String getTime(){
        return time;
    }

    public String getTitle(){
        return title;
    }

//    public int[] getMoodsOptions() { return moodsOptions; }

    public int getMoodType() {  return moodType; }

}
