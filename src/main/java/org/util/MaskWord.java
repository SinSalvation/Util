package org.util;

import java.util.List;

/**
 * Created by jixu_m on 2017/7/14.
 */
public class MaskWord {
    private char value;
    private List<MaskWord> list;

    public char getValue() {
        return value;
    }

    public void setValue(char value) {
        this.value = value;
    }

    public List<MaskWord> getList() {
        return list;
    }

    public void setList(List<MaskWord> list) {
        this.list = list;
    }
}
