package com.example.crosswordsolver;

import android.util.Log;

import java.io.IOException;

import java.lang.reflect.Type;
import java.sql.ResultSet;
import java.util.Random;
import java.util.Scanner;

import static java.util.Arrays.copyOfRange;


public class CrosswordSolver {
    long startTime = System.nanoTime();
    static SolverObject solver = new SolverObject();
    Random rnd = new Random();
    public static void main(String[] args)
    {

/* // Solved Puzzle looks like this
		lineN	{string[16]}	string[]
		[0]	    "AMENITY@GASPUMP"	string
		[1]	    "POMACE@HEMPEN@A"	string
		[2]	    "EDITING@LOURDES"	string
		[3]	    "RETINA@BIRDIE@S"	string
		[4]	    "@R@VENT@DESCRY@"	string
		[5]	    "INVESTOR@T@ANOA"	string
		[6]	    "V@I@S@EASTEREGG"	string
		[7]	    "OARS@U@I@O@PAIR"	string
		[8]	    "RETURNING@S@T@E"	string
		[9]	    "YOUR@C@SNOWSHOE"	string
		[10]	"@NOVELS@UPON@P@"	string
		[11]	"S@SIMOOM@PLOVER"	string
		[12]	"TRIVIAL@VOLCANO"	string
		[13]	"I@TALKER@SEALED"	string
		[14]	"RAYLESS@RENTERS"	string
		[15]	"@@@@@@@@@@@@@@@"	string
 */
        // Puzzle Alanını Oluştur
        System.out.println("+++++++@+++++++");  // 1.  Satır
        System.out.println("++++++@++++++@+");  // 2.  Satır
        System.out.println("+++++++@+++++++");  // 3.  Satır
        System.out.println("++++++@++++++@+");  // 4.  Satır
        System.out.println("@+@++++@++++++@");  // 5.  Satır
        System.out.println("++++++++@+@++++");  // 6.  Satır
        System.out.println("+@+@+@+++++++++");  // 7.  Satır
        System.out.println("++++@+@+@+@++++");  // 8.  Satır
        System.out.println("+++++++++@+@+@+");  // 9.  Satır
        System.out.println("++++@+@++++++++");  // 10. Satır
        System.out.println("@++++++@++++@+@");  // 11. Satır
        System.out.println("+@++++++@++++++");  // 12. Satır
        System.out.println("+++++++@+++++++");  // 13. Satır
        System.out.println("+@++++++@++++++");  // 14. Satır
        System.out.println("+++++++@+++++++");  // 15. Satır

        Scanner reader = new Scanner(System.in);
        int a;
        do {
            System.out.println("Puzzle'ı çözmeye başlamak için '0' yazınız");
            a = Integer.valueOf(reader.next());
        }
        while (a != 0);
        reader.close();

        //region Auto Solver
        CrosswordSolver solverMain = new CrosswordSolver();
        solverMain.ResetThePuzzle();

        while (true) { // Solving loop
            solverMain.start1();
            solverMain.ResetThePuzzle();
        }
        //endregion Auto Solver
    }

    // METHODS ---------


    public void TypeTheBoard() {
        System.out.println(" ");
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        for (int i = 0; i < 16; i++) {
            String line = SolverObject.lineN[i];
            String index = String.format("%02d", i);
            System.out.println(index + ": " + line);
        }
        System.out.println(" ");
    }

    public void SetCursorPosition(int checkXin, int checkYin) {
        //char escCode = 0x1B;
        //System.out.print(String.format("%c[%d;%df",escCode, checkXin, checkYin));
        SolverObject.checkX = checkXin;
        SolverObject.checkY = checkYin;
    }

    //region [ GetWordSpaceHorizontal ] - Bulunulan noktadan sonraki "@" karakterine kadar olan sağa doğru yatay boşluğu hesaplar ve döndürür.
    public int GetWordSpaceHorizontal(int checkXin, int checkYin) // Working
    {
        SetCursorPosition(checkXin, checkYin);
        String Line = SolverObject.lineN[checkYin];
        String PartToCheckAfter = Line.substring(checkXin);
        int b = PartToCheckAfter.indexOf("@");
        if (b == -1) {
            return (15 - checkXin);
        }
        return b;
    }
    //endregion

    //region [ GetWordSpaceVertical ] - Bulunulan noktadan sonraki "@" karakterine kadar olan aşağı doğru dikey boşluğu hesaplar ve döndürür.
    public int GetWordSpaceVertical(int checkXin, int checkYin) { //getwordspacevertical 0 döndürüyor bazen
        String storedWord = "";
        SetCursorPosition(checkXin, checkYin);
        String Line = SolverObject.lineN[checkYin];
        for (int i = 0; i < 10; i++)
        {
            String subStr = Line.substring(checkXin, checkXin + 1);
            if (subStr.equals('@'))
            {
                return i;
            }
            else
            {
                if (i == 0)
                {
                    Line = SolverObject.lineN[checkYin + 1];
                } else {
                    Line = SolverObject.lineN[checkYin + i + 1];
                }
                storedWord += Line.charAt(checkXin);
                while (storedWord.indexOf('@') != -1) {
                    return storedWord.indexOf('@')+1;
                }
            }
        } // For
        return 0;
    }
    //endregion

    //region [ TypeVertically ] -
    public String TypeVertically(String Word) //, int wordLength
    {
        // BUGGY --------------
        int wordLength = Word.length(); // added this after
        for (int i = 0; i < wordLength; i++) {
            //region To use later on
            // String d1 = solver.lineN[solver.checkY].substring(0, 1 + i);
            // String d2 = solver.lineN[solver.checkY].substring(i + 1, 14 - (1 + i));
            //endregion To use later on
            if(SolverObject.checkY == 16) { SolverObject.checkY = 15; }
            String Line = SolverObject.lineN[SolverObject.checkY];
            String firstChar = String.valueOf(Word.charAt(i));
            String partStart = SolverObject.lineN[SolverObject.checkY].substring(0, SolverObject.checkX);
            String partEnd   = SolverObject.lineN[SolverObject.checkY].substring(SolverObject.checkX + 1, 15);
            int a = 1;
            if (SolverObject.checkX == 0)
            {
                SolverObject.lineN[SolverObject.checkY] = firstChar + partEnd;
            } else SolverObject.lineN[SolverObject.checkY] = partStart + firstChar + partEnd;
            if (SolverObject.checkY < 16) {
                SolverObject.checkY++;
            }
        }
        TypeTheBoard();
        return "";
    }
    //endregion

    //region [ PickWord ] - int a = karakter sayısı olmak üzere bir kelime döndürür
    public String PickWord(int a) // Minimum value is 3 max is 10
    {
        int arrayEmptyCells = 0;
        if (a == 3) {
            int number;
            number = rnd.nextInt(solver.list3Letter.length);
            String[] shadowArray = new String[2];
            shadowArray = copyOfRange(solver.list3Letter, 0, solver.list3Letter.length);
            for(int i = 0; i<shadowArray.length; i++)
            {
                if (shadowArray[i] == null)
                arrayEmptyCells++;
            }
            if (arrayEmptyCells == shadowArray.length) { return ""; }
            while (shadowArray[number] == null) {
                number = rnd.nextInt(shadowArray.length);
            }
            String saveBeforeDeleting = shadowArray[number];
            shadowArray[number] = null;
            return saveBeforeDeleting;
        }
        if (a == 4) {

            int number = rnd.nextInt(solver.list4Letter.length);
            String[] shadowArray = new String[14];
            shadowArray = copyOfRange(solver.list4Letter, 0, solver.list4Letter.length);
            for(int i = 0; i<shadowArray.length; i++)
            {
                if (shadowArray[i] == null)
                    arrayEmptyCells++;
            }
            if (arrayEmptyCells == shadowArray.length) { return ""; }
            while (shadowArray[number] == null) {
                number = rnd.nextInt(shadowArray.length);
            }
            String saveBeforeDeleting = shadowArray[number];
            shadowArray[number] = null;
            return saveBeforeDeleting;
        }
        if (a == 5) {
            int number = rnd.nextInt(solver.list5Letter.length);
            String[] shadowArray = new String[7];
            shadowArray = copyOfRange(solver.list5Letter, 0, solver.list5Letter.length);
            for(int i = 0; i<shadowArray.length; i++)
            {
                if (shadowArray[i] == null)
                    arrayEmptyCells++;
            }
            if (arrayEmptyCells == shadowArray.length) { return ""; }
            while (shadowArray[number] == null) {
                number = rnd.nextInt(shadowArray.length);
            }
            String saveBeforeDeleting = shadowArray[number];
            shadowArray[number] = null;
            return saveBeforeDeleting;
        }
        if (a == 6) {
            int number = rnd.nextInt(solver.list6Letter.length);
            String[] shadowArray = new String[16];
            shadowArray = copyOfRange(solver.list6Letter, 0, solver.list6Letter.length);
            for(int i = 0; i<shadowArray.length; i++)
            {
                if (shadowArray[i] == null)
                    arrayEmptyCells++;
            }
            if (arrayEmptyCells == shadowArray.length) { return ""; }
            while (shadowArray[number] == null) {
                number = rnd.nextInt(shadowArray.length);
            }
            String saveBeforeDeleting = shadowArray[number];
            shadowArray[number] = null;
            return saveBeforeDeleting;
        }
        if (a == 7) {
            int number = rnd.nextInt(solver.list7Letter.length);
            String[] shadowArray = new String[10];
            shadowArray = copyOfRange(solver.list7Letter, 0, solver.list7Letter.length);
            for(int i = 0; i<shadowArray.length; i++)
            {
                if (shadowArray[i] == null)
                    arrayEmptyCells++;
            }
            if (arrayEmptyCells == shadowArray.length) { return ""; }
            while (shadowArray[number] == null) {
                number = rnd.nextInt(shadowArray.length);
            }
            String saveBeforeDeleting = shadowArray[number];
            shadowArray[number] = null;
            return saveBeforeDeleting;
        }
        if (a == 8) {
            int number = rnd.nextInt(solver.list8Letter.length);
            String[] shadowArray = new String[6];
            shadowArray = copyOfRange(solver.list8Letter, 0, solver.list8Letter.length);
            for(int i = 0; i<shadowArray.length; i++)
            {
                if (shadowArray[i] == null)
                    arrayEmptyCells++;
            }
            if (arrayEmptyCells == shadowArray.length) { return ""; }
            while (shadowArray[number] == null) {
                number = rnd.nextInt(shadowArray.length);
            }
            String saveBeforeDeleting = shadowArray[number];
            shadowArray[number] = null;
            return saveBeforeDeleting;
        }
        if (a == 9) {
            int number = rnd.nextInt(solver.list9Letter.length);
            String[] shadowArray = new String[2];
            shadowArray = copyOfRange(solver.list9Letter, 0, solver.list9Letter.length);
            for(int i = 0; i<shadowArray.length; i++)
            {
                if (shadowArray[i] == null)
                    arrayEmptyCells++;
            }
            if (arrayEmptyCells == shadowArray.length) { return ""; }
            while (shadowArray[number] == null) {
                number = rnd.nextInt(shadowArray.length);
            }
            String saveBeforeDeleting = shadowArray[number];
            shadowArray[number] = null;
            return saveBeforeDeleting;
        }
        if (a == 10) {
            int number = rnd.nextInt(solver.list10Letter.length);
            String[] shadowArray = new String[2];
            shadowArray = copyOfRange(solver.list10Letter, 0, solver.list10Letter.length);
            for(int i = 0; i<shadowArray.length; i++)
            {
                if (shadowArray[i] == null)
                    arrayEmptyCells++;
            }
            if (arrayEmptyCells == shadowArray.length) { return ""; }
            while (shadowArray[number] == null) {
                number = rnd.nextInt(shadowArray.length);
            }
            String saveBeforeDeleting = shadowArray[number];
            shadowArray[number] = null;
            return saveBeforeDeleting;
        }
        return "";
    }
    //endregion

    //region [ read1Letter ] reads a char on the given (x,y)
    public char read1Letter(int checkXin, int checkYin) {
        String Line = SolverObject.lineN[checkYin];
        char readLetter = Line.charAt(checkXin);
        return readLetter;
    }
    //endregion

    //region [ DeleteWordFromArray ]
    public void DeleteWordFromArray(String[] array, String word) {

        int i = 0;
        while (array[i] != null) {
            if (array[i].contains(word)) {
                array[i] = null;
            }
            if (i < array.length - 1) {
                i++;
            } else {
                break;
            }
        }
        while (array[i] == null && (i + 1 != array.length)) {
            i++;
        }

    }
    //endregion

    //region [ getTheArray ]
    public String[] getTheArray(int wordLength) {
        String[] result = null;
        if (wordLength == 3) {
            result = solver.list3Letter;
        } else if (wordLength == 4) {
            result = solver.list4Letter;
        } else if (wordLength == 5) {
            result = solver.list5Letter;
        } else if (wordLength == 6) {
            result = solver.list6Letter;
        } else if (wordLength == 7) {
            result = solver.list7Letter;
        } else if (wordLength == 8) {
            result = solver.list8Letter;
        } else if (wordLength == 9) {
            result = solver.list9Letter;
        } else if (wordLength == 10) {
            result = solver.list10Letter;
        }
        return result;
    }
    //endregion

