package test;

public class Person implements Comparable<Person>{//可以和person比较
    String name;
    int age;
    int salary;

    public Person() { }

    public Person(String name, int age, int salary) {
        this.name = name;
        this.age = age;
        this.salary = salary;
    }

    @Override
    public boolean equals(Object p) {
        if (p instanceof Person){
            return this.salary == ((Person) p) .salary;
        }else {
            throw new ClassCastException("异常：传入的比较对象不是Person类型");
        }
    }

    @Override
    public String toString() { return name; }

    @Override
    public int compareTo(Person o) {
        return this.salary - o.salary;
    }
}
