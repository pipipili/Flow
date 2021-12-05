package com.example.diary.tools;

import android.Manifest;
import android.content.pm.PackageManager;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import com.aliasi.util.Files;

import java.util.Scanner;

import com.aliasi.classify.Classification;
import com.aliasi.classify.Classified;
import com.aliasi.classify.DynamicLMClassifier;

import com.aliasi.lm.NGramProcessLM;
import com.example.diary.write.MainActivity;
import com.example.diary.write.WriteActivity;

import java.io.IOException;

public class Tools {
    static String diaryfile="C:\\Users\\86150\\Desktop\\diaryfile";
    static String moodfile="C:\\Users\\86150\\Desktop\\moodfile";
    File mPolarityDir;
    String[] mCategories=new String[2];
    DynamicLMClassifier<NGramProcessLM> mClassifier;

    public Tools() {
        System.out.println("\nBASIC POLARITY DEMO");
        mPolarityDir = new File("POLARITY_DIR/Chinese");
        System.out.println("\nData Directory=" + mPolarityDir);
        mCategories[0]="neg";
        mCategories[1]="pos";
        System.out.println(mCategories);

        int nGram = 8;
        mClassifier
                = DynamicLMClassifier
                .createNGramProcess(mCategories, nGram);
    }


    public int run(String review) throws ClassNotFoundException, IOException {//review指的文本内容
        int k;
        train();
        k=evaluate(review);
        return k;
    }

    boolean isTrainingFile(File file) {
        return file.getName().charAt(2) != '9';  // test on fold 9
    }

    public void train() throws IOException {
        int numTrainingCases = 2;
        int numTrainingChars = 0;
        System.out.println("\nTraining.");
        String category1 = mCategories[0];
        String category2 = mCategories[1];
        Classification classification1
                = new Classification(category1);
        Classification classification2
                = new Classification(category2);

        File trainFile1 = new File("POLARITY_DIR/Chinese/neg/degrade.txt");
        File trainFile2 = new File("POLARITY_DIR/Chinese/pos/praise.txt");
        String review = Files.readFromFile(trainFile1, "ISO-8859-1");
        String review1 = Files.readFromFile(trainFile2, "ISO-8859-1");
        numTrainingChars += review.length();
        Classified<CharSequence> classified
                = new Classified<CharSequence>(review, classification1);
        mClassifier.handle(classified);
        numTrainingChars += review1.length();
        Classified<CharSequence> classified1
                = new Classified<CharSequence>(review1, classification2);
        mClassifier.handle(classified1);




        System.out.println("  # Training Cases=" + numTrainingCases);
        System.out.println("  # Training Chars=" + numTrainingChars);
    }

    

    public int evaluate(String review) throws IOException {
        int negnum = 0;
        int posnum = 0;
        int k;
        byte[] review1 = review.getBytes("ISO-8859-1");
        String reviewStr = new String(review1, "ISO-8859-1");
        Classification classification = mClassifier.classify(reviewStr);
        String resultCategory = classification.bestCategory();
        System.out.println("该篇日记的心情是：" + resultCategory);
        if (classification.bestCategory().equals("neg")) {
            negnum = negnum + 1;
        } else{
            posnum = posnum + 1;
        }

        k=posnum;
        System.out.println(posnum);
        System.out.println("negnum:" + negnum + "\n" + "posnum:" + posnum);
        return k;
    }



    //结合按钮与NLP结果，得出当日心情类型
    public static int getMood (int[][] options,int pos){
//参数options为按钮所得结果
        //参数pos与neg为NLP所得结果
        int[] r=new int[4];//四种心情结果
        int neg=0;
        if(pos==1){
            neg=0;
        }
        if(pos==0){
            neg=1;
        }
        r[0]=sum(options,0)+pos;//积极+pos=心情很好
        r[1]=sum(options,1)+pos;//中性+pos=心情不错
        r[2]=sum(options,1)+neg;//中性+neg=心情不太好
        r[3]=sum(options,2)+neg;//消极+neg=心情不好

        return max(r);//选出其中分数最高的一项作为当日心情结果
    }

