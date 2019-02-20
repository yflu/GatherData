package com.eric;

import com.eric.util.HttpClientUtils;
import com.eric.worker.aodai.GatherData;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("输入productid(exit退出):");
        String commc = sc.nextLine();
        while (!"exit".equals(commc)) {
            GatherData gatherData = new GatherData(commc.replace("http://www.lanney.com.au/main/home/product/", ""));
            gatherData.gatherData();
            System.out.println("输入productid(exit退出):");
            commc = sc.nextLine();
        }
        System.out.println("程序已退出！");
    }
}