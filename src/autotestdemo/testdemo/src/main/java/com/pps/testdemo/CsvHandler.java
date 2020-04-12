package com.pps.testdemo;

import java.util.ArrayList;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.BufferedReader;
import java.io.BufferedWriter;

class CsvHander {
    public static ArrayList<String[]> csv_reader(String file_path) {
        ArrayList<String[]> str_array_list = new ArrayList<String[]>();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file_path));
            // BufferedReader reader = new BufferedReader(new FileReader(new
            // File(file_path)));
            reader.readLine();// 第一行信息，为标题信息，不用,如果需要，注释掉
            String line = null;
            while ((line = reader.readLine()) != null) {
                String item[] = line.split(",");// CSV格式文件为逗号分隔符文件，这里根据逗号切分
                if (item.length > 0) {
                    str_array_list.add(item);
                }
            }
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return str_array_list;
    }
    public static void csv_writer(String[] csv_col, ArrayList<String[]> csv_data, String file_path, boolean append) {  
        try {
            // 追加
          BufferedWriter writer = new BufferedWriter(new FileWriter(file_path, append));
          //BufferedWriter writer = new BufferedWriter(new FileWriter(new File(file_path), append));
          if (append == false) {
              for (String string : csv_col) {
                  writer.write(string + ",");
              }
              writer.newLine();
          }
          for (String[] strings : csv_data) {
              for (String string : strings) {
                writer.write(string + ",");
              } 
              writer.newLine();
          }
          writer.close();     
        } catch (Exception e) {
          // BufferedWriter在关闭对象捕捉异常 
          e.printStackTrace();  
        }  
      }
}