    //region [ Reset Lines & Word Arrays]
    public void ResetThePuzzle() {
        // Clear the Console with special byte code for that
        //System.out.print("\033[H\033[2J");
        //SetCursorPosition(0, 0);
        solver = new SolverObject();
        SolverObject.checkX = 0;
        SolverObject.checkY = 0;

        // Re-write the Puzzle
        // Puzzle Alanını Oluştur
        //region [ Puzzle Alanı solver.lineN[] ]
        /*System.out.println("+++++++@+++++++");  // 1.  Satır
        System.out.println("++++++@++++++@+");  // 2.  Satır
        System.out.println("+++++++@+++++++");  // 3.  Satır
        System.out.println("++++++@++++++@+");  // 4.  Satır
        System.out.println("@+@++++@++++++@");  // 5.  Satır
        System.out.println("++++++++@+@++++");  // 6.  Satır
        System.out.println("+@+@+@+++++++++");  // 7.  Satır
        System.out.println("++++@+@+@+@++++");  // 8.  Satır
        System.out.println("+++++++++@+@+@+");  // 9.  Satır
        System.out.println("++++@+@++++++++");  // 10. Satır
        System.out.println("@++++++@++++@+@");  // 11. Satır
        System.out.println("+@++++++@++++++");  // 12. Satır
        System.out.println("+++++++@+++++++");  // 13. Satır
        System.out.println("+@++++++@++++++");  // 14. Satır
        System.out.println("+++++++@+++++++");  // 15. Satır*/

        //String[] solver.lineN = new String[15]; //15
        SolverObject.lineN[0] = "+++++++@+++++++"; // 1.  Satır
        SolverObject.lineN[1] = "++++++@++++++@+"; // 2.  Satır
        SolverObject.lineN[2] = "+++++++@+++++++"; // 3.  Satır
        SolverObject.lineN[3] = "++++++@++++++@+"; // 4.  Satır
        SolverObject.lineN[4] = "@+@++++@++++++@"; // 5.  Satır
        SolverObject.lineN[5] = "++++++++@+@++++"; // 6.  Satır
        SolverObject.lineN[6] = "+@+@+@+++++++++"; // 7.  Satır
        SolverObject.lineN[7] = "++++@+@+@+@++++"; // 8.  Satır
        SolverObject.lineN[8] = "+++++++++@+@+@+"; // 9.  Satır
        SolverObject.lineN[9] = "++++@+@++++++++"; // 10. Satır
        SolverObject.lineN[10] = "@++++++@++++@+@"; // 11. Satır
        SolverObject.lineN[11] = "+@++++++@++++++"; // 12. Satır
        SolverObject.lineN[12] = "+++++++@+++++++"; // 13. Satır
        SolverObject.lineN[13] = "+@++++++@++++++"; // 14. Satır
        SolverObject.lineN[14] = "+++++++@+++++++"; // 15. Satır
        SolverObject.lineN[15] = "@@@@@@@@@@@@@@@"; // block
        TypeTheBoard();
        //endregion

        //String[] solver.list3Letter = new String[2];
        solver.list3Letter[0] = "GNU";
        solver.list3Letter[1] = "TOE";

        //String[] solver.list4Letter = new String[14];
        solver.list4Letter[0] = "AEON";
        solver.list4Letter[1] = "ANOA";
        solver.list4Letter[2] = "APER";
        solver.list4Letter[3] = "EMIT";
        solver.list4Letter[4] = "OARS";
        solver.list4Letter[5] = "PAIR";
        solver.list4Letter[6] = "PASS";
        solver.list4Letter[7] = "RODS";
        solver.list4Letter[8] = "STIR";
        solver.list4Letter[9] = "UPON";
        solver.list4Letter[10] = "VALE";
        solver.list4Letter[11] = "VENT";
        solver.list4Letter[12] = "YOGI";
        solver.list4Letter[13] = "YOUR";

        //String[] solver.list5Letter = new String[7];
        solver.list5Letter[0] = "AGREE";
        solver.list5Letter[1] = "EMILE";
        solver.list5Letter[2] = "GELID";
        solver.list5Letter[3] = "IVORY";
        solver.list5Letter[4] = "RAINS";
        solver.list5Letter[5] = "SOLES";
        solver.list5Letter[6] = "SPUDS";

        //String[] solver.list6Letter = new String[16];
        solver.list6Letter[0] = "BIRDIE";
        solver.list6Letter[1] = "DESCRY";
        solver.list6Letter[2] = "HEMPEN";
        solver.list6Letter[3] = "MODERN";
        solver.list6Letter[4] = "NATIVE";
        solver.list6Letter[5] = "NOVELS";
        solver.list6Letter[6] = "OPENER";
        solver.list6Letter[7] = "OPPOSE";
        solver.list6Letter[8] = "PLOVER";
        solver.list6Letter[9] = "POMACE";
        solver.list6Letter[10] = "RETINA";
        solver.list6Letter[11] = "SEALED";
        solver.list6Letter[12] = "SIMOOM";
        solver.list6Letter[13] = "SNOCAT";
        solver.list6Letter[14] = "TALKER";
        solver.list6Letter[15] = "TENANT";

        //String[] solver.list7Letter = new String[10];
        solver.list7Letter[0] = "AMENITY";
        solver.list7Letter[1] = "EDITING";
        solver.list7Letter[2] = "GASPUMP";
        solver.list7Letter[3] = "ICINESS";
        solver.list7Letter[4] = "LOURDES";
        solver.list7Letter[5] = "RAYLESS";
        solver.list7Letter[6] = "RENTERS";
        solver.list7Letter[7] = "SWOLLEN";
        solver.list7Letter[8] = "TRIVIAL";
        solver.list7Letter[9] = "VOLCANO";

        //String[] solver.list8Letter = new String[6];
        solver.list8Letter[0] = "AMORETTO";
        solver.list8Letter[1] = "INVESTOR";
        solver.list8Letter[2] = "PERICARP";
        solver.list8Letter[3] = "SNOWSHOE";
        solver.list8Letter[4] = "SURVIVAL";
        solver.list8Letter[5] = "UNCLOAKS";

        //String[] solver.list9Letter = new String[2];
        solver.list9Letter[0] = "EASTEREGG";
        solver.list9Letter[1] = "RETURNING";

        //String[] solver.list10Letter = new String[2];
        solver.list10Letter[0] = "UNDERNEATH";
        solver.list10Letter[1] = "VIRTUOSITY";

        SolverObject.thisNumberOfTimes++;
        //System.out.println(solver.thisNumberOfTimes);
        start1();
    }
    //endregion


    private void start1() {
        //region Type the first word at (0,0) aiming to pick "AMENITY" by chance // Done, working
        String Line = SolverObject.lineN[SolverObject.checkY];
        String charToCheck = Line.substring(SolverObject.checkX, SolverObject.checkX + 1);
        String charToCheck2 = Line.substring(SolverObject.checkX, SolverObject.checkX + 2);
        if (!charToCheck.equals("@") && !charToCheck2.equals("@")) {
            // Get horizontal space if theres not a "@" in the following 3 characters
            int hLength = GetWordSpaceHorizontal(SolverObject.checkX, SolverObject.checkY);
            // Pick a word according to that length() gotten from the line above
            String hWord = PickWord(hLength);
            if (hWord == "") return;
            // Re-write the solver.lineN[] array with the hWord included
            String restOfTheLine = SolverObject.lineN[SolverObject.checkY].substring(hLength, 15);
            //String restOfTheLine = solver.lineN[solver.checkY].substring(hLength, 15);
            SolverObject.lineN[SolverObject.checkY] = hWord + restOfTheLine;
            // Delete the used word from the array its in
            DeleteWordFromArray(getTheArray(hLength), hWord);
            // Assign the re-rewritten word to a variable
            String asd = SolverObject.lineN[SolverObject.checkY];
            // Type the hWord to the current x,y
            TypeTheBoard();
            SolverObject.checkY++;

        }
        start2();
        return;
        //endregion
    }
    // start2 getwordspacevertical 0 döndürüyor bazen
    private void start2() {
        //region Check the First VERTICAL Letter and pick a word that starts with that letter to type with it (Aiming to pick "APER" by chance)
        SolverObject.checkX = 0;
        SolverObject.checkY = 0; // go to (0, 0)
        char letter = read1Letter(SolverObject.checkX, SolverObject.checkY);
        int vSpace = GetWordSpaceVertical(SolverObject.checkX, SolverObject.checkY);
        String pickedWord = PickWord(vSpace);
        if (pickedWord == "") { return; }
        int tryN = 0;

        if (!pickedWord.equals("") && !pickedWord.equals(null)) // if (pickedWord != "" && pickedWord != null)
        {
            while ((pickedWord.charAt(0) != letter) && tryN < 41) {
                pickedWord = PickWord(vSpace);
                if (pickedWord == "") { return; }
                if (tryN >= 40) {
                    return;
                }
                tryN++;
            }
        }
        if (pickedWord == "") return;
        TypeVertically(pickedWord);
        DeleteWordFromArray(getTheArray(vSpace), pickedWord);

        start3();
        return;
        //endregion
    }

    private void start3() {
        //region Check the (0, 1) letter and type a HORIZONTAL word with it // aiming to pick "POMACE" by chance // Done, working

        SolverObject.checkX = 0;
        SolverObject.checkY = 1; // go to (0, 1)
        char letter = read1Letter(SolverObject.checkX, SolverObject.checkY);
        // Get horizontal space if theres not a "@" in the following 3 characters
        int hLength = GetWordSpaceHorizontal(SolverObject.checkX, SolverObject.checkY);
        // Pick a word according to that length() gotten from the line above
        String pickedWord = PickWord(hLength);
        if (pickedWord == "") { return; }
        int tryN = 0;
        while ((pickedWord.charAt(0) != letter) && tryN < 41) {
            pickedWord = PickWord(hLength);
            if (pickedWord == "") { return; }
            if (tryN >= 40) {
                return;
            }
            tryN++;
        }

        // Re-write the solver.lineN[] array with the hWord included
        String restOfTheLine = SolverObject.lineN[SolverObject.checkY].substring(hLength, 15);
        SolverObject.lineN[SolverObject.checkY] = pickedWord + restOfTheLine;
        // Delete the used word from the array its in
        DeleteWordFromArray(getTheArray(hLength), pickedWord);
        // Assign the re-rewritten word to a variable
        String asd = SolverObject.lineN[SolverObject.checkY];
        // Type the hWord to the current x,y
        TypeTheBoard();
        SolverObject.checkY++;

        start4();
        return;
        //endregion
    }

    private void start4() {
        //region Check the (0, 2) letter and type a HORIZONTAL word with it // aiming to pick "EDITING" by chance // Done, working

        SolverObject.checkX = 0;
        SolverObject.checkY = 2; // go to (0, 2)
        char letter = read1Letter(SolverObject.checkX, SolverObject.checkY);
        // Get horizontal space if theres not a "@" in the following 3 characters
        int hLength = GetWordSpaceHorizontal(SolverObject.checkX, SolverObject.checkY);
        // Pick a word according to that length() gotten from the line above
        String pickedWord = PickWord(hLength);
        if (pickedWord == "") { return; }

        int tryN = 0;
        while ((pickedWord.charAt(0) != letter) && tryN < 41) {
            pickedWord = PickWord(hLength);
            if (pickedWord == "") { return; }
            if (tryN >= 40) {
                return;
            }
            tryN++;
        }

        // Re-write the solver.lineN[] array with the hWord included
        String restOfTheLine = SolverObject.lineN[SolverObject.checkY].substring(hLength, 15);
        //String restOfTheLine = solver.lineN[solver.checkY].substring(hLength, 15 - hLength);
        SolverObject.lineN[SolverObject.checkY] = pickedWord + restOfTheLine;
        // Delete the used word from the array its in
        DeleteWordFromArray(getTheArray(hLength), pickedWord);
        // Assign the re-rewritten word to a variable
        String asd = SolverObject.lineN[SolverObject.checkY];
        // Type the hWord to the current x,y
        TypeTheBoard();
        SolverObject.checkY++;

        start5();
        return;
        //endregion
    }

    private void start5() {
        //region Check the (0, 3) letter and type a HORIZONTAL word with it // aiming to pick "RETINA" by chance // Done, working

        SolverObject.checkX = 0;
        SolverObject.checkY = 3; // go to (0, 3)
        char letter = read1Letter(SolverObject.checkX, SolverObject.checkY);
        // Get horizontal space if theres not a "@" in the following 3 characters
        int hLength = GetWordSpaceHorizontal(SolverObject.checkX, SolverObject.checkY);
        // Pick a word according to that length() gotten from the line above
        String pickedWord = PickWord(hLength);
        if (pickedWord == "") { return; }
        int tryN = 0;
        while ((pickedWord.charAt(0) != letter) && tryN < 41) {
            pickedWord = PickWord(hLength);
            if (pickedWord == "") { return; }
            if (tryN >= 40) {
                return;
            }
            tryN++;
        }

        // Re-write the solver.lineN[] array with the hWord included
        String restOfTheLine = SolverObject.lineN[SolverObject.checkY].substring(hLength, 15);
        SolverObject.lineN[SolverObject.checkY] = pickedWord + restOfTheLine;
        // Delete the used word from the array its in
        DeleteWordFromArray(getTheArray(hLength), pickedWord);
        // Assign the re-rewritten word to a variable
        String asd = SolverObject.lineN[SolverObject.checkY];
        // Type the hWord to the current x,y
        TypeTheBoard();
        SolverObject.checkY++;

        next0();
        return;
        //endregion
    }

    public void next0() { // Done
        //region Check 4 letters Type the VERTICAL word created at (1,0) (aiming to pick "MODERN" by chance) // Done, working

        SolverObject.checkX = 1;
        SolverObject.checkY = 0; // go to (1, 0)

        char letter1 = read1Letter(SolverObject.checkX, SolverObject.checkY);
        char letter2 = read1Letter(SolverObject.checkX, SolverObject.checkY + 1);
        char letter3 = read1Letter(SolverObject.checkX, SolverObject.checkY + 2);
        char letter4 = read1Letter(SolverObject.checkX, SolverObject.checkY + 3);
        // Get vertical space
        int vSpace = GetWordSpaceVertical(SolverObject.checkX, SolverObject.checkY);
        // Pick a word according to that length() gotten from the line above
        String pickedWord = PickWord(vSpace);
        if (pickedWord == "") { return; }
        // Assign the first x letters to the variables below to check after and for a cleaner code
        char let1 = pickedWord.charAt(0);
        char let2 = pickedWord.charAt(1);
        char let3 = pickedWord.charAt(2);
        char let4 = pickedWord.charAt(3);

        int tryN = 0;
        if (pickedWord != null) {
            if ((let1 == letter1) && (let2 == letter2) && (let3 == letter3) && (let4 == letter4)) // If "correct word" is picked, type it
            {
                SolverObject.checkX = 1;
                SolverObject.checkY = 0; // go to (1, 0)
                TypeVertically(pickedWord);
                // Delete the used word from the array its in
                DeleteWordFromArray(getTheArray(vSpace), pickedWord);

                next1();
                return;
            } else // If "correct word" is not picked, pick another word
            {
                while (tryN < 41) {
                    pickedWord = PickWord(vSpace);
                    if (pickedWord == "") { return; }
                    let1 = pickedWord.charAt(0);
                    let2 = pickedWord.charAt(1);
                    let3 = pickedWord.charAt(2);
                    let4 = pickedWord.charAt(3);
                    if ((let1 == letter1) && (let2 == letter2) && (let3 == letter3) && (let4 == letter4)) // If "correct word" is picked, type it
                    {
                        TypeVertically(pickedWord);
                        // Delete the used word from the array its in
                        DeleteWordFromArray(getTheArray(vSpace), pickedWord);

                        next1();
                        return;
                    }
                    tryN++;
                }
                return;
            }
        }


        //endregion
    }

    public void next1() {
        //region Check 4 letters Type the VERTICAL word created at (3,0) (aiming to pick "NATIVE" by chance) // Done, working

        SolverObject.checkX = 3;
        SolverObject.checkY = 0; // go to (1, 0)
        char letter1 = read1Letter(SolverObject.checkX, SolverObject.checkY);
        char letter2 = read1Letter(SolverObject.checkX, SolverObject.checkY + 1);
        char letter3 = read1Letter(SolverObject.checkX, SolverObject.checkY + 2);
        char letter4 = read1Letter(SolverObject.checkX, SolverObject.checkY + 3);
        // Get vertical space
        int vSpace = GetWordSpaceVertical(SolverObject.checkX, SolverObject.checkY);
        // Pick a word according to that length() gotten from the line above
        String pickedWord = PickWord(vSpace);
        if (pickedWord == "") { return; }
        // Assign the first x letters to the variables below to check after and for a cleaner code
        char let1 = pickedWord.charAt(0);
        char let2 = pickedWord.charAt(1);
        char let3 = pickedWord.charAt(2);
        char let4 = pickedWord.charAt(3);

        int tryN = 0;
        if (pickedWord != null) {
            if ((let1 == letter1) && (let2 == letter2) && (let3 == letter3) && (let4 == letter4)) // If "correct word" is picked, type it
            {
                TypeVertically(pickedWord);
                // Delete the used word from the array its in
                DeleteWordFromArray(getTheArray(vSpace), pickedWord);

                next2();
                return;
            } else // If "correct word" is not picked, pick another word
            {
                while (tryN < 41) {
                    pickedWord = PickWord(vSpace);
                    if (pickedWord == "") { return; }
                    let1 = pickedWord.charAt(0);
                    let2 = pickedWord.charAt(1);
                    let3 = pickedWord.charAt(2);
                    let4 = pickedWord.charAt(3);
                    if ((let1 == letter1) && (let2 == letter2) && (let3 == letter3) && (let4 == letter4)) // If "correct word" is picked, type it
                    {
                        TypeVertically(pickedWord);
                        // Delete the used word from the array its in
                        DeleteWordFromArray(getTheArray(vSpace), pickedWord);

                        next2();
                        return;
                    }
                    tryN++;
                }
                return;
            }
        }
    }

    public void next2() {
        //region Check 4 letters Type the VERTICAL word created at (4,0) (aiming to pick "ICINESS" by chance) // Done, working

        SolverObject.checkX = 4;
        SolverObject.checkY = 0; // go to (1, 0)

        char letter1 = read1Letter(SolverObject.checkX, SolverObject.checkY);
        char letter2 = read1Letter(SolverObject.checkX, SolverObject.checkY + 1);
        char letter3 = read1Letter(SolverObject.checkX, SolverObject.checkY + 2);
        char letter4 = read1Letter(SolverObject.checkX, SolverObject.checkY + 3);
        // Get vertical space
        int vSpace = GetWordSpaceVertical(SolverObject.checkX, SolverObject.checkY);
        // Pick a word according to that length() gotten from the line above
        String pickedWord = PickWord(vSpace);
        if (pickedWord == "") { return; }
        // Assign the first x letters to the variables below to check after and for a cleaner code
        char let1 = pickedWord.charAt(0);
        char let2 = pickedWord.charAt(1);
        char let3 = pickedWord.charAt(2);
        char let4 = pickedWord.charAt(3);

        int tryN = 0;
        if (pickedWord != null) {
            if ((let1 == letter1) && (let2 == letter2) && (let3 == letter3) && (let4 == letter4)) // If "correct word" is picked, type it
            {
                TypeVertically(pickedWord);
                // Delete the used word from the array its in
                DeleteWordFromArray(getTheArray(vSpace), pickedWord);

                next3();
                return;
            } else // If "correct word" is not picked, pick another word
            {
                while (tryN < 41) {
                    pickedWord = PickWord(vSpace);
                    if (pickedWord == "") { return; }
                    let1 = pickedWord.charAt(0);
                    let2 = pickedWord.charAt(1);
                    let3 = pickedWord.charAt(2);
                    let4 = pickedWord.charAt(3);
                    if ((let1 == letter1) && (let2 == letter2) && (let3 == letter3) && (let4 == letter4)) // If "correct word" is picked, type it
                    {
                        TypeVertically(pickedWord);
                        // Delete the used word from the array its in
                        DeleteWordFromArray(getTheArray(vSpace), pickedWord);

                        next3();
                        return;
                    }
                    tryN++;
                }
                return;
            }
        }


    }

