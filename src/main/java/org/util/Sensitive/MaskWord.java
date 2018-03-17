package org.util.Sensitive;

import java.util.List;

/**
 * Created by jixu_m on 2017/7/14.
 */
public class MaskWord {
    private char value;
    private boolean stop;
    private List<MaskWord> list;

    public char getValue() {
        return value;
    }

    public void setValue(char value) {
        this.value = value;
    }

    public boolean isStop() {
        return stop;
    }

    public void setStop(boolean stop) {
        this.stop = stop;
    }

    public List<MaskWord> getList() {
        return list;
    }

    public void setList(List<MaskWord> list) {
        this.list = list;
    }
}
