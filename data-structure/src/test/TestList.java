package test;

import chapter3.list.ListOfTwoWay;
import interfaceUsedInDS.Position;

public class TestList {
    public static void main(String[] args) {
        Person p1 = new Person("程序员", 25, 25);
        Person p2 = new Person("机械师", 30, 20);
        Person p3 = new Person("科学家", 35, 15);
        Person p4 = new Person("小商人", 20, 50);
        Person p5 = new Person("富二代", 15, 100);
        Person p6 = new Person("歌手", 18, 40);
        Person[] ps = new Person[]{p1,p2,p3,p4,p5,p6};

        ListOfTwoWay<Person> persons = new ListOfTwoWay<>();
        //System.out.println("列表是否为空？" + persons.isEmpty());

        Position positionFirst = persons.insertAtFirst(ps[0]);//指针，用于构造列表
        Position position = positionFirst;
        for (int i = 1; i < ps.length; i++) {
            position = persons.insertAfter(position, ps[i]);
        }

        System.out.println("列表长度：" + persons.getSize());
        System.out.println("第六个元素：" + persons.callByRank(5));
        //当前的指针指向“歌手”
        System.out.println("用find方法寻找机械师：" +
                persons.find(ps[1], 5, position).getEle());

        persons.insertionSort(positionFirst, 6);
        System.out.println("排序后的列表：");
        show(persons);

    }

    public static void show(ListOfTwoWay<Person> persons){
        for (int i = 0; i < persons.getSize(); i++) {
            System.out.println(persons.callByRank(i));
        }
    }

}
