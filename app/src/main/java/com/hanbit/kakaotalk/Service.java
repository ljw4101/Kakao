package com.hanbit.kakaotalk;

import java.util.ArrayList;
import java.util.Objects;

/**
 * Created by 1027 on 2017-09-29.
 */

public class Service {
    public static interface iPredicate{
        public void execute();
    }
    public static interface IPost{
        public void excute(Objects o);
    }
    public static interface IList{
        public ArrayList<?> excute(Objects o);
    }
    public static interface IGet{
        public Objects excute(Objects o);
    }
    public static interface IPut{
        public void excute(Objects o);
    }
    public static interface Delete{
        public void excute(Objects o);
    }
}
