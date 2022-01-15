package com.example.crosswordsolver;

public class SolverObject {
    public static int thisNumberOfTimes = 0; // to calculate how many iterations been done

    public static int checkX = 0; // Karakterleri yerleştirme ve okumada kullanılacak
    public static int checkY = 0; // Karakterleri yerleştirme ve okumada kullanılacak

    public static int solveSpeed = 0; // Puzzle çözülürken işlemler arasındaki boşta bekleme süresi


    public static String[] lineN =
    {//new String[16]; //15
    "+++++++@+++++++", // 1.  Satır
    "++++++@++++++@+", // 2.  Satır
    "+++++++@+++++++", // 3.  Satır
    "++++++@++++++@+", // 4.  Satır
    "@+@++++@++++++@", // 5.  Satır
    "++++++++@+@++++", // 6.  Satır
    "+@+@+@+++++++++", // 7.  Satır
    "++++@+@+@+@++++", // 8.  Satır
    "+++++++++@+@+@+", // 9.  Satır
    "++++@+@++++++++", // 10. Satır
    "@++++++@++++@+@", // 11. Satır
    "+@++++++@++++++", // 12. Satır
    "+++++++@+++++++", // 13. Satır
    "+@++++++@++++++", // 14. Satır
    "+++++++@+++++++", // 15. Satır
    "@@@@@@@@@@@@@@@"// block
};
    //endregion

    //region [ listXLetter ]
    String[] list3Letter = new String[2];
    String[] list4Letter = new String[14];
    String[] list5Letter = new String[7];
    String[] list6Letter = new String[16];
    String[] list7Letter = new String[10];
    String[] list8Letter = new String[6];
    String[] list9Letter = new String[2];
    String[] list10Letter = new String[2];
    //endregion



    public static String[] getLineN() {
        return lineN;
    }

    public String[] getList3Letter() {
        return list3Letter;
    }

    public String[] getList4Letter() {
        return list4Letter;
    }

    public String[] getList5Letter() {
        return list5Letter;
    }

    public String[] getList6Letter() {
        return list6Letter;
    }

    public String[] getList7Letter() {
        return list7Letter;
    }

    public String[] getList8Letter() {
        return list8Letter;
    }

    public String[] getList9Letter() {
        return list9Letter;
    }

    public String[] getList10Letter() {
        return list10Letter;
    }

    public int getLength3() {
        return list3Letter.length;
    }

    public int getLength4() {
        return list4Letter.length;
    }

    public int getLength5() {
        return list5Letter.length;
    }

    public int getLength6() {
        return list6Letter.length;
    }

    public int getLength7() {
        return list7Letter.length;
    }

    public int getLength8() {
        return list8Letter.length;
    }

    public int getLength9() {
        return list9Letter.length;
    }

    public int getLength10() {
        return list10Letter.length;
    }
}