    public void next3() {
        //region Check 4 letters Type the VERTICAL word created at (5,0) (aiming to pick "TENANT" by chance) // Done, working

        SolverObject.checkX = 5;
        SolverObject.checkY = 0; // go to (1, 0)

        char letter1 = read1Letter(SolverObject.checkX, SolverObject.checkY);
        char letter2 = read1Letter(SolverObject.checkX, SolverObject.checkY + 1);
        char letter3 = read1Letter(SolverObject.checkX, SolverObject.checkY + 2);
        char letter4 = read1Letter(SolverObject.checkX, SolverObject.checkY + 3);
        // Get vertical space
        int vSpace = GetWordSpaceVertical(SolverObject.checkX, SolverObject.checkY);
        // Pick a word according to that length() gotten from the line above
        String pickedWord = PickWord(vSpace);
        if (pickedWord == "") { return; }
        // Assign the first x letters to the variables below to check after and for a cleaner code
        char let1 = pickedWord.charAt(0);
        char let2 = pickedWord.charAt(1);
        char let3 = pickedWord.charAt(2);
        char let4 = pickedWord.charAt(3);

        int tryN = 0;
        if (pickedWord != null) {
            if ((let1 == letter1) && (let2 == letter2) && (let3 == letter3) && (let4 == letter4)) // If "correct word" is picked, type it
            {
                TypeVertically(pickedWord);
                // Delete the used word from the array its in
                DeleteWordFromArray(getTheArray(vSpace), pickedWord);

                next4();
                return;
            } else // If "correct word" is not picked, pick another word
            {
                while (tryN < 41) {
                    pickedWord = PickWord(vSpace);
                    if (pickedWord == "") { return; }
                    let1 = pickedWord.charAt(0);
                    let2 = pickedWord.charAt(1);
                    let3 = pickedWord.charAt(2);
                    let4 = pickedWord.charAt(3);
                    if ((let1 == letter1) && (let2 == letter2) && (let3 == letter3) && (let4 == letter4)) // If "correct word" is picked, type it
                    {
                        TypeVertically(pickedWord);
                        // Delete the used word from the array its in
                        DeleteWordFromArray(getTheArray(vSpace), pickedWord);

                        next4();
                    }
                    tryN++;
                }
                return;
            }
        }


        //endregion
    }
    //Working
    public void next4() {
        //region Check the (3, 4) 3 letter and type a HORIZONTAL word with it (aiming to pick "VENT" by chance) // Done, working

        SolverObject.checkX = 3;
        SolverObject.checkY = 4; // go to (3, 4)
        // read already written 3 letters
        char letter1 = read1Letter(SolverObject.checkX + 0, SolverObject.checkY);
        char letter2 = read1Letter(SolverObject.checkX + 1, SolverObject.checkY);
        char letter3 = read1Letter(SolverObject.checkX + 2, SolverObject.checkY);
        // Get horizontal space
        int hSpace = GetWordSpaceHorizontal(SolverObject.checkX, SolverObject.checkY);
        // Pick a word according to that length() gotten from the line above
        String pickedWord = PickWord(hSpace);
        if (pickedWord == "") { return; }
        // Assign the first x letters to the variables below to check after and for a cleaner code
        char let1 = pickedWord.charAt(0);
        char let2 = pickedWord.charAt(1);
        char let3 = pickedWord.charAt(2);

        int tryN = 0;
        if (pickedWord != null) {
            if ((let1 == letter1) && (let2 == letter2) && (let3 == letter3)) // If "correct word" is picked, type it
            {
                String d1 = SolverObject.lineN[SolverObject.checkY].substring(0, SolverObject.checkX); // doğru
                String d2 = SolverObject.lineN[SolverObject.checkY].substring(SolverObject.checkX + hSpace, 15); //fix that
                SolverObject.lineN[SolverObject.checkY] = d1 + pickedWord + d2;
                // Delete the used word from the array its in
                DeleteWordFromArray(getTheArray(hSpace), pickedWord);

                next5();
                return;
            } else // If "correct word" is not picked, pick another word
            {
                while (tryN < 41) {
                    pickedWord = PickWord(hSpace);
                    if (pickedWord == "") { return; }
                    let1 = pickedWord.charAt(0);
                    let2 = pickedWord.charAt(1);
                    let3 = pickedWord.charAt(2);
                    if ((let1 == letter1) && (let2 == letter2) && (let3 == letter3)) // If "correct word" is picked, type it
                    {
                        String L = SolverObject.lineN[SolverObject.checkY];
                        String d1 = SolverObject.lineN[SolverObject.checkY].substring(0, SolverObject.checkX); // doğru
                        String d2 = SolverObject.lineN[SolverObject.checkY].substring(SolverObject.checkX + hSpace, 15); //fix that
                        SolverObject.lineN[SolverObject.checkY] = d1 + pickedWord + d2;
                        // Delete the used word from the array its in
                        DeleteWordFromArray(getTheArray(hSpace), pickedWord);
                        TypeTheBoard();

                        next5();
                        return;
                    }
                    tryN++;
                }
                return;
            }
        }


        //endregion
    }

    public void next5() {
        //region Check the (0, 5) 4 letter and type a HORIZONTAL word with it (aiming to pick "INVESTOR" by chance) // Done, working

        SolverObject.checkX = 0;
        SolverObject.checkY = 5; // go to (0, 5)
        // read already written 3 letters
        char letter1 = read1Letter(SolverObject.checkX + 1, SolverObject.checkY);
        char letter2 = read1Letter(SolverObject.checkX + 3, SolverObject.checkY);
        char letter3 = read1Letter(SolverObject.checkX + 4, SolverObject.checkY);
        char letter4 = read1Letter(SolverObject.checkX + 5, SolverObject.checkY);
        // Get horizontal space
        int hSpace = GetWordSpaceHorizontal(SolverObject.checkX, SolverObject.checkY);
        // Pick a word according to that length() gotten from the line above
        String pickedWord = PickWord(hSpace);
        if (pickedWord == "") { return; }
        // Assign the first x letters to the variables below to check after and for a cleaner code
        char let1 = pickedWord.charAt(1);
        char let2 = pickedWord.charAt(3);
        char let3 = pickedWord.charAt(4);
        char let4 = pickedWord.charAt(5);

        int tryN = 0;
        if (pickedWord != null) {
            if ((let1 == letter1) && (let2 == letter2) && (let3 == letter3) && (let4 == letter4)) // If "correct word" is picked, type it
            {
                String d1 = SolverObject.lineN[SolverObject.checkY].substring(0, SolverObject.checkX); // doğru
                String d2 = SolverObject.lineN[SolverObject.checkY].substring(SolverObject.checkX + hSpace, 15); //fix that
                SolverObject.lineN[SolverObject.checkY] = d1 + pickedWord + d2;
                // Delete the used word from the array its in
                DeleteWordFromArray(getTheArray(hSpace), pickedWord);
                TypeTheBoard();

                next6();
                return;
            } else // If "correct word" is not picked, pick another word
            {
                while (tryN < 41) {
                    pickedWord = PickWord(hSpace);
                    if (pickedWord == "") { return; }
                    let1 = pickedWord.charAt(0);
                    let2 = pickedWord.charAt(1);
                    let3 = pickedWord.charAt(2);
                    if ((let1 == letter1) && (let2 == letter2) && (let3 == letter3)) // If "correct word" is picked, type it
                    {
                        String d1 = SolverObject.lineN[SolverObject.checkY].substring(0, SolverObject.checkX); // doğru
                        String d2 = SolverObject.lineN[SolverObject.checkY].substring(SolverObject.checkX + hSpace, 15); //fix that
                        SolverObject.lineN[SolverObject.checkY] = d1 + pickedWord + d2;
                        // Delete the used word from the array its in
                        DeleteWordFromArray(getTheArray(hSpace), pickedWord);
                        TypeTheBoard();

                        next6();
                        return;
                    }
                    tryN++;
                }
                return;
            }
        }


        //endregion
    }

    public void next6() {
        //region Check the First VERTICAL Letter at (0,5) and pick a word that starts with that letter to type with it (Aiming to pick "IVORY" by chance) // Done, working

        SolverObject.checkX = 0;
        SolverObject.checkY = 5; // go to (0, 5)

        char letter = read1Letter(SolverObject.checkX, SolverObject.checkY);
        int vSpace = GetWordSpaceVertical(SolverObject.checkX, SolverObject.checkY);
        String pickedWord = PickWord(vSpace);
        if (pickedWord == "") { return; }

        int tryN = 0;
        if (pickedWord != "" && pickedWord != null) {
            while ((pickedWord.charAt(0) != letter) && tryN < 41) {
                pickedWord = PickWord(vSpace);
                if (pickedWord == "") { return; }
                if (tryN >= 40) {
                    return;
                }
                tryN++;
            }
        }
        TypeVertically(pickedWord);
        // Delete the used word from the array its in
        DeleteWordFromArray(getTheArray(vSpace), pickedWord);

        next7();
        return;
        //endregion
    }

    public void next7() {
        //region Check the (0, 7) letter and type a HORIZONTAL word with it // aiming to pick "OARS" by chance // Done working

        SolverObject.checkX = 0;
        SolverObject.checkY = 7; // go to (0, 7)

        char letter = read1Letter(SolverObject.checkX, SolverObject.checkY);
        // Get horizontal space if theres not a "@" in the following 3 characters
        int hLength = GetWordSpaceHorizontal(SolverObject.checkX, SolverObject.checkY);
        // Pick a word according to that length() gotten from the line above
        String pickedWord = PickWord(hLength);
        if (pickedWord == "") { return; }
        int tryN = 0;
        while ((pickedWord.charAt(0) != letter) && tryN < 41) {
            pickedWord = PickWord(hLength);
            if (pickedWord == "") { return; }
            if (tryN >= 40) {
                return;
            }
            tryN++;
        }

        // Re-write the solver.lineN[] array with the hWord included
        String restOfTheLine = SolverObject.lineN[SolverObject.checkY].substring(hLength, 15);
        SolverObject.lineN[SolverObject.checkY] = pickedWord + restOfTheLine;
        // Delete the used word from the array its in
        DeleteWordFromArray(getTheArray(hLength), pickedWord);
        // Assign the re-rewritten word to a variable
        String asd = SolverObject.lineN[SolverObject.checkY];
        // Type the hWord to the current x,y
        TypeTheBoard();
        SolverObject.checkY++;

        next8();
        return;
        //endregion
    }
    // Buradan üstü çalışıyor
    public void next8() {
        //region Check 2 letters Type the VERTICAL word created at (2,5) (aiming to pick "VIRTUOSITY" by chance) // Done, working
        {
            SolverObject.checkX = 2;
            SolverObject.checkY = 5; // go to (2, 5)
            char letter1 = read1Letter(SolverObject.checkX, SolverObject.checkY);
            char letter2 = read1Letter(SolverObject.checkX, SolverObject.checkY + 2);
            // Get vertical space
            int vSpace = GetWordSpaceVertical(SolverObject.checkX, SolverObject.checkY);
            // Pick a word according to that length() gotten from the line above
            String pickedWord = PickWord(vSpace);
            if (pickedWord == "") { return; }
            // Assign the first x letters to the variables below to check after and for a cleaner code
            char let1 = pickedWord.charAt(0);
            char let2 = pickedWord.charAt(2);

            int tryN = 0;
            if (pickedWord != null) {
                if ((let1 == letter1) && (let2 == letter2)) // If "correct word" is picked, type it
                {
                    TypeVertically(pickedWord);
                    // Delete the used word from the array its in
                    DeleteWordFromArray(getTheArray(vSpace), pickedWord);

                    next9();
                    return;
                } else // If "correct word" is not picked, pick another word
                {
                    while (tryN < 41) {
                        pickedWord = PickWord(vSpace);
                        if (pickedWord == "") { return; }
                        let1 = pickedWord.charAt(0);
                        let2 = pickedWord.charAt(1);
                        if ((let1 == letter1) && (let2 == letter2)) // If "correct word" is picked, type it
                        {
                            TypeVertically(pickedWord);
                            // Delete the used word from the array its in
                            DeleteWordFromArray(getTheArray(vSpace), pickedWord);

                            next9();
                            return;
                        }
                        tryN++;
                    }
                    return;
                }
            }

        }
        //endregion
    }

    public void next9() {
        //region Check the (0, 9) letter and type a HORIZONTAL word with it // aiming to pick "YOUR" by chance // Done working
        {
            SolverObject.checkX = 0;
            SolverObject.checkY = 9; // go to (0, 9)

            char letter = read1Letter(SolverObject.checkX, SolverObject.checkY);
            // Get horizontal space if theres not a "@" in the following 3 characters
            int hLength = GetWordSpaceHorizontal(SolverObject.checkX, SolverObject.checkY);
            // Pick a word according to that length() gotten from the line above
            String pickedWord = PickWord(hLength);
            if (pickedWord == "") { return; }
            int tryN = 0;
            while ((pickedWord.charAt(0) != letter) && tryN < 41) {
                pickedWord = PickWord(hLength);
                if (pickedWord == "") { return; }
                if (tryN >= 40) {
                    return;
                }
                tryN++;
            }

            // Re-write the solver.lineN[] array with the hWord included
            String restOfTheLine = SolverObject.lineN[SolverObject.checkY].substring(hLength, 15);
            SolverObject.lineN[SolverObject.checkY] = pickedWord + restOfTheLine;
            // Delete the used word from the array its in
            DeleteWordFromArray(getTheArray(hLength), pickedWord);
            // Assign the re-rewritten word to a variable
            String asd = SolverObject.lineN[SolverObject.checkY];
            // Type the hWord to the current x,y
            TypeTheBoard();
            SolverObject.checkY++;
            next10();
            return;
        }
        //endregion
    }

    public void next10() {
        //region Check the (0, 8) 4 letter and type a HORIZONTAL word with it (aiming to pick "RETURNING" by chance)
        {
            SolverObject.checkX = 0;
            SolverObject.checkY = 8; // go to (0, 8)
            // read already written 3 letters
            char letter1 = read1Letter(SolverObject.checkX, SolverObject.checkY);
            char letter2 = read1Letter(SolverObject.checkX + 2, SolverObject.checkY);
            // Get horizontal space
            int hSpace = GetWordSpaceHorizontal(SolverObject.checkX, SolverObject.checkY);
            // Pick a word according to that length() gotten from the line above
            String pickedWord = PickWord(hSpace);
            if (pickedWord == "") { return; }
            // Assign the first x letters to the variables below to check after and for a cleaner code
            char let1 = pickedWord.charAt(0);
            char let2 = pickedWord.charAt(2);

            int tryN = 0;
            if (pickedWord != null) {
                if ((let1 == letter1) && (let2 == letter2)) // If "correct word" is picked, type it
                {
                    String d1 = SolverObject.lineN[SolverObject.checkY].substring(0, SolverObject.checkX); // doğru
                    String d2 = SolverObject.lineN[SolverObject.checkY].substring(SolverObject.checkX + hSpace, 15); //fix that
                    SolverObject.lineN[SolverObject.checkY] = d1 + pickedWord + d2;
                    // Delete the used word from the array its in
                    DeleteWordFromArray(getTheArray(hSpace), pickedWord);
                    TypeTheBoard();

                    next11();
                    return;
                } else // If "correct word" is not picked, pick another word
                {
                    while (tryN < 41) {
                        pickedWord = PickWord(hSpace);
                        if (pickedWord == "") { return; }
                        let1 = pickedWord.charAt(0);
                        let2 = pickedWord.charAt(2);
                        if ((let1 == letter1) && (let2 == letter2)) // If "correct word" is picked, type it
                        {
                            String d1 = SolverObject.lineN[SolverObject.checkY].substring(0, SolverObject.checkX); // doğru
                            String d2 = SolverObject.lineN[SolverObject.checkY].substring(SolverObject.checkX + hSpace, 15); //fix that
                            SolverObject.lineN[SolverObject.checkY] = d1 + pickedWord + d2;
                            // Delete the used word from the array its in
                            DeleteWordFromArray(getTheArray(hSpace), pickedWord);
                            TypeTheBoard();

                            next11();
                            return;
                        }
                        tryN++;
                    }
                    return;
                }
            }

        }
        //endregion
    }

