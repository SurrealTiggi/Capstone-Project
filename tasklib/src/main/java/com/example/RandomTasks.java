package com.example;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class RandomTasks {

    public static String[] mStuff = {
            "Put dishes in the sink.",
            "Tidy up room.",
            "Brush teeth.",
            "Pack away toys.",
            "Make your bed.",
            "Pack your bag for school.",
            "Get dressed.",
            "Feed the pets.",
            "Eat all veggies.",
            "Share with your brother/sister."
    };

    public static List<String> mTasks = new LinkedList<String>();

    public String getRandomTasks(int n) {
        for (String tsk: this.mStuff) {
            mTasks.add(tsk);
        }
        //List<String> copy = new LinkedList<String>(mTasks);
        Collections.shuffle(mTasks);
        String result = RandomTasks.join(mTasks.subList(0,n), ": ");
        return result;
    }

    // Below function from com.sun.deploy.util which seemed unnecessary to build intop backend in full
    public static String join(Collection var0, String var1) {
        StringBuffer var2 = new StringBuffer();
        for(Iterator var3 = var0.iterator(); var3.hasNext(); var2.append((String)var3.next())) {
            if(var2.length() != 0) {
                var2.append(var1);
            }
        }
        return var2.toString();
    }
}
