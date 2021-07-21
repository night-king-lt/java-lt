package designPattern.factory;

/**
 * @Author liu.teng
 * @Date 2021/7/20 15:27
 * @Version 1.0
 */
public interface ICar {
    //由于工厂模式仅关系对象的创建，为说明方便，无需定义方法
}
//高档小汽车
class TopCar implements ICar{
}
//中档小汽车
class MidCar implements ICar {
}
//低档小汽车
class LowCar implements ICar {
}