    public void next11() {
        //region Check 3 letters Type the VERTICAL word created at (3,7) (aiming to pick "SURVIVAL" by chance) // Done, working
        {
            SolverObject.checkX = 3;
            SolverObject.checkY = 7; // go to (3, 7)
            char letter1 = read1Letter(SolverObject.checkX, SolverObject.checkY);
            char letter2 = read1Letter(SolverObject.checkX, SolverObject.checkY + 1);
            char letter3 = read1Letter(SolverObject.checkX, SolverObject.checkY + 2);
            // Get vertical space
            int vSpace = GetWordSpaceVertical(SolverObject.checkX, SolverObject.checkY);
            // Pick a word according to that length() gotten from the line above
            String pickedWord = PickWord(vSpace);
            if (pickedWord == "") { return; }
            // Assign the first x letters to the variables below to check after and for a cleaner code
            char let1 = pickedWord.charAt(0);
            char let2 = pickedWord.charAt(1);
            char let3 = pickedWord.charAt(2);

            int tryN = 0;
            if (pickedWord != null) {
                if ((let1 == letter1) && (let2 == letter2) && (let3 == letter3)) // If "correct word" is picked, type it
                {
                    TypeVertically(pickedWord);
                    // Delete the used word from the array its in
                    DeleteWordFromArray(getTheArray(vSpace), pickedWord);

                    next12();
                    return;
                } else // If "correct word" is not picked, pick another word
                {
                    while (tryN < 41) {
                        pickedWord = PickWord(vSpace);
                        if (pickedWord == "") { return; }
                        let1 = pickedWord.charAt(0);
                        let2 = pickedWord.charAt(1);
                        let3 = pickedWord.charAt(2);
                        if ((let1 == letter1) && (let2 == letter2) && (let3 == letter3)) // If "correct word" is picked, type it
                        {
                            TypeVertically(pickedWord);
                            // Delete the used word from the array its in
                            DeleteWordFromArray(getTheArray(vSpace), pickedWord);

                            next12();
                            return;
                        }
                        tryN++;
                    }
                    return;
                }
            }

        }
        //endregion
    }

    public void next12() {
        //region Check the (1, 10) 1 letter and type a HORIZONTAL word with it (aiming to pick "NOVELS" by chance)
        {
            SolverObject.checkX = 1;
            SolverObject.checkY = 10; // go to (1, 10)
            // read already written 3 letters
            char letter1 = read1Letter(SolverObject.checkX + 1, SolverObject.checkY);
            char letter2 = read1Letter(SolverObject.checkX + 2, SolverObject.checkY);
            // Get horizontal space
            int hSpace = GetWordSpaceHorizontal(SolverObject.checkX, SolverObject.checkY);
            // Pick a word according to that length() gotten from the line above
            String pickedWord = PickWord(hSpace);
            if (pickedWord == "") { return; }
            // Assign the first x letters to the variables below to check after and for a cleaner code
            char let1 = pickedWord.charAt(1);
            char let2 = pickedWord.charAt(2);

            int tryN = 0;
            if (pickedWord != null) {
                if ((let1 == letter1) && (let2 == letter2)) // If "correct word" is picked, type it
                {
                    String d1 = SolverObject.lineN[SolverObject.checkY].substring(0, SolverObject.checkX); // doğru
                    String d2 = SolverObject.lineN[SolverObject.checkY].substring(SolverObject.checkX + hSpace, 15); //fix that
                    SolverObject.lineN[SolverObject.checkY] = d1 + pickedWord + d2;
                    // Delete the used word from the array its in
                    DeleteWordFromArray(getTheArray(hSpace), pickedWord);
                    TypeTheBoard();

                    next13();
                    return;
                } else // If "correct word" is not picked, pick another word
                {
                    while (tryN < 41) {
                        pickedWord = PickWord(hSpace);
                        if (pickedWord == "") { return; }
                        let1 = pickedWord.charAt(1);
                        let2 = pickedWord.charAt(2);
                        if ((let1 == letter1) && (let2 == letter2)) // If "correct word" is picked, type it
                        {
                            String d1 = SolverObject.lineN[SolverObject.checkY].substring(0, SolverObject.checkX); // doğru
                            String d2 = SolverObject.lineN[SolverObject.checkY].substring(SolverObject.checkX + hSpace, 15); //fix that
                            SolverObject.lineN[SolverObject.checkY] = d1 + pickedWord + d2;
                            // Delete the used word from the array its in
                            DeleteWordFromArray(getTheArray(hSpace), pickedWord);
                            TypeTheBoard();

                            next13();
                            return;
                        }
                        tryN++;
                    }
                    return;
                }
            }

        }
        //endregion
    }

    public void next13() {
        //region Check the (2, 11) 2 letter and type a HORIZONTAL word with it (aiming to pick "SIMOOM" by chance)
        {
            SolverObject.checkX = 2;
            SolverObject.checkY = 11; // go to (1, 10)
            // read already written 3 letters
            char letter1 = read1Letter(SolverObject.checkX, SolverObject.checkY);
            char letter2 = read1Letter(SolverObject.checkX + 1, SolverObject.checkY);
            // Get horizontal space
            int hSpace = GetWordSpaceHorizontal(SolverObject.checkX, SolverObject.checkY);
            // Pick a word according to that length() gotten from the line above
            String pickedWord = PickWord(hSpace);
            if (pickedWord == "") { return; }
            // Assign the first x letters to the variables below to check after and for a cleaner code
            char let1 = pickedWord.charAt(0);
            char let2 = pickedWord.charAt(1);

            int tryN = 0;
            if (pickedWord != null) {
                if ((let1 == letter1) && (let2 == letter2)) // If "correct word" is picked, type it
                {
                    String d1 = SolverObject.lineN[SolverObject.checkY].substring(0, SolverObject.checkX); // doğru
                    String d2 = SolverObject.lineN[SolverObject.checkY].substring(SolverObject.checkX + hSpace, 15); //fix that
                    SolverObject.lineN[SolverObject.checkY] = d1 + pickedWord + d2;
                    // Delete the used word from the array its in
                    DeleteWordFromArray(getTheArray(hSpace), pickedWord);
                    TypeTheBoard();

                    next14();
                    return;
                } else // If "correct word" is not picked, pick another word
                {
                    while (tryN < 41) {
                        pickedWord = PickWord(hSpace);
                        if (pickedWord == "") { return; }
                        let1 = pickedWord.charAt(0);
                        let2 = pickedWord.charAt(1);
                        if ((let1 == letter1) && (let2 == letter2)) // If "correct word" is picked, type it
                        {
                            String d1 = SolverObject.lineN[SolverObject.checkY].substring(0, SolverObject.checkX); // doğru
                            String d2 = SolverObject.lineN[SolverObject.checkY].substring(SolverObject.checkX + hSpace, 15); //fix that
                            SolverObject.lineN[SolverObject.checkY] = d1 + pickedWord + d2;
                            // Delete the used word from the array its in
                            DeleteWordFromArray(getTheArray(hSpace), pickedWord);
                            TypeTheBoard();

                            next14();
                            return;
                        }
                        tryN++;
                    }
                    return;
                }
            }

        }
        //endregion
    }

    public void next14() {
        //region Check 2 letters Type the VERTICAL word created at (4,10) (aiming to pick "EMILE" by chance) // Done, working
        {
            SolverObject.checkX = 4;
            SolverObject.checkY = 10; // go to (4, 10)
            char letter1 = read1Letter(SolverObject.checkX, SolverObject.checkY);
            char letter2 = read1Letter(SolverObject.checkX, SolverObject.checkY + 1);
            // Get vertical space
            int vSpace = GetWordSpaceVertical(SolverObject.checkX, SolverObject.checkY);
            // Pick a word according to that length() gotten from the line above
            String pickedWord = PickWord(vSpace);
            if (pickedWord == "") { return; }
            // Assign the first x letters to the variables below to check after and for a cleaner code
            char let1 = pickedWord.charAt(0);
            char let2 = pickedWord.charAt(1);

            int tryN = 0;
            if (pickedWord != null) {
                if ((let1 == letter1) && (let2 == letter2)) // If "correct word" is picked, type it
                {
                    TypeVertically(pickedWord);
                    // Delete the used word from the array its in
                    DeleteWordFromArray(getTheArray(vSpace), pickedWord);

                    next15();
                    return;
                } else // If "correct word" is not picked, pick another word
                {
                    while (tryN < 41) {
                        pickedWord = PickWord(vSpace);
                        if (pickedWord == "") { return; }
                        let1 = pickedWord.charAt(0);
                        let2 = pickedWord.charAt(1);
                        if ((let1 == letter1) && (let2 == letter2)) // If "correct word" is picked, type it
                        {
                            TypeVertically(pickedWord);
                            // Delete the used word from the array its in
                            DeleteWordFromArray(getTheArray(vSpace), pickedWord);

                            next15();
                            return;
                        }
                        tryN++;
                    }
                    return;
                }
            }

        }
        //endregion
    }

    public void next15() {
        //region Check the (0, 14) 2 letter and type a HORIZONTAL word with it (aiming to pick "RAYLESS" by chance)
        {
            SolverObject.checkX = 0;
            SolverObject.checkY = 14; // go to (0, 14)
            // read already written 3 letters
            char letter1 = read1Letter(SolverObject.checkX + 2, SolverObject.checkY);
            char letter2 = read1Letter(SolverObject.checkX + 3, SolverObject.checkY);
            char letter3 = read1Letter(SolverObject.checkX + 4, SolverObject.checkY);
            // Get horizontal space
            int hSpace = GetWordSpaceHorizontal(SolverObject.checkX, SolverObject.checkY);
            // Pick a word according to that length() gotten from the line above
            String pickedWord = PickWord(hSpace);
            if (pickedWord == "") { return; }
            // Assign the first x letters to the variables below to check after and for a cleaner code
            char let1 = pickedWord.charAt(2);
            char let2 = pickedWord.charAt(3);
            char let3 = pickedWord.charAt(4);

            int tryN = 0;
            if (pickedWord != null) {
                if ((let1 == letter1) && (let2 == letter2) && (let3 == letter3)) // If "correct word" is picked, type it
                {
                    String d1 = SolverObject.lineN[SolverObject.checkY].substring(0, SolverObject.checkX); // doğru
                    String d2 = SolverObject.lineN[SolverObject.checkY].substring(SolverObject.checkX + hSpace, 15); //fix that
                    SolverObject.lineN[SolverObject.checkY] = d1 + pickedWord + d2;
                    // Delete the used word from the array its in
                    DeleteWordFromArray(getTheArray(hSpace), pickedWord);
                    TypeTheBoard();

                    next16();
                    return;
                } else // If "correct word" is not picked, pick another word
                {
                    while (tryN < 41) {
                        pickedWord = PickWord(hSpace);
                        if (pickedWord == "") { return; }
                        let1 = pickedWord.charAt(2);
                        let2 = pickedWord.charAt(3);
                        let3 = pickedWord.charAt(4);
                        if ((let1 == letter1) && (let2 == letter2) && (let3 == letter3)) // If "correct word" is picked, type it
                        {
                            String d1 = SolverObject.lineN[SolverObject.checkY].substring(0, SolverObject.checkX); // doğru
                            String d2 = SolverObject.lineN[SolverObject.checkY].substring(SolverObject.checkX + hSpace, 15); //fix that
                            SolverObject.lineN[SolverObject.checkY] = d1 + pickedWord + d2;
                            // Delete the used word from the array its in
                            DeleteWordFromArray(getTheArray(hSpace), pickedWord);
                            TypeTheBoard();

                            next16();
                            return;
                        }
                        tryN++;
                    }
                    return;

                }
            }

        }
        //endregion
    }

    public void next16() {
        //region Check the (0, 12) 2 letter and type a HORIZONTAL word with it (aiming to pick "TRIVIAL" by chance)
        {
            SolverObject.checkX = 0;
            SolverObject.checkY = 12; // go to (0, 12)
            // read already written 3 letters
            char letter1 = read1Letter(SolverObject.checkX + 2, SolverObject.checkY);
            char letter2 = read1Letter(SolverObject.checkX + 3, SolverObject.checkY);
            char letter3 = read1Letter(SolverObject.checkX + 4, SolverObject.checkY);
            // Get horizontal space
            int hSpace = GetWordSpaceHorizontal(SolverObject.checkX, SolverObject.checkY);
            // Pick a word according to that length() gotten from the line above
            String pickedWord = PickWord(hSpace);
            if (pickedWord == "") { return; }
            // Assign the first x letters to the variables below to check after and for a cleaner code
            char let1 = pickedWord.charAt(2);
            char let2 = pickedWord.charAt(3);
            char let3 = pickedWord.charAt(4);

            int tryN = 0;
            if (pickedWord != null) {
                if ((let1 == letter1) && (let2 == letter2) && (let3 == letter3)) // If "correct word" is picked, type it
                {
                    String d1 = SolverObject.lineN[SolverObject.checkY].substring(0, SolverObject.checkX); // doğru
                    String d2 = SolverObject.lineN[SolverObject.checkY].substring(SolverObject.checkX + hSpace, 15); //fix that
                    SolverObject.lineN[SolverObject.checkY] = d1 + pickedWord + d2;
                    // Delete the used word from the array its in
                    DeleteWordFromArray(getTheArray(hSpace), pickedWord);
                    TypeTheBoard();

                    next17();
                    return;
                } else // If "correct word" is not picked, pick another word
                {
                    while (tryN < 41) {
                        pickedWord = PickWord(hSpace);
                        if (pickedWord == "") { return; }
                        let1 = pickedWord.charAt(2);
                        let2 = pickedWord.charAt(3);
                        let3 = pickedWord.charAt(4);

                        if ((let1 == letter1) && (let2 == letter2) && (let3 == letter3)) // If "correct word" is picked, type it
                        {
                            String d1 = SolverObject.lineN[SolverObject.checkY].substring(0, SolverObject.checkX); // doğru
                            String d2 = SolverObject.lineN[SolverObject.checkY].substring(SolverObject.checkX + hSpace, 15); //fix that
                            SolverObject.lineN[SolverObject.checkY] = d1 + pickedWord + d2;
                            // Delete the used word from the array its in
                            DeleteWordFromArray(getTheArray(hSpace), pickedWord);
                            TypeTheBoard();

                            next17();
                            return;
                        }
                        tryN++;
                    }
                    return;
                }
            }

        }
        //endregion
    }

    public void next17() {
        //region Check 2 letter Type the VERTICAL word created at (0,11) (aiming to pick "STIR" by chance) // Done, working
        {
            SolverObject.checkX = 0;
            SolverObject.checkY = 11; // go to (4, 10)

            char letter1 = read1Letter(SolverObject.checkX, SolverObject.checkY + 1);
            char letter2 = read1Letter(SolverObject.checkX, SolverObject.checkY + 3);
            // Get vertical space
            int vSpace = GetWordSpaceVertical(SolverObject.checkX, SolverObject.checkY);
            // Pick a word according to that length() gotten from the line above
            String pickedWord = PickWord(vSpace);
            if (pickedWord == "") { return; }
            // Assign the first x letters to the variables below to check after and for a cleaner code
            char let1 = pickedWord.charAt(1);
            char let2 = pickedWord.charAt(3);

            int tryN = 0;
            if (pickedWord != null) {
                if ((let1 == letter1) && (let2 == letter2)) // If "correct word" is picked, type it
                {
                    TypeVertically(pickedWord);
                    // Delete the used word from the array its in
                    DeleteWordFromArray(getTheArray(vSpace), pickedWord);

                    next18();
                    return;
                } else // If "correct word" is not picked, pick another word
                {
                    while (tryN < 41) {
                        pickedWord = PickWord(vSpace);
                        if (pickedWord == "") { return; }
                        let1 = pickedWord.charAt(1);
                        let2 = pickedWord.charAt(3);

                        if ((let1 == letter1) && (let2 == letter2)) // If "correct word" is picked, type it
                        {
                            TypeVertically(pickedWord);
                            // Delete the used word from the array its in
                            DeleteWordFromArray(getTheArray(vSpace), pickedWord);

                            next18();
                            return;
                        }
                        tryN++;
                    }
                    return;
                }
            }

        }
        //endregion
    }

