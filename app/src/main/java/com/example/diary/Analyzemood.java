package com.example.diary;

public class Analyzemood {
    //心情分析总的为4个等级，结合年月日，给出相应的分析
    private int mood;    //对应记录的mood等级0,,1,2,3的一个累加
    public int diarynum; //写的日记总数
    public int  sumpos,sumgood,sumcalm,sumbad;//达到不同心情等级的次数


    public int getMood()
    {
        return mood;
    }

    public void analyze(int  sumpos,int sumgood,int sumcalm, int sumbad) {
        if (mood > 0 && mood < 0.5 * diarynum) {
            String analyze1="嗨，您最近的心情很不错哦~\n"+ "系统帮您记录了这段时间的大致心情：\n"+
                    "心情很好---"+sumpos+"次\n"+"心情不错---"+sumgood+"次\n"+"心情不太好---"+sumcalm+"次\n"+
                    "心情不好---"+sumbad+"次\n"+"为了一直让您保有这种心流的体验\n" +"我们为您提供了一些持续感受幸福的途径\n" +
                    "快来体验吧！";
        }

        else if (mood >= 0.5 * diarynum && mood < 1.5 * diarynum) {
            String analyze2="嗨，您最近的心情还可以哦~\n" + "系统帮您记录了这段时间的大致心情：\n" +
                    "心情很好---"+sumpos+"次\n"+ "心情不错---"+sumgood+"次\n"+ "心情不太好---"+sumcalm+"次\n"+
                    "心情不好---"+sumbad+"次\n"+ "为了让您提升生活的幸福感\n" + "我们为您提供了一些体验心流的途径\n" +
                    "快来体验吧！";
        }

        else if (mood >= 1.5 * diarynum && mood < 2.5 * diarynum) {
            String analyze3="嗨，您最近的心情一般般~\n" + "系统帮您记录了这段时间的大致心情：\n" +
                    "心情很好---"+sumpos+"次\n"+ "心情不错---"+sumgood+"次\n"+ "心情不太好---"+sumcalm+"次\n"+
                    "心情不好---"+sumbad+"次\n"+ "为了让您保有对生活的热情\n" + "我们为您提供了一些通往幸福的途径\n" +
                    "快来体验吧！";
        }

        else if (mood >= 2.5 * diarynum){
            String analyze4="嗨，您最近的心情不太好哦~\n" + "系统帮您记录了这段时间的大致心情：\n" +
                    "心情很好---"+sumpos+"次\n"+ "心情不错---"+sumgood+"次\n"+ "心情不太好---"+sumcalm+"次\n"+
                    "心情不好---"+sumbad+"次\n"+ "为了让您提升对生活的热情\n" + "我们提供了一些打败坏心情的方法\n" +
                    "快来尝试吧！";
        }
    }
}


