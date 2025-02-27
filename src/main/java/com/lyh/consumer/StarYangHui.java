package com.lyh.consumer;

import java.util.Scanner;

public class StarYangHui {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("请输入杨辉三角的行数：");
        int rows = scanner.nextInt();
        int[][] triangle = new int[rows][];

        for (int i = 0; i < rows; i++) {
            //打印前面空格
            for (int k = 0; k < rows - i - 1; k++) {
                System.out.print("  ");
            }
            //打印*号
            for (int j = 0; j <= i; j++) {
                System.out.print("*   ");
            }
            System.out.println();
        }

    }
}