    //返回int数组中最大的一项的角标
    private static int max(int[] r) {
        int max=0;
        int num=-1;
        int i;
        for(i=0;i<r.length;i++)
            if (r[i] > max) {
                max = r[i];
                num=i;
            }
        return num;
    }

    //返回二维数组某一行的加和
    private static int sum(int[][] option,int row) {
        int s=0;
        int i;
        for(i=0;i<option[0].length;i++)
            s+=option[row][i];
        return s;
    }

    //将getMood方法的返回结果转为能被存入moodfile的String类型
    public static String toMoodString(int[] options,int type){
//参数options表示用户输入的按钮选项结果
        //参数type表示getMood方法所返回的结果
        StringBuilder moodResult= new StringBuilder(new String(String.valueOf(type))+"\n");
        int i;
        for(i=0;i<15;i++)
            if(options[i]==1)
                moodResult = new StringBuilder(moodResult + Option.moodWords[i]+"\n");
        return moodResult.toString();
    }

    //在moodfile文件夹中查找该年/该月的ky文件，返回其路径
    public static List<File> searchFiles(final String keyword) {
//参数keyword表示关键字
        //查找年时关键字格式为yyyy
        //查找月时关键字格式为yyyyMM
        List<File> result = new ArrayList<File>();
        File folder= new File("C:\\Users\\86150\\Desktop\\moodfile");

        File[] subFolders = folder.listFiles(new FileFilter() {
            @Override
            public boolean accept(File file) {
                return file.getName().toLowerCase().contains(keyword);
            }
        });

        if (subFolders != null)
            for (File file : subFolders)
                if (file.isFile())// 如果是文件则将文件添加到结果列表中
                    result.add(file);

        return result;
    }
    public static void readTxt(String filePath) {
        try {
            File file = new File(filePath);
            if(file.isFile() && file.exists()) {
                InputStreamReader isr = new InputStreamReader(new FileInputStream(file), "utf-8");
                BufferedReader br = new BufferedReader(isr);
                String lineTxt = null;
                while ((lineTxt = br.readLine()) != null) {
                    System.out.println(lineTxt);
                }
                br.close();
            } else {
                System.out.println("文件不存在!");
            }
        } catch (Exception e) {
            System.out.println("文件读取错误!");
        }

    }

    //产生随机数
    public static int getRandom(int start, int end){
        return (int)(Math.random() * (end-start+1) + start);
    }

    //产生随机的日志文件
    public static String randomDiary(){
        int year=getRandom(2021, 2100);//用户写日记的起始年份和终止
        int month=getRandom(1, 12);//随机抽取一个月份
        int day=getRandom(1, 31);//随机抽取某一天
        String s1=""+year;
        String s2=""+month;
        if(s2.length()==1){
            s2="0"+s2;
        }
        String s3=""+day;
        if(s3.length()==1){
            s3="0"+s3;
        }
        return (s1+s2+s3+".txt");
    }

    public static void ToDoList() {
        File file = new File("C:\\java");
        if (!file.exists()) {//如果文件夹不存在
            file.mkdir();//创建文件夹
        }
        try {
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            String file2=df.format(new Date())+".txt";
            BufferedWriter bw = new BufferedWriter(new FileWriter("C:\\java\\完成情况\\"+file2));//创建文件，以日期命名
            Scanner sc = new Scanner(System.in);
            System.out.println("写入你今天的计划：");
            String content = sc.next();
            String[] content1 = content.split(";");
            itcase:
            while (true) {
//显示系统菜单
                System.out.print("请输出已经完成的任务序号:\n");
                Scanner sc1 = new Scanner(System.in);
                int a = sc1.nextInt();// 用户输入数字
                switch (a) {
                    case 0:// 退出
                        break itcase;
                    default:
                        content1[a-1]=content1[a-1]+"已完成";
                }
            }
            bw.write(df.format(new Date())+"的计划是：\n");//写入今日计划
            for (int i = 0; i < content1.length; i++){
                bw.write(content1[i]+"\n");
                System.out.println(content1[i]);
            }
            bw.close();//关闭文件

        } catch (IOException e) {
            e.printStackTrace();
        }
    }



}