    public void next18() {
        //region Check 5 letter Type the VERTICAL word created at (5,7) (aiming to pick "UNCLOAKS" by chance) // Done, working
        {
            SolverObject.checkX = 5;
            SolverObject.checkY = 7; // go to (5, 7)

            char letter1 = read1Letter(SolverObject.checkX, SolverObject.checkY + 1);
            char letter2 = read1Letter(SolverObject.checkX, SolverObject.checkY + 3);
            char letter3 = read1Letter(SolverObject.checkX, SolverObject.checkY + 4);
            char letter4 = read1Letter(SolverObject.checkX, SolverObject.checkY + 5);
            char letter5 = read1Letter(SolverObject.checkX, SolverObject.checkY + 7);
            // Get vertical space
            int vSpace = GetWordSpaceVertical(SolverObject.checkX, SolverObject.checkY);
            // Pick a word according to that length() gotten from the line above
            String pickedWord = PickWord(vSpace);
            if (pickedWord == "") { return; }
            // Assign the first x letters to the variables below to check after and for a cleaner code
            char let1 = pickedWord.charAt(1);
            char let2 = pickedWord.charAt(3);
            char let3 = pickedWord.charAt(4);
            char let4 = pickedWord.charAt(5);
            char let5 = pickedWord.charAt(7);

            int tryN = 0;
            if (pickedWord != null) {
                if ((let1 == letter1) && (let2 == letter2) && (let3 == letter3) && (let4 == letter4) && (let5 == letter5)) // If "correct word" is picked, type it
                {
                    TypeVertically(pickedWord);
                    // Delete the used word from the array its in
                    DeleteWordFromArray(getTheArray(vSpace), pickedWord);

                    next19();
                    return;
                } else // If "correct word" is not picked, pick another word
                {
                    while (tryN < 41) {
                        pickedWord = PickWord(vSpace);
                        if (pickedWord == "") { return; }
                        let1 = pickedWord.charAt(1);
                        let2 = pickedWord.charAt(3);
                        let3 = pickedWord.charAt(4);
                        let4 = pickedWord.charAt(5);
                        let5 = pickedWord.charAt(7);
                        if ((let1 == letter1) && (let2 == letter2) && (let3 == letter3) && (let4 == letter4) && (let5 == letter5)) // If "correct word" is picked, type it
                        {
                            TypeVertically(pickedWord);
                            // Delete the used word from the array its in
                            DeleteWordFromArray(getTheArray(vSpace), pickedWord);

                            next19();
                            return;
                        }
                        tryN++;
                    }
                    return;
                }
            }

        }
        //endregion
    }

    public void next19() {
        //region Check the (2, 13) 3 letter and type a HORIZONTAL word with it (aiming to pick "TALKER" by chance)
        {
            SolverObject.checkX = 2;
            SolverObject.checkY = 13; // go to (2, 13)
            // read already written 3 letters
            char letter1 = read1Letter(SolverObject.checkX, SolverObject.checkY);
            char letter2 = read1Letter(SolverObject.checkX + 1, SolverObject.checkY);
            char letter3 = read1Letter(SolverObject.checkX + 2, SolverObject.checkY);
            char letter4 = read1Letter(SolverObject.checkX + 3, SolverObject.checkY);
            // Get horizontal space
            int hSpace = GetWordSpaceHorizontal(SolverObject.checkX, SolverObject.checkY);
            // Pick a word according to that length() gotten from the line above
            String pickedWord = PickWord(hSpace);
            if (pickedWord == "") { return; }
            // Assign the first x letters to the variables below to check after and for a cleaner code
            char let1 = pickedWord.charAt(0);
            char let2 = pickedWord.charAt(1);
            char let3 = pickedWord.charAt(2);
            char let4 = pickedWord.charAt(3);

            int tryN = 0;
            if (pickedWord != null) {
                if ((let1 == letter1) && (let2 == letter2) && (let3 == letter3) && (let4 == letter4)) // If "correct word" is picked, type it
                {
                    String d1 = SolverObject.lineN[SolverObject.checkY].substring(0, SolverObject.checkX); // doğru
                    String d2 = SolverObject.lineN[SolverObject.checkY].substring(SolverObject.checkX + hSpace, 15); //fix that
                    SolverObject.lineN[SolverObject.checkY] = d1 + pickedWord + d2;
                    // Delete the used word from the array its in
                    DeleteWordFromArray(getTheArray(hSpace), pickedWord);
                    TypeTheBoard();

                    next20();
                    return;
                } else // If "correct word" is not picked, pick another word
                {
                    while (tryN < 41) {
                        pickedWord = PickWord(hSpace);
                        if (pickedWord == "") { return; }
                        let1 = pickedWord.charAt(0);
                        let2 = pickedWord.charAt(1);
                        let3 = pickedWord.charAt(2);
                        let4 = pickedWord.charAt(3);
                        if ((let1 == letter1) && (let2 == letter2) && (let3 == letter3) && (let4 == letter4)) // If "correct word" is picked, type it
                        {
                            String d1 = SolverObject.lineN[SolverObject.checkY].substring(0, SolverObject.checkX); // doğru
                            String d2 = SolverObject.lineN[SolverObject.checkY].substring(SolverObject.checkX + hSpace, 15); //fix that
                            SolverObject.lineN[SolverObject.checkY] = d1 + pickedWord + d2;
                            // Delete the used word from the array its in
                            DeleteWordFromArray(getTheArray(hSpace), pickedWord);
                            TypeTheBoard();

                            next20();
                            return;
                        }
                        tryN++;
                    }
                    return;
                }
            }

        }
        //endregion
    }

    public void next20() {
        //region Check 2 letter Type the VERTICAL word created at (6,4) (aiming to pick "TOE" by chance) // Done, working
        {
            SolverObject.checkX = 6;
            SolverObject.checkY = 4; // go to (6, 4)

            char letter1 = read1Letter(SolverObject.checkX, SolverObject.checkY);
            char letter2 = read1Letter(SolverObject.checkX, SolverObject.checkY + 1);
            // Get vertical space
            int vSpace = GetWordSpaceVertical(SolverObject.checkX, SolverObject.checkY);
            // Pick a word according to that length() gotten from the line above
            String pickedWord = PickWord(vSpace);
            if (pickedWord == "") { return; }
            // Assign the first x letters to the variables below to check after and for a cleaner code
            char let1 = pickedWord.charAt(0);
            char let2 = pickedWord.charAt(1);

            int tryN = 0;
            if (pickedWord != null) {
                if ((let1 == letter1) && (let2 == letter2)) // If "correct word" is picked, type it
                {
                    TypeVertically(pickedWord);
                    // Delete the used word from the array its in
                    DeleteWordFromArray(getTheArray(vSpace), pickedWord);

                    next21();
                    return;
                } else // If "correct word" is not picked, pick another word
                {
                    while (tryN < 41) {
                        pickedWord = PickWord(vSpace);
                        if (pickedWord == "") { return; }
                        let1 = pickedWord.charAt(0);
                        let2 = pickedWord.charAt(1);

                        if ((let1 == letter1) && (let2 == letter2)) // If "correct word" is picked, type it
                        {
                            TypeVertically(pickedWord);
                            // Delete the used word from the array its in
                            DeleteWordFromArray(getTheArray(vSpace), pickedWord);

                            next21();
                            return;
                        }
                        tryN++;
                    }
                    return;
                }
            }

        }
        //endregion
    }

    public void next21() {
        //region Check 2 letter Type the VERTICAL word created at (7,5) (aiming to pick "RAINS" by chance) // Done, working
        {
            SolverObject.checkX = 7;
            SolverObject.checkY = 5; // go to (7, 5)

            char letter1 = read1Letter(SolverObject.checkX, SolverObject.checkY);
            char letter2 = read1Letter(SolverObject.checkX, SolverObject.checkY + 3);
            // Get vertical space
            int vSpace = GetWordSpaceVertical(SolverObject.checkX, SolverObject.checkY);
            // Pick a word according to that length() gotten from the line above
            String pickedWord = PickWord(vSpace);
            if (pickedWord == "") { return; }
            // Assign the first x letters to the variables below to check after and for a cleaner code
            char let1 = pickedWord.charAt(0);
            char let2 = pickedWord.charAt(3);

            int tryN = 0;
            if (pickedWord != null) {
                if ((let1 == letter1) && (let2 == letter2)) // If "correct word" is picked, type it
                {
                    TypeVertically(pickedWord);
                    // Delete the used word from the array its in
                    DeleteWordFromArray(getTheArray(vSpace), pickedWord);

                    next22();
                    return;
                } else // If "correct word" is not picked, pick another word
                {
                    while (tryN < 41) {
                        pickedWord = PickWord(vSpace);
                        if (pickedWord == "") { return; }
                        let1 = pickedWord.charAt(0);
                        let2 = pickedWord.charAt(3);

                        if ((let1 == letter1) && (let2 == letter2)) // If "correct word" is picked, type it
                        {
                            TypeVertically(pickedWord);
                            // Delete the used word from the array its in
                            DeleteWordFromArray(getTheArray(vSpace), pickedWord);

                            next22();
                            return;
                        }
                        tryN++;
                    }
                    return;
                }
            }

        }
        //endregion
    }

    public void next22() {
        //region Check the (6, 6) 2 letter and type a HORIZONTAL word with it (aiming to pick "EASTEREGG" by chance) // Done, working
        {
            SolverObject.checkX = 6;
            SolverObject.checkY = 6; // go to (6, 6)
            // read already written 2 letters
            char letter1 = read1Letter(SolverObject.checkX, SolverObject.checkY);
            char letter2 = read1Letter(SolverObject.checkX + 1, SolverObject.checkY);
            // Get horizontal space
            int hSpace = GetWordSpaceHorizontal(SolverObject.checkX, SolverObject.checkY);
            // Pick a word according to that length() gotten from the line above
            String pickedWord = PickWord(hSpace);
            if (pickedWord == "") { return; }
            // Assign the first x letters to the variables below to check after and for a cleaner code
            char let1 = pickedWord.charAt(0);
            char let2 = pickedWord.charAt(1);

            int tryN = 0;
            if (pickedWord != null) {
                if ((let1 == letter1) && (let2 == letter2)) // If "correct word" is picked, type it
                {
                    String d1 = SolverObject.lineN[SolverObject.checkY].substring(0, SolverObject.checkX); // doğru
                    String d2 = SolverObject.lineN[SolverObject.checkY].substring(SolverObject.checkX + hSpace, 15); //fix that
                    SolverObject.lineN[SolverObject.checkY] = d1 + pickedWord + d2;
                    // Delete the used word from the array its in
                    DeleteWordFromArray(getTheArray(hSpace), pickedWord);
                    TypeTheBoard();

                    next23();
                    return;
                } else // If "correct word" is not picked, pick another word
                {
                    while (tryN < 41) {
                        pickedWord = PickWord(hSpace);
                        if (pickedWord == "") { return; }
                        let1 = pickedWord.charAt(0);
                        let2 = pickedWord.charAt(1);

                        if ((let1 == letter1) && (let2 == letter2)) // If "correct word" is picked, type it
                        {
                            String d1 = SolverObject.lineN[SolverObject.checkY].substring(0, SolverObject.checkX); // doğru
                            String d2 = SolverObject.lineN[SolverObject.checkY].substring(SolverObject.checkX + hSpace, 15); //fix that
                            SolverObject.lineN[SolverObject.checkY] = d1 + pickedWord + d2;
                            // Delete the used word from the array its in
                            DeleteWordFromArray(getTheArray(hSpace), pickedWord);
                            TypeTheBoard();

                            next23();
                            return;
                        }
                        tryN++;
                    }
                    return;
                }
            }

        }
        //endregion
    }

    public void next23() {
        //region Check 2 letter Type the VERTICAL word created at (8,8) (aiming to pick "GNU" by chance) // Done, working
        {
            SolverObject.checkX = 8;
            SolverObject.checkY = 8; // go to (8, 8)

            char letter1 = read1Letter(SolverObject.checkX, SolverObject.checkY);
            // Get vertical space
            int vSpace = GetWordSpaceVertical(SolverObject.checkX, SolverObject.checkY);
            // Pick a word according to that length() gotten from the line above
            String pickedWord = PickWord(vSpace);
            if (pickedWord == "") { return; }
            // Assign the first x letters to the variables below to check after and for a cleaner code
            char let1 = pickedWord.charAt(0);

            int tryN = 0;
            if (pickedWord != null) {
                if ((let1 == letter1)) // If "correct word" is picked, type it
                {
                    TypeVertically(pickedWord);
                    // Delete the used word from the array its in
                    DeleteWordFromArray(getTheArray(vSpace), pickedWord);

                    next24();
                    return;
                } else // If "correct word" is not picked, pick another word
                {
                    while (tryN < 41) {
                        pickedWord = PickWord(vSpace);
                        if (pickedWord == "") { return; }
                        let1 = pickedWord.charAt(0);

                        if ((let1 == letter1)) // If "correct word" is picked, type it
                        {
                            TypeVertically(pickedWord);
                            // Delete the used word from the array its in
                            DeleteWordFromArray(getTheArray(vSpace), pickedWord);

                            next24();
                            return;
                        }
                        tryN++;
                    }
                    return;
                }
            }

        }
        //endregion
    }

    public void next24() {
        //region Check the (7, 9) 2 letter and type a HORIZONTAL word with it (aiming to pick "SNOWSHOE" by chance) // Done, working
        {
            SolverObject.checkX = 7;
            SolverObject.checkY = 9; // go to (7, 9)
            // read already written 2 letters
            char letter1 = read1Letter(SolverObject.checkX, SolverObject.checkY);
            char letter2 = read1Letter(SolverObject.checkX + 1, SolverObject.checkY);
            // Get horizontal space
            int hSpace = GetWordSpaceHorizontal(SolverObject.checkX, SolverObject.checkY);
            // Pick a word according to that length() gotten from the line above
            String pickedWord = PickWord(hSpace);
            if (pickedWord == "") { return; }
            // Assign the first x letters to the variables below to check after and for a cleaner code
            char let1 = pickedWord.charAt(0);
            char let2 = pickedWord.charAt(1);

            int tryN = 0;
            if (pickedWord != null) {
                if ((let1 == letter1) && (let2 == letter2)) // If "correct word" is picked, type it
                {
                    String d1 = SolverObject.lineN[SolverObject.checkY].substring(0, SolverObject.checkX); // doğru
                    String d2 = SolverObject.lineN[SolverObject.checkY].substring(SolverObject.checkX + hSpace, 15); //fix that
                    SolverObject.lineN[SolverObject.checkY] = d1 + pickedWord + d2;
                    // Delete the used word from the array its in
                    DeleteWordFromArray(getTheArray(hSpace), pickedWord);
                    TypeTheBoard();

                    next25();
                    return;
                } else // If "correct word" is not picked, pick another word
                {
                    while (tryN < 41) {
                        pickedWord = PickWord(hSpace);
                        if (pickedWord == "") { return; }
                        let1 = pickedWord.charAt(0);
                        let2 = pickedWord.charAt(1);

                        if ((let1 == letter1) && (let2 == letter2)) // If "correct word" is picked, type it
                        {
                            String d1 = SolverObject.lineN[SolverObject.checkY].substring(0, SolverObject.checkX); // doğru
                            String d2 = SolverObject.lineN[SolverObject.checkY].substring(SolverObject.checkX + hSpace, 15); //fix that
                            SolverObject.lineN[SolverObject.checkY] = d1 + pickedWord + d2;
                            // Delete the used word from the array its in
                            DeleteWordFromArray(getTheArray(hSpace), pickedWord);
                            TypeTheBoard();

                            next25();
                            return;
                        }
                        tryN++;
                    }
                    return;
                }
            }

        }
        //endregion
    }

