package designPattern.build;

/**
 * @Author liu.teng
 * @Date 2021/7/20
 * @Version 1.0
 *
 *   生成器设计模式涉及4个关键角色：产品（Product）、抽象生成器（IBuild）、具体生成器（Builder）、指挥者（Director）。
 *
 *   定义1个统一调度类，也叫指挥者（Director）类，是对生成器接口IBuild的封装
 */
public class Director {

    private IBuild build;

    public Director(IBuild build){
        this.build = build;
    }

    public Product build(){
        build.createUnit1();
        build.createUnit2();
        build.createUnit3();
        return build.composite();
    }

    public static void main(String []args){
        IBuild build = new BuildProduct();
        Director direct = new Director(build);
        Product p = direct.build();
    }

}
