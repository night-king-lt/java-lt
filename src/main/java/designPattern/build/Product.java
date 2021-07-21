package designPattern.build;

/**
 * @Author liu.teng
 * @Date 2021/7/20
 * @Version 1.0
 *
 * 生成器思路是产品类与创建产品的类相分离。产品类仅1个，创建产品的类有n个。
 *
 * 生成器设计模式涉及4个关键角色：产品（Product）、抽象生成器（IBuild）、具体生成器（Builder）、指挥者（Director）。
 *
 *     这个复杂的对象通常可以分成几个较小的部分
 *
 *     由于不在该类完成Product类对象的创建，所以无需显示定义构造方法。
 */
public class Product {
    Unit1 u1;
    Unit2 u2;
    Unit3 u3;
}

class Unit1{}
class Unit2{}
class Unit3{}
