package com.example.diary.tools;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.List;

public class Option {//按钮类
    static int[][] moods=new int[3][5];
    public static String[] moodWords = new String[]{
            "开心", "愉悦", "感动", "兴奋", "幸福",
            "平静", "充实", "后悔", "向往", "惊讶",
            "疲惫", "伤心", "委屈", "愤怒", "焦虑"
    };

    public static int[][] setMood(int[] mood){
//输入一个长度为15的int数组（按钮的结果）
        //被选中的心情为1，未被选中的心情为0
        //将其转换为二维数组
        //调用前限定mood的加和大于0小于6
        int i,j;
        for(i=0;i<3;i++)
            for(j=0;j<5;j++)
                moods[i][j]=mood[i*5+j];
        System.out.println(moods);
        return moods;
    }

    public static int[][] printMood(){
//返回储存按钮结果的二维数组
        return moods;
    }
}

