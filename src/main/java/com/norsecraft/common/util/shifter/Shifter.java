package com.norsecraft.common.util.shifter;

import com.google.common.collect.Lists;
import com.norsecraft.NorseCraftMod;
import com.norsecraft.common.util.CheckUtil;
import com.norsecraft.common.util.SimpleQueue;

import java.lang.reflect.Array;
import java.util.List;
import java.util.function.Consumer;

public class Shifter<T extends IShiftable<T>> {

    private T[] baseArray;
    private final SimpleQueue<T> rightQueue = new SimpleQueue<>();
    private final SimpleQueue<T> leftQueue = new SimpleQueue<>();
    private final List<T> storedRightShifts = Lists.newArrayList();
    private final List<T> storedLeftShifts = Lists.newArrayList();

    public Shifter(T[] baseArray) {
        this.baseArray = baseArray;
    }

    /*
        [1, 2, 3, 4, 5, 6]
        Adding 7 -> going right
        [2, 3, 4, 5, 6, 7]

     */

    public void addItemToRight(T item) {
        CheckUtil.notNull(item, "Could not shift item. Item is null");
        T[] newArray = (T[]) Array.newInstance(item.getClass(), baseArray.length);
        this.fill(newArray);
        T first = this.baseArray[0];
        this.leftQueue.add(first);
        T tmp;
        for(int i = 0; i < this.baseArray.length - 1; i++) {
            tmp = baseArray[i + 1];
            newArray[i].set(tmp);
        }
        newArray[baseArray.length - 1].set(item);
        baseArray = newArray;
    }

    public void update(int index, T item) {
        CheckUtil.checkTrue(index >= 0 && index < this.baseArray.length, String.format("Could not update baseArray. Index %d is out of bounds. Min: %d, Max: %d",
                index, 0, this.baseArray.length));
        CheckUtil.notNull(item, "Could not update basArray. Item is null");
        CheckUtil.notNull(this.baseArray[index], String.format("Could not update basArray. Item at index %d is null", index));
        this.baseArray[index].set(item);
    }

    public void goRight() {
        if(this.rightQueue.isEmpty())
            return;
        T[] newArray = (T[]) Array.newInstance(this.baseArray[0].getClass(), baseArray.length);
        this.fill(newArray);
        T first = this.baseArray[0];
        this.leftQueue.add(first);
        T toAdd = this.rightQueue.peek();
        T tmp;
        for(int i = 0; i < this.baseArray.length - 1; i++) {
            tmp = baseArray[i + 1];
            newArray[i].set(tmp);
        }
        newArray[newArray.length - 1].set(toAdd);
        NorseCraftMod.LOGGER.info("Scrolled down");
    }

    public void goLeft() {
        if(this.leftQueue.isEmpty())
            return;
        T[] newArray = (T[]) Array.newInstance(this.baseArray[0].getClass(), baseArray.length);
        this.fill(newArray);
        T last = this.baseArray[baseArray.length - 1];
        this.rightQueue.add(last);
        T toAdd = this.leftQueue.peek();
        T tmp;
        for(int i = baseArray.length - 1; i > 0; i--) {
            tmp = baseArray[i - 1];
            newArray[i].set(tmp);
        }
        newArray[0].set(toAdd);
        NorseCraftMod.LOGGER.info("Scrolled up");
    }

    private void fill(T[] newArray) {
        CheckUtil.checkTrue(newArray.length == baseArray.length, String.format("Could not fill the new array. Length is not equals!. Base array length: %d, new array length: %d",
                baseArray.length, newArray.length));
        System.arraycopy(baseArray, 0, newArray, 0, newArray.length);
    }

    public void forEach(Consumer<T> consumer) {
        for (T t : baseArray)
            consumer.accept(t);
    }

    public T[] getBaseArray() {
        return baseArray;
    }
}
