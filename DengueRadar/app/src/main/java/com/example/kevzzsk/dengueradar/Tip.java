package com.example.kevzzsk.dengueradar;

import java.io.Serializable;

public class Tip implements Serializable {
    public String heading;
    public String content;
    public String img;

    public Tip(String heading){
        this.heading = heading;
        this.content = "a;;;;;;;;;;;;;;;;;dfjkasldkfja;lksjdf;lakjds;lkfja;kdjf;alkjdfa;lksjd;flk"
                + "ja;ldskfja;kldsjf;akjf;alkdjf;akljsdf;lkajdf;klaj;sldkjf;alksjdf;kljal;"
                + "ksjdf;alkjl;ksdjfkl;ajl;skdjf;lakjsdlkfjalksdjfkajslkdjf;aldkjf;alksdjf"
                + ";kas;dlkfajs;dlkfj;alskdjf;iweurpoiutoqwehnfoaiweuraowiejrakwnetlakds;l"
                + ";kas;dlkfajs;dlkfj;alskdjf;iweurpoiutoqwehnfoaiweuraowiejrakwnetlakds;l";
        this.img = "@drawable/picture1";
    }
}
