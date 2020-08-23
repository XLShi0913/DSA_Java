package test;

import chapter2.vector.Vector;

public class TestVector {
    public static void main(String[] args) {
        Person p1 = new Person("程序员", 25, 25);
        Person p2 = new Person("机械师", 30, 20);
        Person p3 = new Person("科学家", 35, 15);
        Person p4 = new Person("小商人", 20, 50);
        Person p5 = new Person("富二代", 15, 100);
        Person[] ps = new Person[]{p1, p2, p3, p4, p5};
        Vector persons = new Vector(ps, 0, 5);
        persons.show();
        Person p6 = new Person("歌手", 18, 40);
        persons.insert(p6, 5);
        persons.show();
        System.out.println(persons.find(p3, 0, 5));
        System.out.println(persons.binSearchC(p3, 0, 5));
        persons.bubbleSort();
        persons.mergeSort(0, 5);
        persons.show();
    }
}