    public void next25() {
        //region Check 2 letter Type the VERTICAL word created at (10,8) (aiming to pick "SWOLLEN" by chance) // Done, working
        {
            SolverObject.checkX = 10;
            SolverObject.checkY = 8; // go to (10, 8)

            char letter1 = read1Letter(SolverObject.checkX, SolverObject.checkY + 1);
            // Get vertical space
            int vSpace = GetWordSpaceVertical(SolverObject.checkX, SolverObject.checkY);
            // Pick a word according to that length() gotten from the line above
            String pickedWord = PickWord(vSpace);
            if (pickedWord == "") { return; }
            // Assign the first x letters to the variables below to check after and for a cleaner code
            char let1 = pickedWord.charAt(1);

            int tryN = 0;
            if (pickedWord != null) {
                if ((let1 == letter1)) // If "correct word" is picked, type it
                {
                    TypeVertically(pickedWord);
                    // Delete the used word from the array its in
                    DeleteWordFromArray(getTheArray(vSpace), pickedWord);

                    next26();
                    return;
                } else // If "correct word" is not picked, pick another word
                {
                    while (tryN < 41) {
                        pickedWord = PickWord(vSpace);
                        if (pickedWord == "") { return; }
                        let1 = pickedWord.charAt(1);

                        if ((let1 == letter1)) // If "correct word" is picked, type it
                        {
                            TypeVertically(pickedWord);
                            // Delete the used word from the array its in
                            DeleteWordFromArray(getTheArray(vSpace), pickedWord);

                            next26();
                            return;
                        }
                        tryN++;
                    }
                    return;
                }
            }

        }
        //endregion
    }

    public void next26() {
        //region Check the (8, 14) 2 letter and type a HORIZONTAL word with it (aiming to pick "RENTERS" by chance) // Done, working
        {
            SolverObject.checkX = 8;
            SolverObject.checkY = 14; // go to (8, 14)
            // read already written 2 letters
            char letter1 = read1Letter(SolverObject.checkX + 2, SolverObject.checkY);
            // Get horizontal space
            int hSpace = GetWordSpaceHorizontal(SolverObject.checkX, SolverObject.checkY);
            // Pick a word according to that length() gotten from the line above
            String pickedWord = PickWord(hSpace);
            if (pickedWord == "") { return; }
            // Assign the first x letters to the variables below to check after and for a cleaner code
            char let1 = pickedWord.charAt(2);

            int tryN = 0;
            if (pickedWord != null) {
                if ((let1 == letter1)) // If "correct word" is picked, type it
                {
                    String d1 = SolverObject.lineN[SolverObject.checkY].substring(0, SolverObject.checkX); // doğru
                    String d2 = SolverObject.lineN[SolverObject.checkY].substring(SolverObject.checkX + hSpace, 15); //fix that
                    SolverObject.lineN[SolverObject.checkY] = d1 + pickedWord + d2;
                    // Delete the used word from the array its in
                    DeleteWordFromArray(getTheArray(hSpace), pickedWord);
                    TypeTheBoard();

                    next27();
                    return;
                } else // If "correct word" is not picked, pick another word
                {
                    while (tryN < 41) {
                        pickedWord = PickWord(hSpace);
                        if (pickedWord == "") { return; }
                        let1 = pickedWord.charAt(2);

                        if ((let1 == letter1)) // If "correct word" is picked, type it
                        {
                            String d1 = SolverObject.lineN[SolverObject.checkY].substring(0, SolverObject.checkX); // doğru
                            String d2 = SolverObject.lineN[SolverObject.checkY].substring(SolverObject.checkX + hSpace, 15); //fix that
                            SolverObject.lineN[SolverObject.checkY] = d1 + pickedWord + d2;
                            // Delete the used word from the array its in
                            DeleteWordFromArray(getTheArray(hSpace), pickedWord);
                            TypeTheBoard();

                            next27();
                            return;
                        }
                        tryN++;
                    }
                    return;
                }
            }

        }
        //endregion
    }

    public void next27() {
        //region Check the (9, 11) 1 letter and type a HORIZONTAL word with it (aiming to pick "PLOVER" by chance) // Done, working
        {
            SolverObject.checkX = 9;
            SolverObject.checkY = 11; // go to (9, 11)

            // read already written 2 letters
            char letter1 = read1Letter(SolverObject.checkX + 1, SolverObject.checkY);
            // Get horizontal space
            int hSpace = GetWordSpaceHorizontal(SolverObject.checkX, SolverObject.checkY);
            // Pick a word according to that length() gotten from the line above
            String pickedWord = PickWord(hSpace);
            if (pickedWord == "") { return; }
            // Assign the first x letters to the variables below to check after and for a cleaner code
            char let1 = pickedWord.charAt(1);

            int tryN = 0;
            if (pickedWord != null) {
                if ((let1 == letter1)) // If "correct word" is picked, type it
                {
                    String d1 = SolverObject.lineN[SolverObject.checkY].substring(0, SolverObject.checkX); // doğru
                    String d2 = SolverObject.lineN[SolverObject.checkY].substring(SolverObject.checkX + hSpace, 15); //fix that
                    SolverObject.lineN[SolverObject.checkY] = d1 + pickedWord + d2;
                    // Delete the used word from the array its in
                    DeleteWordFromArray(getTheArray(hSpace), pickedWord);
                    TypeTheBoard();

                    next28();
                    return;
                } else // If "correct word" is not picked, pick another word
                {
                    while (tryN < 41) {
                        pickedWord = PickWord(hSpace);
                        if (pickedWord == "") { return; }
                        let1 = pickedWord.charAt(1);

                        if ((let1 == letter1)) // If "correct word" is picked, type it
                        {
                            String d1 = SolverObject.lineN[SolverObject.checkY].substring(0, SolverObject.checkX); // doğru
                            String d2 = SolverObject.lineN[SolverObject.checkY].substring(SolverObject.checkX + hSpace, 15); //fix that
                            SolverObject.lineN[SolverObject.checkY] = d1 + pickedWord + d2;
                            // Delete the used word from the array its in
                            DeleteWordFromArray(getTheArray(hSpace), pickedWord);
                            TypeTheBoard();

                            next28();
                            return;
                        }
                        tryN++;
                    }
                    return;
                }
            }

        }
        //endregion
    }

    public void next28() {
        //region Check 2 letter Type the VERTICAL word created at (11,9) (aiming to pick "SNOCAT" by chance) // Done, working
        {
            SolverObject.checkX = 11;
            SolverObject.checkY = 9; // go to (11, 9)

            char letter1 = read1Letter(SolverObject.checkX, SolverObject.checkY);
            char letter2 = read1Letter(SolverObject.checkX, SolverObject.checkY + 2);
            char letter3 = read1Letter(SolverObject.checkX, SolverObject.checkY + 5);
            // Get vertical space
            int vSpace = GetWordSpaceVertical(SolverObject.checkX, SolverObject.checkY);
            // Pick a word according to that length() gotten from the line above
            String pickedWord = PickWord(vSpace);
            if (pickedWord == "") { return; }
            // Assign the first x letters to the variables below to check after and for a cleaner code
            char let1 = pickedWord.charAt(0);
            char let2 = pickedWord.charAt(2);
            char let3 = pickedWord.charAt(5);

            int tryN = 0;
            if (pickedWord != null) {
                if ((let1 == letter1) && (let2 == letter2) && (let3 == letter3)) // If "correct word" is picked, type it
                {
                    TypeVertically(pickedWord);
                    // Delete the used word from the array its in
                    DeleteWordFromArray(getTheArray(vSpace), pickedWord);

                    next29();
                    return;
                } else // If "correct word" is not picked, pick another word
                {
                    while (tryN < 41) {
                        pickedWord = PickWord(vSpace);
                        if (pickedWord == "") { return; }
                        let1 = pickedWord.charAt(1);

                        if ((let1 == letter1) && (let2 == letter2) && (let3 == letter3)) // If "correct word" is picked, type it
                        {
                            TypeVertically(pickedWord);
                            // Delete the used word from the array its in
                            DeleteWordFromArray(getTheArray(vSpace), pickedWord);

                            next29();
                            return;
                        }
                        tryN++;
                    }
                    return;
                }
            }

        }
        //endregion
    }

    public void next29() {
        //region Check 1 letter Type the VERTICAL word created at (13,4) (aiming to pick "YOGI" by chance) // Done, working
        {
            SolverObject.checkX = 13;
            SolverObject.checkY = 4; // go to (13, 4)

            char letter1 = read1Letter(SolverObject.checkX, SolverObject.checkY + 2);
            // Get vertical space
            int vSpace = GetWordSpaceVertical(SolverObject.checkX, SolverObject.checkY);
            // Pick a word according to that length() gotten from the line above
            String pickedWord = PickWord(vSpace);
            if (pickedWord == "") { return; }
            // Assign the first x letters to the variables below to check after and for a cleaner code
            char let1 = pickedWord.charAt(2);

            int tryN = 0;
            if (pickedWord != null) {
                if ((let1 == letter1)) // If "correct word" is picked, type it
                {
                    TypeVertically(pickedWord);
                    // Delete the used word from the array its in
                    DeleteWordFromArray(getTheArray(vSpace), pickedWord);

                    next30();
                    return;
                } else // If "correct word" is not picked, pick another word
                {
                    while (tryN < 41) {
                        pickedWord = PickWord(vSpace);
                        if (pickedWord == "") { return; }
                        let1 = pickedWord.charAt(2);

                        if ((let1 == letter1)) // If "correct word" is picked, type it
                        {
                            TypeVertically(pickedWord);
                            // Delete the used word from the array its in
                            DeleteWordFromArray(getTheArray(vSpace), pickedWord);

                            next30();
                            return;
                        }
                        tryN++;
                    }
                    return;
                }
            }

        }
        //endregion
    }

    public void next30() {
        //region Check 1 letter Type the VERTICAL word created at (14,5) (aiming to pick "AGREE" by chance) // Done, working
        {
            SolverObject.checkX = 14;
            SolverObject.checkY = 5; // go to (14, 5)

            char letter1 = read1Letter(SolverObject.checkX, SolverObject.checkY + 1);
            char letter2 = read1Letter(SolverObject.checkX, SolverObject.checkY + 4);
            // Get vertical space
            int vSpace = GetWordSpaceVertical(SolverObject.checkX, SolverObject.checkY);
            // Pick a word according to that length() gotten from the line above
            String pickedWord = PickWord(vSpace);
            if (pickedWord == "") { return; }
            // Assign the first x letters to the variables below to check after and for a cleaner code
            char let1 = pickedWord.charAt(1);
            char let2 = pickedWord.charAt(4);

            int tryN = 0;
            if (pickedWord != null) {
                if ((let1 == letter1) && (let2 == letter2)) // If "correct word" is picked, type it
                {
                    TypeVertically(pickedWord);
                    // Delete the used word from the array its in
                    DeleteWordFromArray(getTheArray(vSpace), pickedWord);

                    next31();
                    return;
                } else // If "correct word" is not picked, pick another word
                {
                    while (tryN < 41) {
                        pickedWord = PickWord(vSpace);
                        if (pickedWord == "") { return; }
                        let1 = pickedWord.charAt(1);
                        let2 = pickedWord.charAt(4);

                        if ((let1 == letter1) && (let2 == letter2)) // If "correct word" is picked, type it
                        {
                            TypeVertically(pickedWord);
                            // Delete the used word from the array its in
                            DeleteWordFromArray(getTheArray(vSpace), pickedWord);

                            next31();
                            return;
                        }
                        tryN++;
                    }
                    return;
                }
            }

        }
        //endregion
    }

    public void next31() {
        //region Check the (9, 13) 1 letter and type a HORIZONTAL word with it (aiming to pick "SEALED" by chance) // Done, working
        {
            SolverObject.checkX = 9;
            SolverObject.checkY = 13; // go to (9, 13)
            // read already written 2 letters
            char letter1 = read1Letter(SolverObject.checkX + 1, SolverObject.checkY);
            char letter2 = read1Letter(SolverObject.checkX + 2, SolverObject.checkY);
            // Get horizontal space
            int hSpace = GetWordSpaceHorizontal(SolverObject.checkX, SolverObject.checkY);
            // Pick a word according to that length() gotten from the line above
            String pickedWord = PickWord(hSpace);
            if (pickedWord == "") { return; }
            // Assign the first x letters to the variables below to check after and for a cleaner code
            char let1 = pickedWord.charAt(1);
            char let2 = pickedWord.charAt(2);

            int tryN = 0;
            if (pickedWord != null) {
                if ((let1 == letter1) && (let2 == letter2)) // If "correct word" is picked, type it
                {
                    String d1 = SolverObject.lineN[SolverObject.checkY].substring(0, SolverObject.checkX); // doğru
                    String d2 = SolverObject.lineN[SolverObject.checkY].substring(SolverObject.checkX + hSpace, 15); //fix that
                    SolverObject.lineN[SolverObject.checkY] = d1 + pickedWord + d2;
                    // Delete the used word from the array its in
                    DeleteWordFromArray(getTheArray(hSpace), pickedWord);
                    TypeTheBoard();

                    next32();
                    return;
                } else // If "correct word" is not picked, pick another word
                {
                    while (tryN < 41) {
                        pickedWord = PickWord(hSpace);
                        if (pickedWord == "") { return; }
                        let1 = pickedWord.charAt(1);
                        let2 = pickedWord.charAt(2);

                        if ((let1 == letter1) && (let2 == letter2)) // If "correct word" is picked, type it
                        {
                            String d1 = SolverObject.lineN[SolverObject.checkY].substring(0, SolverObject.checkX); // doğru
                            String d2 = SolverObject.lineN[SolverObject.checkY].substring(SolverObject.checkX + hSpace, 15); //fix that
                            SolverObject.lineN[SolverObject.checkY] = d1 + pickedWord + d2;
                            // Delete the used word from the array its in
                            DeleteWordFromArray(getTheArray(hSpace), pickedWord);
                            TypeTheBoard();

                            next32();
                            return;
                        }
                        tryN++;
                    }
                    return;
                }
            }

        }
        //endregion
    }

    public void next32() {
        //region Check the (8, 12) 1 letter and type a HORIZONTAL word with it (aiming to pick "VOLCANO" by chance) // Done, working
        {
            SolverObject.checkX = 8;
            SolverObject.checkY = 12; // go to (8, 12)
            // read already written 2 letters
            char letter1 = read1Letter(SolverObject.checkX + 2, SolverObject.checkY);
            char letter2 = read1Letter(SolverObject.checkX + 3, SolverObject.checkY);
            // Get horizontal space
            int hSpace = GetWordSpaceHorizontal(SolverObject.checkX, SolverObject.checkY);
            // Pick a word according to that length() gotten from the line above
            String pickedWord = PickWord(hSpace);
            if (pickedWord == "") { return; }
            // Assign the first x letters to the variables below to check after and for a cleaner code
            char let1 = pickedWord.charAt(2);
            char let2 = pickedWord.charAt(3);

            int tryN = 0;
            if (pickedWord != null) {
                if ((let1 == letter1) && (let2 == letter2)) // If "correct word" is picked, type it
                {
                    String d1 = SolverObject.lineN[SolverObject.checkY].substring(0, SolverObject.checkX); // doğru
                    String d2 = SolverObject.lineN[SolverObject.checkY].substring(SolverObject.checkX + hSpace, 15); //fix that
                    SolverObject.lineN[SolverObject.checkY] = d1 + pickedWord + d2;
                    // Delete the used word from the array its in
                    DeleteWordFromArray(getTheArray(hSpace), pickedWord);
                    TypeTheBoard();

                    next33();
                    return;
                } else // If "correct word" is not picked, pick another word
                {
                    while (tryN < 41) {
                        pickedWord = PickWord(hSpace);
                        if (pickedWord == "") { return; }
                        let1 = pickedWord.charAt(2);
                        let2 = pickedWord.charAt(3);

                        if ((let1 == letter1) && (let2 == letter2)) // If "correct word" is picked, type it
                        {
                            String d1 = SolverObject.lineN[SolverObject.checkY].substring(0, SolverObject.checkX); // doğru
                            String d2 = SolverObject.lineN[SolverObject.checkY].substring(SolverObject.checkX + hSpace, 15); //fix that
                            SolverObject.lineN[SolverObject.checkY] = d1 + pickedWord + d2;
                            // Delete the used word from the array its in
                            DeleteWordFromArray(getTheArray(hSpace), pickedWord);
                            TypeTheBoard();

                            next33();
                            return;
                        }
                        tryN++;
                    }
                    return;
                }
            }

        }
        //endregion
    }

