package com.example.testsingleactivity;

public class EmojiHelper {
    public String[] emotions = new String[]{"happy","sad","fear","tired","laugh","angry"};
    public String[] emotionsRus = new String[]{"счастье","грусть","страх","усталость","смех","злость"};
    public int[] emoji = new int[]{0x1F603,0x1F614,0x1F631,0x1F62B,0x1F606,0x1F620};

    public int getEmoji(String str){
        switch (str){
            case "happy":
            case "счастье":
                return emoji[0];
            case "sad":
            case "грусть":
                return emoji[1];
            case "fear":
            case "страх":
                return emoji[2];
            case "tired":
            case "усталость":
                return emoji[3];
            case "laugh":
            case "смех":
                return emoji[4];
            case "angry":
            case "злость":
                return emoji[5];

        }
        return 0x1F603;
    }

    public String getEmoji(int code,int l){
        switch (code){
            case 128515:
                if(l % 2==0){
                    return "счастье";
                }else
                    return "happy";
            case 128532:
                if(l % 2==0){
                    return "грусть";
                }else
                    return "sad";
            case 128561:
                if(l % 2==0){
                    return "страх";
                }else
                    return "fear";
            case 128555:
                if(l % 2==0){
                    return "усталость";
                }else
                    return "tired";
            case 128518:
                if(l % 2==0){
                    return "смех";
                }else
                    return "laugh";
            case 128544:
                if(l % 2==0){
                    return "злость";
                }else
                    return "angry";
        }
        if(l % 2 == 0){
            return "счастье";
        }else return "happy";
    }
}

