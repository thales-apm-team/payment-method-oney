package com.payline.payment.oney.bean.common.enums;

public class CategoryCodeHandler {
    private CategoryCodeHandler() {
    }

    public static int findCategory(int paylineProductCode) {


//fixme parser en char pour eviter le probleme de taille des charactères
        int oneyCategoryCode;

        switch (paylineProductCode) {
            case 17:
            case 170001:
            case 170002:
                oneyCategoryCode = 1;
                break;
            case 11:
            case 110001:
                oneyCategoryCode = 2;
                break;

            case 10:
            case 14:
            case 19:
            case 24:
            case 24001:
// integer trop grande parser en int les valeurs ?
//                case 2400010001:
//                case 2400010002:
            case 25:
                oneyCategoryCode = 1;
                break;

            case 5:
            case 50001:
            case 500010001:
            case 50002:
            case 50003:
            case 500030001:
            case 50004:
            case 500040001:
            case 500040002:
            case 500040003:
            case 500040004:
            case 599990001:
            case 599990002:
            case 12:
            case 120001:
            case 1200010001:
            case 1200010002:
            case 1200010003:
            case 1200010004:
            case 120002:
            case 120003:
            case 1200030001:
            case 120004:
            case 120005:
            case 120006:
            case 120007:
            case 120008:
            case 1200080001:
            case 16:
                oneyCategoryCode = 4;
                break;


            case 4:
            case 40001:
            case 400010001:
            case 400010002:
            case 40002:
            case 400020001:
            case 400020002:
            case 400020003:
            case 40003:
            case 400030001:
            case 400030002:
                oneyCategoryCode = 5;
                break;

            case 21:
                oneyCategoryCode = 6;
                break;

            case 15:
            case 18:
                oneyCategoryCode = 7;
                break;

            case 1:
            case 100010001:
            case 100010002:
            case 100010003:
                oneyCategoryCode = 8;
                break;

            case 7:
                oneyCategoryCode = 9;
                break;

            case 22:
                oneyCategoryCode = 10;
                break;

            case 23:
                oneyCategoryCode = 11;
                break;

            case 9:
                oneyCategoryCode = 12;
                break;

            case 6:
            case 8:
            case 13:
                oneyCategoryCode = 13;
                break;

            case 20:
                oneyCategoryCode = 14;
                break;

            case 2:
            case 20001:
            case 200010001:
            case 200010002:
            case 200010003:
            case 200010004:
            case 200010005:
            case 200010006:
            case 200010007:
            case 26:
                oneyCategoryCode = 15;
                break;

            case 3:
                oneyCategoryCode = 16;
                break;

            default:
                oneyCategoryCode = 5;
                break;
        }


        return oneyCategoryCode;
    }
}