    public void next33() {
        //region Check the (11, 7) 1 letter and type a HORIZONTAL word with it (aiming to pick "PAIR" by chance) // Done, working
        {
            SolverObject.checkX = 11;
            SolverObject.checkY = 7; // go to (11, 7)
            // read already written 2 letters
            char letter1 = read1Letter(SolverObject.checkX + 2, SolverObject.checkY);
            char letter2 = read1Letter(SolverObject.checkX + 3, SolverObject.checkY);
            // Get horizontal space
            int hSpace = GetWordSpaceHorizontal(SolverObject.checkX, SolverObject.checkY);
            // Pick a word according to that length() gotten from the line above
            String pickedWord = PickWord(hSpace);
            if (pickedWord == "") { return; }
            // Assign the first x letters to the variables below to check after and for a cleaner code
            char let1 = pickedWord.charAt(2);
            char let2 = pickedWord.charAt(3);

            int tryN = 0;
            if (pickedWord != null) {
                if ((let1 == letter1) && (let2 == letter2)) // If "correct word" is picked, type it
                {
                    String d1 = SolverObject.lineN[SolverObject.checkY].substring(0, SolverObject.checkX); // doğru
                    String d2 = SolverObject.lineN[SolverObject.checkY].substring(SolverObject.checkX + hSpace, 15); //fix that
                    SolverObject.lineN[SolverObject.checkY] = d1 + pickedWord + d2;
                    // Delete the used word from the array its in
                    DeleteWordFromArray(getTheArray(hSpace), pickedWord);
                    TypeTheBoard();

                    next34();
                    return;
                } else // If "correct word" is not picked, pick another word
                {
                    while (tryN < 41) {
                        pickedWord = PickWord(hSpace);
                        if (pickedWord == "") { return; }
                        let1 = pickedWord.charAt(2);
                        let2 = pickedWord.charAt(3);

                        if ((let1 == letter1) && (let2 == letter2)) // If "correct word" is picked, type it
                        {
                            String d1 = SolverObject.lineN[SolverObject.checkY].substring(0, SolverObject.checkX); // doğru
                            String d2 = SolverObject.lineN[SolverObject.checkY].substring(SolverObject.checkX + hSpace, 15); //fix that
                            SolverObject.lineN[SolverObject.checkY] = d1 + pickedWord + d2;
                            // Delete the used word from the array its in
                            DeleteWordFromArray(getTheArray(hSpace), pickedWord);
                            TypeTheBoard();

                            next34();
                            return;
                        }
                        tryN++;
                    }
                    return;
                }
            }

        }
        //endregion
    }

    public void next34() {
        //region Check 3 letter Type the VERTICAL word created at (12,0) (aiming to pick "UNDERNEATH" by chance) // Done, working
        {
            SolverObject.checkX = 12;
            SolverObject.checkY = 0; // go to (12, 0)
            char letter1 = read1Letter(SolverObject.checkX, SolverObject.checkY + 6);
            char letter2 = read1Letter(SolverObject.checkX, SolverObject.checkY + 7);
            char letter3 = read1Letter(SolverObject.checkX, SolverObject.checkY + 9);
            // Get vertical space
            int vSpace = GetWordSpaceVertical(SolverObject.checkX, SolverObject.checkY);
            // Pick a word according to that length() gotten from the line above
            String pickedWord = PickWord(vSpace);
            if (pickedWord == "") { return; }
            // Assign the first x letters to the variables below to check after and for a cleaner code
            char let1 = pickedWord.charAt(5);
            char let2 = pickedWord.charAt(7);
            char let3 = pickedWord.charAt(9);

            int tryN = 0;
            if (pickedWord != null) {
                if ((let1 == letter1) && (let2 == letter2) && (let3 == letter3)) // If "correct word" is picked, type it
                {
                    TypeVertically(pickedWord);
                    // Delete the used word from the array its in
                    DeleteWordFromArray(getTheArray(vSpace), pickedWord);

                    next35();
                    return;
                } else // If "correct word" is not picked, pick another word
                {
                    while (tryN < 41) {
                        pickedWord = PickWord(vSpace);
                        if (pickedWord == "") { return; }
                        let1 = pickedWord.charAt(6);
                        let2 = pickedWord.charAt(7);
                        let3 = pickedWord.charAt(9);
                        if ((let1 == letter1) && (let2 == letter2) && (let3 == letter3)) // If "correct word" is picked, type it
                        {
                            TypeVertically(pickedWord);
                            // Delete the used word from the array its in
                            DeleteWordFromArray(getTheArray(vSpace), pickedWord);

                            next35();
                            return;
                        }
                        tryN++;
                    }
                    return;
                }
            }

        }
        //endregion

    }

    public void next35() {
        //region Check the (11, 5) 3 letter and type a HORIZONTAL word with it (aiming to pick "ANOA" by chance) // Done, working
        {
            SolverObject.checkX = 11;
            SolverObject.checkY = 5; // go to (11, 5)

            // read already written 2 letters
            char letter1 = read1Letter(SolverObject.checkX + 1, SolverObject.checkY);
            char letter2 = read1Letter(SolverObject.checkX + 2, SolverObject.checkY);
            char letter3 = read1Letter(SolverObject.checkX + 3, SolverObject.checkY);
            // Get horizontal space
            int hSpace = GetWordSpaceHorizontal(SolverObject.checkX, SolverObject.checkY);
            // Pick a word according to that length() gotten from the line above
            String pickedWord = PickWord(hSpace);
            if (pickedWord == "") { return; }
            // Assign the first x letters to the variables below to check after and for a cleaner code
            char let1 = pickedWord.charAt(1);
            char let2 = pickedWord.charAt(2);
            char let3 = pickedWord.charAt(3);

            int tryN = 0;
            if (pickedWord != null) {
                if ((let1 == letter1) && (let2 == letter2) && (let3 == letter3)) // If "correct word" is picked, type it
                {
                    String d1 = SolverObject.lineN[SolverObject.checkY].substring(0, SolverObject.checkX); // doğru
                    String d2 = SolverObject.lineN[SolverObject.checkY].substring(SolverObject.checkX + hSpace, 15); //fix that
                    SolverObject.lineN[SolverObject.checkY] = d1 + pickedWord + d2;
                    // Delete the used word from the array its in
                    DeleteWordFromArray(getTheArray(hSpace), pickedWord);
                    TypeTheBoard();

                    next36();
                    return;
                } else // If "correct word" is not picked, pick another word
                {
                    while (tryN < 41) {
                        pickedWord = PickWord(hSpace);
                        if (pickedWord == "") { return; }
                        let1 = pickedWord.charAt(1);
                        let2 = pickedWord.charAt(2);
                        let3 = pickedWord.charAt(3);

                        if ((let1 == letter1) && (let2 == letter2) && (let3 == letter3)) // If "correct word" is picked, type it
                        {
                            String d1 = SolverObject.lineN[SolverObject.checkY].substring(0, SolverObject.checkX); // doğru
                            String d2 = SolverObject.lineN[SolverObject.checkY].substring(SolverObject.checkX + hSpace, 15); //fix that
                            SolverObject.lineN[SolverObject.checkY] = d1 + pickedWord + d2;
                            // Delete the used word from the array its in
                            DeleteWordFromArray(getTheArray(hSpace), pickedWord);
                            TypeTheBoard();

                            next36();
                            return;
                        }
                        tryN++;
                    }
                    return;
                }
            }

        }
        //endregion
    }

    public void next36() {
        //region Check 3 letter Type the VERTICAL word created at (11,0) (aiming to pick "PERICARP" by chance) // Done, working
        {
            SolverObject.checkX = 11;
            SolverObject.checkY = 0; // go to (11, 0)

            char letter1 = read1Letter(SolverObject.checkX, SolverObject.checkY + 5);
            char letter2 = read1Letter(SolverObject.checkX, SolverObject.checkY + 6);
            char letter3 = read1Letter(SolverObject.checkX, SolverObject.checkY + 7);
            // Get vertical space
            int vSpace = GetWordSpaceVertical(SolverObject.checkX, SolverObject.checkY);
            // Pick a word according to that length() gotten from the line above
            String pickedWord = PickWord(vSpace);
            if (pickedWord == "") { return; }
            // Assign the first x letters to the variables below to check after and for a cleaner code
            char let1 = pickedWord.charAt(5);
            char let2 = pickedWord.charAt(6);
            char let3 = pickedWord.charAt(7);

            int tryN = 0;
            if (pickedWord != null) {
                if ((let1 == letter1) && (let2 == letter2) && (let3 == letter3)) // If "correct word" is picked, type it
                {
                    TypeVertically(pickedWord);
                    // Delete the used word from the array its in
                    DeleteWordFromArray(getTheArray(vSpace), pickedWord);

                    next37();
                    return;
                } else // If "correct word" is not picked, pick another word
                {
                    while (tryN < 41) {
                        pickedWord = PickWord(vSpace);
                        if (pickedWord == "") { return; }
                        let1 = pickedWord.charAt(5);
                        let2 = pickedWord.charAt(6);
                        let3 = pickedWord.charAt(7);
                        if ((let1 == letter1) && (let2 == letter2) && (let3 == letter3)) // If "correct word" is picked, type it
                        {
                            TypeVertically(pickedWord);
                            // Delete the used word from the array its in
                            DeleteWordFromArray(getTheArray(vSpace), pickedWord);

                            next37();
                            return;
                        }
                        tryN++;
                    }
                    return;
                }
            }

        }
        //endregion
    }

    public void next37() {
        //region Check the (8, 0) 2 letter and type a HORIZONTAL word with it (aiming to pick "GASPUMP" by chance) // Done, working
        {
            SolverObject.checkX = 8;
            SolverObject.checkY = 0; // go to (8, 0)
            // read already written 2 letters
            char letter1 = read1Letter(SolverObject.checkX + 3, SolverObject.checkY);
            char letter2 = read1Letter(SolverObject.checkX + 4, SolverObject.checkY);
            // Get horizontal space
            int hSpace = GetWordSpaceHorizontal(SolverObject.checkX, SolverObject.checkY);
            // Pick a word according to that length() gotten from the line above
            String pickedWord = PickWord(hSpace);
            if (pickedWord == "") { return; }
            // Assign the first x letters to the variables below to check after and for a cleaner code
            char let1 = pickedWord.charAt(3);
            char let2 = pickedWord.charAt(4);

            int tryN = 0;
            if (pickedWord != null) {
                if ((let1 == letter1) && (let2 == letter2)) // If "correct word" is picked, type it
                {
                    String d1 = SolverObject.lineN[SolverObject.checkY].substring(0, SolverObject.checkX); // doğru
                    String d2 = SolverObject.lineN[SolverObject.checkY].substring(SolverObject.checkX + hSpace, 15); //fix that
                    SolverObject.lineN[SolverObject.checkY] = d1 + pickedWord + d2;
                    // Delete the used word from the array its in
                    DeleteWordFromArray(getTheArray(hSpace), pickedWord);
                    TypeTheBoard();

                    next38();
                    return;
                } else // If "correct word" is not picked, pick another word
                {
                    while (tryN < 41) {
                        pickedWord = PickWord(hSpace);
                        if (pickedWord == "") { return; }
                        let1 = pickedWord.charAt(3);
                        let2 = pickedWord.charAt(4);

                        if ((let1 == letter1) && (let2 == letter2)) // If "correct word" is picked, type it
                        {
                            String d1 = SolverObject.lineN[SolverObject.checkY].substring(0, SolverObject.checkX); // doğru
                            String d2 = SolverObject.lineN[SolverObject.checkY].substring(SolverObject.checkX + hSpace, 15); //fix that
                            SolverObject.lineN[SolverObject.checkY] = d1 + pickedWord + d2;
                            // Delete the used word from the array its in
                            DeleteWordFromArray(getTheArray(hSpace), pickedWord);
                            TypeTheBoard();

                            next38();
                            return;
                        }
                        tryN++;
                    }
                    return;
                }
            }

        }
        //endregion
    }

    public void next38() {
        //region Check the (7, 1) 2 letter and type a HORIZONTAL word with it (aiming to pick "HEMPEN" by chance) // Done, working
        {
            SolverObject.checkX = 7;
            SolverObject.checkY = 1; // go to (7, 1)
            // read already written 2 letters
            char letter1 = read1Letter(SolverObject.checkX + 4, SolverObject.checkY);
            char letter2 = read1Letter(SolverObject.checkX + 5, SolverObject.checkY);
            // Get horizontal space
            int hSpace = GetWordSpaceHorizontal(SolverObject.checkX, SolverObject.checkY);
            // Pick a word according to that length() gotten from the line above
            String pickedWord = PickWord(hSpace);
            if (pickedWord == "") { return; }
            // Assign the first x letters to the variables below to check after and for a cleaner code
            char let1 = pickedWord.charAt(4);
            char let2 = pickedWord.charAt(5);

            int tryN = 0;
            if (pickedWord != null) {
                if ((let1 == letter1) && (let2 == letter2)) // If "correct word" is picked, type it
                {
                    String d1 = SolverObject.lineN[SolverObject.checkY].substring(0, SolverObject.checkX); // doğru
                    String d2 = SolverObject.lineN[SolverObject.checkY].substring(SolverObject.checkX + hSpace, 15); //fix that
                    SolverObject.lineN[SolverObject.checkY] = d1 + pickedWord + d2;
                    // Delete the used word from the array its in
                    DeleteWordFromArray(getTheArray(hSpace), pickedWord);
                    TypeTheBoard();

                    next39();
                    return;
                } else // If "correct word" is not picked, pick another word
                {
                    while (tryN < 41) {
                        pickedWord = PickWord(hSpace);
                        if (pickedWord == "") { return; }
                        let1 = pickedWord.charAt(4);
                        let2 = pickedWord.charAt(5);

                        if ((let1 == letter1) && (let2 == letter2)) // If "correct word" is picked, type it
                        {
                            String d1 = SolverObject.lineN[SolverObject.checkY].substring(0, SolverObject.checkX); // doğru
                            String d2 = SolverObject.lineN[SolverObject.checkY].substring(SolverObject.checkX + hSpace, 15); //fix that
                            SolverObject.lineN[SolverObject.checkY] = d1 + pickedWord + d2;
                            // Delete the used word from the array its in
                            DeleteWordFromArray(getTheArray(hSpace), pickedWord);
                            TypeTheBoard();

                            next39();
                            return;
                        }
                        tryN++;
                    }
                    return;
                }
            }

        }
        //endregion
    }

    public void next39() {
        //region Check the (8, 2) 2 letter and type a HORIZONTAL word with it (aiming to pick "LOURDES" by chance) // Done, working
        {
            SolverObject.checkX = 8;
            SolverObject.checkY = 2; // go to (8, 2)
            // read already written 2 letters
            char letter1 = read1Letter(SolverObject.checkX + 3, SolverObject.checkY);
            char letter2 = read1Letter(SolverObject.checkX + 4, SolverObject.checkY);
            // Get horizontal space
            int hSpace = GetWordSpaceHorizontal(SolverObject.checkX, SolverObject.checkY);
            // Pick a word according to that length() gotten from the line above
            String pickedWord = PickWord(hSpace);
            if (pickedWord == "") { return; }
            // Assign the first x letters to the variables below to check after and for a cleaner code
            char let1 = pickedWord.charAt(3);
            char let2 = pickedWord.charAt(4);

            int tryN = 0;
            if (pickedWord != null) {
                if ((let1 == letter1) && (let2 == letter2)) // If "correct word" is picked, type it
                {
                    String d1 = SolverObject.lineN[SolverObject.checkY].substring(0, SolverObject.checkX); // doğru
                    String d2 = SolverObject.lineN[SolverObject.checkY].substring(SolverObject.checkX + hSpace, 15); //fix that
                    SolverObject.lineN[SolverObject.checkY] = d1 + pickedWord + d2;
                    // Delete the used word from the array its in
                    DeleteWordFromArray(getTheArray(hSpace), pickedWord);
                    TypeTheBoard();

                    next40();
                    return;
                } else // If "correct word" is not picked, pick another word
                {
                    while (tryN < 41) {
                        pickedWord = PickWord(hSpace);
                        if (pickedWord == "") { return; }
                        let1 = pickedWord.charAt(3);
                        let2 = pickedWord.charAt(4);

                        if ((let1 == letter1) && (let2 == letter2)) // If "correct word" is picked, type it
                        {
                            String d1 = SolverObject.lineN[SolverObject.checkY].substring(0, SolverObject.checkX); // doğru
                            String d2 = SolverObject.lineN[SolverObject.checkY].substring(SolverObject.checkX + hSpace, 15); //fix that
                            SolverObject.lineN[SolverObject.checkY] = d1 + pickedWord + d2;
                            // Delete the used word from the array its in
                            DeleteWordFromArray(getTheArray(hSpace), pickedWord);
                            TypeTheBoard();

                            next40();
                            return;
                        }
                        tryN++;
                    }
                    return;
                }
            }

        }
        //endregion
    }

    public void next40() {
        //region Check the (7, 3) 2 letter and type a HORIZONTAL word with it (aiming to pick "HEMPEN" by chance) // Done, working
        {
            SolverObject.checkX = 7;
            SolverObject.checkY = 3; // go to (7, 3)
            // read already written 2 letters
            char letter1 = read1Letter(SolverObject.checkX + 4, SolverObject.checkY);
            char letter2 = read1Letter(SolverObject.checkX + 5, SolverObject.checkY);
            // Get horizontal space
            int hSpace = GetWordSpaceHorizontal(SolverObject.checkX, SolverObject.checkY);
            // Pick a word according to that length() gotten from the line above
            String pickedWord = PickWord(hSpace);
            if (pickedWord == "") { return; }
            // Assign the first x letters to the variables below to check after and for a cleaner code
            char let1 = pickedWord.charAt(4);
            char let2 = pickedWord.charAt(5);

            int tryN = 0;
            if (pickedWord != null) {
                if ((let1 == letter1) && (let2 == letter2)) // If "correct word" is picked, type it
                {
                    String d1 = SolverObject.lineN[SolverObject.checkY].substring(0, SolverObject.checkX); // doğru
                    String d2 = SolverObject.lineN[SolverObject.checkY].substring(SolverObject.checkX + hSpace, 15); //fix that
                    SolverObject.lineN[SolverObject.checkY] = d1 + pickedWord + d2;
                    // Delete the used word from the array its in
                    DeleteWordFromArray(getTheArray(hSpace), pickedWord);
                    TypeTheBoard();

                    next41();
                    return;
                } else // If "correct word" is not picked, pick another word
                {
                    while (tryN < 41) {
                        pickedWord = PickWord(hSpace);
                        if (pickedWord == "") { return; }
                        let1 = pickedWord.charAt(4);
                        let2 = pickedWord.charAt(5);

                        if ((let1 == letter1) && (let2 == letter2)) // If "correct word" is picked, type it
                        {
                            String d1 = SolverObject.lineN[SolverObject.checkY].substring(0, SolverObject.checkX); // doğru
                            String d2 = SolverObject.lineN[SolverObject.checkY].substring(SolverObject.checkX + hSpace, 15); //fix that
                            SolverObject.lineN[SolverObject.checkY] = d1 + pickedWord + d2;
                            // Delete the used word from the array its in
                            DeleteWordFromArray(getTheArray(hSpace), pickedWord);
                            TypeTheBoard();

                            next41();
                            return;
                        }
                        tryN++;
                    }
                    return;
                }
            }

        }
        //endregion
    }

    public void next41() {
        //region Check the (8, 4) 3 letter and type a HORIZONTAL word with it (aiming to pick "DESCRY" by chance) // Done, working
        {
            SolverObject.checkX = 8;
            SolverObject.checkY = 4; // go to (8, 4)
            // read already written 2 letters
            char letter1 = read1Letter(SolverObject.checkX + 3, SolverObject.checkY);
            char letter2 = read1Letter(SolverObject.checkX + 4, SolverObject.checkY);
            char letter3 = read1Letter(SolverObject.checkX + 5, SolverObject.checkY);
            // Get horizontal space
            int hSpace = GetWordSpaceHorizontal(SolverObject.checkX, SolverObject.checkY);
            // Pick a word according to that length() gotten from the line above
            String pickedWord = PickWord(hSpace);
            if (pickedWord == "") { return; }
            // Assign the first x letters to the variables below to check after and for a cleaner code
            char let1 = pickedWord.charAt(3);
            char let2 = pickedWord.charAt(4);
            char let3 = pickedWord.charAt(5);

            int tryN = 0;
            if (pickedWord != null) {
                if ((let1 == letter1) && (let2 == letter2) && (let3 == letter3)) // If "correct word" is picked, type it
                {
                    String d1 = SolverObject.lineN[SolverObject.checkY].substring(0, SolverObject.checkX); // doğru
                    String d2 = SolverObject.lineN[SolverObject.checkY].substring(SolverObject.checkX + hSpace, 15); //fix that
                    SolverObject.lineN[SolverObject.checkY] = d1 + pickedWord + d2;
                    // Delete the used word from the array its in
                    DeleteWordFromArray(getTheArray(hSpace), pickedWord);
                    TypeTheBoard();

                    next42();
                    return;
                } else // If "correct word" is not picked, pick another word
                {
                    while (tryN < 41) {
                        pickedWord = PickWord(hSpace);
                        if (pickedWord == "") { return; }
                        let1 = pickedWord.charAt(3);
                        let2 = pickedWord.charAt(4);
                        let3 = pickedWord.charAt(5);

                        if ((let1 == letter1) && (let2 == letter2) && (let3 == letter3)) // If "correct word" is picked, type it
                        {
                            String d1 = SolverObject.lineN[SolverObject.checkY].substring(0, SolverObject.checkX); // doğru
                            String d2 = SolverObject.lineN[SolverObject.checkY].substring(SolverObject.checkX + hSpace, 15); //fix that
                            SolverObject.lineN[SolverObject.checkY] = d1 + pickedWord + d2;
                            // Delete the used word from the array its in
                            DeleteWordFromArray(getTheArray(hSpace), pickedWord);
                            TypeTheBoard();

                            next42();
                            return;
                        }
                        tryN++;
                    }
                    return;
                }
            }

        }
        //endregion
    }

    public void next42() {
        //region Check 6 letter Type the VERTICAL word created at (9,0) (aiming to pick "AMORETTO" by chance) // Done, working
        {
            SolverObject.checkX = 9;
            SolverObject.checkY = 0; // go to (9, 0)

            char letter1 = read1Letter(SolverObject.checkX, SolverObject.checkY);
            char letter2 = read1Letter(SolverObject.checkX, SolverObject.checkY + 1);
            char letter3 = read1Letter(SolverObject.checkX, SolverObject.checkY + 2);
            char letter4 = read1Letter(SolverObject.checkX, SolverObject.checkY + 3);
            char letter5 = read1Letter(SolverObject.checkX, SolverObject.checkY + 4);
            char letter6 = read1Letter(SolverObject.checkX, SolverObject.checkY + 6);
            // Get vertical space
            int vSpace = GetWordSpaceVertical(SolverObject.checkX, SolverObject.checkY);
            // Pick a word according to that length() gotten from the line above
            String pickedWord = PickWord(vSpace);
            if (pickedWord == "") { return; }
            // Assign the first x letters to the variables below to check after and for a cleaner code
            char let1 = pickedWord.charAt(0);
            char let2 = pickedWord.charAt(1);
            char let3 = pickedWord.charAt(2);
            char let4 = pickedWord.charAt(3);
            char let5 = pickedWord.charAt(4);
            char let6 = pickedWord.charAt(6);

            int tryN = 0;
            if (pickedWord != null) {
                if ((let1 == letter1) && (let2 == letter2) && (let3 == letter3) && (let4 == letter4) && (let5 == letter5) && (let6 == letter6)) // If "correct word" is picked, type it
                {
                    TypeVertically(pickedWord);
                    // Delete the used word from the array its in
                    DeleteWordFromArray(getTheArray(vSpace), pickedWord);

                    next43();
                    return;
                } else // If "correct word" is not picked, pick another word
                {
                    while (tryN < 41) {
                        pickedWord = PickWord(vSpace);
                        if (pickedWord == "") { return; }
                        let1 = pickedWord.charAt(0);
                        let2 = pickedWord.charAt(1);
                        let3 = pickedWord.charAt(2);
                        let4 = pickedWord.charAt(3);
                        let5 = pickedWord.charAt(4);
                        let6 = pickedWord.charAt(6);
                        if ((let1 == letter1) && (let2 == letter2) && (let3 == letter3) && (let4 == letter4) && (let5 == letter5) && (let6 == letter6)) // If "correct word" is picked, type it
                        {
                            TypeVertically(pickedWord);
                            // Delete the used word from the array its in
                            DeleteWordFromArray(getTheArray(vSpace), pickedWord);

                            next43();
                            return;
                        }
                        tryN++;
                    }
                    return;
                }
            }

        }
        //endregion
    }

    public void next43() {
        //region Check the (8, 10) 3 letter and type a HORIZONTAL word with it (aiming to pick "UPON" by chance) // Done, working
        {
            SolverObject.checkX = 8;
            SolverObject.checkY = 10; // go to (8, 10)
            // read already written 2 letters
            char letter1 = read1Letter(SolverObject.checkX, SolverObject.checkY);
            char letter2 = read1Letter(SolverObject.checkX + 2, SolverObject.checkY);
            char letter3 = read1Letter(SolverObject.checkX + 3, SolverObject.checkY);
            // Get horizontal space
            int hSpace = GetWordSpaceHorizontal(SolverObject.checkX, SolverObject.checkY);
            // Pick a word according to that length() gotten from the line above
            String pickedWord = PickWord(hSpace);
            if (pickedWord == "") { return; }
            // Assign the first x letters to the variables below to check after and for a cleaner code
            char let1 = pickedWord.charAt(0);
            char let2 = pickedWord.charAt(2);
            char let3 = pickedWord.charAt(3);

            int tryN = 0;
            if (pickedWord != null) {
                if ((let1 == letter1) && (let2 == letter2) && (let3 == letter3)) // If "correct word" is picked, type it
                {
                    String d1 = SolverObject.lineN[SolverObject.checkY].substring(0, SolverObject.checkX); // doğru
                    String d2 = SolverObject.lineN[SolverObject.checkY].substring(SolverObject.checkX + hSpace, 15); //fix that
                    SolverObject.lineN[SolverObject.checkY] = d1 + pickedWord + d2;
                    // Delete the used word from the array its in
                    DeleteWordFromArray(getTheArray(hSpace), pickedWord);
                    TypeTheBoard();

                    next44();
                    return;
                } else // If "correct word" is not picked, pick another word
                {
                    while (tryN < 41) {
                        pickedWord = PickWord(hSpace);
                        if (pickedWord == "") { return; }
                        let1 = pickedWord.charAt(0);
                        let2 = pickedWord.charAt(2);
                        let3 = pickedWord.charAt(3);

                        if ((let1 == letter1) && (let2 == letter2) && (let3 == letter3)) // If "correct word" is picked, type it
                        {
                            String d1 = SolverObject.lineN[SolverObject.checkY].substring(0, SolverObject.checkX); // doğru
                            String d2 = SolverObject.lineN[SolverObject.checkY].substring(SolverObject.checkX + hSpace, 15); //fix that
                            SolverObject.lineN[SolverObject.checkY] = d1 + pickedWord + d2;
                            // Delete the used word from the array its in
                            DeleteWordFromArray(getTheArray(hSpace), pickedWord);
                            TypeTheBoard();

                            next44();
                            return;
                        }
                        tryN++;
                    }
                    return;
                }
            }

        }
        //endregion
    }

    public void next44() {
        //region Check 5 letter Type the VERTICAL word created at (13,9) (aiming to pick "OPENER" by chance) // Done, working
        {
            SolverObject.checkX = 13;
            SolverObject.checkY = 9; // go to (13, 9)
            char letter1 = read1Letter(SolverObject.checkX, SolverObject.checkY);
            char letter2 = read1Letter(SolverObject.checkX, SolverObject.checkY + 2);
            char letter3 = read1Letter(SolverObject.checkX, SolverObject.checkY + 3);
            char letter4 = read1Letter(SolverObject.checkX, SolverObject.checkY + 4);
            char letter5 = read1Letter(SolverObject.checkX, SolverObject.checkY + 5);
            // Get vertical space
            int vSpace = GetWordSpaceVertical(SolverObject.checkX, SolverObject.checkY);
            // Pick a word according to that length() gotten from the line above
            String pickedWord = PickWord(vSpace);
            if (pickedWord == "") { return; }
            // Assign the first x letters to the variables below to check after and for a cleaner code
            char let1 = pickedWord.charAt(0);
            char let2 = pickedWord.charAt(2);
            char let3 = pickedWord.charAt(3);
            char let4 = pickedWord.charAt(4);
            char let5 = pickedWord.charAt(5);

            int tryN = 0;
            if (pickedWord != null) {
                if ((let1 == letter1) && (let2 == letter2) && (let3 == letter3) && (let4 == letter4) && (let5 == letter5)) // If "correct word" is picked, type it
                {
                    TypeVertically(pickedWord);
                    // Delete the used word from the array its in
                    DeleteWordFromArray(getTheArray(vSpace), pickedWord);

                    next45();
                    return;
                } else // If "correct word" is not picked, pick another word
                {
                    while (tryN < 41) {
                        pickedWord = PickWord(vSpace);
                        if (pickedWord == "") { return; }
                        let1 = pickedWord.charAt(0);
                        let2 = pickedWord.charAt(2);
                        let3 = pickedWord.charAt(3);
                        let4 = pickedWord.charAt(4);
                        let5 = pickedWord.charAt(5);
                        if ((let1 == letter1) && (let2 == letter2) && (let3 == letter3) && (let4 == letter4) && (let5 == letter5)) // If "correct word" is picked, type it
                        {
                            TypeVertically(pickedWord);
                            // Delete the used word from the array its in
                            DeleteWordFromArray(getTheArray(vSpace), pickedWord);

                            next45();
                            return;
                        }
                        tryN++;
                    }
                    return;
                }
            }

        }
        //endregion
    }

    public void next45() {
        //region Check 2 letter Type the VERTICAL word created at (14,0) (aiming to pick "PASS" by chance) // Done, working
        {
            SolverObject.checkX = 14;
            SolverObject.checkY = 0; // go to (14, 0)
            char letter1 = read1Letter(SolverObject.checkX, SolverObject.checkY);
            char letter2 = read1Letter(SolverObject.checkX, SolverObject.checkY + 2);
            // Get vertical space
            int vSpace = GetWordSpaceVertical(SolverObject.checkX, SolverObject.checkY);
            // Pick a word according to that length() gotten from the line above
            String pickedWord = PickWord(vSpace);
            if (pickedWord == "") { return; }
            // Assign the first x letters to the variables below to check after and for a cleaner code
            char let1 = pickedWord.charAt(0);
            char let2 = pickedWord.charAt(2);

            int tryN = 0;
            if (pickedWord != null) {
                if ((let1 == letter1) && (let2 == letter2)) // If "correct word" is picked, type it
                {
                    TypeVertically(pickedWord);
                    // Delete the used word from the array its in
                    DeleteWordFromArray(getTheArray(vSpace), pickedWord);
                    TypeTheBoard();
                    long endTime = System.nanoTime();
                    long timeElapsed = endTime - startTime;
                    long mseconds = timeElapsed/1000000;
                    int seconds = (int) (mseconds / 1000) % 60 ;
                    int minutes = (int) ((mseconds / (1000*60)) % 60);
                    int hours   = (int) ((mseconds / (1000*60*60)) % 24);
                    System.out.println("Puzzle "+hours+" Saat "+minutes +" Dakika "+seconds+" Saniyede çözüldü ve "+ SolverObject.thisNumberOfTimes +" Denemede Cozuldu.");
                    System.exit(0); //next46(); 46 ends the program
                } else // If "correct word" is not picked, pick another word
                {
                    while (tryN < 41) {
                        pickedWord = PickWord(vSpace);
                        if (pickedWord == "") { return; }
                        let1 = pickedWord.charAt(0);
                        let2 = pickedWord.charAt(2);
                        if ((let1 == letter1) && (let2 == letter2)) // If "correct word" is picked, type it
                        {
                            TypeVertically(pickedWord);
                            // Delete the used word from the array its in
                            DeleteWordFromArray(getTheArray(vSpace), pickedWord);

                            TypeTheBoard();
                            long endTime = System.nanoTime();
                            long timeElapsed = endTime - startTime;
                            long mseconds = timeElapsed/1000000;
                            int seconds = (int) (mseconds / 1000) % 60 ;
                            int minutes = (int) ((mseconds / (1000*60)) % 60);
                            int hours   = (int) ((mseconds / (1000*60*60)) % 24);
                            System.out.println("Puzzle "+hours+" Saat "+minutes +" Dakika "+seconds+" Saniyede çözüldü ve "+ SolverObject.thisNumberOfTimes +" Denemede Cozuldu.");
                            System.exit(0); //next46(); 46 ends the program
                        } else
                            tryN++;
                    }
                    return; // Return to 0 if you Couldn't solve
                }
            }

        }
        //endregion
    }


}
